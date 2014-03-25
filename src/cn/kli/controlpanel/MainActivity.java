package cn.kli.controlpanel;

import android.os.Bundle;
import cn.kli.controlpanel.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFragment(GuideFragment.class);
    }

}
