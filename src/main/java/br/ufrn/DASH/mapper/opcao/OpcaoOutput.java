package br.ufrn.DASH.mapper.opcao;

import java.util.List;

public record OpcaoOutput (
    Long id,
    String textoAlternativa, 
    Integer ordem,
    Long quesitoId,
    List<Long> quesitosHabilitadosIds)
{}