package com.study.网络编程.TCP协议网络编程;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * InetAddress类:
 * 注意区分静态方法与一般方法
 * @author Peter
 *
 */
public class InetAddress_note {

	public static void main(String[] args) {
		try {
			/*
			 * 1.直接获取本机的InetAddress对象：
			 * dfsj0062-PC/192.168.2.181
			 */
			InetAddress net	   = InetAddress.getLocalHost(); 
			String hostAddress = net.getHostAddress();	//获取些IP地址字符串： 192.168.2.181
			String hostName    = net.getHostName();		//获取此IP地址的主机名： dfsj0062-PC
			
			/*
			 * 2.通过主机名儿取InetAddress实例对象：
			 * dfsj0062-PC/192.168.2.181
			 */
			InetAddress net1 = InetAddress.getByName("dfsj0062-PC");
			
			/*
			 * 3.通过原始IP地址获取InetAddress对象:
			 * /192.168.2.181
			 * 由上可知，返回的InetAddress对象是/192.168.2.181，而不是：dfsj0062-PC/192.168.2.181
			 * 但实际该对象里含有主机名与IP地址，所以依然能用下面两个方法
			 * 
			 */
			byte b[] = {(byte)192,(byte)168,(byte)2,(byte)181};
			InetAddress net2    = InetAddress.getByAddress(b);
			String hostAddress2 = net2.getHostAddress();	// 192.168.2.181
			String hostName2    = net2.getHostName();		// dfsj0062-PC
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
