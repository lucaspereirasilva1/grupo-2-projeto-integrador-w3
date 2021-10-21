package br.com.meli.projetointegrador.exception;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 * Exception personalizada para produtos
 */

public class SectionException extends RuntimeException{

    public SectionException(String message) {
        super(message);
    }
}
