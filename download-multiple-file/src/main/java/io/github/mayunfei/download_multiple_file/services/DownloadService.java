package io.github.mayunfei.download_multiple_file.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import io.github.mayunfei.download_multiple_file.download.DownloadManager;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.utils.L;
import java.util.List;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by mayunfei on 17-3-17.
 */

public class DownloadService extends Service {

  private static final int NOTIFICATION_ID = 1000;
  private Subscription mSubscribe;

  @Override public void onCreate() {
    super.onCreate();

    mSubscribe = DownloadManager.getInstance()
        .getObservableDownloadingBundle()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<TaskBundle>>() {
          @Override public void call(List<TaskBundle> taskBundles) {
            if (taskBundles.size() > 0) {
              startForeground(NOTIFICATION_ID, getNotification());
            } else {
              stopForeground(true);
              stopSelf();
            }
            for (TaskBundle task : taskBundles) {
              L.e("---------------------" + task);
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            L.e("service 错误  " + throwable.toString());
          }
        });
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onDestroy() {
    mSubscribe.unsubscribe();
    super.onDestroy();
  }

  private Notification getNotification() {
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(
        android.support.v7.appcompat.R.drawable.notification_template_icon_bg)
        .setContentTitle("正在后台下载...");
    NotificationManager mNotificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    return mBuilder.build();
  }
}
