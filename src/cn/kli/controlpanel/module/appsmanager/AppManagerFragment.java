package cn.kli.controlpanel.module.appsmanager;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.module.appsmanager.IDataList.OnItemSelectListener;
import cn.kli.controlpanel.module.appsmanager.SearchManager.SearchMode;
import cn.kli.menuui.BaseFragment;

public class AppManagerFragment extends BaseFragment implements TextWatcher, IViewsContainer {
	private DigitsEditText input;
	private IDataList mList;
	private ViewGroup mListContainer;
	private T9KeyBoard mKeyBoard;
	private Handler mHandler;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_app_manager, null);
		//views
		mListContainer = (ViewGroup)root.findViewById(R.id.list_container);
		input = (DigitsEditText)root.findViewById(R.id.input);
		input.addTextChangedListener(this);
		mKeyBoard = (T9KeyBoard)root.findViewById(R.id.keyboard);
		mKeyBoard.setDigitsView(input);
		Mediator.getInstance().setContainer(this);
		Mediator.getInstance().setDigits(input);
		Mediator.getInstance().setKeyBoard(mKeyBoard);
		swapToMode(SearchMode.APP_MODE);
		return root;
	}


	@Override
	public void onResume() {
		super.onResume();
		Mediator.getInstance().showKeyboard();
	}



	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1,
			int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		mList.updateQueryString(arg0.toString());
	}
	
	
	
	@Override
	public boolean onBackPressed() {
		if (Mediator.getInstance().isKeyboardShowing()) {
			Mediator.getInstance().hideKeyboard();
			return true;
		} else {
			return false;
		}
	}

	private SearchMode mMode = SearchMode.APP_MODE;
	
	public void swapToMode(SearchMode mode){
		mList = DataListFactory.create(getActivity(), mMode);
		mList.setOnItemSelectListener(new OnItemSelectListener(){
			@Override
			public void onItemSelect(Object o) {
				input.getText().clear();
			}
		});
		mListContainer.removeAllViews();
		input.getText().clear();
		mListContainer.addView((View) mList);
		Mediator.getInstance().setDataList(mList);
	}

	@Override
	public void swapMode(SearchManager mode) {
		if(mMode == SearchMode.APP_MODE){
			mMode = SearchMode.CONTACTS_MODE;
		}else{
			mMode = SearchMode.APP_MODE;
		}
		swapToMode(mMode);
		
	}

}
