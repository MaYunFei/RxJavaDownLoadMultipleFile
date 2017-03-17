package io.github.mayunfei.downloadmultiplefile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import io.github.mayunfei.download_multiple_file.download.DownloadManager;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.entity.TaskStatus;
import io.github.mayunfei.download_multiple_file.utils.L;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class DownloadListActivity extends AppCompatActivity
    implements DownLoadItem.onCheckListener {

  private List<TaskBundle> list;
  private CommonRcvAdapter<TaskBundle> mAdapter;
  private RecyclerView recyclerView;
  private Button btn_bottom;
  private boolean isEdit = false;
  private HashMap<String, TaskBundle> checkList;

  public static void startDownloadActivity(Context context) {
    context.startActivity(new Intent(context, DownloadListActivity.class));
  }

  private Subscription mSubscribe;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_download_list);
    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    btn_bottom = (Button) findViewById(R.id.btn_bottom);
    LinearLayoutManager layoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    layoutManager.setRecycleChildrenOnDetach(true);
    recyclerView.setLayoutManager(layoutManager);
    list = new ArrayList<>();
    checkList = new HashMap<>();
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
              boolean isPause = true;
              for (TaskBundle itemBundle : list) {
                int status = itemBundle.getStatus();
                if (status == TaskStatus.STATUS_INIT
                    || status == TaskStatus.STATUS_START
                    || status == TaskStatus.STATUS_CONNECTING) {
                  isPause = false;
                  btn_bottom.setText("全部暂停");
                  btn_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                      DownloadManager.getInstance().pauseAll();
                    }
                  });
                }
              }

              if (isPause) {
                btn_bottom.setText("全部开始");
                btn_bottom.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View v) {
                    DownloadManager.getInstance().startAll();
                  }
                });
              }
              recyclerView.getAdapter().notifyDataSetChanged();
            }else {
              list.clear();
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
        return new DownLoadItem(checkList, DownloadListActivity.this);
      }
    };
    recyclerView.setAdapter(mAdapter);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.donloadlist_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.action_download_list_edit:
        if (!isEdit) {
          //变为编辑状态
          clearCheck();
          isEdit = true;
          invalidateOptionsMenu();
        } else {
          //变为非编辑状态
          //如果选中
          if (checkList.size() > 0) {
            //删除
            for (String key : checkList.keySet()) {
              DownloadManager.getInstance().cancel(key);
            }
          } else {
            //不删除
          }
          clearCheck();
          isEdit = false;

          invalidateOptionsMenu();
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void clearCheck() {
    //清除选项
    checkList.clear();
    recyclerView.getAdapter().notifyDataSetChanged();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (mSubscribe != null) {
      mSubscribe.unsubscribe();
    }
  }

  @Override public boolean onPrepareOptionsMenu(Menu menu) {
    MenuItem item = menu.findItem(R.id.action_download_list_edit);
    if (isEdit) {
      if (checkList.size() == 0) {
        item.setTitle("完成");
      } else {
        item.setTitle("删除");
      }
    } else {
      item.setTitle("编辑");
    }
    return super.onPrepareOptionsMenu(menu);
  }

  @Override public void onCheckClick(TaskBundle taskBundle) {
    if (isEdit) {
      if (checkList.containsKey(taskBundle.getKey())) {
        checkList.remove(taskBundle.getKey());
      } else {
        checkList.put(taskBundle.getKey(), taskBundle);
      }
      invalidateOptionsMenu();
      recyclerView.getAdapter().notifyDataSetChanged();
    }
  }
}
