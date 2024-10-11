package br.ufrn.DASH.mapper.resposta;

import java.util.List;

public record RespostaOutput(
    List<String>conteudo,
    Long id)
{}
