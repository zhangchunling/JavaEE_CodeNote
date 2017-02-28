package com.study.网络编程.代理服务器;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
/**
 * 代理服务器：
 * 除了创建Proxy对象和利用Proxy对象来打开URLConnetion连接外，其余就是对URLConnection的使用了
 * @author Peter
 *
 */
public class ProxyTest {
	Proxy proxy ;
	URL url;
	URLConnection conn;
	Scanner scan;
	PrintStream ps;
	String proxyAddress ="202.128.23.32";//代理服务器地址
	int proxyPort =8080;//代理服务器端口 
	
	public void init(){
		String urlStr="http://www.oneedu.cn";//试图打开的网站地址
		try {
			url = new URL(urlStr);//试图打开的网站地址
			//创建代理服务器对象
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort));
			//使用指定的代理服务器打开连接
			conn = url.openConnection(proxy);
			//设置超时时长5秒
			conn.setConnectTimeout(5000);
			scan = new Scanner(conn.getInputStream());
			//初始化流
			ps = new PrintStream("Index.htm");
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				System.out.println(line);//输出网页资源内容
				ps.println(line);//将网页内容输出到指定输出流
			}
		} catch (MalformedURLException e) {
			System.out.println(urlStr+"不是有效的网址");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(ps!=null){
				ps.close();
			}
		}		
				
	}
	
	public static void main(String[] args) {
		new ProxyTest().init();
	}
}
