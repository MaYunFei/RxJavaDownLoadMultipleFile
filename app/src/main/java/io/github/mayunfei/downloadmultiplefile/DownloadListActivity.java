package io.github.mayunfei.downloadmultiplefile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import io.github.mayunfei.download_multiple_file.download.DownloadManager;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.utils.L;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class DownloadListActivity extends AppCompatActivity {

  private List<TaskBundle> list;
  private CommonRcvAdapter<TaskBundle> mAdapter;
  private RecyclerView recyclerView;

  public static void startDownloadActivity(Context context) {
    context.startActivity(new Intent(context, DownloadListActivity.class));
  }

  private Subscription mSubscribe;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_download_list);
    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

    LinearLayoutManager layoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    layoutManager.setRecycleChildrenOnDetach(true);
    recyclerView.setLayoutManager(layoutManager);
    list = new ArrayList<>();
    mSubscribe = DownloadManager.getInstance()
        .getObservableAllTaskBundle()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<TaskBundle>>() {
          @Override public void call(List<TaskBundle> taskBundles) {
            L.i("%%%%%%%%%%%%%%%%%%%%%% \n" + taskBundles);
            if (taskBundles.size() > 0) {
              TaskBundle taskBundle = taskBundles.get(0);
              list.clear();
              list.addAll(taskBundles);
              recyclerView.getAdapter().notifyDataSetChanged();
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            L.e(throwable.getMessage());
          }
        });

    mAdapter = new CommonRcvAdapter<TaskBundle>(list) {
      public AdapterItem createItem(Object type) {
        return new DownLoadItem();
      }
    };
    recyclerView.setAdapter(mAdapter);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (mSubscribe != null) {
      mSubscribe.unsubscribe();
    }
  }
}
