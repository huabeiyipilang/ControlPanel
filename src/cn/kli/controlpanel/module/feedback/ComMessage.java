package cn.kli.controlpanel.module.feedback;

import android.os.Parcel;
import android.os.Parcelable;

public class ComMessage implements Parcelable{
	final static int SUCCESS = 1;
	final static int ERROR_NETWORK_NOT_ENABLE = 2;
	final static int ERROR_HOST = 3;
	final static int ERROR_UNKNOWN = 4;
	
	public String tag;
	public String title;
	public String content;
	public int cause;
	
	public ComMessage(){
	}
	
	private ComMessage(Parcel in){
		tag = in.readString();
		title = in.readString();
		content = in.readString();
		cause = in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(tag);
		out.writeString(title);
		out.writeString(content);
		out.writeInt(cause);
	}
	
	public static final Parcelable.Creator<ComMessage> CREATOR = new Parcelable.Creator<ComMessage>(){

		@Override
		public ComMessage createFromParcel(Parcel in) {
			return new ComMessage(in);
		}

		@Override
		public ComMessage[] newArray(int size) {
			return new ComMessage[size];
		}
	};
	
	public String getCause(){
		String res = "";
		switch(cause){
		case SUCCESS:
			res = "SUCCESS";
			break;
		case ERROR_NETWORK_NOT_ENABLE:
			res = "ERROR_NETWORK_NOT_ENABLE";
			break;
		case ERROR_HOST:
			res = "ERROR_HOST";
			break;
		case ERROR_UNKNOWN:
			res = "ERROR_UNKNOWN";
			break;
		}
		return res;
		
	}
}
