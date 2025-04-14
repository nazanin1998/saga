package com.nazaninfz.saga.orchestrator.utils;

import com.nazaninfz.saga.orchestrator.core.entity.SagaRemoteCommandEntity;
import com.nazaninfz.saga.orchestrator.core.enums.RemoteStatus;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class RemoteCommandActionUtils {

    public void readyToSend(SagaRemoteCommandEntity command) {
        command.setRemoteStatus(RemoteStatus.READY_TO_SEND);
        command.setCommitStartDate(new Date());
    }

    public void receiveResponseStatus(SagaRemoteCommandEntity command) {
        command.setRemoteStatus(RemoteStatus.RECEIVE_RESPONSE);
        command.setCommitEndDate(new Date());
    }

    public void parsedResponse(SagaRemoteCommandEntity command) {
        command.setRemoteStatus(RemoteStatus.PARSED_RESPONSE);
    }

    public void timeout(SagaRemoteCommandEntity command) {
        command.setRemoteStatus(RemoteStatus.TIMEOUT);
    }

    public void parseResponseFailed(SagaRemoteCommandEntity command) {
        command.setRemoteStatus(RemoteStatus.PARSE_RESPONSE_FAILED);
    }

    public void microserviceThrowsException(SagaRemoteCommandEntity command) {
        command.setRemoteStatus(RemoteStatus.MICRO_SERVICE_THROWS_EXCEPTION);
    }

    public void emptyMessagingData(SagaRemoteCommandEntity command) {
        command.setRemoteStatus(RemoteStatus.EMPTY_MESSAGING_DATA);
    }

    public void messagingLayerException(SagaRemoteCommandEntity command) {
        command.setRemoteStatus(RemoteStatus.MESSAGING_LAYER_EXCEPTION);
    }
}
