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
}
