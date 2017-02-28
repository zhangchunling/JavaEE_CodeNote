package com.study.网络编程.TCP协议网络编程.Demo.CS系统.私聊;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * 客户端：
 * @author Peter
 *
 */
public class Client {

	private static final int SERVER_PORT = 33333;//服务器端口号
	private Socket socket;
	private PrintStream ps;
	private BufferedReader brServer;//服务器传来的数据
	private BufferedReader inputMsg;//键盘输入信息
	
	public static void main(String[] args) {
		Client client = new Client();
		client.init();//初始化登录
		client.readAndSend();//读取
	}
	/**
	 * 初始化：登录
	 */
	public void init(){		
		try {
			//连接到服务器
			socket = new Socket("", this.SERVER_PORT);
			//得到Socket的输出流
			ps = new PrintStream(socket.getOutputStream());
			//得到Socket的输入流
			brServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String tip = "";
			//采用死循环不断地弹出对话框，要求输入用户名
			while(true){
				String userName = JOptionPane.showInputDialog(tip+"输入用户名：");//showInputDialog可保存打印文件
				//***将输入的用户前后增加协议字符后发送***
				ps.println(MyProtocol.USER_ROUND+userName+MyProtocol.USER_ROUND);
				//读取服务器和响应
				String result = brServer.readLine();
				//如果用户名重复，继续重输
				if(result.equals(MyProtocol.NAME_REP)){
					tip = "用户名重复，请重新";
					continue;
				}
				//如果登录成功(在服务器没有该用户名算登录成功)，则结束循环
				if(result.equals(MyProtocol.LOGIN_SUCCESS)){
					break;
				}
				
			}
			//以该Socket对应的输入流启动ClientThread线程：处理服务器返回的数据
			new ClientThread(brServer).start();
			
		//捕捉到异常，关闭资源并退出程序
		} catch (UnknownHostException e) {
			System.out.println("找不到远程服务器，请确定服务器已经启动！");
			this.closeAll();//关闭
			System.exit(1); //退出
		} catch (IOException e) {
			System.out.println("网络异常，请重新登录！");
			this.closeAll();
			System.exit(1);
			e.printStackTrace();
		}
	}
	/**
	 * 读取键盘的输出，并向网络发送信息
	 * 要私聊，则输入信息格式为：@用户名：内容；
	 * 不私聊，则只输入信息就行了
	 */
	public void readAndSend(){
		//键盘输入
		inputMsg = new BufferedReader(new InputStreamReader(System.in));
		try {
			//不断读取键盘的输入 
			String line = null;
			while((line=inputMsg.readLine())!=null){
				//如果发送信息中有冒号，且以//开头，则是私聊信息
				if(line.indexOf(":")>0&&line.startsWith("@")){
					line = line.substring(1);//去掉前面的"@"
					//组装成私聊信息格式：PRIVATE_ROUND私聊用户名SPLIT_SIGN私聊内容PRIVATE_ROUND
					String msg = MyProtocol.PRIVATE_ROUND+line.split(":")[0]+MyProtocol.SPLIT_SIGN+
					line.split(":")[1]+MyProtocol.PRIVATE_ROUND;		
					ps.println(msg);
				//公聊信息
				}else{
					ps.println(MyProtocol.MSG_ROUND+line+MyProtocol.MSG_ROUND);
				}
			}
		} catch (IOException e) {
			System.out.println("网络异常，请重新登录！");
			this.closeAll();
			System.exit(1);
		}
	}
	/**
	 * 关闭所有
	 */
	private void closeAll(){
		
		try {
			if(brServer!=null){
				brServer.close();
			}
			if(inputMsg!=null){
				inputMsg.close();
			}
			if(ps!=null){
				ps.close();
			}
			if(socket!=null){
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
