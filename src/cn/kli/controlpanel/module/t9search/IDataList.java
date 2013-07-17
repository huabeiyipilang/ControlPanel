package cn.kli.controlpanel.module.t9search;

public interface IDataList {
	
	interface OnItemSelectListener{
		void onItemSelect(Object o);
	}
	void setOnItemSelectListener(OnItemSelectListener listener);
	void updateQueryString(String query);
	void selectTheFirst();
}
