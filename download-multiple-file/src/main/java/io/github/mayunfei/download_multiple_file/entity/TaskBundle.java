package io.github.mayunfei.download_multiple_file.entity;

import java.util.List;

/**
 * 下载整体
 * Created by yunfei on 17-3-14.
 */

public class TaskBundle {

  public static final String TASK_TABLE_NAME = "taskBundle";
  public static final String COLUMN_BUNDLE_ID = "bundleId";
  public static final String COLUMN_KEY = "key";
  public static final String COLUMN_FILEPATH = "filePath";
  public static final String COLUMN_TOTAL_SIZE = "totalSize";
  public static final String COLUMN_COMPLETE_SIZE = "completeSize";
  public static final String COLUMN_STATUS = "status";
  public static final String CREATE_SQL = "CREATE TABLE if not exists "
      +
      TASK_TABLE_NAME
      + "("
      + COLUMN_BUNDLE_ID
      + " INTEGER PRIMARY KEY,"
      + COLUMN_KEY
      + " TEXT,"
      + COLUMN_FILEPATH
      + " TEXT,"
      + COLUMN_TOTAL_SIZE
      + " INTEGER,"
      + COLUMN_COMPLETE_SIZE
      + " INTEGER,"
      + COLUMN_STATUS
      + " INTEGER,"
      + COLUMN_FILEPATH
      + " TEXT"
      + ");";

  private int bundleId;
  //唯一值
  private String key;
  private String filePath;
  private int totalSize;
  private int completeSize;
  private int status;
  private List<TaskEntity> taskList;
}
