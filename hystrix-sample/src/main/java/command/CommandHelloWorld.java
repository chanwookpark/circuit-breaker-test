package command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import exception.CircuitBreakingException;

/**
 * Created by chanwook on 2015. 1. 26..
 */
public class CommandHelloWorld extends HystrixCommand<String> {
    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        if ("ERROR".equals(name)) {
            throw new CircuitBreakingException();
        }
        return "Hello " + name;
    }
}
