package io.github.mayunfei.download_multiple_file.download;

import io.github.mayunfei.download_multiple_file.entity.TaskBundle;

/**
 * Created by mayunfei on 17-3-15.
 */

public interface DownloadTaskListener {
  void onQueue(TaskBundle bundle);

  /**
   * connecting
   */
  void onConnecting(TaskBundle bundle);

  /**
   * downloading
   */
  void onStart(TaskBundle bundle);

  /**
   * pauseTask
   */
  void onPause(TaskBundle bundle);

  /**
   * cancel
   */
  void onCancel(TaskBundle bundle);

  /**
   * success
   */
  void onFinish(TaskBundle bundle);

  /**
   * failure
   */
  void onError(TaskBundle bundle, int code);
}
