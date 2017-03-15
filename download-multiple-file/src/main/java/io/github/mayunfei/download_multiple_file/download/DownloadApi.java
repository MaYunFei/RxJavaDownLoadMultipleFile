package io.github.mayunfei.download_multiple_file.download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by mayunfei on 17-3-15.
 */

public interface DownloadApi {

  /**
   * 下载文件
   * 如果需要 大文件下载可以使用 @Streaming
   */
  @GET Call<ResponseBody> download(@Header("Range") String range, @Url String url);

  /**
   * rxjava 下载
   */
  @GET Observable<Response<ResponseBody>> downloadRxjva(@Header("Range") String range,
      @Url String url);

  /**
   * rxjava
   * 非断点下载
   */
  @GET Observable<Response<ResponseBody>> downloadFile(@Url String fileUrl);
}
