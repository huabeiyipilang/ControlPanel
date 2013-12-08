package cn.kli.controlpanel.module.feedback;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import cn.kli.utils.klilog;

public abstract class Sender {
	
	private final static int MSG_SEND = 1;
	private final static int MSG_SENT = 2;
	
	private HandlerThread mHandlerThread;
	
	private Handler mHandler;
	private Message mCallback;
	
	interface SenderCallBack{
		void onSendCompleted(ComMessage cmsg);
	}
	
	public Sender(Context context){
		mHandlerThread = new HandlerThread("handler_thread");
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper()){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				klilog.info(msgToString(msg.what));
				switch(msg.what){
				case MSG_SEND:
					Message send_msg = obtainMessage(MSG_SENT);
					send_msg.obj = onMsgSend((ComMessage)msg.obj);
					send_msg.sendToTarget();
					break;
				case MSG_SENT:
					onMsgSent((ComMessage)msg.obj);
					break;
				}
			}
			
		};
	}
	
	public void send(ComMessage cmsg, Message callback){
		mCallback = callback;
		Message msg = mHandler.obtainMessage(MSG_SEND);
		msg.obj = cmsg;
		msg.sendToTarget();
	}
	
	abstract ComMessage onMsgSend(ComMessage cmsg);
	
	private void onMsgSent(ComMessage cmsg){
		mCallback.obj = cmsg;
		mCallback.sendToTarget();
	}

	private String msgToString(int msg){
		String res = "";
		switch(msg){
		case MSG_SEND:
			res = "MSG_SEND";
			break;
		case MSG_SENT:
			res = "MSG_SENT";
			break;
		}
		return res;
	}
}
