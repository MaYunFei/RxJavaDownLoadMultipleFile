package io.github.mayunfei.download_multiple_file.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.entity.TaskEntity;
import io.github.mayunfei.download_multiple_file.utils.L;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 数据库操作
 * Created by yunfei on 17-3-14.
 */

public class DownloadDao {

  private final BriteDatabase db;

  public DownloadDao(Context context) {
    db = new SqlBrite.Builder().build()
        .wrapDatabaseHelper(new SQLiteHelper(context.getApplicationContext()), Schedulers.io());
  }

  /**
   * 插入TaskBundle
   */
  public void insertTaskBundle(TaskBundle taskBundle) {
    if (!isExistTaskBundle(taskBundle.getKey())) {
      BriteDatabase.Transaction transaction = db.newTransaction();
      try {
        int nextBundleId =
            getNextID(db, TaskBundle.TASK_BUNDLE_TABLE_NAME, TaskBundle.COLUMN_BUNDLE_ID);
        db.insert(TaskBundle.TASK_BUNDLE_TABLE_NAME, getTaskBundleValues(taskBundle));
        List<TaskEntity> taskList = taskBundle.getTaskList();

        taskBundle.setBundleId(nextBundleId);

        if (taskList != null && taskList.size() > 0) {
          int nextTaskId = getNextID(db, TaskEntity.TASK_TABLE_NAME, TaskEntity.COLUMN_TASK_ID);
          for (TaskEntity taskEntity : taskList) {
            taskEntity.setTaskBundleId(nextBundleId);
            taskEntity.setTaskId(nextTaskId);
            db.insert(TaskEntity.TASK_TABLE_NAME, getTaskEntity(taskEntity));
            nextTaskId++;
          }
        }
        transaction.markSuccessful();
      } finally {
        transaction.end();
      }
    }
  }

  private int getTaskBundleIdByKey(String key) {
    Cursor query = null;
    try {
      query = db.query("SELECT "
          + TaskBundle.COLUMN_BUNDLE_ID
          + " FROM"
          + TaskBundle.TASK_BUNDLE_TABLE_NAME
          + " WHERE "
          + TaskBundle.COLUMN_KEY
          + "=?", key);
      if (query.moveToFirst()) return query.getInt(0);
      return -1;
    } finally {
      if (query != null) {
        query.close();
      }
    }
  }

  //private int getTaskBundleLastId() {
  //  Cursor query = null;
  //  try {
  //    query = db.query("SELECT last_insert_rowid() from" + TaskBundle.TASK_BUNDLE_TABLE_NAME);
  //    if (query.moveToFirst()) return query.getInt(0);
  //    return 0;
  //  } finally {
  //    if (query != null) {
  //      query.close();
  //    }
  //  }
  //}

  /**
   * 更新TaskBundle
   */
  public void updateTaskBundle(TaskBundle taskBundle) {

    if (taskBundle.getTaskList() == null || taskBundle.getTaskList().size() == 0) {
      db.update(TaskBundle.TASK_BUNDLE_TABLE_NAME, getTaskBundleValues(taskBundle),
          TaskBundle.COLUMN_KEY + "=?", taskBundle.getKey());
    } else {
      if (!isExistTaskEntity(taskBundle.getBundleId())) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        db.update(TaskBundle.TASK_BUNDLE_TABLE_NAME, getTaskBundleValues(taskBundle),
            TaskBundle.COLUMN_KEY + "=?", taskBundle.getKey());
        int nextTaskId = getNextID(db, TaskEntity.TASK_TABLE_NAME, TaskEntity.COLUMN_TASK_ID);
        for (TaskEntity taskEntity : taskBundle.getTaskList()) {
          taskEntity.setTaskBundleId(taskBundle.getBundleId());
          taskEntity.setTaskId(nextTaskId);
          db.insert(TaskEntity.TASK_TABLE_NAME, getTaskEntity(taskEntity));
          nextTaskId++;
        }
        transaction.markSuccessful();
        transaction.end();
      }
      db.update(TaskBundle.TASK_BUNDLE_TABLE_NAME, getTaskBundleValues(taskBundle),
          TaskBundle.COLUMN_KEY + "=?", taskBundle.getKey());
    }
  }

  public boolean isExistTaskEntity(int taskBundleId) {
    Cursor query = db.query("SELECT * FROM "
        + TaskEntity.TASK_TABLE_NAME
        + " WHERE "
        + TaskEntity.COLUMN_TASK_BUNDLE_ID
        + " =?", taskBundleId + "");
    if (query.getCount() <= 0) {
      query.close();
      return false;
    }
    query.close();
    return true;
  }

  /**
   * 更新 TaskEntity
   */
  public void updateTaskEntity(TaskEntity taskEntity) {
    db.update(TaskEntity.TASK_TABLE_NAME, getTaskEntity(taskEntity),
        TaskEntity.COLUMN_TASK_ID + "=?", taskEntity.getTaskId() + "");
  }

  public boolean isExistTaskBundle(String key) {
    String sql = "SELECT * " +
        "FROM " + TaskBundle.TASK_BUNDLE_TABLE_NAME + " " +
        "WHERE " +
        TaskBundle.COLUMN_KEY + " = ? ";
    Cursor cursor = null;
    try {
      cursor = db.query(sql, key);
      return cursor.getCount() > 0;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  private ContentValues getTaskBundleValues(TaskBundle taskBundle) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(TaskBundle.COLUMN_BUNDLE_ID, taskBundle.getBundleId());
    contentValues.put(TaskBundle.COLUMN_KEY, taskBundle.getKey());
    contentValues.put(TaskBundle.COLUMN_FILEPATH, taskBundle.getFilePath());
    contentValues.put(TaskBundle.COLUMN_TOTAL_SIZE, taskBundle.getTotalSize());
    contentValues.put(TaskBundle.COLUMN_COMPLETED_SIZE, taskBundle.getCompleteSize());
    contentValues.put(TaskBundle.COLUMN_STATUS, taskBundle.getStatus());
    contentValues.put(TaskBundle.COLUMN_IS_INIT, taskBundle.isInit());
    contentValues.put(TaskBundle.COLUMN_M3U8, taskBundle.getM3u8());
    contentValues.put(TaskBundle.COLUMN_HTML, taskBundle.getHtml());
    contentValues.put(TaskBundle.COLUMN_ARG0, taskBundle.getArg0());
    contentValues.put(TaskBundle.COLUMN_ARG1, taskBundle.getArg1());
    contentValues.put(TaskBundle.COLUMN_ARG2, taskBundle.getArg2());
    return contentValues;
  }

  private ContentValues getTaskEntity(TaskEntity taskEntity) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(TaskEntity.COLUMN_TASK_ID, taskEntity.getTaskId());
    contentValues.put(TaskEntity.COLUMN_TASK_BUNDLE_ID, taskEntity.getTaskBundleId());
    contentValues.put(TaskEntity.COLUMN_IS_FINISH, taskEntity.isFinish());
    contentValues.put(TaskEntity.COLUMN_URL, taskEntity.getUrl());
    contentValues.put(TaskEntity.COLUMN_FILENAME, taskEntity.getFileName());
    contentValues.put(TaskEntity.COLUMN_FILEPATH, taskEntity.getFilePath());
    contentValues.put(TaskEntity.COLUMN_TOTAL_SIZE, taskEntity.getTotalSize());
    contentValues.put(TaskEntity.COLUMN_COMPLETED_SIZE, taskEntity.getCompletedSize());
    return contentValues;
  }

  public Observable<List<TaskBundle>> selectAllTaskBundle() {
    QueryObservable query = db.createQuery(TaskBundle.TASK_BUNDLE_TABLE_NAME,
        "SELECT * FROM " + TaskBundle.TASK_BUNDLE_TABLE_NAME);
    return query.flatMap(new Func1<SqlBrite.Query, Observable<List<TaskBundle>>>() {
      @Override public Observable<List<TaskBundle>> call(SqlBrite.Query query) {
        List<TaskBundle> list = new ArrayList<TaskBundle>();

        Cursor cursor = query.run();
        while (cursor.moveToNext()) {
          list.add(getTaskBundle(cursor));
        }
        cursor.close();
        return Observable.just(list);
      }
    });
  }

  public void close() {
    db.close();
  }

  public List<TaskEntity> getTaskEntityListByBundleId(int bundleId) {

    List<TaskEntity> taskEntityList = new ArrayList<>();

    Cursor cursor = db.query("SELECT * FROM "
        + TaskEntity.TASK_TABLE_NAME
        + " WHERE "
        + TaskEntity.COLUMN_TASK_BUNDLE_ID
        + "=?", bundleId + "");

    try {
      if (cursor.moveToNext()) {
        taskEntityList.add(getTaskEntity(cursor));
      }
      return taskEntityList;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  public static final String COLUMN_KEY = "key";
  public static final String COLUMN_FILEPATH = "filePath";
  public static final String COLUMN_TOTAL_SIZE = "totalSize";
  public static final String COLUMN_COMPLETED_SIZE = "completeSize";
  public static final String COLUMN_STATUS = "status";
  public static final String COLUMN_IS_INIT = "status";
  public static final String COLUMN_M3U8 = "m3u8";
  public static final String COLUMN_HTML = "html";
  public static final String COLUMN_ARG0 = "arg0";
  public static final String COLUMN_ARG1 = "arg1";
  public static final String COLUMN_ARG2 = "arg2";

  private TaskBundle getTaskBundle(Cursor cursor) {
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

  private TaskEntity getTaskEntity(Cursor cursor) {
    int groupId = DBHelper.getInt(cursor, TaskEntity.COLUMN_TASK_BUNDLE_ID);
    int taskId = DBHelper.getInt(cursor, TaskEntity.COLUMN_TASK_ID);
    boolean isFinish = DBHelper.getBoolean(cursor, TaskEntity.COLUMN_IS_FINISH);
    String url = DBHelper.getString(cursor, TaskEntity.COLUMN_URL);
    String fileName = DBHelper.getString(cursor, TaskEntity.COLUMN_FILENAME);
    String filePath = DBHelper.getString(cursor, TaskEntity.COLUMN_FILEPATH);
    long totalSize = DBHelper.getLong(cursor, TaskEntity.COLUMN_TOTAL_SIZE);
    long completedSize = DBHelper.getLong(cursor, TaskEntity.COLUMN_COMPLETED_SIZE);
    return TaskEntity.newBuilder()
        .taskBundleId(groupId)
        .taskId(taskId)
        .isFinish(isFinish)
        .url(url)
        .fileName(fileName)
        .filePath(filePath)
        .totalSize(totalSize)
        .completedSize(completedSize)
        .build();
  }

  private int getNextID(BriteDatabase db, String tableName, String idName) {
    StringBuilder sqlB = new StringBuilder();
    sqlB.append("SELECT max(").append(idName).append(") AS maxId ");
    sqlB.append("FROM ").append(tableName);
    L.i("max id " + sqlB.toString());
    int maxId = 1;
    Cursor cursor = db.query(sqlB.toString());
    if (cursor.moveToFirst()) {
      maxId = DBHelper.getInt(cursor, "maxId");

      if (maxId >= 0) {
        return maxId + 1;
      }
    }
    cursor.close();
    return maxId;
  }
}
