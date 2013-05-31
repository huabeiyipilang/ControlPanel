package cn.kli.controlpanel.updater;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class UpdateReceiver extends BroadcastReceiver {
	MyLog klilog = new MyLog(UpdateReceiver.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
			long cache_id = UpdateUtils.getDownloadId(context);
			long downloadId = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			if (cache_id != downloadId) {
				Log.e("dfdun", "download id = " + downloadId);
				return;
			}
			DownloadManager dm = ((DownloadManager) context
					.getSystemService("download"));
			Query query = new Query();
			query.setFilterById(cache_id);
			Cursor c = dm.query(query);
			if (c.moveToFirst()) {
				int columnIndex = c
						.getColumnIndex(DownloadManager.COLUMN_STATUS);
				if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
					String update_uri = c.getString(c
									.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
//					autoInstall(update_uri);
					install(context, update_uri);
					//wipe cache
					UpdateUtils.putDownloadInfo(context, 0);
				}
			}
		}
	}
	
	private void install(Context context, final String uri){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(uri),"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	private void autoInstall(final String uri){
		new Thread(){

			@Override
			public void run() {
				super.run();
				String path = uri.substring(uri.indexOf("/mnt"));
				klilog.i("install start file = "+path);
				long start = System.currentTimeMillis();
				rootCommand("pm install -r "+ path + "\n");
				klilog.i("install success use time:"+(System.currentTimeMillis() - start));
			}
			
		}.start();
	}
	
	private boolean rootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
				// nothing
			}
		}

		Log.d("*** DEBUG ***", "Root SUC ");
		return true;
	}
	
	private class InstallThread extends Thread { 
		private String mPath;
		private Context mContext;

		public InstallThread(Context context, String path){
			mPath = path;
			mContext = context;
		}

		public void run() { 
			Process process = null; 
			OutputStream out = null; 
			InputStream in = null; 
			try { 
				klilog.i("install start file = "+mPath);
				long start = System.currentTimeMillis();
				// 请求root 
				process = Runtime.getRuntime().exec("su");  
				out = process.getOutputStream(); 
				// 调用安装 
				out.write(("pm install -r " + mPath + "\n").getBytes()); 
				/*
				in = process.getInputStream(); 
				int len = 0; 
				byte[] bs = new byte[256]; 
				while (-1 != (len = in.read(bs))) { 
					String state = new String(bs, 0, len); 
					if (state.equals("Success\n")) {
						Toast.makeText(mContext, "Install success!", Toast.LENGTH_LONG).show();
						klilog.i("install success use time:"+(System.currentTimeMillis() - start));
					} else{
						klilog.i("install failed use time:"+(System.currentTimeMillis() - start));
					}
				} */
			} catch (Exception e) { 
				Toast.makeText(mContext, "Install failed!", Toast.LENGTH_LONG).show();
				e.printStackTrace(); 
			} finally { 
				try { 
					if (out != null) { 
						out.flush(); 
						out.close(); 
					} 
					if (in != null) { 
						in.close(); 
					} 
				} catch (IOException e) { 
					e.printStackTrace(); 
				} 
			} 
		} 
	}
}
