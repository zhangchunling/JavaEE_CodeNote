package com.study.网络编程.UDP协议网络编程.多点广播;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 * 多点广播:IP地址范围24.0.0.0~239.255.255.255
 * 	使用MulticastSocket发送、接收数据报的代码与DatagramSocket一样一样的。
 * 
 * 1.MulticastSocket类，是DatagramSocket的子类，它有如下构造器:
 *   MultilcastSocket():使用本机默认地址、随机端口创建对象。只用于发送数据报的对象可用该构造。
 *   MultilcastSocket(int port):使用本机地址、指定端口创建对象。还需接收数据报时，则必须要指定端口。
 *   MultilcastSocket(SocketAddress add):使用本机指定IP、指定端口创建对象。它是抽象类，其子类为：InetSocketAddress
 * 2.方法：
 *   joinGroup(InetAddress addr):将该MulticastSocket加入指定的多点广播地址。
 *   leaveGroup(InetAddress addr):使该MulticastSocket离开指定的多点广播地址。
 *   
 * 	 InetAddress getInterface() :获取MultilcastSocket监听的网络接口地址。 
 *   setInterface(InetAddress inf)：设置多播网络接口。可选择 MulticastSocket所使用的网络接口，可避免某些系统中有多个网络接口的问题
           其他方法
	 boolean getLoopbackMode()： 获取多播数据报的本地回送的设置。 
	 NetworkInterface getNetworkInterface() ： 获取多播网络接口集合。 
	 int getTimeToLive()：    获取在套接字上发出的多播数据包的默认生存时间。  
	 void joinGroup(SocketAddress mcastaddr, NetworkInterface netIf)： 加入指定接口上的指定多播组。 
	 void leaveGroup(SocketAddress mcastaddr, NetworkInterface netIf) ：   离开指定本地接口上的多播组。 
	 void setLoopbackMode(boolean b) ：loopback:环回，设置是否可以接受到自己发送的数据报 
	 void setNetworkInterface(NetworkInterface netIf) ： 指定在此套接字上发送的输出多播数据报的网络接口。
	  
	 void setTimeToLive(int ttl)：ttl参数表示可跨过多少个网络，控制发送范围，默认为1。  0 <= ttl <= 255 范围内
	 	ttl = 0:指定数据报应停留在本地主机，
	 	ttl = 1:只能发送到本地局域网（默认值）
	 	ttl = 32:只能发送到本站点的网络上
	 	ttl = 64:只能保留在本地区
	 	ttl = 128:只能保留在本大洲
	 	ttl = 255:可以发送到全球
 * 3.扩展：
 * 	 多点广播时，所有通信实体都是平等的，它们都将数据报发送到 多点广播IP地址，并使用MulticastSocket接收其他人发送的广播数据报。
 * @author Peter
 *
 */
public class MulticastSocketTest implements Runnable{

	private static final String BROADCAST_IP= "230.0.0.1";//多点广播地址
	public static final int BROADCAST_PORT = 30000;//多点广播目的地的端口
	private static final int DATA_LEN = 4096;//每个数据报最大大小为4K
	
	private MulticastSocket socket = null;
	private InetAddress addr = null;
	private Scanner scan = null;
	
	byte[] inBuff = new byte[DATA_LEN];//接收网络数据的容器
	private DatagramPacket inPacket =new DatagramPacket(inBuff, inBuff.length);//接收数据的集装箱
	private DatagramPacket outPacket = null;
	
	@Override
	public void run() {
		while(true){
			try {
				//接收数据，装进inPacket的字节数组里
				socket.receive(inPacket);
				//打印
				System.out.println("聊天信息："+new String(inBuff,0,inPacket.getLength()));
			} catch (IOException e) {
				e.printStackTrace();
				try {
					if(socket!=null){
						socket.leaveGroup(addr);//离开多点广播地址
						socket.close();
					}
					System.exit(1);//退出系统
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		
	}
	
	public void init() throws IOException{
		//创建发送、接收数据的MulticastSocket对象，因为需要接收，所以需要指定端口。
		socket = new MulticastSocket(BROADCAST_PORT);
		addr =  InetAddress.getByName(BROADCAST_IP);//多点广播地址
		//将该socket加入指定的多点广播地址：加入后，发送的信息，这在这里面的都会收到相同的信息
		socket.joinGroup(addr);//需要发送或接收广播信息的，都要将其InetAddress都要加进来
		//loopback:环回，设置是否可以接受到自己发送的数据报
		socket.setLoopbackMode(false);
		
		//创建发送的集装箱：指定地址和端口
		outPacket = new DatagramPacket(new byte[0], 0,addr,BROADCAST_PORT);
		//启动本实例的run方法作为线程体的线程
		new Thread(this).start();//**接收**
		
		System.out.println("请输入数据：");
		scan = new Scanner(System.in);
		while(scan.hasNextLine()){//循环输入
			byte[] b = scan.nextLine().getBytes();
			outPacket.setData(b);//添加发送数据
			socket.send(outPacket);//**发送到多点广播地址**
		}
		socket.close();
	}
	
	public static void main(String[] args) throws IOException {
		new MulticastSocketTest().init();
	}
	
}
