package command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.exception.HystrixBadRequestException;

/**
 * Created by chanwook on 2015. 1. 30..
 */
public class CommandBadRequest extends HystrixCommand<String> {

    public CommandBadRequest() {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
    }

    @Override
    protected String run() throws Exception {
        throw new HystrixBadRequestException("예외가 나겠지만 통과", new RuntimeException());
    }
}
