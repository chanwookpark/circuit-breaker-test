package command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import exception.CircuitBreakingException;

/**
 * Created by chanwook on 2015. 1. 30..
 */
public class CommandErrorHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandErrorHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        throw new CircuitBreakingException();
    }

    @Override
    protected String getFallback() {
        return "Fail " + name;
    }
}
