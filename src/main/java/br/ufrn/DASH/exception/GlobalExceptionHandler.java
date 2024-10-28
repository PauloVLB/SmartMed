package br.ufrn.DASH.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProntuarioTemplateException.class)
    public ResponseEntity<Map<String, Object>> handleProntuarioTemplateException(ProntuarioTemplateException ex){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProntuarioNotTemplateException.class)
    public ResponseEntity<Map<String, Object>> handleProntuarioNotTemplateException(ProntuarioNotTemplateException ex){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QuesitoNotInProntuarioException.class)
    public ResponseEntity<Map<String, Object>> handleQuesitoNotInProntuarioException(QuesitoNotInProntuarioException ex){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RespostaAndOpcaoIncompatibleException.class)
    public ResponseEntity<Map<String, Object>> handleRespostaAndOpcaoIncompatibleException(RespostaAndOpcaoIncompatibleException ex){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RespostaFullOfOpcaoException.class)
    public ResponseEntity<Map<String, Object>> handleRespostaFullOfOpcaoException(RespostaFullOfOpcaoException ex){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        errorResponse.put("error", HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(OpcaoHabilitadoraAlreadyInQuesitoException.class)
    public ResponseEntity<Map<String, Object>> handleOpcaoHabilitadoraAlreadyInQuesitoException(OpcaoHabilitadoraAlreadyInQuesitoException ex){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
