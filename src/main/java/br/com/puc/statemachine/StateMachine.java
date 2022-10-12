package br.com.puc.statemachine;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class StateMachine<TStatus extends Enum, TEvent extends Enum> {
    protected StateMachineContext context;
    protected List<Transition<TStatus, TEvent>> transitions;

    public <T extends EventContext<TEvent>> boolean sendEvent(T eventContext) throws JsonProcessingException {
        TEvent eventoAtual = eventContext.getType();

        Optional<Transition<TStatus, TEvent>> transicaoPossivel = this.transitions.stream()
                .filter(t -> t.getEvents().stream()
                        .anyMatch(e -> e.getEvent().equals(eventoAtual)) &&
                        t.getFrom().equals(context.getCurrentStatus())
                ).findFirst();

        if(transicaoPossivel.isPresent()){
            Transition<TStatus, TEvent> transicao = transicaoPossivel.get();
            TStatus statusDestino = transicao.getTo();

            context.registerEvent(statusDestino, eventoAtual);

            Optional<TransitionEvent<TEvent, TStatus>> eventoAtualOptional = transicao.getEvents().stream()
                    .filter(t -> t.getEvent().equals(eventoAtual)).findFirst();

            if(eventoAtualOptional.isPresent()){
                TransitionEvent<TEvent, TStatus> eventAtual = eventoAtualOptional.get();

                if(eventAtual.getAction() != null){
                    eventAtual.getAction().execute(eventContext, eventoAtual, context);
                }
            }

            if(transicao.isTransitionCompleted(context)){
                context.setCurrentStatus(statusDestino);

                if(transicao.getAction() != null){
                    transicao.getAction().execute(eventContext, (TEvent) eventoAtual, context);
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
