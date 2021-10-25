package br.com.meli.projetointegrador.exception;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Exception personalizada para ordem de entrada
 */

public class InboundOrderException extends RuntimeException{

    public InboundOrderException(String message) {
        super(message);
    }

}
