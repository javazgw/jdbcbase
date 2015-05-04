package com.base.myproject.server.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Tools {

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}
/**
 * 删除目录
 */
	public static void delDirectionry( String dir)
	{
		File file = new File(dir);
		try {
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	// 复制文件夹
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()&& !file[i].isHidden()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	/**
	 * 检查文件夹是否存在 true存在 否则不存在
	 * 
	 * @return
	 */
	public static boolean isExistsDir(String dir) {
		File f = new File(dir);

		return f.exists();

	}

	/**
	 * 生成文件
	 * 
	 * @param filename
	 * @param content
	 * @return
	 */
	public static boolean createfile(String filename, String content) {
		// File f = new File(filename);
		FileWriter writer;
		try {
			writer = new FileWriter(filename);

			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String args[]) throws IOException {
		// if(args.length!=2)
		// {
		// System.err.println("请录入拷贝文件路径，目标路径");
		// throw new RuntimeException("请录入拷贝文件路径，目标路径");
		//
		//
		// }
		//
		// // 源文件夹
		// String url1 = args[0];
		// // 目标文件夹
		// String url2 = args[1];
		// // 创建目标文件夹
		// // (new File(url1)).mkdirs();
		// // 获取源文件夹当前下的文件或目录
		// File[] file = (new File(url1)).listFiles();
		// for (int i = 0; i < file.length; i++) {
		// if (file[i].isFile()) {
		// // 复制文件
		// copyFile(file[i],new File(url2+file[i].getName()));
		// }
		// if (file[i].isDirectory()) {
		// // 复制目录
		// String sourceDir=url1+File.separator+file[i].getName();
		// String targetDir=url2+File.separator+file[i].getName();
		// copyDirectiory(sourceDir, targetDir);
		// }
		// }
		System.out.println(isExistsDir("c:\\db.pro"));
		System.out.println(isExistsDir("c:\\db2.pro"));

		createfile("c:\\db.pro", "dddd\n\nddd222");

	}
}
