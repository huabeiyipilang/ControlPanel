package cn.kli.controlpanel.module.t9search;

import java.util.List;

import com.baidu.mobstat.StatService;

import cn.kli.controlpanel.Baidu;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.base.DragGridView;
import cn.kli.controlpanel.launcher.Module;
import cn.kli.controlpanel.launcher.TagGridView.DropListener;
import cn.kli.controlpanel.module.t9search.AppsManager.State;
import cn.kli.utils.UIUtils;
import cn.kli.utils.klilog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppsListView extends LinearLayout implements OnItemClickListener, IDataList {
	
	private final static int MSG_UPDATE_ADAPTER = 1;
	private final static int MSG_UPDATE_BUILD_PROGRESS = 2;
	
	private Context mContext;
	private DragGridView mGridView;
	private ViewGroup mContainer;
	private AppsManager mAppsManager;
	private PackagesAdapter mAdapter;
	private OnItemSelectListener mListener = new OnItemSelectListener(){

		@Override
		public void onItemSelect(Object o) {
			
		}
		
	};
	
	private AppsManager.StateChangeListener mDataChangeListener = new AppsManager.StateChangeListener(){

		@Override
		public void onStateChanged(State state) {
			klilog.info("State changed:"+state);
			switch(state){
			case IDLE:
				mContainer.removeAllViews();
				mContainer.addView(mGridView);
				updateQueryString(null);
				break;
			case BUILDING:
			case QUERYING:
				break;
			}
		}

		@Override
		public void onProgressUpdate(int progress) {
			klilog.info("Progress changed:"+progress);
			Message msg = mHandler.obtainMessage(MSG_UPDATE_BUILD_PROGRESS);
			msg.obj = progress;
			msg.sendToTarget();
		}

		@Override
		public void onQueryCompleted(List<AppItem> list) {
			updateList(list);
		}
		
	};
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_UPDATE_ADAPTER:
				List<AppItem> items = (List<AppItem>)msg.obj;
				mAdapter = new PackagesAdapter(mContext, items);
				((GridView)mGridView).setAdapter(mAdapter);
				break;
			case MSG_UPDATE_BUILD_PROGRESS:
			}
		}
		
	};
	
	public AppsListView(final Context context) {
		super(context);
		init(context);
	}
	
	public AppsListView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		mContext = context;
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		LayoutInflater inflater = LayoutInflater.from(context);
		mContainer = (ViewGroup)inflater.inflate(R.layout.package_list, this,true);


		mAppsManager = AppsManager.getInstance(mContext);
		mAppsManager.listen(mDataChangeListener);
		
		mGridView = new DragGridView(mContext);
		mGridView.setAutoMove(false);
		mGridView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if(arg1 == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
					Mediator.getInstance().hideKeyboard();
				}
			}
			
		});
		((GridView)mGridView).setNumColumns(4);
		mGridView.setOnItemClickListener(this);
		mGridView.setSelector(R.drawable.app_icon_bg);
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				((DragGridView) mGridView).onDrag();
				return true;
			}
			
		});
		mGridView.setDropListener(new DropListener());

		if(mAppsManager.hasBuilt()){
			mContainer.removeAllViews();
			mContainer.addView(mGridView);
			updateQueryString("");
		}else{
			mAppsManager.build();
		}
	}
	
	private class DropListener implements DragGridView.DropListener{
		private int dragViewHeight;
		private Rect rect;
		private Drawable bkg_black;
		private Drawable bkg_blue;

		@Override
		public void onDrag(View dragView) {
			klilog.info("startAnimation");
			//收键盘，出选项
//			mAddShortcut.startAnimation(mSlideInAnim);
//			dragViewHeight = dragView.getBottom() - dragView.getTop();
//			rect = new Rect(mAddShortcut.getLeft(), mAddShortcut.getTop(), mAddShortcut.getRight(), mAddShortcut.getBottom());
//			bkg_black = getContext().getResources().getDrawable(R.color.translucent_background);
//			bkg_blue = getContext().getResources().getDrawable(R.color.translucent_blue);
		}

		@Override
		public void onDrop(int item, int x, int y) {
			mGridView.setEnabled(true);
			/*
			mAddShortcut.startAnimation(mSlideOutAnim);
			if(isDragIn(x, y)){
				Module module = (Module)mAdapter.getItem(item);
				UIUtils.addShortcut(getContext(), new Intent(getContext(),module.cls), module.name, module.icon);
				StatService.onEvent(getContext(), Baidu.EVENT_DRAG_TO_LAUNCHER, getContext().getString(module.name));
				Toast.makeText(getContext(), R.string.lockscreen_added_toast, Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessage(MSG_SHOW_LAUNCHER);
			}*/
		}

		@Override
		public void onDragOver(int item, int x, int y) {
			/*
			if(isDragIn(x, y)){
				mAddShortcut.setBackgroundDrawable(bkg_black);
			}else{
				mAddShortcut.setBackgroundDrawable(bkg_blue);
			}*/
		}
		
		private boolean isDragIn(int x, int y){
			klilog.info("rect:"+rect+", x:"+x+", y:"+y);
			return x > rect.left && x < rect.right && y + dragViewHeight/2 > rect.top;
		}

		@Override
		public void onItemExchange(int from, int to) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class PackagesAdapter extends ArrayAdapter<AppItem>{
		private LayoutInflater inflater;
		private List<AppItem> items;
		public PackagesAdapter(Context context, List<AppItem> items) {
			super(context, 0, items);
			inflater = LayoutInflater.from(context);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null || convertView.getTag() == null){
				convertView = inflater.inflate(R.layout.package_item, null);
				holder = new ViewHolder();
				holder.p_name = (TextView)convertView.findViewById(R.id.p_name);
				holder.p_key = (TextView)convertView.findViewById(R.id.p_key);
				holder.p_icon = (ImageView)convertView.findViewById(R.id.p_icon);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			AppItem item = items.get(position);
			holder.p_icon.setImageBitmap(item.icon);
			holder.p_name.setText(item.name);
			holder.p_key.setText(item.key);
			return convertView;
		}
		
		private class ViewHolder{
			ImageView p_icon;
			TextView p_name;
			TextView p_key;
		}
		
	}
	
	private void updateList(List<AppItem> items){
		Message msg = new Message();
		msg.what = MSG_UPDATE_ADAPTER;
		msg.obj = items;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(mAdapter != null){
			AppItem item = mAdapter.items.get(position);
			selectItem(item);
		}
		mListener.onItemSelect(null);
	}
	
	private void selectItem(AppItem item){
		mAppsManager.onAppOpen(item);
		Mediator.getInstance().keyboardClear();
		StatService.onEvent(mContext, "t9 open app", item.name);
		launchApp(item.intent);
	}
	
	private void launchApp(Intent intent){
		if(intent == null){
			return;
		}
		try {
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setOnItemSelectListener(OnItemSelectListener listener) {
		mListener = listener;
	}

	@Override
	public void updateQueryString(String query) {
		mAppsManager.query(query);
	}

	@Override
	public void selectTheFirst() {

		if(mAdapter != null && mAdapter.items.size() > 0){
			AppItem item = mAdapter.items.get(0);
			selectItem(item);
		}
		
	}

}
