package cn.kli.controlpanel;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import cn.kli.controlpanel.base.BaseFragment;

public class BlackActivity extends SherlockFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);
        Class<BaseFragment> fragment = (Class<BaseFragment>) getIntent().getSerializableExtra("fragment");
        setContentFragment(fragment);
    }
    
    private void setContentFragment(Class<? extends BaseFragment> fragmentClass) {
        Bundle arguments = null;
        if (getIntent() != null) {
            arguments = getIntent().getExtras();
        }
        setContentFragment(fragmentClass, arguments);
    }

    private void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments) {

        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, fragment);
        t.commit();
    }
}
