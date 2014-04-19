package cn.kli.controlpanel.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import cn.kli.controlpanel.R;

/**
 * Created by carl on 14-4-2.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onStart() {
        super.onStart();
        for(BaseFragment fragment : getAvailableFragment()){
            fragment.onActivityStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(BaseFragment fragment : getAvailableFragment()){
            fragment.onActivityResume();
        }
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass){
        setContentFragment(fragmentClass, null);
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments){

        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            for(BaseFragment fragment : getAvailableFragment()){
                if(fragment.onBackKeyDown()){
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * BaseFragment
     * @Title: getAvailableFragment
     * @return
     * @return List<BaseFragment>
     * @date 2014-3-10 上午11:18:01
     */
    private List<BaseFragment> getAvailableFragment(){
        List<BaseFragment> res = new ArrayList<BaseFragment>();
        List<Fragment> list = getSupportFragmentManager().getFragments();
        if(list != null && list.size() != 0){
            for(Fragment fragment : list){
                if(fragment instanceof BaseFragment && fragment.isVisible()){
                    res.add((BaseFragment)fragment);
                }
            }
        }
        return res;
    }
}
