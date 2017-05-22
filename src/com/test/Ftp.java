package com.test;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Ftp {
	public FTPClient ftp;

	public Ftp(boolean isPrintCommmand) {
		ftp = new FTPClient();
		if (isPrintCommmand) {
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		}
	}

	// 登录
	public boolean login(String host, int port, String username, String password) throws IOException {
		this.ftp.connect(host, port);
		if (FTPReply.isPositiveCompletion(this.ftp.getReplyCode())) {
			//if (this.ftp.login(username, password)) {
				this.ftp.setControlEncoding("GBK");
				return true;
			//}
		}
		if (this.ftp.isConnected()) {
			this.ftp.disconnect();
		}
		return false;
	}

	// 关闭连接
	public void disConnection() throws IOException {
		if (this.ftp.isConnected()) {
			this.ftp.disconnect();
		}
	}

	// 通过路径获得路径下所有文件 输出文件名
	public void List(String pathName) throws IOException {
		if (pathName.startsWith("/") && pathName.endsWith("/")) {
			String directory = pathName;
			this.ftp.changeWorkingDirectory(directory);
			FTPFile[] files = this.ftp.listFiles();
			for (int i = 0; i < files.length; i++) {
				System.out.println("得到文件:" + files[i].getName());
				if (files[i].isFile()) {
				} else if (files[i].isDirectory()) {
					List(directory + files[i].getName() + "/");
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println(55);
		Ftp f = new Ftp(true);
		if (f.login("192.168.2.206", 8080, "zhuchao", "zhuchao")) {
			f.List("/app/"); // 地址，端口号，用户名，密码
		}
		f.disConnection();
	}
}
