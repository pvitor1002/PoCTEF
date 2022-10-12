package br.com.puc.statemachine;

import java.util.UUID;

public interface EventContext<TEvent> {
    UUID getId();
    TEvent getType();
}
