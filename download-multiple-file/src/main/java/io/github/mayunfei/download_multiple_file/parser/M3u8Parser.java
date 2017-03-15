package io.github.mayunfei.download_multiple_file.parser;

import io.github.mayunfei.download_multiple_file.download.DownloadApi;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.entity.TaskEntity;
import io.github.mayunfei.download_multiple_file.entity.TaskStatus;
import io.github.mayunfei.download_multiple_file.utils.FileUtils;
import io.github.mayunfei.download_multiple_file.utils.L;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yunfei on 17-3-15.
 */

public class M3u8Parser implements TaskParser {
  private static final String M3U8PATH = File.separator + "m3u8" + File.separator;
  DownloadApi downloadApi;
  String url;
  static final String EXTINF_TAG_PREFIX = "#EXTINF";
  static final String NEW_LINE_CHAR = "\\r?\\n";

  public M3u8Parser(DownloadApi downloadApi, String url) {
    this.downloadApi = downloadApi;
    this.url = url;
  }

  @Override public boolean parseTask(TaskBundle taskBundle) {

    Call<ResponseBody> requestM3u8 = downloadApi.requestM3u8(url);
    Response<ResponseBody> response = null;
    try {

      response = requestM3u8.execute();
      if (taskBundle.getStatus() == TaskStatus.STATUS_CANCEL
          || taskBundle.getStatus() == TaskStatus.STATUS_PAUSE) {
        return false;
      }
      if (response.isSuccessful()) {
        String m3u8Str = response.body().string();
        saveFile(taskBundle.getFilePath(), m3u8Str);
        Scanner scanner = new Scanner(m3u8Str).useDelimiter(EXTINF_TAG_PREFIX);
        if (scanner.hasNext()) {
          String info = scanner.next();
          L.i("m3u8 info");
        }
        if (taskBundle.getTaskList() == null) {
          taskBundle.setTaskList(new ArrayList<TaskEntity>());
        }
        while (scanner.hasNext()) {
          String next = scanner.next();
          String[] item = next.split(NEW_LINE_CHAR);
          String videoUrl = item[1];
          if (!videoUrl.toLowerCase().contains("http://") || !videoUrl.toLowerCase()
              .contains("https://")) {
            videoUrl = url.substring(0, url.lastIndexOf("/") + 1) + videoUrl;
          }
          TaskEntity taskEntity = TaskEntity.newBuilder()
              .url(videoUrl)
              .fileName(FileUtils.getFileNameFromUrl(videoUrl))
              .filePath(taskBundle.getFilePath() + M3U8PATH)
              .build();
          taskBundle.getTaskList().add(taskEntity);
        }

        return true;
      } else {
        //请求失败
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean saveFile(String path, String m3u8Str) {
    PrintWriter printWriter = null;
    String fileName = FileUtils.getFileNameFromUrl(url);
    File file = new File(path + M3U8PATH, fileName);
    try {
      if (!file.getParentFile().exists()) {
        boolean mkdirs = file.getParentFile().mkdirs();
        if (mkdirs) {
          L.i("M3u8 file create file");
        }
      }
      printWriter = new PrintWriter(file);
      printWriter.print(m3u8Str);
      printWriter.flush();
      printWriter.close();
      return true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      if (file.exists()) {
        file.delete();
      }
      return false;
    }
  }
}
