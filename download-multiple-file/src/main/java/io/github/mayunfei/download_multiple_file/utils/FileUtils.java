package io.github.mayunfei.download_multiple_file.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;

/**
 * Created by mayunfei on 17-3-15.
 */

public class FileUtils {
  private FileUtils() {
  }

  public static String getDefaultFilePath() {
    String filePath =
        Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunfei/download/";
    File file = new File(filePath);
    if (!file.exists()) {
      boolean createDir = file.mkdirs();
      if (createDir) {
        L.i("create file success");
      }
    }
    return filePath;
  }

  public static String getFilePath(String path) {
    File file = new File(path);
    if (!file.exists()) {
      boolean createDir = file.mkdirs();
      if (createDir) {
        L.i("create file " + path);
      }
    }
    return file.getAbsolutePath();
  }

  public static String getFileNameFromUrl(String url) {
    if (!TextUtils.isEmpty(url)) {
      return url.substring(url.lastIndexOf("/") + 1);
    }
    return System.currentTimeMillis() + "";
  }

  /**
   * 删除目录
   *
   * @param dirPath 目录路径
   * @return {@code true}: 删除成功<br>{@code false}: 删除失败
   */
  public static boolean deleteDir(String dirPath) {
    return deleteDir(getFileByPath(dirPath));
  }

  /**
   * 删除目录
   *
   * @param dir 目录
   * @return {@code true}: 删除成功<br>{@code false}: 删除失败
   */
  public static boolean deleteDir(File dir) {
    if (dir == null) return false;
    // 目录不存在返回true
    if (!dir.exists()) return true;
    // 不是目录返回false
    if (!dir.isDirectory()) return false;
    // 现在文件存在且是文件夹
    File[] files = dir.listFiles();
    if (files != null && files.length != 0) {
      for (File file : files) {
        if (file.isFile()) {
          if (!deleteFile(file)) return false;
        } else if (file.isDirectory()) {
          if (!deleteDir(file)) return false;
        }
      }
    }
    return dir.delete();
  }

  /**
   * 删除文件
   *
   * @param file 文件
   * @return {@code true}: 删除成功<br>{@code false}: 删除失败
   */
  public static boolean deleteFile(File file) {
    return file != null && (!file.exists() || file.isFile() && file.delete());
  }

  /**
   * 根据文件路径获取文件
   *
   * @param filePath 文件路径
   * @return 文件
   */
  public static File getFileByPath(String filePath) {
    return TextUtils.isEmpty(filePath) ? null : new File(filePath);
  }
}
