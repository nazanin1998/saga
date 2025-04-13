package com.nazaninfz.saga.orchestrator.core.exception;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.rabbitproducer.constant.Constants;

public class InputDecorationException extends MicroserviceException {

    public InputDecorationException(Throwable e) {
        super(Constants.PRODUCER_COMPONENT, Constants.PRODUCER_COMPONENT_NUMBER, "input.decoration.exception", 2, 500, e);
    }
}
