package io.github.mayunfei.download_multiple_file.entity;

/**
 * 每一个小的下载任务
 * Created by yunfei on 17-3-14.
 */

public class TaskEntity {

  public static final String TASK_TABLE_NAME = "tasks";
  public static final String COLUMN_TASK_ID = "taskId";
  public static final String COLUMN_TASK_BUNDLE_ID = "taskBundleId";
  public static final String COLUMN_IS_FINISH = "isFinish";
  public static final String COLUMN_URL = "url";
  public static final String COLUMN_FILENAME = "fileName";
  public static final String COLUMN_FILEPATH = "filePath";
  public static final String COLUMN_TOTAL_SIZE = "totalSize";
  public static final String COLUMN_COMPLETED_SIZE = "completedSize";
  public static final String CREATE_SQL = "CREATE TABLE if not exists "
      +
      TASK_TABLE_NAME
      + "("
      + COLUMN_TASK_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT,"
      + COLUMN_TASK_BUNDLE_ID
      + " INTEGER,"
      + COLUMN_IS_FINISH
      + " BOOLEAN NOT NULL CHECK ("
      + COLUMN_IS_FINISH
      + " IN (0,1)),"
      + COLUMN_URL
      + " TEXT UNIQUE,"
      + COLUMN_TOTAL_SIZE
      + " LONG,"
      + COLUMN_COMPLETED_SIZE
      + " LONG,"
      + COLUMN_FILENAME
      + " TEXT,"
      + COLUMN_FILEPATH
      + " TEXT"
      + ");";
  private int taskBundleId;
  //id
  private int taskId;
  //是否已经下载完场
  private boolean isFinish;
  //下载链接
  private String url;
  //下载名称
  private String fileName;
  //文件地址
  private String filePath;
  //总共大小
  private long totalSize;
  //已经完成的大小 用于断点续传
  private long completedSize;

  public int getTaskBundleId() {
    return taskBundleId;
  }

  public void setTaskBundleId(int taskBundleId) {
    this.taskBundleId = taskBundleId;
  }

  public int getTaskId() {
    return taskId;
  }

  public void setTaskId(int taskId) {
    this.taskId = taskId;
  }

  public boolean isFinish() {
    return isFinish;
  }

  public void setFinish(boolean finish) {
    isFinish = finish;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public long getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(long totalSize) {
    this.totalSize = totalSize;
  }

  public long getCompletedSize() {
    return completedSize;
  }

  public void setCompletedSize(long completeSize) {
    this.completedSize = completeSize;
  }
}
