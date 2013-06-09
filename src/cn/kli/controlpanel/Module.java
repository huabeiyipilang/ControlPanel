package cn.kli.controlpanel;

public class Module {
	public int name;
	public int icon;
	public Class<?> cls;
	Group parent;
	
	public void setParentGroup(Group group){
		parent = group;
		parent.addChild(this);
	}
}
