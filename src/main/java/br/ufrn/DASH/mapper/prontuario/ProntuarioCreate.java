package br.ufrn.DASH.mapper.prontuario;


public record ProntuarioCreate(
    String nome, 
    String descricao,
    Boolean ehPublico,
    Boolean ehTemplate)
{}
