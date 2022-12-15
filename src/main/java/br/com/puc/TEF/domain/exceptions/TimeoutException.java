package br.com.puc.TEF.domain.exceptions;

public class TimeoutException extends RuntimeException {

    public TimeoutException() { super();}

    public TimeoutException(String s) { super(s);}
}
