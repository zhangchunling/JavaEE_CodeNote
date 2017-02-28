package com.study.网络编程.UDP协议网络编程;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
/**
 * 服务器:服务器端与客户端代码极为相似，只有在发送时送往地址不一样
 * 实际上，UDP协议的通信双方没有客户端与服务端的概念
 * 	DatagramSocket:创建码头
 * 	DatagramPacket:创建集装箱
 * @author Peter
 *
 */
public class UDPServer {

	public static final int PORT = 44444;	//端口号
	private static final int DATA_LEN =4096;//定义每个数据报的最大长度为4K
	private DatagramSocket socket = null;	//定义"接收货物的码头"：服务器
	byte[] inBuff = new byte[DATA_LEN];		//定义接收网络数据的字节数组
	/*
	 * inBuff与inPacket.getData();内容一样
	 */
	private DatagramPacket inPacket = 
			new DatagramPacket(inBuff, inBuff.length);//以指定字节数组创建"收货的的集装箱"
	
	private DatagramPacket outPacket ;		//定义一个"发货的集装箱"	
	String[] books = new String[]{"服务器数据1","服数据2","服数据3"};//服务器发送该数组的元素
	
	public void init() throws IOException{
		
		socket = new DatagramSocket(44444);//创建码头
		
		for(int i=0;i<1000;i++){//可接收一千多个同时发来的数据包
			/*
			 * 接收另一码头发来的货
			 */
			socket.receive(inPacket);//接收数据,并放进集装箱inPacket里
			System.out.println("接收信息："+new String(inBuff));//打印接收的数据
			
			/*
			 * 给发数据报的码头 反馈信息
			 */
			byte[] sendData = books[0].getBytes();//要发送的数据
			//创建发货的集装箱outPacket，给定要发的数据、长度，以及目的主机的IP和端口号
			outPacket = new DatagramPacket(sendData, sendData.length, inPacket.getAddress(), inPacket.getPort());
			socket.send(outPacket);//发送
		}
		if(socket!=null){
			socket.close();
		}
		
	}
	public static void main(String[] args) throws IOException {
		new UDPServer().init();
	}
	
	//1.DatagramSocket：码头
	/**
	 * 构造器：
	 * 1.DatagramSocket socket1 = new DatagramSocket();
	 *    会将socket1对象绑定到 本机 默认IP地址，本机所有可用端口随机选择一个。
	 * 2.DatagramSocket socket2 = new DatagramSocket(44444);
	 *    会将socket2对象绑定到 本机 默认IP地址，端口用指定的44444
	 * 3.DatagramSocket(int port,InetAddress addr)
	 *    DatagramSocket socket3 = new DatagramSocket(44444,"192.168.2.181");
	 *    指定IP地址，指定端口创建一个对象
	 */
	/**
	 * 常用方法：
	 * void receive(DatagramPacket p):从该DatagramSocket接收数据报包，类比为从该码头获取传来的集装箱
	 * void send(DatagramPacket p):从该DatagramSocket发送数据报包，类比为从该码头发送信箱
	 */
	//2.DatagramPacket:集装箱
	/**
	 * 构造器：
	 * 1.DatagramPacket(byte[] buf,int length):给定大小的"接收货物的集装箱"
	 * 	   创建对象时给了一个空buf数组存接收的数据包，且规定大小为length
	 * 2.DatagramPacket(byte[] buf, int length, InetAddress address, int port):发送货物的集装箱
	 *   创建一个包含数据的数组的对象，该包只能发送到指定地址和端口的主机上
	 * 3.DatagramPacket(byte[] buf, int offset, int length) ："接收货物的集装箱"
	 *   构造 DatagramPacket，将接收到的数据放入空buf时，从offset开始，最多放length个字节,
	 * 4.DatagramPacket(byte[] buf, int offset, int length, InetAddress address, int port)：发货的集装箱
	 *   创建一个用于发送的集装箱，将从offset开始，最多length个字节的数据量，发送到指定主机上的指定端口号。
	 *   
	 *  接收货物的集装箱，给出了接收数据的字节数组和其长度；然后调用DatagramSocket的receive方法后等待数据报的到来，此时会一直等侍直到来
	 *  发送货物的集装箱，字节数组里存了要发的数据，指定了发往的IP地址与端口号；然后调用DatagramSocket的send方法发送。
	 */
	/*当服务器（客户端）接收到一个DatagramPacket对象后，如果想向数据报的发送端"反馈"一些信息，
	由于UDP是面向非连接的，所以接收者并不知道数据报是谁发来的，但可调用DatagramPacket的方法获取发送者的IP和端口：
	InetAddress getAddress():返回该数据包来源的主机IP。若该数据包是要发送出去的，则是本机的；若是接收到的，则是发送端的IP
	int getPort():返回数据包来源的机器的端口。是要发送的数据包，则是本机的端口;若是接收到的数据包，则是远程主机的端口。
	getSocketAddress()： 获取此包来源的主机的 SocketAddress（通常为 IP 地址 + 端口号）。*/
}
