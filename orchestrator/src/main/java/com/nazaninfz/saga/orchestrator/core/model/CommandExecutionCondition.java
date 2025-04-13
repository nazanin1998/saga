package com.nazaninfz.saga.orchestrator.core.model;

import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandInput;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors(chain = true)
@NoArgsConstructor
public abstract class CommandExecutionCondition extends CommandHelperIdentifier {

    public abstract boolean isSatisfied(
            SagaCommandInput input,
            Map<String, SagaCommandOutput> outputMap,
            Map<String, Object> contextMap);
}
