package cn.kli.controlpanel.launcher;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import cn.kli.controlpanel.Group;
import cn.kli.controlpanel.R;

public class TagGridView extends LinearLayout {
	private Context mContext;
	
	//views
	private GridView mGridView; 
	
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
		mGridView = (GridView)root.findViewById(R.id.gridview);
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
				((BaseTagView)view).onLongClick();
				return true;
			}
			
		});
	}
}
