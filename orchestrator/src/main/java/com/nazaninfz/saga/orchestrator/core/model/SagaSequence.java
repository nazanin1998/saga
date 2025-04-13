package com.nazaninfz.saga.orchestrator.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SagaSequence {
    private String sequenceTitle;
    private Map<String, Object> contextMap;
    private SagaCommand rootCommand;
}
