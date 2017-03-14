package io.github.mayunfei.download_multiple_file.utils;

import android.util.Log;

/**
 * Created by yunfei on 17-3-14.
 */

public class L {
  private static final boolean isDebug = true;

  private L() {
  }

  private static final String TAG = "YunFei_Download";

  public static void i(String message) {
    i(TAG, message);
  }

  public static void i(String tag, String message) {
    Log.i(tag, message);
  }

  public static void e(String message) {
    e(TAG, message);
  }

  public static void e(String tag, String message) {
    if (isDebug) {
      Log.e(tag, message);
    }
  }
}
