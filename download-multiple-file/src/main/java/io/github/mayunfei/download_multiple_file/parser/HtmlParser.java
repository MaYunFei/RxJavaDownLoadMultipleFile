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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by mayunfei on 17-3-16.
 */

public class HtmlParser implements TaskParser {

  private static final String HTMLPATH = "html" + File.separator;
  DownloadApi downloadApi;
  String url;

  public HtmlParser(DownloadApi downloadApi, String url) {
    this.downloadApi = downloadApi;
    this.url = url;
  }

  @Override public boolean parseTask(TaskBundle taskBundle) {

    Call<ResponseBody> requestHtml = downloadApi.requestM3u8(url);
    Response<ResponseBody> response = null;
    List<String> list = new ArrayList<>();
    try {

      response = requestHtml.execute();
      if (taskBundle.getStatus() == TaskStatus.STATUS_CANCEL
          || taskBundle.getStatus() == TaskStatus.STATUS_PAUSE) {
        return false;
      }
      if (response.isSuccessful()) {
        String html = response.body().string();
        if (!saveFile(taskBundle.getFilePath(), html)) {
          return false;
        }

        Document doc = Jsoup.parse(html);
        Elements csss = doc.select("link");
        Elements jss = doc.select("script");
        Elements imgs = doc.select("img");
        int index = url.lastIndexOf("/");
        String rootUrl = url.substring(0, index + 1);
        for (Element element : csss) {
          String result = element.attr("href");
          String path = toAbsolutePath(rootUrl, result);

          if (!list.contains(path)) list.add(toAbsolutePath(rootUrl, result));
          // 替换文件为相对路径
          element.attr("href", result.substring(result.lastIndexOf("/") + 1));
        }
        for (Element element : jss) {
          String result = element.attr("src");
          String path = toAbsolutePath(rootUrl, result);
          if (!list.contains(path)) list.add(path);
          // 替换文件为相对路径
          element.attr("src", result.substring(result.lastIndexOf("/") + 1));
        }
        for (Element element : imgs) {
          String result = element.attr("src");
          String path = toAbsolutePath(rootUrl, result);
          if (!list.contains(path)) list.add(path);
          // 替换文件为相对路径
          element.attr("src", result.substring(result.lastIndexOf("/") + 1));
        }
        if (taskBundle.getTaskList() == null) {
          taskBundle.setTaskList(new ArrayList<TaskEntity>());
        }
        for (String path : list) {
          TaskEntity taskEntity = TaskEntity.newBuilder()
              .url(path)
              .fileName(FileUtils.getFileNameFromUrl(path))
              .filePath(taskBundle.getFilePath() + HTMLPATH)
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
    File file = new File(path + HTMLPATH, fileName);
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

  private String toAbsolutePath(String rootUrl, String url) {
    String result = "";
    if (url.startsWith("http://") || url.startsWith("https://")) {
      result = url;
    } else {
      result = rootUrl + url;
    }
    return result;
  }
}
