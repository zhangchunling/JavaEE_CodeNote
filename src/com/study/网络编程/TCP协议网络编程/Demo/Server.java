package com.study.网络编程.TCP协议网络编程.Demo;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * TCP Server
 * @author 矫迩
 * HTTP(HyperText Transfer Protocol)：超文本传输协议：规定好了数据格式
 * HTML语言 （超级文本标记语言）
 */
public class Server {	
	// 1.请求协议
	public static void main(String[] args) throws Exception {
		//创建服务器套接字
		ServerSocket server = new ServerSocket(20000);//指定端口号创建服务器
		
		//这儿接收时会出现等待，当客户端访问时，会返回与客户端Socket对应的Socket
		Socket socket = server.accept();	
		//接收客户端输入流
		InputStream inputStream = socket.getInputStream();
		int read;
		System.out.println("来自客户端的数据：");
		while((read=inputStream.read()) != -1) {			
			System.out.print((char)read);
		}
		
		socket.close();
		inputStream.close();		
		server.close();
		
	}	
}


/**请求协议：共三部分*//*客户端输入网址：http://192.168.1.254:123/后会收到如下——请求协议*/	
/*1.请求头 请求行
//GET / HTTP/1.1    GET:请求方式  HTTP/1.1:请求版本
*/
/*2.消息报头 key:value	冒号前面是key，后面是值
//Accept: text/html, application/xhtml+xml, 
//Accept-Language: zh-CN
//User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)	Trident：内核
//Accept-Encoding: gzip, deflate  X //有了这行，服务器发给客户端的是压缩包
//Host: 192.168.1.254:123
//DNT: 1  	DNT(do not track)
//Connection: Keep-Alive
//					这儿有个换行不能漏掉了，所以在客户端写请求协议时，上面一行要加两个\r\n
*/

/*3.请求正文
	内容……*/


/**请求协议*/
/*以网址http://192.168.1.254:123/324324/fasfasdfasfasffa/fasfasd访问时，加粗部分自动加到GET与HTTP/1.1之间*/
//GET /324324/fasfasdfasfasffa/fasfasd HTTP/1.1
//Accept: text/html, application/xhtml+xml, */*
//Accept-Language: zh-CN
//User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)
//Accept-Encoding: gzip, deflate
//Host: 192.168.1.254:123
//DNT: 1
//Connection: Keep-Alive

