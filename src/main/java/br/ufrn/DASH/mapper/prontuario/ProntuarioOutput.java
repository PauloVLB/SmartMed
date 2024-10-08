package br.ufrn.DASH.mapper.prontuario;

public record ProntuarioOutput(
    String nome, 
    String descricao, 
    Boolean finalizado, 
    Boolean ehPublico,
    Long id)
{}