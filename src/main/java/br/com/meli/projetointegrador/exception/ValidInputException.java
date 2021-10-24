package br.com.meli.projetointegrador.exception;

public class ValidInputException extends RuntimeException {

/**
 * @author Edemilson Nobre
 * @version 1.0.0
 * @since 15/10/2021
 * Exception personalizada para entrada de dados
 */

    public ValidInputException(String message) {
        super(message);
    }
  
}
