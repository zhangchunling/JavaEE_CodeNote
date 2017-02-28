package com.study.网络编程.UDP协议网络编程.即时通信;

import java.net.SocketAddress;

/**
 * 该类封装用户名、图标、对应的SocketAddress、交谈窗口、失去联系次数等。
 * 通过该类的封装，所有的客户端只需要维护该UserInfo类的列表，就可以广播、私聊
 * @author Peter
 *
 */
public class UserInfo {
	private String icon;//该用户的图标
	private String name;//该用户的名字
	private SocketAddress address;//该用户的MulticastSocket所在的IP和端口
	private int lostTimes;//该用户失去联系的次数据
	private ChatFrame chatFrame;//该用户对应的交谈窗口
	/**
	 * 无参构造 
	 */
	public UserInfo(){}
	
	/**
	 * 自定义有参构造
	 */
	public UserInfo(String icon,String name,SocketAddress addr,int lostTimes){
		this.icon=icon;
		this.name=name;
		this.address=addr;
		this.lostTimes=lostTimes;
	}

	/**
	 * 重写hashCode方法
	 */
	public int hashCode(){
		return this.address.hashCode();
	}
	
	/**
	 * 重写equals方法：
	 * 传的参数，是当前类的实例，则比较地址是否相等
	 */
	public boolean equals(Object obj){
		if(obj!=null&&obj.getClass()==UserInfo.class){
			return ((UserInfo)obj).getAddress().equals(address);
		}
		return false;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SocketAddress getAddress() {
		return address;
	}

	public void setAddress(SocketAddress address) {
		this.address = address;
	}

	public int getLostTimes() {
		return lostTimes;
	}

	public void setLostTimes(int lostTimes) {
		this.lostTimes = lostTimes;
	}

	public ChatFrame getChatFrame() {
		return chatFrame;
	}

	public void setChatFrame(ChatFrame chatFrame) {
		this.chatFrame = chatFrame;
	}

	
	
}
