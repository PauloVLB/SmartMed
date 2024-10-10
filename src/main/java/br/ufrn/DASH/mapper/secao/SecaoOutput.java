package br.ufrn.DASH.mapper.secao;

import java.util.List;

public record SecaoOutput(
    Long id,
    String titulo, 
    Integer ordem, 
    Integer nivel,
    List<Long> subSecoesIds,
    Long superSecaoId,
    Long prontuarioId)
{}
