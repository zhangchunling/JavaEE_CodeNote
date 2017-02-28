package com.study.网络编程.TCP协议网络编程;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * URL类笔记:
 * 它是指向互联网“资源”的指针。资源可以是简单的文件或目录，也可以是更为复杂的对象引用。
 * @author Peter
 *
 */
public class URL_note {

	public static void main(String[] args) {
		try {
			/*
			 * 构造
			 */
			URL url = new URL("http://123.56.185.137:8080/ims/ims/groupList.htm?userName=张三");//根据 String 表示形式创建 URL 对象
			
			/*
			 * 方法
			 */
			String protocol	 = url.getProtocol();//获取协议名： http
			String host		 = url.getHost();	 //获取主机名: localhost
			int port 		 = url.getPort();	 //获取端口号: 8080
			String path 	 = url.getPath();	 //获取些URL的路径部分: /AGX1.0/ims/adv.do
			String file 	 = url.getFile();	 //获取些URL的资源名: /AGX1.0/ims/adv.do?userName=张三
			String query 	 = url.getQuery();	 //获取此URL的查询字符串部分： userName=张三
				
			try {
				/*
				 * 返回一个URLConnection对象，它表示到URL所引用的远程对象的连接。
				 */
				URLConnection con = url.openConnection();				
				int length 		  = con.getContentLength();//获取指定网络资源长度
				Object content    = con.getContent();//获取网络资源内容:sun.net.www.protocol.http.HttpURLConnection$HttpInputStream@983d95
				
				/*
				 * 通过openStream()方法，可方便地读取远程资源，甚至实现多线程下载。
				 * 多线程下载 的核心代码 也就是从这里延伸的，详情参见：java讲义——>第17章 网络编程——>17.2.3使用URL和URLConnection的代码
				 */
				InputStream in  = url.openStream();//打开些URL的连接，并返回一个读取该URL资源的InputStream.	'
			
				/*打印*/
				System.out.println(con);
				//sun.net.www.protocol.http.HttpURLConnection:http://localhost:8080/AGX1.0/ims/adv.do?userName=张三				
				System.out.println(con.getURL().getProtocol());//http
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*
			 * 创建一个和URL的连接，并发送请求、读取些URL引用的资源需要如下步骤：
			 *  1.调用URL对象的openConnction()方法建立URLConnection对象。
			 *  2.设置URLConnection的参数和普通请求属性。
			 *  3.如果只是发送GET方式请求，使用connect方法建立和远程资源之间的实际连接即可；
			 *    如果是POST方式，需要获取URLConnection实例对应的输出流来发送请求参数。
			 *  4.将远程资源变为可用。程序可以访问远程资源的头字段，或通过输入流来读取远程资源的数据。
			 */
			try {
				//1.建立URLConnection对象：
				URLConnection con = url.openConnection();
				
				//2.在建立和远程资源的实际连接前，设置请求字段：
				con.setConnectTimeout(20);//超过20ms就算连接超时
				con.setAllowUserInteraction(true);//设置此 URLConnection 的 allowUserInteraction 字段的值
				con.setDoInput(true); //将此 URLConnection 的 doInput 字段的值设置为指定的值。
				con.setIfModifiedSince(port);//将此 URLConnection 的 ifModifiedSince 字段的值设置为指定的值。				
				con.setUseCaches(true);//将此 URLConnection 的 useCaches 字段的值设置为指定的值。				
				//通过setRequestProperty(String key, String value)来设置字段：
				con.setRequestProperty("accept", "*/*");//设置accept字段的值
				//通过addRequestProperty(String key, String value) 追加字段的值，且不覆盖原有值，是追加到后面
				
				con.connect();//建立实际的连接
				
				//3.当远程资源可用之后，可访问头字段和内容：
				Object obj = con.getContent();//获取URLConnection的内容
				String field = con.getHeaderField("fieldName");//获取指定响应头字段的值
				InputStream in = con.getInputStream();//返回URLConnection对应的输入流。获取URLConnection响应的内容
				OutputStream out = con.getOutputStream();//返回URLConnection对应的输出流，向URLConnection发送请求参数
				/* 注：如果既要使用输入流读出URLConnection响应的内容，也要使用输出流发送请求参数，
				 * 一定要先使用输出流，再使用输入流
				 */
				

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
