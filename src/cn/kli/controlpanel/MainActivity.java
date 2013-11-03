package cn.kli.controlpanel;

import cn.kli.menuui.MenuUIActivity;

import com.baidu.mobads.appoffers.OffersManager;
import com.baidu.mobstat.StatService;

public class MainActivity extends MenuUIActivity {

	@Override
	protected void translateToFragment(String clsName) {
		if(Object.class.getName().equals(clsName)){
            StatService.onEvent(this, Baidu.EVENT_OPEN_ADS_WALL, "");
            OffersManager.showOffers(this);
            return;
		}
		super.translateToFragment(clsName);
	}

}
