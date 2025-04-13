package com.nazaninfz.saga.orchestrator.core.exception;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.rabbitproducer.constant.Constants;

public class ConditionCheckException extends MicroserviceException {

    public ConditionCheckException(Throwable e) {
        super(Constants.PRODUCER_COMPONENT, Constants.PRODUCER_COMPONENT_NUMBER, "condition.check.exception", 1, 500, e);
    }
}
