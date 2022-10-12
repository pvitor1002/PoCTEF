package br.com.puc.TEF.domain.entities.maquinaestado;

public enum FaseEvento {
    TRANSFERENCIA_SOLICITADA("TRANSFERENCIA_SOLICITADA"),
    SENHA_PROCESSADA("SENHA_PROCESSADA"),
    CREDITO_EFETUADO("CREDITO_EFETUADO"),
    DEBITO_EFETUADO("DEBITO_EFETUADO"),
    TRANSFERENCIA_REALIZADA("TRANSFERENCIA_REALIZADA");

    private String fase;

    private FaseEvento(String fase) {this.fase = fase;}

    @Override
    public String toString() {return this.fase;}
}
