package br.com.meli.projetointegrador.exception;

/**
 * @author Edemilson Nobre
 * @version 1.0.0
 * @since 15/10/2021
 * Exception personalizada para entrada de dados
 */

public class ValidInputException extends RuntimeException{

    public ValidInputException(String message) {
        super(message);
    }
}
