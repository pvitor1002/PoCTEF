package br.com.puc.statemachine;

import java.util.List;
import java.util.stream.Collectors;

public class OrTransition<TStatus, TEvent> extends Transition<TStatus, TEvent> {

    public OrTransition(TStatus from, TStatus to, List<TransitionEvent<TEvent, TStatus>> transitionEvents, TransitionAction<TEvent, EventContext<TEvent>, TStatus> action){
        super(from, to, transitionEvents, action);
    }

    @Override
    boolean isTransitionCompleted(StateMachineContext<TStatus, TEvent> context){
        final List<TEvent> eventos = getEvents().stream()
                .map(TransitionEvent::getEvent)
                .collect(Collectors.toList());
        return context.getCurrentStatus().equals(getFrom()) &&
                context.getEventsOfStatus(getTo()).stream().anyMatch(eventos::contains);
    }
}
