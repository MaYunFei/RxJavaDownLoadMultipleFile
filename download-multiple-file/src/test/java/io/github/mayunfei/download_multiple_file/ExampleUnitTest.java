package io.github.mayunfei.download_multiple_file;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() throws Exception {
    //assertEquals(4, 2 + 2);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H", Locale.US);
    String format = simpleDateFormat.format(new Date(Long.parseLong("1489580580000")));
    System.out.print("-------------   " + format);
  }
}