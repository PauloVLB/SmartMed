package br.ufrn.DASH.mapper.resposta;

import java.util.List;

import br.ufrn.DASH.mapper.opcao.OpcaoCompleteOutput;

public record RespostaCompleteOutput(
    Long id,
    List<String>conteudo,
    List<OpcaoCompleteOutput> opcoesMarcadas,
    Long idQuesito)
{}
