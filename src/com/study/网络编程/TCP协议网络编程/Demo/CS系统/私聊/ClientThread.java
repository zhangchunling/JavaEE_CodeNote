package com.study.网络编程.TCP协议网络编程.Demo.CS系统.私聊;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 增加了对用户输入信息的判断：如果内容以双斜线(//)开头，并包冒号(:)，则认为用户想发私聊信息
 * 将冒号(:)之前部分当成私聊用户名，之后当天聊天信息。
 * @author Peter
 *
 */
public class ClientThread extends Thread{

	BufferedReader br = null;//该客户端负责处理的输入流
	/**
	 * 构造器
	 * @param br
	 */
	public ClientThread (BufferedReader br){
		this.br = br;
	}
	
	public void run(){
		
		try {
			String line = null;
			while((line=br.readLine())!=null){
				System.out.println(line);//打印
			}
			/*
			 * 本例仅打印了从服务器端读到内容。实际上，此处的情况可以更复杂：
			 * 如果我们希望客户端能看到聊天室的用户列表，则可以让服务器在每次有用户登录、用户退出时，
			 * 将所有用户列表信息都向客户发送一遍。为了区分服务器发送的是聊天信息，还是用户列表，
			 * 服务器也应该在要发送的信息、后都添加一定的协议字符串，客户端此处则根据协议字符串的不同而进行不同的处理。
			 * 更复杂的情况：
			 * 如果两端进行游戏，则还有可能发送游戏信息，例如两端进行了五子棋游戏，则还需要发送下棋坐标信息等，
			 * 服务器同样在这些下棋坐标信息前、后添加协议字符串后再发送，客户端就可以根据该信息知道对手的下棋坐标。
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(br!=null){					
					br.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
