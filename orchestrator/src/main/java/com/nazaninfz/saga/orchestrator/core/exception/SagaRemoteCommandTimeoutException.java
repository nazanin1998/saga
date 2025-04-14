package com.nazaninfz.saga.orchestrator.core.exception;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.rabbitproducer.constant.Constants;

public class SagaRemoteCommandTimeoutException extends MicroserviceException {

    public SagaRemoteCommandTimeoutException() {
        super(Constants.PRODUCER_COMPONENT, Constants.PRODUCER_COMPONENT_NUMBER, "saga.remote.command.timout.exception", 1, 408);
    }
}
