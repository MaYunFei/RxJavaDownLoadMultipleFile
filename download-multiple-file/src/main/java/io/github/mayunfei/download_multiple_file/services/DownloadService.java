package io.github.mayunfei.download_multiple_file.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by mayunfei on 17-3-17.
 */

public class DownloadService extends Service {
  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }
}
