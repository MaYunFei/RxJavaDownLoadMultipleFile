package io.github.mayunfei.download_multiple_file.download;

import io.github.mayunfei.download_multiple_file.db.DownloadDao;
import io.github.mayunfei.download_multiple_file.entity.TaskEntity;
import io.github.mayunfei.download_multiple_file.utils.IOUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 文件下载
 * Created by mayunfei on 17-3-15.
 */

public class FileDownload {

  private DownloadApi downloadApi;
  private TaskEntity mTaskEntity;
  private DownloadDao mDao;
  private AtomicBoolean isCancle;


  public FileDownload(DownloadApi downloadApi, DownloadDao mDao, TaskEntity mTaskEntity,
      AtomicBoolean isCancle) {
    this.downloadApi = downloadApi;
    this.mTaskEntity = mTaskEntity;
    this.mDao = mDao;
    this.isCancle = isCancle;
  }

  public void download() {
    RandomAccessFile tempFile = null;
    InputStream inputStream = null;
    BufferedInputStream bis = null;
    try {

      tempFile =
          new RandomAccessFile(new File(mTaskEntity.getFilePath(), mTaskEntity.getFileName()),
              "rwd");
      Long completeSize = mTaskEntity.getCompletedSize();
      if (tempFile.length() == 0) {
        completeSize = 0L;
      }
      tempFile.seek(completeSize);
      String range = "bytes=" + completeSize + "-";

      Call<ResponseBody> download = downloadApi.download(range, mTaskEntity.getUrl());

      Response<ResponseBody> response = download.execute();

      if (response.isSuccessful()) {
        ResponseBody responseBody = response.body();
        if (mTaskEntity.getTotalSize() <= 0) { // 第一次下载 total 值是不知道的
          mTaskEntity.setTotalSize(responseBody.contentLength());
        }

        //配置完成 通知UI 开始下载


        double updateSize = mTaskEntity.getTotalSize() / 100;
        inputStream = responseBody.byteStream();
        bis = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int length;
        int buffOffset = 0;
        while ((length = bis.read(buffer)) > 0 && !isCancle.get()) {
          tempFile.write(buffer, 0, length);
          completeSize += length;
          buffOffset += length;
          mTaskEntity.setCompletedSize(completeSize);
          // 避免一直调用数据库
          if (buffOffset >= updateSize) {
            buffOffset = 0;
            //mDownloadDao.updateTaskEntity(mTaskEntity);
            //handler.sendEmptyMessage(TaskStatus.TASK_STATUS_DOWNLOADING);
          }
        }
        if (isCancle.get()) {

        }
        //一个完成
        if (mTaskEntity.getCompletedSize() == mTaskEntity.getTotalSize()) {
          mTaskEntity.setFinish(true);
          //通知UI 设置数据库
        }
      } else {
        // throw Exception
        mTaskEntity.setFinish(false);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      IOUtils.close(tempFile, inputStream, bis);
    }
  }
}
