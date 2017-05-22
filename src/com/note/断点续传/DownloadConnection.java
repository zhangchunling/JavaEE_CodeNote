package com.note.断点续传;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 这个下载连接类主要采用的方式是 将文件大小进行分块然后 并且有序下载 主要的目的是为了让一次下载的数量不要过于的大 以避免io阻塞
 * 若要实现断点续传本程序的改进应该是很方便的 记住lastSize 既可以立马进行分解
 * 
 * @author sada
 * 
 */
public class DownloadConnection {

	// private static final String mtempurl =
	// "http://219.138.125.22/myweb/mp3/CMP3/JH19.MP3";
	private static final String mtempurl = "http://file16.top100.cn/201105110911/AA5CC27CBE34DEB50A194581D1300881/Special_323149/%E8%8D%B7%E5%A1%98%E6%9C%88%E8%89%B2.mp3";

	public static void main(String[] args) {
		/*
		 * DownloadConnection dConnection = new DownloadConnection(mtempurl, 0);
		 * dConnection.download(mtempurl);
		 */
		String path = "http://thumb.kting.cn/data/uploads/books/18/1439255696.jpg";
		URL url;
		try {
			url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// connection.setUseCaches(false);
			/*
			 * connection.setConnectTimeout(1000);
			 * connection.setReadTimeout(1000);
			 */
			// connection.setRequestProperty("RANGE", "5000-");
			// 上传
			InputStream in = connection.getInputStream();
			System.out.println(connection.getContentLength());
			byte[] buffer = new byte[10];
			int len = 0;
			BufferedOutputStream w = new BufferedOutputStream(new FileOutputStream(new File("d://T222.jpg")));

			while ((len = in.read(buffer)) != -1) {
				w.write(buffer);

			}
			w.flush();
			/*
			 * byte[] b = new byte[2]; BufferedWriter bw = new
			 * BufferedWriter(new FileWriter(new File("d://a.jpg")));
			 * while((len=in.read(b))>0){ bw.write(b.toString()); }
			 */
			// w.flush();
			in.close();
			w.close();
			// connection.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** TAG */
	private static final String TAG = "Sada.DownloadConnection";
	/** 用于操作下载暂停，写本地文件时候跳出写循环的标志 */
	private boolean stop;
	/** 下载的文件的总长度 */
	private int totalSize;
	/** 如果是继续下载，lastSize表示上次已经下载的长度 */
	private int lastSize;
	/** 當前下載數據塊已下載數據大小，该变量会在写循环中增加 */
	private int currSize;
	/** 当前需要下载的文件大小，用于继续下载时，写循环的结束标志 */
	private int contentLength;

	/** 最大塊大小1MB */
	private static final int MAX_SIZE_ONE_MEGA = 1024 * 1024;

	private int lastloadsize = 0; // 已传的偏移
	private int currloadsize = 0; // 还要传得偏移
	private int contentlength = 0; // 总共要传得大小

	private int pogresslength = 0; // 进度条显示

	private static byte[] buffer;

	private int currentPercentage = 0;
	private RandomAccessFile raf;
	private File temp;
	private InputStream is; // 从网络读输入流

	/*---------------------------------*/
	/** 连接实例 */
	protected HttpURLConnection conn;
	/** 连接的完整url */
	protected String url;
	/** 响应行的响应码，例如：404, 200, 500, 201等 */
	protected int responseCode;
	/** 响应行的响应消息关键字，例如：OK，Created，Unauthorized等 */
	protected String responseMessage;
	protected int timeout = -1;

	/** 文件名 */
	private String filename = "";

	/**
	 * @ 构造方法，传入下载文件的blob url 和下载任务对应的download bean
	 */
	public DownloadConnection(String url, int continuteSize) {
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}
		this.url = url;
		lastSize = continuteSize;
		if (buffer == null) {
			buffer = new byte[8 * 1024];
		}
	}

	/** 准备请求行 */
	protected void prepareRequestLine() throws Exception {
		conn.setRequestMethod("GET");
	}

	/** 准备消息头 */
	protected void prepareHeaders() {

		int startfilesize = lastloadsize; // 当前文件偏移位置
		int endfilesize = currloadsize - 1; // 目的文件偏移位置
		conn.setAllowUserInteraction(true);
		// 设置传输位置
		conn.setRequestProperty("RANGE", "bytes=" + startfilesize + "-" + endfilesize);
		// Log.i(TAG,"(start = " + startfilesize + ")" + " - " + "(end = " +
		// endfilesize + ")");
	}

	public void download(String surl) {
		try {
			int totalsize = 0; // 整理后的文件大小
			URL mUrl = new URL(url);
			conn = (HttpURLConnection) mUrl.openConnection();

			// ---------------- 改进
			totalsize = conn.getContentLength() - this.lastSize;// 本次续下载的大小
			this.totalSize = conn.getContentLength();
			pogresslength = this.lastSize; // 初始化起始的进度偏移位置 在断点时用到
			conn.disconnect();
			// Log.e(TAG, "the file size = " + this.totalSize);

			filename = getFileNameFromUrl(surl);
			File cache = new File("D:\\TEMP");
			if (cache != null && !cache.exists()) {
				cache.mkdirs();
			}
			temp = new File("D:\\TEMP" + "\\" + filename);
			raf = new RandomAccessFile(temp, "rw");
			raf.seek(raf.length());

			int offset = 0; // 上次下载的位置

			for (int i = 0; i < totalsize / MAX_SIZE_ONE_MEGA; i++) {
				lastloadsize = offset; // 已传偏移
				currloadsize = lastloadsize + MAX_SIZE_ONE_MEGA; // 将传到的偏移位置
				contentlength = currloadsize - lastloadsize; // 总共要传的大小
				// Log.v(TAG, "the size long " + contentlength);

				connect();
				if (stop) {
					// 暂停下载数据
					// 写数据库 pogresslength已下载长度
					return;
				}

				offset += MAX_SIZE_ONE_MEGA;
			}
			if (totalsize % MAX_SIZE_ONE_MEGA > 0) {
				totalsize = totalsize % MAX_SIZE_ONE_MEGA;
				lastloadsize = offset; // 已传偏移
				currloadsize = lastloadsize + totalsize; // 将传到的偏移位置
				contentlength = currloadsize - lastloadsize; // 总共要传得大小
				// Log.v(TAG, "the size long " + contentlength);
				if (contentlength > 0) { // 当下载最后大小为0时 直接做后面的操作
					connect();
				}

				if (stop) {
					// 写数据库 pogresslength
					return;
				}

			}

			/*
			 * Log.v(TAG, "Download file success!\t\n" + "Size: " + lastloadsize
			 * + " bytes."); Log.v(TAG,"lastsize = " + pogresslength);
			 * Log.v(TAG,"fileSize = " + this.totalSize);
			 */

			// 更新下载进度到数据库
			File temp = new File("D:\\TEMP" + "\\" + filename);

			if (temp != null && temp.exists() && temp.length() != this.totalSize) { // 大小不一致则删除临时文件
				throw new Exception("The temp file is not completed!");
			}
		} catch (Exception e) {

		} finally {
			if (raf != null) {
				try {
					raf.close();
					raf = null;
				} catch (IOException e) {
				}
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
				is = null;
			}
			System.gc();
		}
	}

	public void connect() throws Exception {

		URL mUrl = new URL(url);
		conn = (HttpURLConnection) mUrl.openConnection();
		conn.setDoInput(true);

		// 2.准备请求行
		prepareRequestLine();
		// 3.再准备消息头
		prepareHeaders();
		conn.setDoOutput(false);
		conn.connect();

		responseCode = conn.getResponseCode();
		responseMessage = conn.getResponseMessage();
		// Log.d(TAG, "Response:" + responseCode + " " + responseMessage);

		if (responseCode == 206 && "Partial Content".equals(responseMessage)) {
			contentLength = conn.getContentLength();
			// Log.d(TAG, "the size long=" + contentLength);
			is = conn.getInputStream();

			if (!temp.exists() && pogresslength != 0) {
				// 失败重新开始
				throw new Exception("The temp file has bean deleted");
			}

			currSize = 0;
			int ch = -1;
			// 恰好写完
			while (currSize < contentlength) {
				// 判断是否暂停的标志
				if (stop) {
					break;
				}
				ch = is.read(buffer);
				if (ch == -1) {
					break;
				}
				raf.write(buffer, 0, ch);
				currSize += ch;

				pogresslength = lastloadsize + currSize;
				if (currSize == contentlength) {
					break;
				}
			}
			// 下载进度更新
			// Log.d(TAG, "Download block:\t" + currSize + " bytes. " +
			// contentLength);
			currSize = 0;

			is.close();
			conn.disconnect();
			conn = null;
		} else {
			conn.disconnect();
			if (conn != null) {
				conn = null;
			}
			throw new Exception("Response header is incorrect!Must be: HTTP/1.1 206 Partial Content." + "code = "
					+ responseCode + "   message = " + responseMessage);
		}
	}

	/** 暂停下载 */
	public void pauseDownload() {
		stop = true;
	}

	/** 取消下载 先将下载线程暂停 然后将本地文件删除 */
	public void cancelDownload() {
		stop = true;
	}

	/**
	 * 状态改变 这里主要指下载的进度改变监听
	 */
	public void onStatusChanged() {
		long percentage = pogresslength * 100L / totalSize;
		// Log.d(TAG, "Downloading percentage 2B: " + percentage);
		if (percentage > 0) {
			if (percentage > currentPercentage) {
				currentPercentage = (int) percentage;
				// 更新进度条
				// Log.d(TAG, "Downloading percentage: " + currentPercentage + "
				// %(" + filename + ").");
			}
		}
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setUrl(String mUrl) {
		url = mUrl;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * 依据URL获取文件名当为空时 设置文件名为 default.temp
	 * 
	 * @param url
	 * @return
	 */
	private static String getFileNameFromUrl(String url) {

		if (url == null) { // 1
			url = "default.temp";
		} else {
			if (url.lastIndexOf("/") != -1) {
				url = url.substring(url.lastIndexOf("/") + 1, url.length());
				if (isReg(url)) { // 若有非法字符存在则
					if (url.lastIndexOf(".") != -1) {
						url = url.substring(url.lastIndexOf(".") + 1, url.length());
						url = "temp." + url;
					} else {
						url = "default.temp";
					}
				}
			} else {
				if (url.lastIndexOf(".") != -1) {
					url = url.substring(url.lastIndexOf(".") + 1, url.length());
					url = "temp." + url;
				} else {
					url = "default.temp";
				}
			}
		}
		System.out.println("the file name is " + url);
		return url;
	}

	public static boolean isReg(String code) {
		if (code.contains("/")) {
			return false;
		}
		if (code.contains("\\")) {
			return false;

		}
		if (code.contains("[")) {
			return false;
		}
		if (code.contains("]")) {
			return false;
		}
		if (code.contains("?")) {
			return false;
		}
		if (code.contains("<")) {
			return false;
		}
		if (code.contains(">")) {
			return false;
		}
		if (code.contains("*")) {
			return false;
		}
		if (code.contains(":")) {
			return false;
		}
		if (code.contains("|")) {
			return false;
		}
		if (code.contains("\"")) {
			return false;
		}
		return true;
	}
}