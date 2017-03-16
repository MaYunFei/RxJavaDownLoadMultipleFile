package io.github.mayunfei.download_multiple_file.db;

import android.database.Cursor;

/**
 * Created by mayunfei on 17-3-9.
 */

public class DBHelper {
  public static int getInt(Cursor cursor, String columnName) {
    if (null != cursor) {
      return cursor.getInt(cursor.getColumnIndex(columnName));
    }
    return 0;
  }

  public static long getLong(Cursor cursor, String columnName) {
    if (null != cursor) {
      return cursor.getLong(cursor.getColumnIndex(columnName));
    }
    return 0;
  }

  public static boolean getBoolean(Cursor cursor, String columnName) {
    return cursor != null && cursor.getInt(cursor.getColumnIndex(columnName)) > 0;
  }

  public static String getString(Cursor cursor, String columnName) {
    if (null != cursor) {
      return cursor.getString(cursor.getColumnIndex(columnName));
    }
    return "";
  }
}
