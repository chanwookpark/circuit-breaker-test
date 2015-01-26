import org.junit.Test;
import rx.Observable;
import rx.functions.Action1;

import static org.junit.Assert.assertEquals;

/**
 * Created by chanwook on 2015. 1. 26..
 */
public class HystrixTests {
    String name = "chanwook";
    String expectedName = "Hello " + name;

    @Test
    public void sync() throws Exception {
        assertEquals(expectedName, new CommandHelloWorld(name).execute());
    }

    @Test
    public void async() throws Exception {
        assertEquals(expectedName, new CommandHelloWorld(name).queue().get());
    }

    @Test
    public void reactive() throws Exception {
        Observable<String> observe = new CommandHelloWorld(name).observe();

        // blocking
        assertEquals(expectedName, observe.toBlocking().single());

        // non-blocking
        observe.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                assertEquals(expectedName, s);
            }
        });
    }
}
