package br.com.puc.statemachine;

@FunctionalInterface
public interface EventAction<TEvent, TEventContext extends EventContext, TStatus> {
    void execute(TEventContext eventContext, TEvent event, StateMachineContext<TStatus, TEvent> stateContext);
}
