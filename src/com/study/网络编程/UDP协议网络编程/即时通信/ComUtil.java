package com.study.网络编程.UDP协议网络编程.即时通信;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import javax.swing.JOptionPane;

/**
 * 聊天交换信息的工具类:
 * 
 * @author Peter
 *
 */
public class ComUtil {

	//本程序的多点广播IP地址
	private static final String BROADCAST_IP="230.0.0.1";
	//多点广播目的的端口
	public static final int BROADCAST_PORT=30000;
	private static final int DATA_LEN = 4096;//每个数据报最大为4K
	
	private MulticastSocket multicastSocket=null;
	private DatagramSocket datagramSocket =null;//私聊实例
	private InetAddress broadcastAddress = null;//广播的InetAddress地址
	
	byte[] inBuff = new byte[DATA_LEN];//接收网络数据的字节数组
	//接收数据的集装箱
	private DatagramPacket inPacket = new DatagramPacket(inBuff, inBuff.length);
	private DatagramPacket outPacket = null;//发数据的集装箱
	//聊天的主界面
	private LanChat lanTalk;
	/**
	 * 有参构造:初始化
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public ComUtil(LanChat lanTalk) throws IOException, InterruptedException{
		this.lanTalk=lanTalk;
		//创建发送、接收数据的MulticastSocket对象，因为需要接收数据，所以要指定端口
		multicastSocket = new MulticastSocket(BROADCAST_PORT);
		//创建私聊的DatagramSocket对象
		datagramSocket = new DatagramSocket(BROADCAST_PORT+1);
		//通过多点广播的IP地址得到多点广播的InetAddress地址
		broadcastAddress=InetAddress.getByName(BROADCAST_IP);
		//该MulticastSocket加入指定的多点广播地址
		multicastSocket.joinGroup(broadcastAddress);
		//设置回环，即发送的数据报能被回送到自身
		multicastSocket.setLoopbackMode(false);
		//创建发送数据的集装箱：指定了发往地址（广播地址）和端口
		outPacket = new DatagramPacket(new byte[0],0,broadcastAddress,BROADCAST_PORT);
		//启动两个线程读取网络数据
		new ReadBroad().start();
		Thread.sleep(1);
		new ReadSingle().start();		
	}
	
	/**
	 * 广播消息
	 * @param msg
	 */
	public void broadCast(String msg){
		byte[] buff = msg.getBytes();
		outPacket.setData(buff);//添加数据
		try {
			multicastSocket.send(outPacket);//发送
		} catch (IOException e) {
			e.printStackTrace();
			if(multicastSocket!=null){
				multicastSocket.close();
			}
			//打印错误文件信息
			JOptionPane.showMessageDialog(null, "发送信息异常，请确认30000端口空闲，且网络连接正常！","网络异常",JOptionPane.ERROR_MESSAGE);
			System.exit(1);//退出系统

		}
	}
	/**
	 * 发送私聊方法
	 * @param msg
	 * @param dest 私聊信息發送的目的地
	 */
	public void sendSingle(String msg,SocketAddress dest){		
		try {
			byte[] buff = msg.getBytes();
			DatagramPacket ouPacket = new DatagramPacket(buff, buff.length,dest);			
			datagramSocket.send(ouPacket);//发送			
		} catch (IOException e) {
			e.printStackTrace();
			if(multicastSocket!=null){
				multicastSocket.close();
			}
			//打印错误文件信息
			JOptionPane.showMessageDialog(null, "发送信息异常，请确认30001端口空闲，且网络连接正常！","网络异常",JOptionPane.ERROR_MESSAGE);
			System.exit(1);//退出系统

		}
	}
	/**
	 * 内部线程类：不断从DatagramSocket中读取数据的线程
	 * @author Peter
	 *
	 */
	class ReadSingle extends Thread{
		byte[] singlBuff = new byte[DATA_LEN];//定义接收网络数据的字节数组
		private DatagramPacket singlePacket = new DatagramPacket(singlBuff, singlBuff.length);
		
		public void run(){
			while(true){
				try {
					datagramSocket.receive(singlePacket);//读取数据放进inPacket这个集装箱里
					//lanTalk.processMsg(datagramSocket,true);//处理读到的信息
				} catch (IOException e) {
					e.printStackTrace();
					if(multicastSocket!=null){
						multicastSocket.close();
					}
					//打印错误文件信息
					JOptionPane.showMessageDialog(null, "发送信息异常，请确认30001端口空闲，且网络连接正常！","网络异常",JOptionPane.ERROR_MESSAGE);
					System.exit(1);//退出系统
				}
			}
		}
	}
	
	/**
	 * 持续读取MulticastSocket的线程
	 */
	class ReadBroad extends Thread{
		public void run(){
			while(true){
				try {
					multicastSocket.receive(inPacket);
					String msg = new String(inBuff, 0, inPacket.getLength());//得到字节数组数据转成字符串
					//说明内容是在线信息
					if(msg.startsWith(MyProtocol.PRESENCE)&&msg.endsWith(MyProtocol.PRESENCE)){
						String userMsg = msg.substring(2,msg.length()-2);
						String[] userInfo = userMsg.split(MyProtocol.SPLITTER);
						UserInfo user = new UserInfo(userInfo[1], userInfo[0], inPacket.getSocketAddress(), 0);
						boolean addFlag = true;//判断是否需要添加该用户的旗帜
						for(int i=1;i<lanTalk.getUserNum();i++){//循环已有用户，该循环必须完成
							UserInfo current = lanTalk.getUser(i);
							current.setLostTimes(current.getLostTimes()+1);//失阖联系次数据加1
							//如果该信息由指定用户发来
							if(current.equals(user)){
								current.setLostTimes(0);
								addFlag = false;//设置该用户无须添加
							}
							if(current.getLostTimes()>2){
								delList.add(i);//添加
							}
							
						}
						//删除delList中的所有索引对应的用户
						for(int i=0;i<delList.size();i++){
							lanTalk.removeUser(delList.get(i));
						}
						if(addFlag==true){
							lanTalk.addUser(user);
						}
					}else{
						lanTalk.processMsg(inPacket,false);//处理读到的信息
					}
				} catch (IOException e) {
					e.printStackTrace();
					if(multicastSocket!=null){
						multicastSocket.close();
					}
					//打印错误文件信息
					JOptionPane.showMessageDialog(null, "发送信息异常，请确认30000端口空闲，且网络连接正常！","网络异常",JOptionPane.ERROR_MESSAGE);
					System.exit(1);//退出系统
				}
			}
		}
	}
}
