package com.nazaninfz.saga.orchestrator.core.exception;

import com.nazaninfz.commons.exception.MicroserviceException;
import com.nazaninfz.messagingsharedmodel.models.response.Result;

public class MicroServiceCommitException extends MicroserviceException {
    public MicroServiceCommitException(Result result) {
        super(result.getServiceName(),
                result.getServiceNumber(),
                result.getMessage(),
                result.getExceptionNumber(),
                result.getStatusCode(),
                result.getExceptionSubText());
    }
}
