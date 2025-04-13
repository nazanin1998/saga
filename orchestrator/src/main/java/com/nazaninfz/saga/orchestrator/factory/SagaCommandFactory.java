//package com.nazaninfz.saga.orchestrator.factory;
//
//import com.nazaninfz.saga.orchestrator.model.SagaCommand;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.InvocationTargetException;
//
//@Slf4j
//@Component
//public class SagaCommandFactory {
//
//    public SagaCommand createSimpleCommand(
//            String commandTitle,
//            int orderId,
//            Class<? extends SagaCommand> commandClazz
//    ) {
//        SagaCommand command = instantiateSagaCommand(commandClazz);
//        command.setCommandId(SagaBaseCommand.generateId());
//        command.setCommandTitle(commandTitle);
//        command.setOrderId(orderId);
//        return command;
//    }
//
//    public SagaCommandEntity commandEntityFrom(SagaCommand command) {
//        return new SagaCommandEntity()
//                .setCommandTitle(command.getCommandTitle())
//                .setInput(command.getInput())
//                .setOrderId(command.getOrderId())
//                .setCommandId(command.getCommandId())
//                .setSequenceId(command.getSequenceId())
//                .setCommandStatus(CommandStatus.INITIALIZED);
//                .setRollbackRequired(command.getRollbackRequired())
//                .setOnExceptionBehavior(command.getOnExceptionBehavior())
//    }
//
//    private SagaCommand instantiateSagaCommand(
//            Class<? extends SagaCommand> commandClazz
//    ) {
//        try {
//            return commandClazz.getDeclaredConstructor().newInstance();
//        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
//                 IllegalAccessException e) {
//            log.error("exception in instantiation of simple command");
//            throw new RuntimeException();
//        }
//    }
//}
