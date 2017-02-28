package com.study.网络编程.UDP协议网络编程;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
/**
 * 客户端：服务器端与客户端代码极为相似，只有在发送时送往地址不一样
 *  实际上，UDP协议的通信双方没有客户端与服务端的概念
 * 	DatagramSocket:创建码头
 * 	DatagramPacket:创建集装箱
 * @author Peter
 *
 */
public class UDPClient {

	public static final int DEST_PORT = 44444;
	public static final String DEST_IP = "192.168.2.181";
	private static final int DATA_LEN = 4096;//定义每个数据报大小
	private DatagramSocket socket = null;//定义码头
	byte[] inBuff = new byte[DATA_LEN];//接收网络数据的字节数组
	private DatagramPacket outPackt = null;//定义发货的集装箱
	private DatagramPacket inPackt = new DatagramPacket(inBuff, inBuff.length);//接收货物的集装箱
	
	public void init() throws IOException{
		//创建码头
		socket = new DatagramSocket();
		//创建发货的集装箱:长度为0的字节数组
		outPackt = new DatagramPacket(new byte[0],0, InetAddress.getByName(DEST_IP), DEST_PORT);
		//创建键盘输入流
		System.out.println("请输入要发送的数据：");
		Scanner scan = new Scanner(System.in);
		//不断读取键盘输入流
		while(scan.hasNextLine()){
			byte[] buff = scan.nextLine().getBytes();
			outPackt.setData(buff);//将数据装进发货的集装箱
			socket.send(outPackt);//发送
			
			//接收反馈信息
			socket.receive(inPackt);
			System.out.println("反馈信息："+new String(inBuff,0,inPackt.getLength()));
			
			if(socket!=null){
				socket.close();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new UDPClient().init();
	}
}
