package fer.com.androidplayground;

import android.os.Bundle;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        Bundle b = new Bundle();
        b.putString("key", "value");
        assertEquals(4, 2 + 2);
        assertSame("value", b.getString("key"));
    }
}