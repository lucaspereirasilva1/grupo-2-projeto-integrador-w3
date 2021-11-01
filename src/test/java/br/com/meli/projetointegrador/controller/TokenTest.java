package br.com.meli.projetointegrador.controller;

import lombok.Data;

import java.util.List;

@Data
public class TokenTest {

    private String id;
    private String username;
    private String email;
    private List<String> roles;
    private String accessToken;
    private String tokenType;

}
