package br.com.meli.projetointegrador.controller;

import lombok.Data;

import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste do controller responsavel pela regra de negocio relacionada ao TokenTest
 */

@Data
public class TokenTest {

    private String id;
    private String username;
    private String email;
    private List<String> roles;
    private String accessToken;
    private String tokenType;

}
