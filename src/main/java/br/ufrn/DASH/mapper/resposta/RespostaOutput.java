package br.ufrn.DASH.mapper.resposta;

import java.util.List;

public record RespostaOutput(
    List<String>conteudo,
    List<Long> opcoesMarcadasIds,
    Long id,
    Long idQuesito)
{}
