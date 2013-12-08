package cn.kli.controlpanel.updater;

import java.util.ArrayList;
import java.util.List;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.kli.controlpanel.R;
import cn.kli.utils.klilog;

public class AppListView extends LinearLayout {
	private final static int INIT_LIST = 1;
	private Context mContext;
	
	//views
	private ListView mListView;
	
	//data
	private AppAdapter mAdapter;
	private List<UpdateAppInfo> mList;
	
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case INIT_LIST:
				mAdapter = new AppAdapter(mList);
				mListView.setAdapter(mAdapter);
			}
		}
		
	};
	
	public AppListView(Context context) {
		super(context);
		mContext = context;
		initViews();
	}
	
	public AppListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initViews();
	}

	private void initViews(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View root = inflater.inflate(R.layout.app_list_view, this);
		mListView = (ListView)root.findViewById(R.id.update_app_list);
		new AsyncTask(){

			@Override
			protected Object doInBackground(Object... arg0) {
				mList = getLocalAppInfo();
				mHandler.sendEmptyMessage(INIT_LIST);
				return null;
			}
			
		}.execute("");
	}

	public void setRemoteList(final List<UpdateInfo> list, final Message callback){
		new AsyncTask(){

			@Override
			protected Object doInBackground(Object... arg0) {
				mList = mergeRemoteList(list);
				mHandler.sendEmptyMessage(INIT_LIST);
				callback.sendToTarget();
				return null;
			}
			
		}.execute("");
	}
	
	private List<UpdateAppInfo> mergeRemoteList(List<UpdateInfo> list) {
		UpdateAppInfo tmp = null;
		List<UpdateAppInfo> newApps = new ArrayList<UpdateAppInfo>();
		List<UpdateAppInfo> res = getLocalAppInfo();
		for(UpdateInfo remoteInfo : list) {
			boolean hasLocal = false;
			for(UpdateAppInfo localInfo : res) {
				if (remoteInfo.pkg_name.equals(localInfo.pkgInfo.packageName)) {
					localInfo.updateInfo = remoteInfo;
					hasLocal = true;
					break;
				}
			}
			if(!hasLocal){
				tmp = new UpdateAppInfo();
				tmp.updateInfo = remoteInfo;
				newApps.add(tmp);
			}
		}
		
		res.addAll(newApps);
		return res;
	}
	
	private List<UpdateAppInfo> getLocalAppInfo(){
		PackageManager pm = mContext.getPackageManager();
		List<PackageInfo> all = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		List<UpdateAppInfo> local = new ArrayList<UpdateAppInfo>();
		UpdateAppInfo tmp;
		for(PackageInfo info : all){
			if(info.packageName.startsWith("cn.kli.queen")){
				tmp = new UpdateAppInfo();
				tmp.setPkgInfo(info);
				local.add(tmp);
			}
		}
		return local;
	}
	
	private class UpdateAppInfo{
		public final static int TYPE_NO 		= 1;
		public final static int TYPE_UPDATE 	= 2;
		public final static int TYPE_NEW 		= 3;
		public final static int TYPE_UNINSTALL 	= 4;
		private UpdateInfo updateInfo;
		private PackageInfo pkgInfo;
		
		public void setUpdateInfo(UpdateInfo updateInfo) {
			this.updateInfo = updateInfo;
		}
		
		public void setPkgInfo(PackageInfo pkgInfo) {
			this.pkgInfo = pkgInfo;
		}
		
		public int getUpdateType(){
			if(updateInfo == null){
				return TYPE_UNINSTALL;
			}else if(pkgInfo == null){
				return TYPE_NEW;
			}else if(Integer.valueOf(updateInfo.version_code) >
							Integer.valueOf(pkgInfo.versionCode)){
				return TYPE_UPDATE;
			}
			return TYPE_NO;
		}
		
		public Drawable getIcon(){
			Drawable icon = null;
			if(pkgInfo != null){

				PackageManager pm = mContext.getPackageManager();
				try {
					icon = pm.getApplicationIcon(pkgInfo.packageName);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}else if(updateInfo != null){
				
			}
			if(icon == null){
				icon = mContext.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
			}
			return icon;
		}
		
		public String getName(){
			PackageManager pm = mContext.getPackageManager();
			String name = null;
			if(pkgInfo != null){
				name = (String) pm.getApplicationLabel(pkgInfo.applicationInfo);
			}else{
				name = updateInfo.app_name;
			}
			return name;
		}
		
		public String getCurrentVersion(){
			if(pkgInfo != null){
				return pkgInfo.versionName;
			}
			return "";
		}
	}
	
	private class AppAdapter extends BaseAdapter{
		private List<UpdateAppInfo> infoList;
		public AppAdapter(List<UpdateAppInfo> list){
			infoList = list;
		}
		@Override
		public int getCount() {
			return infoList.size();
		}

		@Override
		public Object getItem(int pos) {
			return infoList.get(pos);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int pos, View arg1, ViewGroup arg2) {
			return new AppUpdateView(mContext, infoList.get(pos));
		}
	}
	
	private class AppUpdateView extends LinearLayout{
		private LayoutInflater inflater;
		//views
		private ImageView icon;
		private TextView name;
		private TextView version;
		private Button button;
		
		public AppUpdateView(Context context, final UpdateAppInfo info) {
			super(context);
			inflater = LayoutInflater.from(context);
			View root = inflater.inflate(R.layout.app_list_item, this);
			icon = (ImageView)root.findViewById(R.id.app_icon);
			name = (TextView)root.findViewById(R.id.app_name);
			version = (TextView)root.findViewById(R.id.app_current_version);
			button = (Button)root.findViewById(R.id.app_upload);

			try {
				icon.setImageDrawable(info.getIcon());
			} catch (Exception e) {
				e.printStackTrace();
			}
			name.setText(info.getName());
			version.setText(info.getCurrentVersion());
			
			switch(info.getUpdateType()){
			case UpdateAppInfo.TYPE_NEW:
				button.setEnabled(true);
				button.setText(R.string.install);
				button.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						download(info);
					}
					
				});
				break;
			case UpdateAppInfo.TYPE_NO:
				button.setText(R.string.update);
				button.setEnabled(false);
				break;
			case UpdateAppInfo.TYPE_UNINSTALL:
				button.setText(R.string.update);
				button.setEnabled(false);
				break;
			case UpdateAppInfo.TYPE_UPDATE:
				button.setText(R.string.update);
				button.setEnabled(true);
				button.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						download(info);
					}
					
				});
				break;
			}
		}
		
	}
	
	private void download(UpdateAppInfo info){
        DownloadManager dm=((DownloadManager)mContext.getSystemService("download"));
        String url = info.updateInfo.url;
        Uri uri = Uri.parse(url);
        Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).mkdirs();
        Request dwreq = new DownloadManager.Request(uri);
        dwreq.setTitle(mContext.getString(R.string.download_title, info.updateInfo.app_name));

        String filename = url.substring(url.lastIndexOf("/") + 1);
        klilog.info("download  file name = "+filename);
        dwreq.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                filename);
//        dwreq.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
        
        long id = dm.enqueue(dwreq);
        UpdateUtils.putDownloadInfo(mContext, id);
    }
}
