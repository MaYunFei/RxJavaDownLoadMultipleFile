package io.github.mayunfei.downloadmultiplefile;

import android.app.Application;
import io.github.mayunfei.download_multiple_file.download.DownloadManager;

/**
 * Created by mayunfei on 17-3-15.
 */

public class App extends Application {
  @Override public void onCreate() {
    super.onCreate();
    DownloadManager.getInstance().init(this, 1);
  }
}
