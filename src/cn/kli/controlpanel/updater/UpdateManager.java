package cn.kli.controlpanel.updater;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public class UpdateManager {
	private MyLog klilog = new MyLog(this.getClass());
	public static final int SYNC_SUCCESS = 1;
	public static final int SYNC_FAIL = 2;

	public static final int CHECK_FAILED = 1;
	public static final int CHECK_NO_UPDATE = 2;
	public static final int CHECK_HAS_UPDATE = 3;
	public static final int CHECK_NO_ROLLBACK = 4;
	public static final int CHECK_HAS_ROLLBACK = 5;
	
	private static UpdateManager sInstance = null;
	private Context mContext;
	private List<UpdateInfo> mUpdateList;
    
	private UpdateManager(Context context){
		mContext = context;
	}
	
	public static UpdateManager getInstance(Context context){
		if(sInstance == null){
			sInstance = new UpdateManager(context);
		}
		return sInstance;
	}
	
	public void sync(Message callback){
		new SyncDataTask(callback).execute("");
	}
	
	public List<UpdateInfo> getUpdateList(){
		return mUpdateList;
	}

	private class SyncDataTask extends AsyncTask {
		
		private Message callback;

		public SyncDataTask(Message msg){
			callback = msg;
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			String url = UpdateUtils.URL_TO_CHECK_UPDATE;
			return SyncData(url);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			callback.arg1 = (Integer) result;
			callback.sendToTarget();
		}
		
		private int SyncData(String syncUrl) {
			int res = SYNC_SUCCESS;
			HttpGet getMethod = new HttpGet(syncUrl);
			HttpClient httpClient = new DefaultHttpClient();
			try {
				HttpResponse response = httpClient.execute(getMethod);
				String result = EntityUtils.toString(response.getEntity(),
						UpdateUtils.ENCODE);
				int code = response.getStatusLine().getStatusCode();
				if (200 == code) { // get code of httpresponse
					UpdateInfoHelper helper = new UpdateInfoHelper(result);
					mUpdateList = helper.getUpdateList();
				}else{
					klilog.e("Http response error, code:" + code);
					res = SYNC_FAIL;
				}
			} catch (ClientProtocolException e) {
				res = SYNC_FAIL;
				e.printStackTrace();
			} catch (IOException e) {
				res = SYNC_FAIL;
				e.printStackTrace();
			}
			return res;
		}

	}
}
