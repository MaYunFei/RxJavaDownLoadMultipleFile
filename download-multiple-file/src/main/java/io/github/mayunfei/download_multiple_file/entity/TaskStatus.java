package io.github.mayunfei.download_multiple_file.entity;

/**
 * Created by yunfei on 17-3-14.
 */

public class TaskStatus {
  //准备阶段
  public static final int STATUS_INIT = 0;
  //线程等待状态
  public static final int STATUS_QUEUE = 1;
  //下载中
  public static final int STATUS_CONNECTING = 2;
  //暂停
  public static final int STATUS_PAUSE = 3;
  //取消
  public static final int STATUS_CANCLE = 4;
  //网络异常
  public static final int STATUS_ERROR_NET = 5;
  //存储异常
  public static final int STATUS_ERROR_STORAGE = 6;
  //下载完成
  public static final int STATUS_FINIDHED = 7;
}
