package command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * Created by chanwook on 2015. 1. 30..
 */
public class CommandThreadPool extends HystrixCommand<String> {

    private final String name;

    public CommandThreadPool(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey(name))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("tp-" + name))
        );
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        Thread thread = Thread.currentThread();
        return "[" + name + "] " + thread.getThreadGroup().getName() + ", " + thread.getName() + ", " + thread.getId();
    }
}
