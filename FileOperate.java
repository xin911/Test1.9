/**
 * 
 */
package FileUtil;

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
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author dell
 *
 */
public class FileOperate {
	// public static Logger logger = Logger.getAnonymousLogger();

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
	 * @deprecated
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
	public static long readLargeFileByByte(String filePath, int n, String outputFilePath) {
		long start = System.currentTimeMillis();
		BufferedInputStream bis = null;
		BufferedReader bfr = null;
		FileWriter fileWriter = null;
		String line;
		int i = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
			bfr = new BufferedReader(new InputStreamReader(bis, "UTF-8"), n);
			fileWriter = new FileWriter(new File(outputFilePath));
			while ((line = bfr.readLine()) != null) {
				fileWriter.append(line + "\n");
				i++;
			}
			System.out.println(fileWriter.toString());
			fileWriter.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
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
			System.out.println("[" + i + ":行数" + "][" + Runtime.getRuntime().freeMemory() / (1014 * 1024) + "/"
					+ Runtime.getRuntime().totalMemory() / (1014 * 1024) + "/"
					+ Runtime.getRuntime().maxMemory() / (1014 * 1024) + "][进程数:"
					+ ManagementFactory.getThreadMXBean().getThreadCount() + "]");
		}
		return System.currentTimeMillis() - start;
	}

	/**
	 * NIO 行读取
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
		int i = 0;
		try {
			fis = new FileInputStream(new File(filePath));
			// fos = new FileOutputStream(new File(fileOutPath));
			fc = fis.getChannel();
			// FileChannel fc2 = fos.getChannel();
			// fc.transferTo(0, fc.size(), fos);//连接两个通道，并且从in通道读取，然后写入out通道
			while (fc.read(bb = ByteBuffer.allocate(n)) != -1) {
				bb.flip();
				// System.out.print("[" +
				// Charset.defaultCharset().newDecoder().decode(bb) + "]");
				bb.clear();
				i++;
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
		System.out.println("\n[" + i + ":行数" + "][" + Runtime.getRuntime().freeMemory() / (1014 * 1024) + "/"
				+ Runtime.getRuntime().totalMemory() / (1014 * 1024) + "/"
				+ Runtime.getRuntime().maxMemory() / (1014 * 1024) + "][进程数:"
				+ ManagementFactory.getThreadMXBean().getThreadCount() + "]");
		return System.currentTimeMillis() - start;
	}

	public static long readBymappedByteBuffer(String filePath, int n) {
		long start = System.currentTimeMillis();
		BufferedInputStream bufferedInputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
			while(bufferedInputStream.read(new byte[12])!= null)
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		MappedByteBuffer mappedByteBuffer = (MappedByteBuffer) MappedByteBuffer.allocateDirect(n);
		return System.currentTimeMillis() - start;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// readFileByLine
		System.out.println(readFileByNIO("D:/JAVA/trunk/Test1.8/resource/read2.txt", null, 5 * 1024 * 1024));

	}
}
