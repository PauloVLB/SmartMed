package br.ufrn.DASH.mapper.prontuario;

public record ProntuarioUpdate(
    String nome, 
    String descricao, 
    Boolean finalizado, 
    Boolean ehPublico,
    Boolean ehTemplate)
{}