package br.com.puc.statemachine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Transition<TStatus, TEvent> {
    private TStatus from;
    private TStatus to;
    private List<TransitionEvent<TEvent, TStatus>> events;
    private TransitionAction<TEvent, EventContext<TEvent>, TStatus> action;

    void setAction(TransitionAction<TEvent, EventContext<TEvent>, TStatus> action) {
        this.action = action;
    }

    boolean isTransitionCompleted(StateMachineContext<TStatus, TEvent> context) {
        return context.getCurrentStatus().equals(from) &&
                context.getEventsOfStatus(to)
                        .containsAll(events.stream()
                                .map(TransitionEvent::getEvent)
                                .collect(Collectors.toList())
                        );
    }
}
