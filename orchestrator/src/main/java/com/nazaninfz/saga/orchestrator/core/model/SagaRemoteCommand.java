package com.nazaninfz.saga.orchestrator.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nazaninfz.messagingsharedmodel.models.MessagingData;
import com.nazaninfz.producer.Producer;
import com.nazaninfz.saga.orchestrator.engine.SagaRemoteCommandExecutor;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@AllArgsConstructor
@Accessors(chain = true)
public class SagaRemoteCommand extends SagaCommand {

    public static final String TITLE = "REMOTE_COMMAND";

    private MessagingData messaging;

    private Map<String, Object> remoteHeaders;

    private Long timeout;

    @JsonIgnore
    private final Producer producer;

    @JsonIgnore
    private final SagaRemoteCommandExecutor executor;

//    @JsonIgnore
//    private final SagaCommandRepo sagaCommandRepo;
//
    public SagaRemoteCommand(Producer producer, SagaRemoteCommandExecutor executor) {
        this.producer = producer;
        this.executor = executor;
    }


    @Override
    final public void commit() {
        executor.commit(this);
    }

    @Override
//    @JsonIgnore
    final public void rollback() {
        // This method does nothing.
    }


}
