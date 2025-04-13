package com.nazaninfz.saga.orchestrator.core.exception;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.rabbitproducer.constant.Constants;

public class CommitingException extends MicroserviceException {

    public CommitingException(Throwable e) {
        super(Constants.PRODUCER_COMPONENT, Constants.PRODUCER_COMPONENT_NUMBER, "commiting.command.exception", 5, 500, e);
    }
}
