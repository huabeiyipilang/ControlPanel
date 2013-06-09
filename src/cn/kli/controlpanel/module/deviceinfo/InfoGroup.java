package cn.kli.controlpanel.module.deviceinfo;

import java.util.ArrayList;
import java.util.List;

public class InfoGroup {
	public int name;
	private List<InfoChild> children = new ArrayList<InfoChild>();

	public void addChild(InfoChild child){
		if(!children.contains(child)){
			children.add(child);
		}
	}
}
