package com.nazaninfz.saga.orchestrator.core.mapper;

import com.nazaninfz.saga.orchestrator.core.entity.SagaCommandEntity;
import com.nazaninfz.saga.orchestrator.core.entity.SagaSequenceEntity;
import com.nazaninfz.saga.orchestrator.core.model.SagaCommand;
import com.nazaninfz.saga.orchestrator.core.model.SagaSequence;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SagaMapper {
    SagaSequenceEntity toEntity(SagaSequence sagaSequence);
    SagaCommandEntity toEntity(SagaCommand sagaCommand);
}
