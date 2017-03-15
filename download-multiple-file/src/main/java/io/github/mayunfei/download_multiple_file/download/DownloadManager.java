package io.github.mayunfei.download_multiple_file.download;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import io.github.mayunfei.download_multiple_file.db.DownloadDao;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.entity.TaskStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by mayunfei on 17-3-15.
 */

public class DownloadManager {

  private static final int DEF_THREAD_COUNT = 1;
  //请求
  private Retrofit mRetrofit;
  private DownloadApi mDownloadApi;
  // 线程池
  private ThreadPoolExecutor mExecutor;
  private static DownloadManager mInstance;
  private LinkedBlockingQueue<Runnable> mThreadQueue;
  private DownloadDao mDao;
  // task list
  private Map<String, DownloadTask> mCurrentTaskList;
  private int mThreadCount;

  private DownloadManager() {

  }

  public void init(Context context) {
    init(context, DEF_THREAD_COUNT);
  }

  public void init(Context context, int count) {
    init(context, count, null);
  }

  public void init(Context context, int count, Retrofit retrofit) {
    mDao = new DownloadDao(context);
    mThreadCount = count;
    mExecutor = new ThreadPoolExecutor(mThreadCount, mThreadCount, 20, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>());
    mThreadQueue = (LinkedBlockingQueue<Runnable>) mExecutor.getQueue();
    if (retrofit == null) {
      retrofit =
          new Retrofit.Builder().baseUrl("http://blog.csdn.net/zggzcgy/article/details/23987637/")
              .client(getOkHttpClient())
              .build();
    }
    mRetrofit = retrofit;
    //下载api
    mDownloadApi = mRetrofit.create(DownloadApi.class);

    mCurrentTaskList = new HashMap<>();
  }

  private OkHttpClient getOkHttpClient() {
    return new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
  }

  public static synchronized DownloadManager getInstance() {
    if (mInstance == null) {
      mInstance = new DownloadManager();
    }
    return mInstance;
  }

  /**
   * 添加任务
   */
  public void addTaskBundle(@NonNull TaskBundle taskBundle) {
    DownloadTask currentTask = mCurrentTaskList.get(taskBundle.getKey());
    if (currentTask != null) {
      addDownLoadTask(currentTask);
    } else {
      DownloadTask downloadTask = new DownloadTask();
      downloadTask.setTaskBundle(taskBundle);
      addDownLoadTask(downloadTask);
    }
  }

  public void bindListener(@NonNull TaskBundle taskBundle,
      @Nullable DownloadTaskListener downloadTaskListener) {
    bindListener(taskBundle.getKey(), downloadTaskListener);
  }

  public void bindListener(@NonNull String key,
      @Nullable DownloadTaskListener downloadTaskListener) {
    DownloadTask downloadTask = mCurrentTaskList.get(key);
    if (downloadTask == null) {
      Log.e("DownLoadManager", "必须先启动下载才能监听");
    } else {
      downloadTask.setDownLoadListener(downloadTaskListener);
    }
  }

  private void addDownLoadTask(@NonNull DownloadTask downloadTask) {
    //是否已经添加
    DownloadTask tempDownloadTask = mCurrentTaskList.get(downloadTask.getTaskBundle().getKey());
    TaskBundle taskBundle = downloadTask.getTaskBundle();
    if (taskBundle == null || taskBundle.getStatus() == TaskStatus.STATUS_FINISHED) return;
    //配置任务
    downloadTask.setDownloadApi(mDownloadApi);
    downloadTask.setDao(mDao);
    downloadTask.setRetrofit(mRetrofit);
    mCurrentTaskList.put(taskBundle.getKey(), downloadTask);
    if (!mThreadQueue.contains(downloadTask)) {
      mExecutor.execute(downloadTask);
    }
    if (mExecutor.getTaskCount() > mThreadCount) {
      downloadTask.queue();
    }
  }

  public void pause(String key) {
    DownloadTask downloadTask = mCurrentTaskList.get(key);
    if (downloadTask != null) {
      if (mThreadQueue.contains(downloadTask)) {
        mThreadQueue.remove(downloadTask);
      }
      downloadTask.pause();
    } else {
      Log.e("DownLoadManager", "必须先启动下载才能暂停");
    }
  }
}
