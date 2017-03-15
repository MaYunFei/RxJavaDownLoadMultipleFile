package io.github.mayunfei.download_multiple_file.download;

import io.github.mayunfei.download_multiple_file.db.DownloadDao;
import io.github.mayunfei.download_multiple_file.entity.TaskEntity;
import java.io.File;
import java.io.IOException;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by mayunfei on 17-3-15.
 */

public class RxFileDownload {
  private DownloadApi downloadApi;
  private TaskEntity mTaskEntity;
  private DownloadDao mDao;

  public void download() {
    Subscription subscribe = downloadApi.downloadFile(mTaskEntity.getUrl())
        .flatMap(new Func1<Response<ResponseBody>, Observable<File>>() {
          @Override public Observable<File> call(Response<ResponseBody> responseBodyResponse) {
            return saveFile(responseBodyResponse);
          }
        })
        .subscribe(new Action1<File>() {
          @Override public void call(File file) {

          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {

          }
        });

  }

  public Observable<File> saveFile(final Response<ResponseBody> response) {
    return Observable.create(new Observable.OnSubscribe<File>() {
      @Override public void call(Subscriber<? super File> subscriber) {
        try {
          File file = new File(mTaskEntity.getFilePath(), mTaskEntity.getFileName());

          BufferedSink sink = Okio.buffer(Okio.sink(file));
          // you can access body of response
          sink.writeAll(response.body().source());
          sink.close();
          subscriber.onNext(file);
          subscriber.onCompleted();
        } catch (IOException e) {
          e.printStackTrace();
          subscriber.onError(e);
        }
      }
    });
  }
}
