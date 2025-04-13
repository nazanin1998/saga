package com.nazaninfz.saga.orchestrator.core.model;

import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepStatus;
import com.nazaninfz.saga.orchestrator.core.enums.SagaCommandStepType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class SagaCommandStepHistory extends CommandHelperIdentifier {
    private SagaCommandStepType stepType;
    private SagaCommandStepStatus stepStatus;
    private String details;
}
