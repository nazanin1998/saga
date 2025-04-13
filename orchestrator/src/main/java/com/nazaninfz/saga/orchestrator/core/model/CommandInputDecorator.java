package com.nazaninfz.saga.orchestrator.core.model;

import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandInput;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandOutput;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@NoArgsConstructor
@Accessors(chain = true)
public abstract class CommandInputDecorator extends CommandHelperIdentifier {

    public abstract void decorate(
            SagaCommandInput input,
            Map<String, SagaCommandOutput> outputMap,
            Map<String, Object> contextMap);
}