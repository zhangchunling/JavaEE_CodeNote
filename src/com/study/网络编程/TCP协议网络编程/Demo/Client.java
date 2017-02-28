package com.study.网络编程.TCP协议网络编程.Demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
/**
 * TCP Client：模拟IE浏览器
 * @author 矫迩
 */
public class Client {
	public static void main(String[] args) throws Exception {//抛出父类异常
		Socket socket = new Socket("192.168.2.181", 20000);//服务器的IP地址和端口号
		
		StringBuffer sb = new StringBuffer();//服务器会获得客户端的信息，即下面sb里的信息
		sb.append("GET / HTTP/1.1\r\n");
		sb.append("Accept: text/html, application/xhtml+xml, */*\r\n");
		sb.append("Accept-Language: zh-CN\r\n");
		sb.append("User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)\r\n");
		sb.append("Host: 192.168.2.181:8080\r\n");
		sb.append("Connection: Keep-Alive\r\n\r\n");//必须两个\r\n

		//Socket调getOutputStream()返回套接字的输出流，输入流FileOutputStream有write(byte[] b)方法，需要字节数组
		//StringBuffer类创建的sb相当于一个字符串缓冲区，调用toString()：返回了字符串表现形式，因此需getBytes()方法转成数组。
		socket.getOutputStream().write(sb.toString().getBytes());
		//缓冲读
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		String line;		
		while((line=br.readLine()) != null) {
			System.out.println("来自服务器的数据："+line);
		}	
		socket.close();	 
	}
}
/*** 运行上面客户端后，会从服务器返回如下信息（响应协议）：*/	
/**请求头*/
//HTTP/1.1 200 OK  	//1.应响行 ：200 ：正常  404： 页面没找到  500：服务器异常  
/**2.消息报头 BWS(baiduWebServer):百度网络服务器
//Server: Apache-Coyote/1.1
//Accept-Ranges: bytes
//ETag: W/"187-1429166590000"
//Last-Modified: Thu, 16 Apr 2015 06:43:10 GMT
//Content-Type: text/html     //返回的内容类型：html文本
//Content-Length: 187
//Date: Thu, 16 Apr 2015 07:58:45 GMT

/**3.响应正文*/
//<!DOCTYPE html>
//<html>
//<head>
//	<meta http-equiv="content-type" content="text/html;charset=utf-8">
//	<title>我的网站</title>
//</head>
//<body>
//	我的网站
//</body>
//</html>	
