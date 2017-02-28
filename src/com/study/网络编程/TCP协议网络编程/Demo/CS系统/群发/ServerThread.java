package com.study.网络编程.TCP协议网络编程.Demo.CS系统.群发;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 处理Socket通信的线程类
 * 
 * @author Peter
 *
 */
public class ServerThread implements Runnable{
	
	Socket socket = null;	 //定义当前所处理的Socket
	BufferedReader br = null;//定义当前所处理的Socket对应的输入流
	
	/**
	 * 有参构造
	 * @throws IOException 正规的写法不应抛出，而直接try catch
	 */
	public ServerThread(Socket s) throws IOException{
		socket = s;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}	
	
	@Override
	public void run() {
		String content = null;
		while((content=this.readFromClient())!=null){//读到数据
			//广播 ：将该Socket的数据给MyServer服务器里的所有来访问服务器的客户端的Socket，包括发出该数据的客户端
			for(Socket s: MyServer.socketList){
				try {
					PrintStream ps = new PrintStream(s.getOutputStream());
					ps.println(content);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 读取客户端数据
	 * @return
	 */
	private String readFromClient(){
		String str = null;
		try {
			str = br.readLine();
		} catch (IOException e) {
			MyServer.socketList.remove(socket);//出现异常时 移除服务器里的该Socket
			e.printStackTrace();
		}
		return str;
	}

}
