package com.nazaninfz.saga.orchestrator.core.exception;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.rabbitproducer.constant.Constants;

public class PostExecutionException extends MicroserviceException {

    public PostExecutionException(Throwable e) {
        super(Constants.PRODUCER_COMPONENT, Constants.PRODUCER_COMPONENT_NUMBER, "post.execution.exception", 3, 500, e);
    }
}
