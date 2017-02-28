package com.study.网络编程.TCP协议网络编程.Demo.CS系统.群发;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 服务端(ServerSocket监听主类)：
 * 	服务器在接收客户端连接之后，实户端可能需要和服务器长间通信，即服务器不断读客户端的数据和向客户端写数据；
 * 客户端也要不断地读取务器的数据和向服务器写数据。当我们使用传统的BufferedReader的readLine()时，
 * 在该方法成功返回之前，线程被阻塞，程序无法继续执行。所以服务器要为每个Socket单独启动一条线程，一条线程
 * 负责与一个客户端进行通信。
 *  一个客户端发送给服务器的数据，被服务器通过多线程发给每个客户端的socket就实现了 广播。
 *  
 * @author Peter
 * 详情参见《JAVA疯狂讲义》第17章 ——17.3.4加入多线程
 *
 */
public class MyServer {
	//定义保存所有Socket的List
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
	
	public static void main(String[] args) throws IOException {
		System.out.println("服务器：");
		//创建端口号为2500的服务器
		ServerSocket server = new ServerSocket(25000);	
		
		while(true){//接受多个客户端的访问			
			
			Socket socket = server.accept();// accept()方法这儿会阻塞，将一直等待客户端的请求
			socketList.add(socket);//来一个客户端的socket就添加进集合
			
			//每当来一个客户端连接，就启动一条ServerThread线程为该客户端服务
			new Thread(new ServerThread(socket)).start();
		}
		
	}
/**
 * 该服务器端会出现以下异常： java.net.SocketException: Connection reset或者Connect reset by peer:Socket write error）。
 * 该异常在客户端和服务器端均有可能发生，引起该异常的原因有两个，
 * 第一个就是如果一端的Socket被关闭（或主动关闭或者因为异常退出而引起的关闭），另一端仍发送数据，发送的第一个数据包引发该异常(Connect reset by peer)。
 * 另一个是一端退出，但退出时并未关闭该连接，另一端如果在从连接中读数据则抛出该异常（Connection reset）。
 * 简单的说就是：在连接断开后的读和写操作引起的。 
 */
}
