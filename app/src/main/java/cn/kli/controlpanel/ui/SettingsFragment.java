package cn.kli.controlpanel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import cn.kli.controlpanel.R;
import cn.kli.controlpanel.framework.BaseFragment;
import cn.kli.controlpanel.service.MainService;
import cn.kli.controlpanel.utils.Prefs;

/**
 * Created by carl on 14-4-16.
 */
public class SettingsFragment extends BaseFragment {

    private CheckBox mServiceSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate((R.layout.fragment_settings), container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mServiceSwitch = (CheckBox)getView().findViewById(R.id.cb_enable_service);
        mServiceSwitch.setChecked(Prefs.getInstance().getBoolean(Prefs.BOOLEAN_SERVICE_ENABLE, true));
        mServiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getActivity().startService(new Intent(getActivity(), MainService.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), MainService.class));
                }
            }
        });
    }
}
