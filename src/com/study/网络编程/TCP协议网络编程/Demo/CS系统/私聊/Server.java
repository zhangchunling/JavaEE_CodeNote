package com.study.网络编程.TCP协议网络编程.Demo.CS系统.私聊;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Peter
 *
 */
public class Server {
	//服务器端口
	private static final int SERVER_PORT = 33333;
	//使用MyMap保存每个客户端名字和对应输出流
	public static MyMap<String, PrintStream> clientsMap = new MyMap<String,PrintStream>();
	
	public void init(){
		ServerSocket server = null;
		try {
			server = new ServerSocket(SERVER_PORT);
			while(true){//利用死循环可不断接收多个客户端的访问
				Socket socket = server.accept();//等待客户端访问
				new ServerThread(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
