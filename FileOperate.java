/**
 * 
 */
package com.fileoperate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author dell
 *
 */
public class FileOperate {
	// public static Logger logger = Logger.getLogger("INFO");

	/**
	 * 
	 */
	public FileOperate() {
	}

	/**
	 * 字节流读取，会遇到中文乱码问题
	 * 
	 * @deprecated
	 * @param filePath
	 */
	public static long readFileByByte(String filePath) {
		long start = System.currentTimeMillis();
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
			int tmp;
			StringBuffer sb = new StringBuffer();
			// read() 来自native的read0方法
			while ((tmp = is.read()) != -1) {
				sb.append((char) tmp);
			}
			System.out.println(sb);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return System.currentTimeMillis() - start;
	}

	/**
	 * 定长字节流读取，会遇到中文乱码问题.无法避开半中文字问题.
	 * 
	 * @param filePath
	 */
	public static long readFileByByte(String filePath, int n) {
		long start = System.currentTimeMillis();
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
			int tmp;
			StringBuffer sb = new StringBuffer();
			byte[] b = new byte[n];
			while ((tmp = is.read(b)) != -1) {
				sb.append(new String(b, 0, tmp));
			}
			System.out.println(sb);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return System.currentTimeMillis() - start;
	}

	/**
	 * 字符读取
	 * 
	 * @param filePath
	 */
	public static long readFileByCharacter(String filePath) {
		long start = System.currentTimeMillis();
		FileReader fr = null;
		try {
			fr = new FileReader(new File(filePath));
			int tmp;
			StringBuffer sb = new StringBuffer();
			while ((tmp = fr.read()) != -1) {
				sb.append((char) tmp);
			}
			System.out.println(sb);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return System.currentTimeMillis() - start;
	}

	/**
	 * 读取定长字符
	 * 
	 * @param filePath
	 */
	public static long readFileByCharacter(String filePath, int n) {
		long start = System.currentTimeMillis();
		FileReader fr = null;
		try {
			fr = new FileReader(new File(filePath));
			int tmp;
			StringBuffer sb = new StringBuffer();
			char[] c = new char[n];
			while ((tmp = fr.read(c)) != -1) {
				sb.append(c, 0, tmp);
			}
			System.out.println(sb);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return System.currentTimeMillis() - start;
	}

	/**
	 * 行读取都是以char来操作的
	 * 
	 * @param filePath
	 */
	public static void readFileByLine(String filePath) {
		//
		BufferedReader br = null;
		//
		StringBuffer sb = new StringBuffer();
		//
		String tmp = null;
		try {
			br = new BufferedReader(new FileReader(new File("filePath")));
			while ((tmp = br.readLine()) != null) {
				sb.append(tmp);
			}
			System.out.println(sb);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 5M緩存行读取. 2G以上大文件推荐 原理就是以5M进行行读取。
	 * 
	 * @param filePath
	 */
	public static long readLargeFileByByte(String filePath, int n,
			String outputFilePath) {
		long start = System.currentTimeMillis();
		BufferedInputStream bis = null;
		BufferedReader bfr = null;
		FileWriter fileWriter = null;
		String line;
		try {
			bis = new BufferedInputStream(new FileInputStream(
					new File(filePath)));
			bfr = new BufferedReader(new InputStreamReader(bis, "UTF-8"), n);
			fileWriter = new FileWriter(new File(outputFilePath));
			StringBuffer sb = new StringBuffer();
			Map<String, Integer> map = new HashMap<String, Integer>();
			while ((line = bfr.readLine()) != null) {
				// fileWriter.append(sb.append(line + "\n"));
				if (map.containsKey(line.trim())) {
					map.put(line.trim(), map.get(line.trim()) + 1);
				} else {
					map.put(line.trim(), 1);
				}
			}
			// iterateSet(map);
			// forEach(map);
			fileWriter.append(entrySet(map));
			// System.out.println(sb.toString());
			fileWriter.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (bfr != null) {
					bfr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return System.currentTimeMillis() - start;
	}

	/**
	 * NIO Byte读取
	 * 
	 * @param filePath
	 */
	public static long readFileByNIO(String filePath, String fileOutPath, int n) {
		long start = System.currentTimeMillis();
		ByteBuffer bb = null;
		FileInputStream fis = null;
		// FileOutputStream fos = null;
		FileChannel fc = null;
		// FileChannel fc2 = null;
		try {
			fis = new FileInputStream(new File(filePath));
			// fos = new FileOutputStream(new File(fileOutPath));
			fc = fis.getChannel();
			// FileChannel fc2 = fos.getChannel();
			bb = ByteBuffer.allocate(n);
			while (fc.read(bb) != -1) {
				bb.clear();
				bb.flip();
				// fc2.write(bb);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fc != null) {
					fc.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return System.currentTimeMillis() - start;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("大文件行读取[]: "
		// iplog.20170825
				+ readLargeFileByByte("D:/相关文档/利农商城/iplog.20170825",
				// 5 * 1024* 1024
						5, "C:/Users/dell/Desktop/accesslog.170812") + "ms");

		/**
		 * System.out.println("流读取[]: " +
		 * readFileByCharacter("C:/Users/dell/Desktop/accesslog.170810", 1024) +
		 * "ms");
		 */
		/**
		 * System.out.println("行读取[]: " +
		 * readFileByCharacter("C:/Users/dell/Desktop/accesslog.170810", 1024) +
		 * "ms");
		 */
		// readFileByLine
	}

	/**
	 * entrySet遍历用时：44526672ns
	 * 
	 * @param map
	 */
	public static StringBuffer entrySet(Map<String, Integer> map) {
		long start = System.nanoTime();
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			// System.out.println(entry.getKey() + "|" + entry.getValue());
			sb.append(entry.getKey() + "|" + entry.getValue() + "\n");
		}
		System.out
				.println("entrySet遍历用时：" + (System.nanoTime() - start) + "ns");
		return sb;
	}

	/**
	 * iterateSet用时：1425709ns
	 * 
	 * @param map
	 */
	public static StringBuffer iterateSet(Map<String, Integer> map) {
		long start = System.nanoTime();
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, Integer>> iter = map.entrySet().iterator();
		Map.Entry<String, Integer> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			// System.out.println(entry.getKey() + "|" + entry.getValue());
		}
		System.out
				.println("iterateSet用时：" + (System.nanoTime() - start) + "ns");
		return sb;
	}

	/**
	 * entrySet遍历用时：44526672ns forEach遍历用时：127330675ns
	 * 
	 * @param map
	 */
	public static StringBuffer forEach(Map<String, Integer> map) {
		long start = System.nanoTime();
		StringBuffer sb = new StringBuffer();
		map.forEach((k, v) -> {
			// System.out.println(k + "|" + v);
		});
		System.out.println("forEach遍历用时：" + (System.nanoTime() - start) + "ns");
		return sb;
	}
}
