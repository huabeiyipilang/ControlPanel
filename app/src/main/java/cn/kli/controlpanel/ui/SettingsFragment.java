package cn.kli.controlpanel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import cn.kli.controlpanel.R;
import cn.kli.controlpanel.base.BaseFragment;
import cn.kli.controlpanel.service.MainService;

/**
 * Created by carl on 14-4-16.
 */
public class SettingsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate((R.layout.fragment_settings), container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((CheckBox)getView().findViewById(R.id.cb_enable_service)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    getActivity().startService(new Intent(getActivity(), MainService.class));
                }else{
                    getActivity().stopService(new Intent(getActivity(), MainService.class));
                }
            }
        });
    }
}
