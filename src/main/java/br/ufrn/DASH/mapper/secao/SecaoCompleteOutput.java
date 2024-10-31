package br.ufrn.DASH.mapper.secao;

import java.util.List;

import br.ufrn.DASH.mapper.quesito.QuesitoCompleteOutput;

public record SecaoCompleteOutput(
    Long id,
    String titulo, 
    Integer ordem, 
    Integer nivel,
    List<SecaoCompleteOutput> subSecoes,
    Long superSecaoId,
    Long prontuarioId,
    List<QuesitoCompleteOutput> quesitos)
{}
