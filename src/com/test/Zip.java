package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Zip {

	public static void main(String[] args) {
//		URL url;
//		try {
//			url = new URL("http://192.168.2.206/res/upload/cdn/kting_huawei_fm/ampUser25/album/copyright/20170331/2b1i4t94.jpg");
//			HttpURLConnection connection =  (HttpURLConnection)url.openConnection();
//			connection.setConnectTimeout(5000);
//			connection.setReadTimeout(5000);			
//			InputStream in = connection.getInputStream();
//			
//			OutputStream out = new BufferedOutputStream( new FileOutputStream( "D:/"+"aaa"+".jpg" ) );;
//				  
//	        byte[] buf = new byte[1024];
//			int len;
//			while ((len = in.read(buf)) > 0) {
//				//out.write(buf, 0, len);
//				out.write( buf, 0, len );
//			}
//			out.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		//String[] url = {"http://192.168.2.206/res/upload/cdn/kting_huawei_fm/ampUser25/album/copyright/20170331/2b1i4t94.jpg"};
		List<String> list = new ArrayList<String>();
		list.add("http://192.168.2.206/res/upload/cdn/kting_huawei_fm/ampUser25/album/copyright/20170331/2b1i4t94.jpg");
		list.add("http://192.168.2.206/res/upload/cdn/kting_huawei_fm/ampUser40/album/copyright/20170331/2b1cey1c.jpg");
		try {
			//new Zip().writeZip(url, "p");
			new Zip().writeOneByOne(list, "8888");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void writeOneByOne(List<String> urlList, String zipname) throws IOException {
		OutputStream out = new BufferedOutputStream( new FileOutputStream( "D:/"+zipname+".zip" ) );
		
		byte[] buf = new byte[8192];
		int len;
		System.out.println(urlList.size());
		ZipOutputStream zos = new ZipOutputStream( out );
		for (String s: urlList ) {		
			//获取文件流
			URL url = new URL(s);
			
			HttpURLConnection connection =  (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);			
			InputStream in = connection.getInputStream();
			//InputStream in = new FileInputStream(file);			
			BufferedInputStream bis = new BufferedInputStream(in);
			String[] fileName = s.split("/");
			 ZipEntry ze = new ZipEntry( fileName[fileName.length-1] );
	            zos.putNextEntry( ze );		  
			  
			while ((len = bis.read(buf)) > 0) {
				//out.write(buf, 0, len);
				zos.write( buf, 0, len );
			}
			zos.closeEntry();
			

		}
		// zos.setEncoding("GBK");
		zos.closeEntry();
		  zos.close();
		
	}
	
	 private static void writeZip(String[] url,String zipname) throws IOException {
	        String[] files = url;
	        OutputStream os = new BufferedOutputStream( new FileOutputStream( "D:/"+zipname+".zip" ) );
	        ZipOutputStream zos = new ZipOutputStream( os );
	        byte[] buf = new byte[8192];
	        int len;
	        for (int i=0;i<files.length;i++) {  
	            File file = new File( files[i] );
	            if ( !file.isFile() ) continue;
	            ZipEntry ze = new ZipEntry( file.getName() );
	            zos.putNextEntry( ze );
	            BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );
	            while ( ( len = bis.read( buf ) ) > 0 ) {
	                zos.write( buf, 0, len );
	            }
	            zos.closeEntry();
	        }
	       // zos.setEncoding("GBK");
	        zos.closeEntry();
	        zos.close();
	        
	        for(int i=0;i<files.length;i++){
	         System.out.println("------------"+files );
	         File file= new File(files[i] );
	         file.delete();
	        }
	    }
}
