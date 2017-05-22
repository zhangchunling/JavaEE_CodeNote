package com.note.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.Channel;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP
 * 操作另一台服务器（上传、下载、查看、删除文件）
 * 类似现有的Xftp软件
 * @author Peter
 *
 */
public class SFTPUtils {
	//private ChannelSftp sftp;
	// private static final String host = "192.168.2.195";
	// private static final int port = 22;
	// private static final String username = "root";
	// private static final String password = "hwfm123";
	// private static final String directory = "/data/webapps/nginx/book";

	// private static final String downloadFile = "xxx.txt";
	// private static final String uploadFile = "d:/a.jpg";
	// private static final String saveFile = "d:/b.jpg";
	// private static final String deleteFile = "d:/c.jpg";

	public static void main(String[] args) throws Exception {
		SFTPUtils sf = new SFTPUtils();
		// 获取连接
		if (sf.connect("192.168.2.195", 22, "root", "hwfm123")) {
			Vector<LsEntry> files = sf.listFiles("/data/webapps/nginx/book"); // 查看文件列表
			for (LsEntry file : files) {
				// System.out.println(file.getLongname());
				if (!(file.getFilename().equals(".") || file.getFilename().equals(".."))) {
					System.out.println(file.getFilename() + "--");
					Vector<LsEntry> sonfiles = sf.listFiles("/data/webapps/nginx/book" + "/" + file.getFilename());
					for (LsEntry f : sonfiles) {
						if (!(f.getFilename().equals(".") || f.getFilename().equals(".."))) {
							System.out.println(f.getFilename());
						}
					}
				}

			}
		}

		// sf.upload(directory, uploadFile, sftp); //上传文件

		// sf.download(directory, downloadFile, saveFile, sftp); // 删除文件

		// sf.delete(directory, deleteFile, sftp); // 删除文件

	}

	/**
	 * 连接sftp服务器
	 * 
	 * @param host
	 *            主机
	 * @param port
	 *            端口,一般是22
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	public boolean connect(String host, int port, String username, String password) {
		ChannelSftp sftpTemp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			System.out.println("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("Session connected.");
			System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftpTemp = (ChannelSftp) channel;
			System.out.println("Connected to " + host + ".");
			System.out.println();
			this.sftp = sftpTemp;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 上传文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 */
	public void upload(String directory, String uploadFile) {
		try {
			this.sftp.cd(directory);
			File file = new File(uploadFile);
			this.sftp.put(new FileInputStream(file), file.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param directory
	 *            下载目录
	 * @param downloadFile
	 *            下载的文件
	 * @param saveFile
	 *            存在本地的路径
	 */
	public void download(String directory, String downloadFile, String saveFile) {
		try {
			this.sftp.cd(directory);
			File file = new File(saveFile);
			this.sftp.get(downloadFile, new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 */
	public void delete(String directory, String deleteFile) {
		try {
			this.sftp.cd(directory);
			this.sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列出目录下的文件
	 * 
	 * @param directory
	 *            要列出的目录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vector<LsEntry> listFiles(String directory) {
		try {
			return this.sftp.ls(directory);
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return null;
	}
}
