package com.note.文件完整性验证;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
  /**
     用MD5验证上传文件的完整性：
   
     在项目的开发资源开发中，测试发现一个问题：将资源上传到服务器上，提示已经上传成功，但是当打开这个文件时发现失败，
     由于各种原因资源已经毁坏。
   
      怎么样能保证资源的完整性，处理办法就是用MD5验证文件的完整性。
   
     任何一个字符串或文件，无论是可执行程序、图像文件、临时文件或者其他任何类型的文件，也不管它体积多大，
     都有且只有一个独一无二的MD5信息码，并且如果这个文件被修改过，它的MD5码也将随之改变。
     
   Message-Digest泛指字节串(Message)的Hash变换，就是把一个任意长度的字节串变换成一定长的大整数。
     注意这里说的是“字节串”不是“字符串”，因为这种变换只与字节的值有关，与字符集或编码方式无关。
      在Java中，java.security.MessageDigest （rt.jar中）已经定义了 MD5 的计算，所以我们只需要
      简单地调用即可得到 MD5 的128 位整数。然后将此 128 位计 16 个字节转换成 16 进制表示即可。
   */
public class FileSecurity {  
    /** 
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合 
     */  
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
   
    protected static MessageDigest messagedigest = null;  
    
    static {  
        try {  
        	//初始化
            messagedigest = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException nsaex) {  
            System.err.println(FileSecurity.class.getName()  
                    + "初始化失败，MessageDigest不支持MD5Util。");  
            nsaex.printStackTrace();  
        }  
    }  
       
    /** 
     * 生成字符串的md5校验值 
     *  
     * @param s 
     * @return 
     */  
    public static String getMD5String(String s) {  
        return getMD5String(s.getBytes());  
    }  
       
    /** 
     * 判断字符串的md5校验码是否与一个已知的md5码相匹配 
     *  
     * @param password 要校验的字符串 
     * @param md5PwdStr 已知的md5校验码 
     * @return 
     */  
    public static boolean checkPassword(String md5, String md5PwdStr) {  
        return md5.equals(md5PwdStr);  
    }  
       
    /** 
     * 生成文件的md5校验值 
     *  
     * @param file 
     * @return 
     * @throws IOException 
     */  
    public static String getFileMD5String(File file) throws IOException {         
        InputStream fis;  
        fis = new FileInputStream(file);  
        byte[] buffer = new byte[1024];  
        int numRead = 0;  
        while ((numRead = fis.read(buffer)) > 0) {  
            messagedigest.update(buffer, 0, numRead);  
        }  
        fis.close();  
        return bufferToHex(messagedigest.digest());  
    }  
   
    public static String getMD5String(byte[] bytes) {  
        messagedigest.update(bytes);  
        return bufferToHex(messagedigest.digest());  
    }  
   
    private static String bufferToHex(byte bytes[]) {  
        return bufferToHex(bytes, 0, bytes.length);  
    }  
   
    private static String bufferToHex(byte bytes[], int m, int n) {  
        StringBuffer stringbuffer = new StringBuffer(2 * n);  
        int k = m + n;  
        for (int l = m; l < k; l++) {  
            appendHexPair(bytes[l], stringbuffer);  
        }  
        return stringbuffer.toString();  
    }  
   
    //将字节数组转成十六进制字符串
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {  
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同   
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换   
        stringbuffer.append(c0);  
        stringbuffer.append(c1);  
    }  
       
    public static void main(String[] args) throws IOException {  
        long begin = System.currentTimeMillis();  

        File file = new File("D:/123456.txt");  
        String md5 = getFileMD5String(file);  
        System.out.println("MD5:"+md5); 
        
        //文件名不同，内容相同；  
        File file2 = new File("D:/123456(2).txt");  
        String md52= getFileMD5String(file2);  
        System.out.println("MD5:"+md52);  
          
        //文件名不同，内容不同；  
        File file3 = new File("D:/123456(3).txt");  
        String md53= getFileMD5String(file3);  
        System.out.println("MD5:"+md53);  
        
        //测试压缩包  
        File fileZip = new File("D:/1.zip");  
        String md5Zip= getFileMD5String(fileZip);  
        System.out.println("MD5:"+md5Zip);  
        
        //测试压缩包  
        File fileZip2 = new File("D:/2.zip");  
        String md5Zip2= getFileMD5String(fileZip2);  
        System.out.println("MD5:"+md5Zip2);       
        
          
        System.out.println("两个文件名不同，内同相同"+ checkPassword(md5, md52));  
        System.out.println("文件名不同，内容不同"+ checkPassword(md5, md53));  
        System.out.println("测试压缩包,内容不同"+ checkPassword(md5Zip, md5Zip2));  
   
        long end = System.currentTimeMillis();  
        System.out.println("md5:" + md5 + " time:" + ((end - begin) / 1000) + "s");  
    }  
}  