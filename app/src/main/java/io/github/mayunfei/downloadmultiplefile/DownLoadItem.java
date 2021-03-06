package io.github.mayunfei.downloadmultiplefile;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import io.github.mayunfei.download_multiple_file.download.DownloadManager;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.entity.TaskStatus;
import java.util.HashMap;
import java.util.List;
import kale.adapter.item.AdapterItem;

/**
 * Created by mayunfei on 17-3-16.
 */

public class DownLoadItem implements AdapterItem<TaskBundle> {

  public interface onCheckListener {
    void onCheckClick(TaskBundle taskBundle);
  }

  Button btn_status;
  TextView tv_key;
  ContentLoadingProgressBar progress;
  AppCompatCheckBox checkBox;
  HashMap<String,TaskBundle> checklist = new HashMap<>();
  View rootView;

  public DownLoadItem(HashMap<String, TaskBundle> checklist,
      DownLoadItem.onCheckListener onCheckListener) {
    this.checklist = checklist;
    this.onCheckListener = onCheckListener;
  }

  private onCheckListener onCheckListener;

  @Override public int getLayoutResId() {
    return R.layout.downloaditem;
  }

  @Override public void bindViews(View view) {
    btn_status = (Button) view.findViewById(R.id.btn_status);
    tv_key = (TextView) view.findViewById(R.id.tv_key);
    progress = (ContentLoadingProgressBar) view.findViewById(R.id.progress);
    checkBox = (AppCompatCheckBox) view.findViewById(R.id.checkbox);
    rootView = view;
  }

  @Override public void setViews() {

  }

  @Override public void handleData(final TaskBundle taskBundle, int i) {
    if (checklist.containsKey(taskBundle.getKey())){
      checkBox.setChecked(true);
    }else {
      checkBox.setChecked(false);
    }
    rootView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onCheckListener!=null){
          onCheckListener.onCheckClick(taskBundle);
        }
      }
    });

    tv_key.setText(taskBundle.getKey());
    progress.setMax(taskBundle.getTotalSize());
    progress.setProgress(taskBundle.getCompleteSize());
    switch (taskBundle.getStatus()) {
      case TaskStatus.STATUS_START:
      case TaskStatus.STATUS_INIT:
      case TaskStatus.STATUS_CONNECTING:
        btn_status.setText("正在下载...");
        btn_status.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            DownloadManager.getInstance().pause(taskBundle.getKey());
          }
        });
        break;
      case TaskStatus.STATUS_CANCEL:
        btn_status.setText("已经取消");
        break;
      case TaskStatus.STATUS_ERROR_NET:
        btn_status.setText("网络错误");
        btn_status.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            DownloadManager.getInstance().addTaskBundle(taskBundle);
          }
        });
        break;
      case TaskStatus.STATUS_ERROR_STORAGE:
        btn_status.setText("文件错误");
        btn_status.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            DownloadManager.getInstance().addTaskBundle(taskBundle);
          }
        });
        break;
      case TaskStatus.STATUS_FINISHED:
        btn_status.setText("完成");

        break;
      case TaskStatus.STATUS_PAUSE:
        btn_status.setText("暂停");
        btn_status.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            DownloadManager.getInstance().addTaskBundle(taskBundle);
          }
        });
        break;
      case TaskStatus.STATUS_QUEUE:
        btn_status.setText("等待中...");
        btn_status.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
          }
        });
        break;
    }
  }
}
