package com.study.网络编程.TCP协议网络编程.Demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * TCP客户端
 * TCP通信原理：
 * 利用ServerSocket建立 TCP服务器，利用Socket建立TCP客户端。
 * 服务器端通过ServerSocket建立监听(即用accept()方法监听来自客户端的Socket连接，没有则等侍)，
 * 客户端端通过Socket连接到指定服务器后(new一个Socket)，服务器端和客户端就会产生一个互相连接的Socket。
 * 然后，双方就通过Socket的IO流方法进行通信。
 * @author Peter
 *
 */
public class TCPClient {

	public static void main(String[] args) {
		new TCPClient().note();
	}
	public void note(){
		try {
		/*
		 * Socket常用构造器：
		 * 1.Socket(InetAddress address, int port) ：并将其连接到指定 IP地址的指定端口号。
		 * 2.Socket(String host, int port)：   将其连接到指定主机上的指定端口号。用的较多。
		 * 3.Socket(InetAddress address, int port, InetAddress localAddr, int localPort): 
      	 *  创建一个套接字并将其连接到指定远程地址上的指定远程端口,并指定本地IP地址和本地端口号，适于本地主机有多个IP的情形
		 */
			//连接指定服务器，让服务器的ServerSocket的accept()向下执行。于是服务器端和客户端就产生一对互相连接的Socket
			Socket socket = new Socket("192.168.2.181", 10000);
			
			socket.setSoTimeout(10000);//10秒后还未读写完成，就会招抛出SocketTimeoutException异常
			
			//将Socket对应的输入流包装成BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = br.readLine();
			System.out.println("来自服务器的数据："+line);
					
			//关闭输入流、Socket
			br.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
