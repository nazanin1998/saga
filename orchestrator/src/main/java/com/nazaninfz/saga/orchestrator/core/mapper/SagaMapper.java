package com.nazaninfz.saga.orchestrator.core.mapper;

import com.nazaninfz.messagingsharedmodel.models.request.RequestObject;
import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.entity.SagaRemoteCommandEntity;
import com.nazaninfz.saga.orchestrator.core.entity.SagaSequenceEntity;
import com.nazaninfz.saga.orchestrator.core.model.SagaCommand;
import com.nazaninfz.saga.orchestrator.core.model.SagaSequence;
import com.nazaninfz.saga.orchestrator.core.model.SagaRemoteCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SagaMapper {
    SagaSequenceEntity toEntity(SagaSequence sagaSequence);

    SagaCommandEntity toEntity(SagaCommand sagaCommand);

    SagaRemoteCommandEntity toEntity(SagaRemoteCommand sagaCommand);

    @Mapping(target = "sendDate", source = "commitStartDate")
    @Mapping(target = "headers", source = "remoteHeaders")
    @Mapping(target = "body", source = "input")
    RequestObject toRequestObject(SagaRemoteCommandEntity entity);
}
