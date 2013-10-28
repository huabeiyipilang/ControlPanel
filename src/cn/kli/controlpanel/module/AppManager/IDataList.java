package cn.kli.controlpanel.module.AppManager;

public interface IDataList {
	
	interface OnItemSelectListener{
		void onItemSelect(Object o);
	}
	void setOnItemSelectListener(OnItemSelectListener listener);
	void updateQueryString(String query);
	void selectTheFirst();
}
