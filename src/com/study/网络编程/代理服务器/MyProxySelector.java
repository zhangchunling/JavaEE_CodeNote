package com.study.网络编程.代理服务器;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;
/**
 * 通过继承ProxySelector实现自己定义的代理选择器
 * @author Peter
 *
 */
public class MyProxySelector extends ProxySelector{

	//实现该方法让代理选择器根据不同的URI来使用不同的代理服务器
	//该方法就是代理选择器管理网络连接使用代理服务器的关键
	@Override
	public List<Proxy> select(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	//当系统通过默认的代理服务器建立连接失败后，代理选择器将自动调用该方法
	//通过重写该方法可以对连接代理服务器失败的情形进行处理
	/**
	 * 系统默认的代理选择器也重写了该方法，它重写的处理策略是：
	 * 当系统设置的代理服务器失败时，默认代理选择器将会采用直连的方式连接远程资源，
	 * 所以当程序等待了足够长时间时，程序依然可以接收到远程资源的所有内容
	 */
	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		// TODO Auto-generated method stub
		
	}

}
