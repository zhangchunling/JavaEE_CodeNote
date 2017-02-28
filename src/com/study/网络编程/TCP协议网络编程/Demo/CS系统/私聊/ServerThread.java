package com.study.网络编程.TCP协议网络编程.Demo.CS系统.私聊;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 处理客户端发来的数据，是单发还是广播，以及判登录情况，将登录的用户与对应的PrintStream放进对应的MyMap中
 * @author Peter
 *
 */
public class ServerThread extends Thread{
	private Socket socket;
	BufferedReader br = null;
	PrintStream ps = null;
	/**
	 * 构造
	 * @param soc
	 */
	public ServerThread(Socket socket){
		this.socket = socket;
	}
		
	public void run(){
		try {
			//获取客户端的Socket对应的输入流
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//获取客户端的Socket对应的输出流
			ps = new PrintStream(socket.getOutputStream());
			
			String line = null;
			while((line=br.readLine())!=null){
				//如果是登录的用户名，说明需要将当前的信息和用户名保存
				if(line.startsWith(MyProtocol.USER_ROUND)&&line.endsWith(MyProtocol.USER_ROUND)){
					String userName = this.getRealMsg(line);//得到真实消息
					if(Server.clientsMap.containsKey(userName)){//如果用户名重复，返回-1
						System.out.println("重复");
						ps.println(MyProtocol.NAME_REP);
					}else{//不重复则返回1，并添加用户名和对应数据流
						ps.print(MyProtocol.LOGIN_SUCCESS);
						Server.clientsMap.put(userName, ps);
					}
				
				//如果是私聊信息，只向特定的输出流发送
				}else if(line.startsWith(MyProtocol.PRIVATE_ROUND)&&line.endsWith(MyProtocol.PRIVATE_ROUND)){
					//得到真实信息
					String userAndMsg = this.getRealMsg(line);
					//用协议字符分割数据，前面部分是要私聊的用户名，后面部分是聊天信息
					String user = userAndMsg.split(MyProtocol.SPLIT_SIGN)[0];
					String msg = userAndMsg.split(MyProtocol.SPLIT_SIGN)[1];
					//获取发送该私聊信息的用户名
					String userName = Server.clientsMap.getKeyByValue(ps);
					//获取要被私聊的客户端的Socket对应的PrintSteam流，并发送私聊信息
					Server.clientsMap.get(user).println(userName+"悄悄对你说："+msg);
				
				//公聊，要向每个Socket发送
				}else{
					String msg = this.getRealMsg(line);//得到真实信息
					for(PrintStream clientPs : Server.clientsMap.getValueSet()){
						String userName = Server.clientsMap.getKeyByValue(ps);//得到发信息的用户名
						clientPs.println(userName+"说："+msg);//发送
					}
				}
			}
		//如果有异常，说明该Socket对应的客户端已经出现了问题，需要所MyMap中删除	
		} catch (IOException e) {
			Server.clientsMap.removeByValue(ps);//删除
			System.out.println(Server.clientsMap.size());//查看还有剩下多少个
			//关闭连接、IO资源
			try {
				if(br!=null){
					br.close();
				}
				if(socket!=null){
					socket.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
			if(ps!=null){
				ps.close();
			}
			
			e.printStackTrace();
		}
		
	}
	

	/**
	 * 将读到的内容去掉前后的协议字符，恢复成真实数据
	 * @param line
	 * @return
	 */
	public String getRealMsg(String line){
		return line.substring(MyProtocol.PROTOCOL_LEN,line.length()-MyProtocol.PROTOCOL_LEN);
	}
}
