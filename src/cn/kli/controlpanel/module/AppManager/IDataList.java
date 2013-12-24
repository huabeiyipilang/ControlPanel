package cn.kli.controlpanel.module.appmanager;

public interface IDataList {
	
	interface OnItemSelectListener{
		void onItemSelect(Object o);
	}
	void setOnItemSelectListener(OnItemSelectListener listener);
	void updateQueryString(String query);
	void selectTheFirst();
}
