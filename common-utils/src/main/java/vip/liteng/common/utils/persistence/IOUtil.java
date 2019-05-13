package vip.liteng.common.utils.persistence;

import android.text.TextUtils;

import java.io.*;
import java.util.Locale;

/**
 * @author liteng
 */
public class IOUtil {
    public static final String DEFAULT_CHARSET = "utf-8";

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /** 1024 * 1024 * 1024 */
    public static final long GB = 1073741824;
    /** 1024 * 1024 */
    public static final long MB = 1048576;
    public static final long KB = 1024;

    /**
     * 从路径中获取文件全名
     *
     * @param filePath 文件路径
     * @return 从路径中获取的文件全名
     */
    public static String getFileNameInPath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int lastSeparator = filePath.lastIndexOf(File.separator);
        return filePath.substring(lastSeparator + 1);
    }

    /**
     * 从路径中获取文件后缀
     *
     * @param filePath 文件路径
     * @return 从路径中获取的文件后缀
     */
    public static String getFileSuffix(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int lastSeparator = filePath.lastIndexOf(".");
        return filePath.substring(lastSeparator + 1);
    }


    /**
     * 文件大小获取
     *
     * @param path 文件路径
     * @return 文件大小字符串
     */
    public static String getSize(String path) {
        File file = new File(path);
        return getSize(file);
    }

    /**
     * 文件大小获取
     *
     * @param file File对象
     * @return 文件大小字符串
     */
    public static String getSize(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            if (length >= GB) {
                return String.format(Locale.getDefault(), "%.2f GB", length * 1.0f / GB);
            } else if (length >= MB) {
                return String.format(Locale.getDefault(), "%.2f MB", length * 1.0f / MB);
            } else {
                return String.format(Locale.getDefault(), "%.2f KB", length * 1.0f / KB);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis);
        }
        return "未知";
    }


    public static boolean exists(String filePath) {
        File file = new File(filePath);
        return exists(file);
    }

    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    public static boolean createFileNotExists(String filePath) {
        File file = new File(filePath);
        return createFileNotExists(file);
    }

    public static boolean createFileNotExists(File file) {
        if (exists(file)) {
            return true;
        }

        File parentDir = file.getParentFile();

        if (parentDir != null && !exists(parentDir)) {
            parentDir.mkdirs();
        }

        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readFile(String filePath) {
        File file = new File(filePath);
        return readFile(file);
    }

    public static String readFile(File file) {
        InputStreamReader isr;
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder();
        try {
            isr = new InputStreamReader(new FileInputStream(file), DEFAULT_CHARSET);
            bufferedReader = new BufferedReader(isr, DEFAULT_BUFFER_SIZE);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedReader.readLine();
                builder.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bufferedReader);
        }
        return builder.toString();
    }


    public static void writeFile(String content, String filePath) {
        writeFile(content, filePath, false);
    }

    public static void writeFile(String content, File file) {
        writeFile(content, file, false);
    }


    public static void writeFile(String content, String filePath, boolean append) {
        File file = new File(filePath);
        writeFile(content, file, append);
    }

    public static void writeFile(String content, File file, boolean append) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(file, append);
            bufferedWriter = new BufferedWriter(fileWriter, DEFAULT_BUFFER_SIZE);
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fileWriter);
            close(bufferedWriter);
        }
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return deleteFile(file);
    }

    public static boolean deleteFile(File file) {
        if (exists(file)) {
            return file.delete();
        }
        // 如果文件不存在则认为已经删成功
        return true;
    }


    public static boolean deleteDir(String dirPath) {
        File dir = new File(dirPath);
        return deleteDir(dir);
    }

    public static boolean deleteDir(File dir) {
        // 如果不存在也认为删除成功
        if (!exists(dir)) {
            return true;
        }

        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isFile()) {
                        child.delete();
                    } else if (child.isDirectory()) {
                        deleteDir(child);
                    }
                }
            }
        }
        // 先将文件夹里的文件删除掉，最后删除空文件夹
        return dir.delete();
    }

    /**
     * 输入流转byte[]
     *
     * @param inStream InputStream
     * @return Byte数组
     */
    public static byte[] input2byte(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc;
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return swapStream.toByteArray();
    }


    public static void close(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Writer writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(FileInputStream fis) {
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
