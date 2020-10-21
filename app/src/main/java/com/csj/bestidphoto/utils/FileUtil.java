package com.csj.bestidphoto.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.util.Log;

public class FileUtil {

	/*
     * 判断路径是否存在，不存在则建立路径
	 */

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File createNewFile(String path){
		File f = new File(path);
		if (!f.exists()) {
			try {
				// 在指定的文件夹中创建文件
				if(f.createNewFile()){
					return f;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static File createNewFile(File f){
		if (!f.exists()) {
			try {
				// 在指定的文件夹中创建文件
				if(f.createNewFile()){
					return f;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 删除文件夹
	 *
	 * @param folderPath
	 *            String 文件夹路径及名称 如c:/fqf
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			Log.i("FileUtil", "删除文件夹操作出错");
			e.printStackTrace();

		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 *
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		if(tempList == null){
			return;
		}
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 *
	 * 2018年6月15日
	 * 作者：csj
	 * 方法描述：把byte[]保存到文件
	 * @param data
	 * @param dir
	 * @param filename
	 * @throws IOException
	 */
	public static void writeBytesToFile(byte[] data,String dir,String filename) throws IOException{
		makeRootDirectory(dir);
		String path = dir+filename;
		OutputStream out = new FileOutputStream(path);
		InputStream is = new ByteArrayInputStream(data);
		byte[] buff = new byte[1024];
		int len = 0;
		while((len=is.read(buff))!=-1){
			out.write(buff, 0, len);
		}
		is.close();
		out.close();
	}

	public static void deleteFile(String path){
		File temp = new File(path);
		if(temp.exists()&&temp.isFile()){
			temp.delete();
		}
	}

	public static void deleteFile(File file){
		if(file.exists()&&file.isFile()){
			file.delete();
		}
	}

	/**
	 * 判断txt文件编码格式
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String resolveCode(String path) throws Exception {
		//      String filePath = “D:/article.txt”; //[-76, -85, -71]  ANSI
		//      String filePath = “D:/article111.txt”;  //[-2, -1, 79] unicode big endian
		//      String filePath = “D:/article222.txt”;  //[-1, -2, 32]  unicode
		//      String filePath = “D:/article333.txt”;  //[-17, -69, -65] UTF-8
		InputStream inputStream = new FileInputStream(path);
		byte[] head = new byte[3];
		inputStream.read(head);
		String code = "gb2312";  //或GBK
		if (head[0] == -1 && head[1] == -2){
			code = "UTF-16";
		}else if (head[0] == -2 && head[1] == -1){
			code = "Unicode";
		}else if (head[0] == -17 && head[1] == -69 && head[2] == -65){
			code = "UTF-8";
		}

		inputStream.close();

		System.out.println(code);
		return code;
	}

}
