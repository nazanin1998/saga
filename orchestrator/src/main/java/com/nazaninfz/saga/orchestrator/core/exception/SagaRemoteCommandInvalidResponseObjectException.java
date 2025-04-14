package com.nazaninfz.saga.orchestrator.core.exception;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.rabbitproducer.constant.Constants;

public class SagaRemoteCommandInvalidResponseObjectException extends MicroserviceException {

    public SagaRemoteCommandInvalidResponseObjectException() {
        super(Constants.PRODUCER_COMPONENT, Constants.PRODUCER_COMPONENT_NUMBER, "invalid.response.object.exception", 7, 500);
    }
}
