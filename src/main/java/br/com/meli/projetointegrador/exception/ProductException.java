package br.com.meli.projetointegrador.exception;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 * Exception personalizada para produtos
 */

public class ProductException extends RuntimeException{

    public ProductException(String message) {
        super(message);
    }
}
