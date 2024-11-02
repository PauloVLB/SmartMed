package br.ufrn.DASH.mapper.secao;

import java.util.List;

import br.ufrn.DASH.mapper.subItem.ItemOutput;

public record SecaoCompleteOutput(
    Long id,
    String tipoDeItem,
    String numeracao,
    String titulo, 
    Integer ordem, 
    Integer nivel,
    List<ItemOutput> subItens,
    Long superSecaoId,
    Long prontuarioId) implements ItemOutput
{}
