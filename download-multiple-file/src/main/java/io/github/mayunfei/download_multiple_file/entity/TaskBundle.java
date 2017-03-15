package io.github.mayunfei.download_multiple_file.entity;

import java.util.List;

/**
 * 下载整体
 * Created by yunfei on 17-3-14.
 */

public class TaskBundle {

  public static final String TASK_BUNDLE_TABLE_NAME = "taskBundle";
  public static final String COLUMN_BUNDLE_ID = "bundleId";
  public static final String COLUMN_KEY = "key";
  public static final String COLUMN_FILEPATH = "filePath";
  public static final String COLUMN_TOTAL_SIZE = "totalSize";
  public static final String COLUMN_COMPLETED_SIZE = "completeSize";
  public static final String COLUMN_STATUS = "status";
  public static final String COLUMN_ARG0 = "arg0";
  public static final String COLUMN_ARG1 = "arg1";
  public static final String COLUMN_ARG2 = "arg2";

  public static final String CREATE_SQL = "CREATE TABLE if not exists "
      +
      TASK_BUNDLE_TABLE_NAME
      + "("
      + COLUMN_BUNDLE_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT,"
      + COLUMN_KEY
      + " TEXT UNIQUE,"
      //唯一
      + COLUMN_TOTAL_SIZE
      + " INTEGER,"
      + COLUMN_COMPLETED_SIZE
      + " INTEGER,"
      + COLUMN_STATUS
      + " INTEGER,"
      + COLUMN_FILEPATH
      + " TEXT,"
      + COLUMN_ARG0
      + " TEXT,"
      + COLUMN_ARG1
      + " TEXT,"
      + COLUMN_ARG2
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
  private String arg0;
  private String arg1;
  private String arg2;

  public int getBundleId() {
    return bundleId;
  }

  public void setBundleId(int bundleId) {
    this.bundleId = bundleId;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public int getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(int totalSize) {
    this.totalSize = totalSize;
  }

  public int getCompleteSize() {
    return completeSize;
  }

  public void setCompleteSize(int completeSize) {
    this.completeSize = completeSize;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public List<TaskEntity> getTaskList() {
    return taskList;
  }

  public void setTaskList(List<TaskEntity> taskList) {
    this.taskList = taskList;
  }

  public String getArg0() {
    return arg0;
  }

  public void setArg0(String arg0) {
    this.arg0 = arg0;
  }

  public String getArg1() {
    return arg1;
  }

  public void setArg1(String arg1) {
    this.arg1 = arg1;
  }

  public String getArg2() {
    return arg2;
  }

  public void setArg2(String arg2) {
    this.arg2 = arg2;
  }
}
