package cn.kli.controlpanel.framework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import cn.kli.controlpanel.R;

/**
 * Created by carl on 14-4-2.
 */
public class BaseFragment extends Fragment {

    protected boolean onBackKeyDown(){
        return false;
    }

    protected void onActivityResume(){

    }

    protected void onActivityStart(){

    }

    protected void replaceFragment(Class<?> fragmentClass, Bundle arguments) {
        Fragment fragment = Fragment.instantiate(getActivity(), fragmentClass.getName(), arguments);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    protected void openFragment(Fragment fromFragment, Class<?> fragmentClass, Bundle arguments) {
        Fragment fragment = Fragment.instantiate(getActivity(), fragmentClass.getName(), arguments);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(fromFragment);
        transaction.add(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
