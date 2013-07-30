package cn.kli.controlpanel.launcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.guide.TipsView;
import cn.kli.utils.UIUtils;
import cn.kli.utils.klilog;

public class TagGridView extends LinearLayout {
	private static final int MSG_MOVE_TO_BG = 1;
	//views
	private DragGridView mGridView; 
	private TextView mAddShortcut;
	
	private TagViewAdapter mAdapter;

	private Animation mSlideInAnim;
	private Animation mSlideOutAnim;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_MOVE_TO_BG:
				Context context = TagGridView.this.getContext();
				((ControlActivity) context).moveTaskToBack(true);
				break;
			}
		}
		
	};

	public TagGridView(Context context) {
		super(context);
	}
	
	public TagGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setGroup(Group group){
		if(group != null){
			init(group);
		}
	}

	private void init(Group group) {
		mAdapter = new TagViewAdapter(getContext(), group);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View root = inflater.inflate(R.layout.launcher_tag_gridview, this);
		mAddShortcut = (TextView)findViewById(R.id.tv_add_shortcut);
		mSlideInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_up);
		mSlideInAnim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				klilog.i("Animation end");
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				klilog.i("Animation start");
				mAddShortcut.setVisibility(View.VISIBLE);
			}
			
		});
		
		mSlideOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
		mSlideOutAnim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				mAddShortcut.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
		});
		
		mGridView = (DragGridView)root.findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position = 0;
				klilog.i("onItemClick");
				((BaseTagView)view).onClick();
			}
			
		});
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				mGridView.onDrag();
				return true;
			}
			
		});
		mGridView.setDropListener(new DropListener());
		
	}
	
	private class DropListener implements DragGridView.DropListener{
		private int dragViewHeight;
		private Rect rect;
		private Drawable bkg_black;
		private Drawable bkg_blue;
		
		@Override
		public void onItemExchange(int from, int to) {
			Module module = (Module)mAdapter.getItem(from);
			mAdapter.move(module, from, to);
		}


		@Override
		public void onDrag(View dragView) {
			klilog.i("startAnimation");
			mAddShortcut.startAnimation(mSlideInAnim);
			dragViewHeight = dragView.getBottom() - dragView.getTop();
			rect = new Rect(mAddShortcut.getLeft(), mAddShortcut.getTop(), mAddShortcut.getRight(), mAddShortcut.getBottom());
			bkg_black = getContext().getResources().getDrawable(R.color.translucent_background);
			bkg_blue = getContext().getResources().getDrawable(R.color.translucent_blue);
		}

		@Override
		public void onDrop(int item, int x, int y) {
			mAddShortcut.startAnimation(mSlideOutAnim);
			if(isDragIn(x, y)){
				Module module = (Module)mAdapter.getItem(item);
				UIUtils.addShortcut(getContext(), new Intent(getContext(),module.cls), module.name, module.icon);
				Toast.makeText(getContext(), R.string.lockscreen_added_toast, Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessage(MSG_MOVE_TO_BG);
			}
		}

		@Override
		public void onDragOver(int item, int x, int y) {
			if(isDragIn(x, y)){
				mAddShortcut.setBackground(bkg_black);
			}else{
				mAddShortcut.setBackground(bkg_blue);
			}
		}
		
		private boolean isDragIn(int x, int y){
			klilog.i("rect:"+rect+", x:"+x+", y:"+y);
			return x > rect.left && x < rect.right && y + dragViewHeight/2 > rect.top;
		}
		
	}
	
}
