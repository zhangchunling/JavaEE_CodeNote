package com.study.网络编程.代理服务器;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Scanner;

/**
 * 代理服务器选抒器
 * @author Peter
 *
 */
public class ProxySelectorTest {

	/**
	 * 测试本地JVM的网络默认配置
	 * 如果要访问的路径是HTTP的，则对应去找下面设置的HTTP的代理服务器
	 * 如果要访问的路径是HTTPS的，则对应去找下面设置的HTTPS的代理服务器
	 * 如果要访问的路径是FTP的，则对应去找下面设置的FTP的代理服务器
	 * 如果要访问的路径是SOCKES的，则对应去找下面设置的SOCKS的代理服务器
	 */
	public void setLocalProxy(){
		//获得系统的Properties，几个方法里得到的都是一个
		Properties prop = System.getProperties();
		//1.设置HTTP访问要使用的代理服务器的地址
		prop.setProperty("http.proxHost", "10.10.0.96");
		//设置HTTP访问要使用的代理服务器的端口
		prop.setProperty("http.proxyPort", "8080");
		//设置HTTP访问不需要通过代理服务器访问的主机      /**表示访问哪些主机不需要使用代理服务器*/
		prop.setProperty("http.nonProxyHosts", "localhost|10.20.*");
		//2.设置安全HTTPS访问使用的代理服务器地址与端口,
		//它没有https.nonProxyHosts属性，它按照http.nonProxyHosts中设置的规则访问
		prop.setProperty("https.proxyHost", "94.73.234.75");//俄罗斯代理IP,可访问goole
		prop.setProperty("https.proxyPort", "3128");
		//3.设置FTP访问的代理服务器的主机、端口及不需要使用代理服务器的主机
		prop.setProperty("ftp.proxyHost", "10.10.0.96");
		prop.setProperty("ftp.proxyPort", "2121");
		prop.setProperty("ftp.nonProxyHosts", "localhost|10.10.*");
		//4.设置socks代理服务器的地址与端口
		prop.setProperty("socks.ProxyHost", "10.10.0.96");
		prop.setProperty("socks.ProxyPort", "1080");
	}
	
	/**
	 * 清除proxy设置
	 */
	public void removeLocalProxy(){
		//获得系统的Properties，几个方法里得到的都是一个
		Properties prop = System.getProperties();
		//清除HTTP访问的代理服务器设置
		prop.remove("http.proxyHost");
		prop.remove("http.proxyPort");
		prop.remove("http.nonProxyHosts");
		//清除HTTPS访问的代理服务器设置
		prop.remove("https.proxyHost");
		prop.remove("https.proxyPort");
		//清除FTP访问的代理服务器设置
		prop.remove("ftp.proxyHost");
		prop.remove("ftp.proxyPort");
		prop.remove("ftp.nonProxyHosts");
		//清除SOCKS的代理服务器设置
		prop.remove("socks.ProxyHost");
		prop.remove("socks.ProxyPort");
	}
	
	/**
	 * 测试HTTP访问
	 * @throws IOException 
	 */
	public void showHttpProxy() throws IOException{
		URL url = new URL("https://www.google.com.hk/");//要访问的网页地址
		URLConnection conn = url.openConnection();//直接打开连接，但系统会调调用刚设置的HTTP代理服务器
		Scanner scan = new Scanner(conn.getInputStream());
		while(scan.hasNextLine()){//读取远程主机内容
			System.out.println(scan.nextLine());
		}
	}
	
	public static void main(String[] args) throws IOException {
		ProxySelectorTest test = new ProxySelectorTest();
	/*	就下面的代码指定只是HTTP的代理服务器
	 * //获得系统的Properties，几个方法里得到的都是一个
		Properties prop = System.getProperties();
		//1.设置HTTP访问要使用的代理服务器的地址
		prop.setProperty("http.proxHost", "10.10.0.96");
		//设置HTTP访问要使用的代理服务器的端口
		prop.setProperty("http.proxyPort", "8080");
		
		URL url = new URL("http://www.baidu.com");//要访问的网页地址
		URLConnection conn = url.openConnection();//直接打开连接，但系统会调调用刚设置的HTTP代理服务器
		Scanner scan = new Scanner(conn.getInputStream());
		while(scan.hasNextLine()){//读取远程主机内容
			System.out.println(scan.nextLine());
		}*/	
		test.setLocalProxy();//根据链接自动选择代理服务器（HTTP或HTTPS或FTP或SOCKS）
		test.showHttpProxy();
		test.removeLocalProxy();
	}
}
