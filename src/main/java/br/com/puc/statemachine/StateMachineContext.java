package br.com.puc.statemachine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.TypeMismatchException;

import java.util.*;

@ToString
public class StateMachineContext<TStatus, TEvent> {

    @Getter
    private Map<TStatus, Set<TEvent>> statusMap;
    @Getter
    private TStatus currentStatus;

    @Getter
    private Map<String, StateObject> contextStateMap;

    public StateMachineContext(){
        this.statusMap = new HashMap<>();
        this.contextStateMap = new HashMap<>();
    }

    void registerEvent(TStatus status, TEvent event){
        if(!statusMap.containsKey(status)){
            statusMap.put(status, new HashSet<>());
        }

        statusMap.get(status).add(event);
    }

    @Deprecated
    public boolean isStatusCompleted(TStatus status, List<TEvent> eventsRequired){
        if(statusMap.containsKey(status)){
            return statusMap.get(status).containsAll(eventsRequired);
        }
        return false;
    }

    Set<TEvent> getEventsOfStatus(TStatus status){
        if(statusMap.containsKey(status)){
            return statusMap.get(status);
        }
        throw new RuntimeException();
    }

    void setCurrentStatus(TStatus status){ this.currentStatus = status; }

    @JsonIgnore
    public <R> R getOption(String key, Class<R> type){
        if(!this.contextStateMap.containsKey(key))
            return null;

        Object value = this.contextStateMap.get(key).getValue();

        if(type.isInstance(value)){
            return (R)value;
        }

        throw new TypeMismatchException(value, type);
    }

    @JsonIgnore
    public <R> void setOption(String key, R value) { this.contextStateMap.put(key, new StateObject<R>(key, value)); }
}
