package br.ufrn.DASH.mapper.opcao;

public record OpcaoOutput (
    Long id,
    String textoAlternativa, 
    Integer ordem,
    List<Long> quesitosHabilitadosIds,
    List<Long> diagnosticosIds)
    Long quesitoId)
{}