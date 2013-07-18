package cn.kli.controlpanel.module.feedback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import cn.kli.controlpanel.R;

public class ComposeMessageActivity extends Activity implements OnClickListener {

	private final static int MSG_SEND_MESSAGE = 1;
	private final static int MSG_MESSAGE_SENT = 2;
	private String mType;
	private ProgressDialog mDialog;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_SEND_MESSAGE:
				showDialog();
				EmailSender sender = new EmailSender(ComposeMessageActivity.this);
				sender.send(createMessage(), this.obtainMessage(MSG_MESSAGE_SENT));
				break;
			case MSG_MESSAGE_SENT:
				hideDialog();
				Toast.makeText(ComposeMessageActivity.this, R.string.thx_for_feedback, Toast.LENGTH_LONG).show();
				finish();
				break;
			}
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_compose_message);
	    mType = getType();
	    this.setTitle(mType);
	    findViewById(R.id.bt_msg_commit).setOnClickListener(this);
	}
	
	private String getType(){
		int cls = getIntent().getIntExtra("module_name", 0);
		return getString(cls);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.bt_msg_commit:
			mHandler.sendEmptyMessage(MSG_SEND_MESSAGE);
			break;
		}
	}

	private ComMessage createMessage(){
		ComMessage msg = new ComMessage();
		EditText editor = (EditText)findViewById(R.id.et_msg_edit);
		String version = "unknown";
		try {
			version = "v"+this.getPackageManager().getPackageInfo(this.getPackageName(), 1).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg.tag = mType;
		msg.title = "version:"+version;
		msg.content = editor.getText().toString();
		return msg;
	}
	
	private void showDialog(){
		mDialog = new ProgressDialog(this);
		mDialog.show();
	}
	
	private void hideDialog(){
		if(mDialog != null && mDialog.isShowing()){
			mDialog.dismiss();
			mDialog = null;
		}
	}
}
