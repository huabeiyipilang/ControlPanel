package cn.kli.controlpanel.module.t9search;

import cn.kli.controlpanel.module.t9search.SearchManager.SearchMode;
import android.content.Context;

public class DataListFactory {
	public static IDataList create(Context context, SearchMode mode){
		IDataList list = null;
		switch(mode){
		case APP_MODE:
			list = new AppsListView(context);
			break;
		case CONTACTS_MODE:
			list = new ContactsList(context);
			break;
		}
		return list;
	}
}
