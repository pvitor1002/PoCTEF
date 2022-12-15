package br.com.puc.TEF.adapters.controller.server;

import br.com.puc.TEF.adapters.controller.server.contract.response.ErrorResponse;
import br.com.puc.TEF.adapters.datastore.ContextDatastore;
import br.com.puc.TEF.adapters.event.producer.TransferenciaProducer;
import br.com.puc.TEF.application.usecase.SalvarTefUseCase;
import br.com.puc.TEF.domain.exceptions.TimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice(assignableTypes = {TransferenciaController.class})
@RequiredArgsConstructor
public class TransferenciaErrorHandler {

    private final ContextDatastore contextDatastore;
    private final TransferenciaProducer transferenciaProducer;

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerException(MethodArgumentNotValidException e){
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .mensagem(errors.toString())
                .codigo("400")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<?> handleException(TimeoutException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo("408")
                .mensagem(e.getMessage())
                .build();

        System.err.println(e.getMessage());

        transferenciaProducer.produceTimeout(contextDatastore.getTransactionId().toString());

        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(errorResponse);
    }

}
