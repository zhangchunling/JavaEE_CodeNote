package com.study.网络编程.TCP协议网络编程.Demo.CS系统.私聊;

/**
 * 定义协议字符
 * 客户端发送来的信息必须有特殊标识，这样才能判断是公聊还是私聊。
 * 由于服务器和客户端都要使用这些协议字符串，所以两端要同时保留该接口对应的class文件
 * @author Peter
 *
 */
public interface MyProtocol {
	int PROTOCOL_LEN = 2;//定义协议字符串的长度
	/*
	 * 下面是一些协议字符串，服务器和客户端交换的信息都应该在前、后添加这种特殊字符串
	 */
	String MSG_ROUND = "##";	//是否是聊天信息
	String USER_ROUND = "【【";	//登录用户名协议
	String LOGIN_SUCCESS = "1";	//是否登录
	String NAME_REP = "-1";		//标记用户名重复
	String PRIVATE_ROUND = "@@";//私聊信息协议字符
	String SPLIT_SIGN = "**";	//分割符
}
