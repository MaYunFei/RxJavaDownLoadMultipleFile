package io.github.mayunfei.downloadmultiplefile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import io.github.mayunfei.download_multiple_file.db.DownloadDao;
import io.github.mayunfei.download_multiple_file.download.DownloadManager;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.entity.TaskEntity;
import io.github.mayunfei.download_multiple_file.entity.TaskStatus;
import io.github.mayunfei.download_multiple_file.utils.FileUtils;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
          MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }
    //daoTest();

  }

  void testDownloadManager() {
    TaskBundle taskBundle = new TaskBundle();
    taskBundle.setStatus(TaskStatus.STATUS_INIT);
    taskBundle.setKey("123");
    taskBundle.setFilePath(FileUtils.getDefaultFilePath());
    List<TaskEntity> taskEntityList = new ArrayList<>();
    taskEntityList.add(
        getTestTaskEntity(FileUtils.getDefaultFilePath(), "d704d5c7c226a371f8b34926f14330f0-000.ts",
            "https://mv.dongaocloud.com/2b4f/2b51/d42/278/d704d5c7c226a371f8b34926f14330f0/d704d5c7c226a371f8b34926f14330f0-000.ts"));
    taskEntityList.add(
        getTestTaskEntity(FileUtils.getDefaultFilePath(), "d704d5c7c226a371f8b34926f14330f0-001.ts",
            "https://mv.dongaocloud.com/2b4f/2b51/d42/278/d704d5c7c226a371f8b34926f14330f0/d704d5c7c226a371f8b34926f14330f0-001.ts"));
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
        testDownloadManager();
      } else {
        //没有权限
      }
      return;
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
