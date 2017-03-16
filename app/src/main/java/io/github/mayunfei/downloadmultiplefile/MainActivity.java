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
import io.github.mayunfei.download_multiple_file.utils.L;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
  private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
  private Button mBtn1;
  private Button mBtn2;
  private Button mBtn3;
  private Button mBtn4;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mBtn1 = ((Button) findViewById(R.id.btn_1));
    mBtn2 = ((Button) findViewById(R.id.btn_2));
    mBtn3 = ((Button) findViewById(R.id.btn_3));
    mBtn4 = ((Button) findViewById(R.id.btn_4));

    mBtn4.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        DownloadListActivity.startDownloadActivity(MainActivity.this);
      }
    });
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
          MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }

    //daoTest();
    mBtn2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        DownloadManager.getInstance().pause("456");
      }
    });
    mBtn1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        testDownloadManager();
      }
    });
  }

  void testDownloadManager() {
    TaskBundle taskBundle = new TaskBundle();
    taskBundle.setStatus(TaskStatus.STATUS_INIT);
    taskBundle.setKey("456");
    taskBundle.setFilePath(FileUtils.getDefaultFilePath());
    taskBundle.setM3u8(
        "https://md.dongaocloud.com/2b4f/2b52/5b3/81e/61e08244fcd53892b90031ee873de2b2/video.m3u8");
    taskBundle.setHtml("http://www.dongao.com/");
    DownloadManager.getInstance().addTaskBundle(taskBundle);
  }
  void testDownloadManager2() {
    TaskBundle taskBundle = new TaskBundle();
    taskBundle.setStatus(TaskStatus.STATUS_INIT);
    taskBundle.setKey("456");
    taskBundle.setFilePath(FileUtils.getDefaultFilePath());
    taskBundle.setM3u8(
        "https://md.dongaocloud.com/2b4f/2b52/5b3/81e/61e08244fcd53892b90031ee873de2b2/video.m3u8");
    DownloadManager.getInstance().addTaskBundle(taskBundle);
  }
  void testDownloadManage3() {
    TaskBundle taskBundle = new TaskBundle();
    taskBundle.setStatus(TaskStatus.STATUS_INIT);
    taskBundle.setKey("456");
    taskBundle.setFilePath(FileUtils.getDefaultFilePath());
    taskBundle.setM3u8(
        "https://md.dongaocloud.com/2b4f/2b52/5b3/81e/61e08244fcd53892b90031ee873de2b2/video.m3u8");
    DownloadManager.getInstance().addTaskBundle(taskBundle);
  }

  TaskEntity getTestTaskEntity(String path, String name, String url) {
    return TaskEntity.newBuilder().url(url).filePath(path).fileName(name).build();
  }

  void daoTest() {
    DownloadDao mDao = new DownloadDao(this);
    TaskBundle taskBundle = new TaskBundle();
    taskBundle.setStatus(TaskStatus.STATUS_INIT);
    taskBundle.setKey("123");
    taskBundle.setArg0(
        "https://md.dongaocloud.com/2b4f/2b52/5b3/81e/61e08244fcd53892b90031ee873de2b2/video.m3u8");
    List<TaskEntity> taskEntityList = new ArrayList<>();
    taskEntityList.add(TaskEntity.newBuilder().url("123").build());
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
