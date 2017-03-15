package io.github.mayunfei.download_multiple_file.parser;

import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import io.github.mayunfei.download_multiple_file.entity.TaskEntity;
import java.util.List;

/**
 * 用于解析小任务
 * Created by mayunfei on 17-3-15.
 */

public interface TaskParser {
  boolean parseTask(TaskBundle taskBundle);
}
