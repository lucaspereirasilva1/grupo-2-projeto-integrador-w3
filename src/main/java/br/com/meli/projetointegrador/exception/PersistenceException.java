package br.com.meli.projetointegrador.exception;

import org.springframework.dao.DataAccessException;

public class PersistenceException extends DataAccessException {

    public PersistenceException(String msg) {
        super(msg);
    }

    public PersistenceException(String msg, String cause) {
        super(msg + cause);
    }

    public PersistenceException(String msg, Throwable t) {
        super(msg, t);
    }

}
