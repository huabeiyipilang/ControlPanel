package cn.kli.controlpanel.launcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import cn.kli.controlpanel.Group;
import cn.kli.controlpanel.Module;
import cn.kli.controlpanel.R;
import cn.kli.utils.UIUtils;
import cn.kli.utils.klilog;

public class TagGridView extends LinearLayout {
	private Context mContext;
	
	//views
	private DragGridView mGridView; 
	private TextView mAddShortcut;
	
	private TagViewAdapter mAdapter;

	public TagGridView(Context context) {
		super(context);
		mContext = context;
	}
	
	public TagGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public void setGroup(Group group){
		if(group != null){
			init(group);
		}
	}

	private void init(Group group) {
		mAdapter = new TagViewAdapter(mContext, group);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View root = inflater.inflate(R.layout.launcher_tag_gridview, this);
		mAddShortcut = (TextView)findViewById(R.id.tv_add_shortcut);
		mGridView = (DragGridView)root.findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position = 0;
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
		private Drawable bkg;
		
		@Override
		public void onItemExchange(int from, int to) {
			Module module = (Module)mAdapter.getItem(from);
			mAdapter.move(module, from, to);
		}


		@Override
		public void onDrag(View dragView) {
			mAddShortcut.setVisibility(View.VISIBLE);
			dragViewHeight = dragView.getBottom() - dragView.getTop();
			rect = new Rect(mAddShortcut.getLeft(), mAddShortcut.getTop(), mAddShortcut.getRight(), mAddShortcut.getBottom());
			bkg = getContext().getResources().getDrawable(R.color.translucent_background);
		}

		@Override
		public void onDrop(int item, int x, int y) {
			mAddShortcut.setVisibility(View.INVISIBLE);
			if(isDragIn(x, y)){
				Module module = (Module)mAdapter.getItem(item);
				UIUtils.addShortcut(getContext(), new Intent(getContext(),module.cls), module.name, module.icon);
				Toast.makeText(getContext(), R.string.lockscreen_added_toast, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onDragOver(int item, int x, int y) {
			if(isDragIn(x, y)){
				mAddShortcut.setBackground(bkg);
				mAddShortcut.setTextColor(Color.WHITE);
			}else{
				mAddShortcut.setBackground(null);
				mAddShortcut.setTextColor(Color.BLACK);
			}
		}
		
		private boolean isDragIn(int x, int y){
			klilog.i("rect:"+rect+", x:"+x+", y:"+y);
			return x > rect.left && x < rect.right && y + dragViewHeight/2 > rect.top;
		}
		
	}
	
}
