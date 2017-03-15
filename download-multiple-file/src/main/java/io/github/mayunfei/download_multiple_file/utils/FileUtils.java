package io.github.mayunfei.download_multiple_file.utils;

import android.os.Environment;
import android.util.Log;
import java.io.File;

/**
 * Created by mayunfei on 17-3-15.
 */

public class FileUtils {
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
}
