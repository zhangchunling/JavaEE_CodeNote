package com.study.网络编程.TCP协议网络编程.Demo.CS系统.群发;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * 读取Socket输入流(服务端的数据)
 * @author Peter
 *
 */
public class ClientThread implements Runnable {

	private Socket socket;
	BufferedReader br = null;
	public ClientThread(Socket s) throws IOException{
		socket= s;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	@Override
	public void run() {
		try {
			String content = null;
			//打印客户来的信息
			while ((content=br.readLine())!=null) {
				System.out.println(content);			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
