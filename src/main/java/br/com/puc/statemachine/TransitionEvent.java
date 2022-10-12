package br.com.puc.statemachine;

import lombok.Data;

@Data
public class TransitionEvent<TEvent, TStatus> {
    private TEvent event;
    private EventAction<TEvent, EventContext<TEvent>, TStatus> action;

    public <T extends EventContext<TEvent>> TransitionEvent(TEvent event, EventAction<TEvent, T, TStatus> action){
        this.event = event;
        this.action = (EventAction<TEvent, EventContext<TEvent>, TStatus>) action;
    }
}
