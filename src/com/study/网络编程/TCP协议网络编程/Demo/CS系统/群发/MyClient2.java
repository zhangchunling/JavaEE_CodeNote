package com.study.网络编程.TCP协议网络编程.Demo.CS系统.群发;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * 客户端：写数据到Socket的输出流里，好发送给服务器
 *  每个客户端包含2条线程：一条负责用户的键盘输入，并将数据写入Socket对应的输出流中，MyClient就是；
 *  一条负责读取Socket对应的输入流数据（服务端来的），并将这些数据打印输出，ClientThread就是。
 *  
 * @author Peter
 *
 */
public class MyClient2 {

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("客户端2：");
		Socket socket = new Socket("192.168.2.181", 25000);//指定IP和端口连接服务器
		new Thread(new ClientThread(socket)).start();//启动ClentThread线程读取服务器的数据
		
		//获取Socket的输出流，用来发数据给服务器
		PrintStream ps = new PrintStream(socket.getOutputStream());
		String line = null;
		System.out.println("请输入内容：");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while((line=br.readLine())!=null){
			ps.println(line);//将用户输入的信息写Socket输出流，好发送给服务器
		}
	}
}
