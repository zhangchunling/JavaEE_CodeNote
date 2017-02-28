package com.study.网络编程.TCP协议网络编程;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URLEncoder与URLDecoder类
 * @author Peter
 *
 */
public class URLEncoder_URLDecoder {

	public static void main(String[] args) {
		
		try {
			String url = "htt://localhost:8080/AGX/ims/adv.do?userName=张三";
			/*
			 * 1.URLEncoder（编码）:按指定编码将url转为application/x-www-form-urlencoded格式:
			 * htt%3A%2F%2Flocalhost%3A8080%2FAGX%2Fims%2Fadv.do%3FuserName%3D%E5%BC%A0%E4%B8%89
			 * */
			String application = URLEncoder.encode(url,"utf-8");
			
			/*
			 * 2.URLDecoder（解码）:按指定编码对被URLEncoder转码的字符进行 解码：
			 * htt://localhost:8080/AGX/ims/adv.do?userName=张三
			 */
			String result = URLDecoder.decode(application, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
