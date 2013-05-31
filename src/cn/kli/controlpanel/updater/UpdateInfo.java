package cn.kli.controlpanel.updater;

import android.os.Parcel;
import android.os.Parcelable;

public class UpdateInfo implements Parcelable {
	
	public String app_name;
	public String pkg_name;
	public String version_code;
	public String version_name;
	public String description;
	public String url;
	public String size;
	public String md5;
	
	
	private MyLog klilog = new MyLog(UpdateInfo.class);
	
	public UpdateInfo(){
		
	}
	
	private UpdateInfo(Parcel in){
		app_name = in.readString();
		pkg_name = in.readString();
		version_code = in.readString();
		version_name = in.readString();
		description = in.readString();
		url = in.readString();
		size = in.readString();
		md5 = in.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(app_name);
		out.writeString(pkg_name);
		out.writeString(version_code);
		out.writeString(version_name);
		out.writeString(description);
		out.writeString(url);
		out.writeString(size);
		out.writeString(md5);
	}
	
	public static final Parcelable.Creator<UpdateInfo> CREATOR = new Parcelable.Creator<UpdateInfo>(){

		@Override
		public UpdateInfo createFromParcel(Parcel in) {
			return new UpdateInfo(in);
		}

		@Override
		public UpdateInfo[] newArray(int size) {
			return new UpdateInfo[size];
		}
		
	};
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(app_name+";");
		builder.append(pkg_name+";");
		builder.append(version_code+";");
		builder.append(version_name+";");
		builder.append(description+";");
		builder.append(url+";");
		builder.append(size+";");
		builder.append(md5+";");
		
		return builder.toString();
	}
	
	public static UpdateInfo createFromString(String s) {
		UpdateInfo info = new UpdateInfo();
		String[] values = s.split(";");
		int i = 0;
		info.app_name = values[i++];
		info.pkg_name = values[i++];
		info.version_code = values[i++];
		info.version_name = values[i++];
		info.description = values[i++];
		info.url = values[i++];
		info.size = values[i++];
		info.md5 = values[i++];
		return info;
	}

	public void dump(){
		klilog.i("=========UpdateInfo===========");
		klilog.i("index			:"+app_name);
		klilog.i("version_from	:"+pkg_name);
		klilog.i("version_to	:"+version_code);
		klilog.i("version_to	:"+version_name);
		klilog.i("description	:"+description);
		klilog.i("url			:"+url);
		klilog.i("size			:"+size);
		klilog.i("md5			:"+md5);
	}
}
