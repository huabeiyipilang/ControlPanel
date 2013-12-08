package cn.kli.controlpanel.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * @author zhudl
 * 
 */
public class DragGridView extends GridView {

	/**
	 * 监听对象
	 */
	private DropListener mDropListener;
	/**
	 * 当前拖拽的view
	 */
	private View mDragItem;
	/**
	 * 当前拖拽对象的缓存图片封装成的ImageView对象
	 */
	private ImageView mDragView;
	/**
	 * 记录了拖拽的view的编号，后来记录了drop的放置位置的编号
	 */
	private int mDragPos;
	/**
	 * 记录拖拽的view的编号,后和mDragPos比较，判断放置的位置是不是拖拽位置
	 */
	private int mFirstDragPos;
	/**
	 * 记录y方向上鼠标拖拽时距离被拖拽view top的值
	 */
	private int mDragPointY;
	/**
	 * 记录x方向上鼠标拖拽时距离被拖拽view left的值
	 */
	private int mDragPointX;

	/**
	 * GridViewInterceptor视图y方向上和整个屏幕之间的差值，实际上就是现实电池状态的状态栏和应用名栏所占的高度， 这个值是固定的
	 * 除非在屏幕转换后发生变化，在这里mCoordOffsetY一直是50，mCoordOffsetX是0，切屏后y为0，x为50
	 */
	private int mCoordOffsetY;
	/**
	 * 同上，
	 */
	private int mCoordOffsetX;
	/**
	 * 
	 */
	private Rect mTempRect = new Rect();
	/**
	 * WindowManager对象
	 */
	private WindowManager mWindowManager;
	/**
	 * 相应的layout属性
	 */
	private WindowManager.LayoutParams mWindowParams;
	/**
	 * 拖拽后的背景颜色
	 */
	private int dragndropBackgroundColor = 0x00000000;
	/**
	 * 被拖拽的缓存图片
	 */
	private Bitmap mDragBitmap;

	/**
	 * 记录每个view的正常高度
	 */
	private int mItemHeightNormal = 64;
	
	/**
	 * 此类作用是控制拖拽到顶部或底部时，自动滑屏
	 */
	private ListMoveHandler mListMoveHandler;
	
	private boolean mAutoMove = true;
	
	private View mCacheTouchView;
	private int mDragViewId;
	
	private int mTouchX, mTouchY;

	public DragGridView(Context context) {
		super(context);
		mListMoveHandler = new ListMoveHandler();
	}

	public DragGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mListMoveHandler = new ListMoveHandler();
	}

	/**
	 * 重写onTouchEvent方法，在调用onInterceptTouchEvent方法后调用，这里这两个方法的调用规则比较复杂，
	 * 总的来说在这个类中，Action_Down事件先执行onInterceptTouchEvent，然后执行本方法，其他事件也是这样，
	 * 只是在onInterceptTouchEvent中未做处理
	 * 
	 * @see android.widget.AbsListView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if ((mDropListener != null) && mDragView != null) {
			int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				Rect r = mTempRect;
				mDragView.getDrawingRect(r);
				stopDragging();
				onDrop((int) ev.getX(), (int) ev.getY());
				onItemExchange((int) ev.getX(), (int) ev.getY());
				if (mListMoveHandler.mIsStart) {
					mListMoveHandler.stop();
				}
				// 这里做移动
				unExpandViews(false);
				break;

			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				int x = (int) ev.getX();
				int y = (int) ev.getY();

				if(mAutoMove){
					// 到最顶端了,设置往上滑动
					if (y - mDragPointY < 0) {
						// 使用handler来更新drag画面
						if (!mListMoveHandler.mIsStart) {
							mListMoveHandler.start(false);
						} else if (mListMoveHandler.mIsUp) {
							mListMoveHandler.stop();
							mListMoveHandler.start(false);
						}
						// 拖动到的高度高于480到底部了,设置往下滑动
					} else if ((y - mDragPointY + mItemHeightNormal + mCoordOffsetY) > 480) {
						if (!mListMoveHandler.mIsStart) {
							mListMoveHandler.start(true);
						} else if (!mListMoveHandler.mIsUp) {
							mListMoveHandler.stop();
							mListMoveHandler.start(true);
						}
					} else {
						if (mListMoveHandler.mIsStart) {
							mListMoveHandler.stop();
						}

					}
				}
				mDropListener.onDragOver(mFirstDragPos, x, y);
				dragView(x, y);
				break;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}
	
	/**
	 * 拖拽的时候，拖到底部或者上部是否自动滚动
	 * @Title: setAutoMove
	 * @param auto
	 * @return void
	 * @date 2013-11-7 下午10:40:34
	 */
	public void setAutoMove(boolean auto){
		mAutoMove = auto;
	}
	
	/**
	 * 拖拽的时候，显示view的id，改view需要在item中，若找不到，则默认是整个item。
	 * @Title: setDragViewId
	 * @param id
	 * @return void
	 * @date 2013-11-7 下午10:41:15
	 */
	public void setDragViewId(int id){
		mDragViewId = id;
	}

	/**
	 * 重写onInterceptTouchEvent方法,执行鼠标按下后的操作，return false 为的是onTouchEvent事件得以接着处理
	 * 
	 * @see android.widget.AbsListView#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mDropListener != null) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTouchX = (int) ev.getX();
				mTouchY = (int) ev.getY();
				int itemnum = pointToPosition(mTouchX, mTouchY);
				if (itemnum == AdapterView.INVALID_POSITION) {
					break;
				}
				mCacheTouchView = getChildAt(itemnum - getFirstVisiblePosition()).findViewById(mDragViewId);
				if(mCacheTouchView == null){
				    mCacheTouchView = getChildAt(itemnum - getFirstVisiblePosition());
				}
				
				// 鼠标相对item原点的坐标
				mDragPointY = mTouchY - mCacheTouchView.getTop();
				mDragPointX = mTouchX - mCacheTouchView.getLeft();
				// ev.getRawY是相对屏幕原点的坐标，x，y是相对本gridview原点的坐标,竖屏时mCoordOffsetX一直为0，
				// y大于0的恒定值，横屏则相反
				mCoordOffsetY = ((int) ev.getRawY()) - mTouchY;
				mCoordOffsetX = ((int) ev.getRawX()) - mTouchX;
				// 这里没有和item相同，大概是希望能copy到这个对象 避免回收
				mDragItem = getChildAt(itemnum - getFirstVisiblePosition());
				Rect r = mTempRect;
				r.left = mDragItem.getLeft();
				r.right = mDragItem.getRight();
				r.top = mDragItem.getTop();
				r.bottom = mDragItem.getBottom();
				mItemHeightNormal = r.bottom - r.top;
				mDragPos = pointToPosition(mTouchX, mTouchY);
				// 记录初始要拖拽的view的编号
				mFirstDragPos = mDragPos;
				mDragView = null;
				break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	public void onDrag(){
		// 如果mDragItem不为空的话这个if是肯定满足的
		if ((mTempRect.left < mTouchX) && (mTouchX < mTempRect.right)) {
			mDropListener.onDrag(mDragItem);
			mCacheTouchView.setDrawingCacheEnabled(true);
			mCacheTouchView.setVisibility(View.INVISIBLE);
			Bitmap bitmap = getDragViewBitmap(mCacheTouchView);
			startDragging(bitmap, mTouchX, mTouchY);
		}
	}
	
	private Bitmap getDragViewBitmap(View view){
	    View dragView = view.findViewById(mDragViewId);
        if(dragView == null){
            dragView = view;
        }
        return Bitmap.createBitmap(dragView.getDrawingCache());
	}
	
	private void onItemExchange(int x, int y) {
		if (mDropListener != null && mDragPos >= 0 && mDragPos < getCount()) {
			mDragPos = pointToPosition(x, y);
			if (mDragPos != INVALID_POSITION) {
				mDropListener.onItemExchange(mFirstDragPos, mDragPos);
			}
		}
	}
	
	private void onDrop(int x, int y) {

		if (mDropListener != null && mDragPos >= 0 && mDragPos < getCount()) {
			mDropListener.onDrop(mFirstDragPos, x, y);
		}
	}
	

	/**
	 * 开始拖拽，实际上就是用WindowManager不断跟住鼠标画ImageView
	 * 
	 * @param bm
	 * @param x
	 * @param y
	 */
	private void startDragging(Bitmap bm, int x, int y) {
		stopDragging();
		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWindowParams.x = x - mDragPointX + mCoordOffsetX;
		mWindowParams.y = y - mDragPointY + mCoordOffsetY;

		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;
		ImageView v = new ImageView(getContext());
		v.setBackgroundColor(dragndropBackgroundColor);
		v.setImageBitmap(bm);
		mDragBitmap = bm;
		mWindowManager = (WindowManager) getContext()
				.getSystemService("window");
		mWindowManager.addView(v, mWindowParams);
		mDragView = v;
		
	}

	private void stopDragging() {
		if (mDragView != null) {
			mDragView.setVisibility(View.GONE);
			WindowManager wm = (WindowManager) getContext().getSystemService(
					"window");
			wm.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			mDragBitmap.recycle();
			mDragBitmap = null;
		}
	}

	/**
	 * 更新ImageView的位置
	 * 
	 * @param x
	 * @param y
	 */
	private void dragView(int x, int y) {
		float alpha = 1.0f;
		mWindowParams.alpha = alpha;
		mWindowParams.y = y - mDragPointY + mCoordOffsetY;
		mWindowParams.x = x - mDragPointX + mCoordOffsetX;
		
		mWindowManager.updateViewLayout(mDragView, mWindowParams);
	}


	/*
	 * 重置views
	 */
	private void unExpandViews(boolean deletion) {
		setAdapter(getAdapter());
	}

	public interface DropListener {
		void onDrag(View dragView);
		void onDrop(int item, int x, int y);
		void onDragOver(int item, int x, int y);
		void onItemExchange(int from, int to);
	}

	public void setDropListener(DropListener onDrop) {
		mDropListener = onDrop;
	}
	
	private class ListMoveHandler extends Handler {

		private final int SCROLLDISTANCE = 20;
		private final int SCROLLDURATION = 200;
		private final int MESSAGEWHAT = 111;
		private final int MESSAGEDELAY = 100;
		private boolean mIsStart = false;
		private boolean mIsUp = false;

		public void start(boolean isUp) {
			mIsUp = isUp;
			this.mIsStart = true;
			this.sendEmptyMessageDelayed(MESSAGEWHAT, MESSAGEDELAY);
		}

		public void stop() {
			this.mIsStart = false;
			this.removeMessages(MESSAGEWHAT);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			DragGridView.this.smoothScrollBy(mIsUp ? SCROLLDISTANCE
					: -SCROLLDISTANCE, SCROLLDURATION);
			if (mIsStart) {
				this.sendEmptyMessageDelayed(MESSAGEWHAT, MESSAGEDELAY);
			}
		}
	}
}
