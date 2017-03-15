package io.github.mayunfei.download_multiple_file.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.entity.TaskEntity;
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
        db.insert(TaskBundle.TASK_BUNDLE_TABLE_NAME, getTaskBundleValues(taskBundle));
        List<TaskEntity> taskList = taskBundle.getTaskList();
        int bundleLastId = getTaskBundleLastId();
        taskBundle.setBundleId(bundleLastId);
        if (taskList != null && taskList.size() > 0) {
          for (TaskEntity taskEntity : taskList) {
            taskEntity.setTaskBundleId(bundleLastId);
            db.insert(TaskEntity.TASK_TABLE_NAME, getTaskEntity(taskEntity));
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

  private int getTaskBundleLastId() {
    Cursor query = null;
    try {
      query = db.query("SELECT last_insert_rowid() from" + TaskBundle.TASK_BUNDLE_TABLE_NAME);
      if (query.moveToFirst()) return query.getInt(0);
      return 0;
    } finally {
      if (query != null) {
        query.close();
      }
    }
  }

  /**
   * 更新TaskBundle
   */
  public void UpdateTaskBundle(TaskBundle taskBundle) {

    if (taskBundle.getTaskList() == null || taskBundle.getTaskList().size() == 0) {
      db.update(TaskBundle.TASK_BUNDLE_TABLE_NAME, getTaskBundleValues(taskBundle),
          TaskBundle.COLUMN_KEY + "=?", taskBundle.getKey());
    } else {
      if (!isExistTaskEntity(taskBundle.getBundleId())) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        db.update(TaskBundle.TASK_BUNDLE_TABLE_NAME, getTaskBundleValues(taskBundle),
            TaskBundle.COLUMN_KEY + "=?", taskBundle.getKey());
        for (TaskEntity taskEntity : taskBundle.getTaskList()) {
          taskEntity.setTaskBundleId(taskBundle.getBundleId());
          db.insert(TaskEntity.TASK_TABLE_NAME, getTaskEntity(taskEntity));
        }
        transaction.markSuccessful();
        transaction.end();
      }
      db.update(TaskBundle.TASK_BUNDLE_TABLE_NAME, getTaskBundleValues(taskBundle),
          TaskBundle.COLUMN_KEY + "=?", taskBundle.getKey());
    }
  }

  private boolean isExistTaskEntity(int taskBundleId) {
    Cursor query = db.query("SELECT * FROM "
        + TaskEntity.TASK_TABLE_NAME
        + " WHERE "
        + TaskEntity.COLUMN_TASK_BUNDLE_ID
        + " =?", taskBundleId + "");
    if (query.getCount() < 0) {
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
          String username = cursor.getString(cursor.getColumnIndex("USERNAME"));
          String id = cursor.getString(cursor.getColumnIndex("_id"));
          String nickname = cursor.getString(cursor.getColumnIndex("NICKNAME"));
        }
        cursor.close();
        return Observable.just(list);
      }
    });
  }




  public void close() {
    db.close();
  }
}
