package br.com.puc.TEF.domain.types;

import lombok.Getter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public enum TipoEventoTransferencia {

    CRIAR_MAQUINA_ESTADOS("CRIAR_MAQUINA_ESTADOS"),
    SENHA_PROCESSADA("SENHA_PROCESSADA"),
    CREDITO_PROCESSADO("CREDITO_PROCESSADO"),
    DEBITO_PROCESSADO("DEBITO_PROCESSADO");

    @Getter
    private String eventType;

    TipoEventoTransferencia(String name) {
        try{
            Field field = ReflectionUtils.findField(this.getClass(), "name");
            ReflectionUtils.makeAccessible(field);
            this.eventType = (String) ReflectionUtils.getField(field, this);
            ReflectionUtils.setField(field, this, name);
        } catch (Exception e) {}
    }

    public static TipoEventoTransferencia fromString(String text) {
        for(TipoEventoTransferencia b : TipoEventoTransferencia.values()){
            if(b.eventType.equalsIgnoreCase(text)){
                return b;
            }
        }
        return null;
    }
}
