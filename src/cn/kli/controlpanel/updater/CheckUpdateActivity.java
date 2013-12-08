package cn.kli.controlpanel.updater;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.kli.controlpanel.R;

public class CheckUpdateActivity extends Activity implements OnClickListener{
	private final static int MSG_SYNC_START = 1;
	private final static int MSG_SYNC_FINISHED = 2;
	private final static int MSG_CHECK_UPDATE = 3;
	private final static int MSG_CHECK_FINISHED = 4;

	private MyLog klilog = new MyLog(this.getClass());
	//views
	private AppListView mAppList;
	
	private ProgressDialog mWaitingDialog;
	private UpdateManager mManager;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SYNC_START:
				showCheckingDialog();
				Message sync_msg = mHandler.obtainMessage(MSG_SYNC_FINISHED);
				mManager.sync(sync_msg);
				break;
			case MSG_SYNC_FINISHED:
				onSyncFinished(msg);
				break;
			case MSG_CHECK_UPDATE:
				mAppList.setRemoteList(mManager.getUpdateList(), obtainMessage(MSG_CHECK_FINISHED));
				break;
			case MSG_CHECK_FINISHED:
				hideCheckingDialog();
				break;
			}
		}
	};
	
	private void showCheckingDialog(){
		mWaitingDialog = new ProgressDialog(CheckUpdateActivity.this);
		mWaitingDialog.setMessage(getResources().getString(R.string.checking));
		mWaitingDialog.setCancelable(false);
		mWaitingDialog.show();
	}
	
	private void hideCheckingDialog(){
		if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
			mWaitingDialog = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_check);
		mManager = UpdateManager.getInstance(this);
		findViewById(R.id.check_update).setOnClickListener(this);
		mAppList = (AppListView)findViewById(R.id.app_list_view);
		Toast.makeText(this, R.string.loading_local_module, Toast.LENGTH_SHORT).show();
	}

	private void onSyncFinished(Message msg){
		switch (msg.arg1) {
		case UpdateManager.SYNC_SUCCESS:
			mHandler.sendEmptyMessage(MSG_CHECK_UPDATE);
			break;
		case UpdateManager.SYNC_FAIL:
			Toast.makeText(CheckUpdateActivity.this,
					R.string.sync_failed, Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.check_update:
			mHandler.sendEmptyMessage(MSG_SYNC_START);
			break;
		}
	}


}
