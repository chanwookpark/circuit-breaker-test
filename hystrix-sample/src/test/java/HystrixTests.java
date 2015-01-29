import com.netflix.hystrix.exception.HystrixBadRequestException;
import command.CommandBadRequest;
import command.CommandErrorHelloWorld;
import command.CommandHelloWorld;
import command.CommandThreadPool;
import exception.CircuitBreakingException;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import static org.junit.Assert.*;

/**
 * https://github.com/Netflix/Hystrix/wiki/How-To-Use 학습
 * <p/>
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

        Observable<String> errorObserve = new CommandHelloWorld("ERROR").observe();

        // non-blocking with onError
        errorObserve.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                fail("에러가 발생해야 합니다!");
            }

            @Override
            public void onError(Throwable e) {
                // 성공!
                assertTrue(e instanceof CircuitBreakingException);
            }

            @Override
            public void onNext(String s) {
                fail("에러가 발생해야 합니다!");
            }
        });

        assertEquals("Fail 1", new CommandErrorHelloWorld("1").execute());
        assertEquals("Fail 2", new CommandErrorHelloWorld("2").execute());
    }

    @Test(expected = HystrixBadRequestException.class)
    public void badRequest() throws Exception {
        new CommandBadRequest().execute();
    }

    @Test
    public void threadPool() throws Exception {
        // Command A: [commandA] main, hystrix-tp-commandA-1, 12
        System.out.println("Command A: " + new CommandThreadPool("commandA").execute());
        // Command B: [commandB] main, hystrix-tp-commandB-1, 13
        System.out.println("Command B: " + new CommandThreadPool("commandB").execute());
    }
}
