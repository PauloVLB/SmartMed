package br.ufrn.DASH.mapper.opcao;

public record OpcaoCompleteOutput (
    Long id,
    String textoAlternativa, 
    Integer ordem,
    Long quesitoId)
{}