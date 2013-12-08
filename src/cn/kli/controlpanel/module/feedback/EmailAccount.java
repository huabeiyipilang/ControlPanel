package cn.kli.controlpanel.module.feedback;

import android.content.Context;
import cn.kli.controlpanel.R;

public class EmailAccount {
	
	public String host;
	public String auth;
	public String address;
	public String username;
	public String password;
	
	public EmailAccount(Context context){
		host = context.getResources().getString(R.string.email_account_host);
		address = context.getResources().getString(R.string.email_account_address);
		username = context.getResources().getString(R.string.email_account_username);
		password = context.getResources().getString(R.string.email_account_password);
		auth = context.getResources().getString(R.bool.email_account_auth);
	}
}
