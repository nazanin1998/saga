package com.nazaninfz.saga.orchestrator.core.exception;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.rabbitproducer.constant.Constants;

public class UnknownCommandTypeException extends MicroserviceException {

    public UnknownCommandTypeException() {
        super(Constants.PRODUCER_COMPONENT, Constants.PRODUCER_COMPONENT_NUMBER, "unknown.command.type.exception", 4, 500);
    }
}
