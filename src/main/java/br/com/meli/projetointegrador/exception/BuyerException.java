package br.com.meli.projetointegrador.exception;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Exception personalizada para buyer
 */

public class BuyerException extends RuntimeException {

    public BuyerException(String message) {
        super(message);
    }

}
