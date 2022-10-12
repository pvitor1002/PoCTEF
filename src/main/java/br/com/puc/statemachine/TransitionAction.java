package br.com.puc.statemachine;

import com.fasterxml.jackson.core.JsonProcessingException;

@FunctionalInterface
public interface TransitionAction<TEvent, TEventContext extends EventContext, TStatus> {
    void execute(TEventContext context, TEvent event, StateMachineContext<TStatus, TEvent> stateContext) throws JsonProcessingException;
}
