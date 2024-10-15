package br.ufrn.DASH.mapper.resposta;

import java.util.List;

public record RespostaOutput(
    Long id,
    List<String>conteudo,
    List<Long> opcoesMarcadasIds,
    Long idQuesito)
{}
