package br.com.puc.statemachine;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StateMachineBuilder<TStatus extends Enum, TEvent extends Enum> {
    private List<Transition<TStatus, TEvent>> allowedTransitions;
    private TStatus statusInicial;

    public StateMachineBuilder() { this.allowedTransitions = new ArrayList<>(); }

    public static <TStatus extends Enum, TEvent extends Enum> StateMachineBuilder<TStatus, TEvent> create(Class<TStatus> s, Class<TEvent> e){
        return new StateMachineBuilder<TStatus, TEvent>();
    }

    public StateMachineBuilder<TStatus, TEvent> initial(TStatus initial){
        this.statusInicial = initial;
        return this;
    }

    public StateMachineTransitionBuilder<TStatus, TEvent> addScheduledTransition(TStatus from, TStatus to, Duration period){
        return this.addTransition(from, to);
    }

    public StateMachineTransitionBuilder<TStatus, TEvent> addTransition(TStatus from, TStatus to){
        Optional<Transition<TStatus, TEvent>> transicaoExistente = this.allowedTransitions.stream().filter(t -> t.getFrom().equals(from) && t.getTo().equals(to)).findFirst();

        if(transicaoExistente.isPresent()) {
            Transition<TStatus, TEvent> transition = transicaoExistente.get();
            return new StateMachineTransitionBuilder<>(transition, this);
        } else {
            Transition<TStatus, TEvent> transition = new Transition<>(from, to, new ArrayList<>(), null);

            this.allowedTransitions.add(transition);

            return new StateMachineTransitionBuilder<>(transition, this);
        }
    }

    public StateMachineTransitionBuilder<TStatus, TEvent> addOrTransition(TStatus from, TStatus to){
        Optional<Transition<TStatus, TEvent>> transicaoExistente = this.allowedTransitions.stream().filter(t -> t.getFrom().equals(from) && t.getTo().equals(to)).findFirst();

        if(transicaoExistente.isPresent()){
            Transition<TStatus, TEvent> transition = transicaoExistente.get();
            return new StateMachineTransitionBuilder<>(transition, this);
        } else {
            Transition<TStatus, TEvent> transition = new OrTransition<>(from, to, new ArrayList<>(), null);

            this.allowedTransitions.add(transition);

            return new StateMachineTransitionBuilder<>(transition, this);
        }
    }

    public StateMachine<TStatus, TEvent> build(StateMachineContext<TStatus, TEvent> context) {
        if(context.getCurrentStatus() == null){
            context.setCurrentStatus(this.statusInicial);
        }

        StateMachine<TStatus, TEvent> stateMachine = new StateMachine<>(context, allowedTransitions);

        return stateMachine;
    }
}
