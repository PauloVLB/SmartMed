package br.ufrn.DASH.mapper.opcao;

public record OpcaoOutput (
    Long id,
    String textoAlternativa, 
    Integer ordem,
    Long quesitoId)
{}