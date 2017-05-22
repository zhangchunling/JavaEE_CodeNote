package com.note.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * 打包下载器:网络访问下载
 * 
 * @author Peter
 * @Time 2017-4-1
 */
public class DownloadZip {

	private DownloadZip() {
		throw new Error("you can't instantiate this class: DownloadZip ");
	}

	/**
	 * 将多个文件打包并下载
	 * 
	 * @param response
	 * @param url
	 *            文件路径List
	 * @param zipname
	 *            打包后的文件名
	 * @return
	 */
	public static boolean download(HttpServletResponse response, List<String> url, String zipname) {
		try {
			write(response, url, zipname);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void write(HttpServletResponse response, List<String> urlList, String zipname) throws IOException {
		OutputStream out = null;
		// 设置reponse返回数据类型，文件名，以及处理文件名乱码
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipname, "utf-8"));
		// 设置返回内容形式：下载
		response.setContentType("application/x-download; charset=utf-8");
		// 取得输出流：输出压缩包到本的区别就在这儿，即获取的输出流不一样
		out = response.getOutputStream();
		// 压缩输出流
		ZipOutputStream zos = new ZipOutputStream(out);
		byte[] buf = new byte[8192];
		int len;
		for (String s : urlList) {
			// 获取文件流
			URL url = new URL(s);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			InputStream in = connection.getInputStream();

			// 给每一个压缩对象赋名字
			String[] array = s.split("/");
			ZipEntry ze = new ZipEntry(array[array.length - 1]);

			// 将一个要压缩的对象放进压缩流中
			zos.putNextEntry(ze);

			// 输出
			while ((len = in.read(buf)) > 0) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
		}
		zos.close();
		out.close();

	}

}
