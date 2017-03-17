package io.github.mayunfei.download_multiple_file.entity;

import android.database.Cursor;
import io.github.mayunfei.download_multiple_file.db.DBHelper;
import java.util.List;
import rx.functions.Func1;

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
  public static final String COLUMN_IS_INIT = "isInit";
  public static final String COLUMN_M3U8 = "m3u8";
  public static final String COLUMN_HTML = "html";
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
      + COLUMN_IS_INIT
      + " BOOLEAN NOT NULL CHECK ("
      + COLUMN_IS_INIT
      + " IN (0,1)),"
      + COLUMN_M3U8
      + " TEXT,"
      + COLUMN_HTML
      + " TEXT,"
      + COLUMN_ARG0
      + " TEXT,"
      + COLUMN_ARG1
      + " TEXT,"
      + COLUMN_ARG2
      + " TEXT"
      + ");";

  private int bundleId = -1;
  //唯一值
  private String key;
  private String filePath;
  private int totalSize;
  private int completeSize;
  private int status;
  private boolean isInit;
  private List<TaskEntity> taskList;
  private String m3u8;
  private String html;
  private String arg0;
  private String arg1;
  private String arg2;

  private TaskBundle(Builder builder) {
    setBundleId(builder.bundleId);
    setKey(builder.key);
    setFilePath(builder.filePath);
    setTotalSize(builder.totalSize);
    setCompleteSize(builder.completeSize);
    setStatus(builder.status);
    setInit(builder.isInit);
    setM3u8(builder.m3u8);
    setHtml(builder.html);
    setArg0(builder.arg0);
    setArg1(builder.arg1);
    setArg2(builder.arg2);
  }

  public static final Func1<Cursor, TaskBundle> MAPPER = new Func1<Cursor, TaskBundle>() {
    @Override public TaskBundle call(Cursor cursor) {
      int bundleId = DBHelper.getInt(cursor, TaskBundle.COLUMN_BUNDLE_ID);
      String key = DBHelper.getString(cursor, TaskBundle.COLUMN_KEY);
      String path = DBHelper.getString(cursor, TaskBundle.COLUMN_FILEPATH);
      int totalSize = DBHelper.getInt(cursor, TaskBundle.COLUMN_TOTAL_SIZE);
      int completedSize = DBHelper.getInt(cursor, TaskBundle.COLUMN_COMPLETED_SIZE);
      int status = DBHelper.getInt(cursor, TaskBundle.COLUMN_STATUS);
      boolean isInit = DBHelper.getBoolean(cursor, TaskBundle.COLUMN_IS_INIT);
      String m3u8 = DBHelper.getString(cursor, TaskBundle.COLUMN_M3U8);
      String html = DBHelper.getString(cursor, TaskBundle.COLUMN_HTML);
      String arg0 = DBHelper.getString(cursor, TaskBundle.COLUMN_ARG0);
      String arg1 = DBHelper.getString(cursor, TaskBundle.COLUMN_ARG1);
      String arg2 = DBHelper.getString(cursor, TaskBundle.COLUMN_ARG2);
      return TaskBundle.newBuilder()
          .bundleId(bundleId)
          .key(key)
          .filePath(path)
          .totalSize(totalSize)
          .completeSize(completedSize)
          .status(status)
          .isInit(isInit)
          .m3u8(m3u8)
          .html(html)
          .arg0(arg0)
          .arg1(arg1)
          .arg2(arg2)
          .build();
    }
  };

  @Override public String toString() {
    String tmpStatus = "";
    switch (status) {
      case TaskStatus.STATUS_START:
        tmpStatus = "开始";
        break;
      case TaskStatus.STATUS_INIT:
        tmpStatus = "初始化";
        break;
      case TaskStatus.STATUS_CANCEL:
        tmpStatus = "取消";
        break;
      case TaskStatus.STATUS_QUEUE:
        tmpStatus = "等待";
        break;
      case TaskStatus.STATUS_CONNECTING:
        tmpStatus = "连接";
        break;
      case TaskStatus.STATUS_PAUSE:
        tmpStatus = "暂停";
        break;
      case TaskStatus.STATUS_ERROR_NET:
        tmpStatus = "网络错误";
        break;
      case TaskStatus.STATUS_ERROR_STORAGE:
        tmpStatus = "文件错误";
        break;
      case TaskStatus.STATUS_FINISHED:
        tmpStatus = "完成";
        break;
    }

    return "TaskBundle{" +
        "bundleId=" + bundleId +
        ", key='" + key + '\'' +
        ", totalSize=" + totalSize +
        ", completeSize=" + completeSize +
        ", status=" + tmpStatus +
        ", filePath='" + filePath + '\'' +
        ", isInit=" + isInit +
        ", taskList=" + taskList +
        ", m3u8='" + m3u8 + '\'' +
        ", html='" + html + '\'' +
        '}';
  }

  public TaskBundle() {

  }

  public static Builder newBuilder() {
    return new Builder();
  }

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

  public boolean isInit() {
    return isInit;
  }

  public void setInit(boolean init) {
    isInit = init;
  }

  public String getM3u8() {
    return m3u8;
  }

  public void setM3u8(String m3u8) {
    this.m3u8 = m3u8;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
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

  public void init(TaskBundle taskBundle) {
    bundleId = taskBundle.bundleId;
    key = taskBundle.key;
    filePath = taskBundle.filePath;
    totalSize = taskBundle.totalSize;
    completeSize = taskBundle.completeSize;
    status = taskBundle.status;
    isInit = taskBundle.isInit;
    m3u8 = taskBundle.m3u8;
    html = taskBundle.html;
    arg0 = taskBundle.arg0;
    arg1 = taskBundle.arg1;
    arg2 = taskBundle.arg2;
  }

  public static final class Builder {
    private int bundleId = -1;
    private String key;
    private String filePath;
    private int totalSize;
    private int completeSize;
    private int status;
    private boolean isInit;
    private String m3u8;
    private String html;
    private String arg0;
    private String arg1;
    private String arg2;

    private Builder() {
    }

    public Builder bundleId(int val) {
      bundleId = val;
      return this;
    }

    public Builder key(String val) {
      key = val;
      return this;
    }

    public Builder filePath(String val) {
      filePath = val;
      return this;
    }

    public Builder totalSize(int val) {
      totalSize = val;
      return this;
    }

    public Builder completeSize(int val) {
      completeSize = val;
      return this;
    }

    public Builder status(int val) {
      status = val;
      return this;
    }

    public Builder isInit(boolean val) {
      isInit = val;
      return this;
    }

    public Builder m3u8(String val) {
      m3u8 = val;
      return this;
    }

    public Builder html(String val) {
      html = val;
      return this;
    }

    public Builder arg0(String val) {
      arg0 = val;
      return this;
    }

    public Builder arg1(String val) {
      arg1 = val;
      return this;
    }

    public Builder arg2(String val) {
      arg2 = val;
      return this;
    }

    public TaskBundle build() {
      return new TaskBundle(this);
    }
  }
}
