package io.github.mayunfei.downloadmultiplefile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import io.github.mayunfei.download_multiple_file.db.DownloadDao;
import io.github.mayunfei.download_multiple_file.download.DownloadManager;
import io.github.mayunfei.download_multiple_file.download.DownloadTask;
import io.github.mayunfei.download_multiple_file.download.DownloadTaskListener;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.entity.TaskEntity;
import io.github.mayunfei.download_multiple_file.entity.TaskStatus;
import io.github.mayunfei.download_multiple_file.utils.FileUtils;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
  private Button mBtn1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mBtn1 = ((Button)findViewById(R.id.btn_1));

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
          MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }
    //daoTest();
    mBtn1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        testDownloadManager();
        DownloadManager.getInstance().bindListener("456", new DownloadTaskListener() {
          @Override public void onQueue(TaskBundle bundle) {

          }

          @Override public void onConnecting(final TaskBundle bundle) {
            mBtn1.setOnClickListener(new View.OnClickListener() {
              @Override public void onClick(View v) {
                DownloadManager.getInstance().pause(bundle.getKey());
              }
            });
          }

          @Override public void onStart(TaskBundle bundle) {

          }

          @Override public void onPause(TaskBundle bundle) {

          }

          @Override public void onCancel(TaskBundle bundle) {

          }

          @Override public void onFinish(TaskBundle bundle) {

          }

          @Override public void onError(TaskBundle bundle, int code) {

          }
        });
        //DownloadManager.getInstance().bindListener("456", new DownloadTaskListener() {
        //  @Override public void onQueue(DownloadTask downloadTask) {
        //
        //  }
        //
        //  @Override public void onConnecting(DownloadTask downloadTask) {
        //    DownloadManager.getInstance().pause("456");
        //  }
        //
        //  @Override public void onStart(DownloadTask downloadTask) {
        //
        //  }
        //
        //  @Override public void onPause(DownloadTask downloadTask) {
        //
        //  }
        //
        //  @Override public void onCancel(DownloadTask downloadTask) {
        //
        //  }
        //
        //  @Override public void onFinish(DownloadTask downloadTask) {
        //
        //  }
        //
        //  @Override public void onError(DownloadTask downloadTask, int code) {
        //
        //  }
        //});

      }
    });

  }

  void testDownloadManager() {
    TaskBundle taskBundle = new TaskBundle();
    taskBundle.setStatus(TaskStatus.STATUS_INIT);
    taskBundle.setKey("456");
    taskBundle.setFilePath(FileUtils.getDefaultFilePath());
    List<TaskEntity> taskEntityList = new ArrayList<>();
    taskEntityList.add(
        getTestTaskEntity(FileUtils.getDefaultFilePath(), "d704d5c7c226a371f8b34926f14330f0-000.ts",
            "https://mv.dongaocloud.com/2b4f/2b51/d42/278/d704d5c7c226a371f8b34926f14330f0/d704d5c7c226a371f8b34926f14330f0-000.ts"));
    taskEntityList.add(
        getTestTaskEntity(FileUtils.getDefaultFilePath(), "CloudMusic_2.8.1_official_4.apk",
            "http://s1.music.126.net/download/android/CloudMusic_2.8.1_official_4.apk"));
    taskBundle.setTaskList(taskEntityList);
    taskBundle.setTotalSize(taskBundle.getTaskList().size());
    DownloadManager.getInstance().addTaskBundle(taskBundle);
  }

  TaskEntity getTestTaskEntity(String path, String name, String url) {
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setUrl(url);
    taskEntity.setFilePath(path);
    taskEntity.setFileName(name);
    return taskEntity;
  }

  void daoTest() {
    DownloadDao mDao = new DownloadDao(this);
    TaskBundle taskBundle = new TaskBundle();
    taskBundle.setStatus(TaskStatus.STATUS_INIT);
    taskBundle.setKey("123");
    List<TaskEntity> taskEntityList = new ArrayList<>();
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setUrl("null");
    taskEntityList.add(taskEntity);
    taskBundle.setTaskList(taskEntityList);
    mDao.insertTaskBundle(taskBundle);
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {

    if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        //testDownloadManager();
      } else {
        //没有权限
      }
      return;
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
