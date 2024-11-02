package br.ufrn.DASH.mapper.opcao;

import java.util.List;

public record OpcaoCompleteOutput (
    Long id,
    String textoAlternativa, 
    Integer ordem,
    Long quesitoId,
    List<Long> quesitosHabilitadosIds)
{}