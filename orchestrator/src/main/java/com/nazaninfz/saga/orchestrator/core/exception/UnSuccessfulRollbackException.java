package com.nazaninfz.saga.orchestrator.core.exception;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.rabbitproducer.constant.Constants;


public class UnSuccessfulRollbackException extends MicroserviceException {

    public UnSuccessfulRollbackException(Throwable e) {
        super(Constants.PRODUCER_COMPONENT, Constants.PRODUCER_COMPONENT_NUMBER, "unsuccessful.rollback.exception", 6, 500, e);
    }
}
