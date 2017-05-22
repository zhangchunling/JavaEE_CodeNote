package com.note.文件完整性验证;

public class StringMD5 {

	public static void main(String[] args) {
		//b[1] = 'b';
		byte[] bytes = "abc".getBytes();
		for(int i=0;i<bytes.length;i++){
			System.out.println(bytes[i]);
		}
		System.out.println("=============================");
		System.out.println((64>>>4));
		System.out.println(StringMD5.byteArrayToHex("1".getBytes()));
	}
	//将字节数组换成16进制的字符串
	public static String byteArrayToHex(byte[] byteArray) {  
		  
		   // 首先初始化一个字符数组，用来存放每个16进制字符  
		  
		   char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };  
		  
		  
		  
		   // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））  
		  
		   char[] resultCharArray =new char[byteArray.length * 2];  		  
		  
		  
		   // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去  		  
		   int index = 0;  
		  
		   for (byte b : byteArray) {  
			   
		      resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];  
		  
		      resultCharArray[index++] = hexDigits[b& 0xf];  
		  
		   }  
		  
		  
		  
		   // 字符数组组合成字符串返回  
		  
		   return new String(resultCharArray);  
	}
}
