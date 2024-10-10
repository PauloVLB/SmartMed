package br.ufrn.DASH.mapper.secao;

public record SecaoCreate(
    String titulo, 
    Integer ordem,
    Integer nivel)
{}