package com.nazaninfz.saga.orchestrator.core.model;

import com.nazaninfz.messagingsharedmodel.enums.RollbackType;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandBehavior;
import com.nazaninfz.saga.orchestrator.core.interfaces.SagaCommandInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class SagaCommand extends SagaBaseCommand implements SagaCommandBehavior {
    private String commandTitle;
    private int orderId;
    private SagaCommandInput input;
    private RollbackType rollbackIfCurrentCommandHasException;
    private RollbackType rollbackIfNextCommandsHaveException;
    private CommandInputDecorator decorator;
    private List<CommandExecutionCondition> conditions;
    private List<CommandPostExecution> postExecutions;
    private List<SagaBaseCommand> nextCommands;
}
