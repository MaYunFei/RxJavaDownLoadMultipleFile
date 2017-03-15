package io.github.mayunfei.download_multiple_file.download;

/**
 * Created by mayunfei on 17-3-15.
 */

public interface DownloadTaskListener {
  void onQueue(DownloadTask downloadTask);

  /**
   * connecting
   */
  void onConnecting(DownloadTask downloadTask);

  /**
   * downloading
   */
  void onStart(DownloadTask downloadTask);

  /**
   * pauseTask
   */
  void onPause(DownloadTask downloadTask);

  /**
   * cancel
   */
  void onCancel(DownloadTask downloadTask);

  /**
   * success
   */
  void onFinish(DownloadTask downloadTask);

  /**
   * failure
   */
  void onError(DownloadTask downloadTask, int code);
}
