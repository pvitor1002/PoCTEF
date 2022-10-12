package br.com.puc.statemachine;

public class StateMachineTransitionBuilder<TStatus extends Enum, TEvent extends Enum> extends StateMachineBuilder<TStatus, TEvent> {
    private Transition<TStatus, TEvent> currentTransition;
    private StateMachineBuilder<TStatus, TEvent> parentBuilder;

    protected StateMachineTransitionBuilder(Transition<TStatus, TEvent> currentTransition, StateMachineBuilder<TStatus, TEvent> parentBuilder){
        this.currentTransition = currentTransition;
        this.parentBuilder = parentBuilder;
    }

    public StateMachineTransitionBuilder<TStatus, TEvent> withEvent(TEvent event){
        return this.withEvent(event, null);
    }

    public <T extends EventContext<TEvent>> StateMachineTransitionBuilder<TStatus, TEvent> withEvent(TEvent event, EventAction<TEvent, T, TStatus> action){
        currentTransition.getEvents().add(new TransitionEvent<TEvent, TStatus>(event, action));

        return this;
    }

    public StateMachineBuilder<TStatus, TEvent> withAction(TransitionAction<TEvent, EventContext<TEvent>, TStatus> action){
        currentTransition.setAction(action);

        return parentBuilder;
    }
}
