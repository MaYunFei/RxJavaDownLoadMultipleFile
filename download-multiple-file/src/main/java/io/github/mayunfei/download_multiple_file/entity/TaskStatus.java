package io.github.mayunfei.download_multiple_file.entity;

/**
 * Created by yunfei on 17-3-14.
 */

public class TaskStatus {
  public static final int STATUS_START = 0;
  //准备阶段
  public static final int STATUS_INIT = 1;
  //线程等待状态
  public static final int STATUS_QUEUE =2;
  //下载中
  public static final int STATUS_CONNECTING =3;
  //暂停
  public static final int STATUS_PAUSE = 4;
  //取消
  public static final int STATUS_CANCEL = 5;
  //网络异常
  public static final int STATUS_ERROR_NET = 6;
  //存储异常
  public static final int STATUS_ERROR_STORAGE = 7;
  //下载完成
  public static final int STATUS_FINISHED = 8;
}
