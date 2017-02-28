package com.study.网络编程.TCP协议网络编程.Demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * TCP服务器
 * TCP通信原理：
 * 利用ServerSocket建立 TCP服务器，利用Socket建立TCP客户端。
 * 服务器端通过ServerSocket建立监听(即用accept()方法监听来自客户端的Socket连接，没有则等侍)，
 * 客户端端通过Socket连接到指定服务器后(new一个Socket)，服务器端和客户端就会产生一个互相连接的Socket。
 * 然后，双方就通过Socket的IO流方法进行通信。
 * @author Peter
 *
 */
public class TCPServer {

	public static void main(String[] args) {
		System.out.println("服务器");
		TCPServer server = new TCPServer();
		server.note();
	}
	
public void note(){
	ServerSocket server = null;
	try {
		/*
		 * 构造：
		 * ServerSocket(int port): 用指定的端口(0~65535)创建一个ServerSocket：客户端访问时也要是访端口
		 * ServerSocket(int port,int backlog): backlog:改变连接队列长度的参数
		 * ServerSocket(int port,int backlog,InetAddress addr):机器存在多个IP时，通过addr绑定指定的IP地址。
		 */
		server = new ServerSocket(10000);//推荐用1万以上的端口，避免冲突
		/*accept()方法：
		 * 监听客户端连接请求，如果接收到，该方法会返回一个与客户端Socket对应的Socket，
		 * 否则该方法一直处于等待状态，线程也被阻塞。
		 * */
		while(true){//之所有用死循环，因为可能会有多个客户端来访问
			Socket socket = server.accept();
			socket.setSoTimeout(10000);//10秒后还未读写完成，就会招抛出SocketTimeoutException异常
			
			//将Socket对应的输出流包装成PrintStream
			PrintStream ps = new PrintStream(socket.getOutputStream());			
			ps.print("您好，我是来自服务器的信息");//进行普通IO操作
						
			ps.close();//关闭输出流
			socket.close();//关闭Socket
		}
		
	} catch (IOException e) {
		e.printStackTrace();
	}finally{
		try {
			server.close();//关闭服务器
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}






}
