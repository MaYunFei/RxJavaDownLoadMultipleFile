package io.github.mayunfei.download_multiple_file.db;

import android.content.Context;
import android.database.Observable;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import io.github.mayunfei.download_multiple_file.entity.TaskBundle;
import rx.schedulers.Schedulers;

/**
 * Created by yunfei on 17-3-14.
 */

public class DownloadDao {
  private final BriteDatabase db;
  SqlBrite sqlBrite;
  SQLiteHelper sqLiteHelper;

  public DownloadDao(Context context) {
    sqLiteHelper = new SQLiteHelper(context.getApplicationContext());
    sqlBrite = new SqlBrite.Builder().build();
    db = sqlBrite.wrapDatabaseHelper(sqLiteHelper, Schedulers.io());
  }


}
