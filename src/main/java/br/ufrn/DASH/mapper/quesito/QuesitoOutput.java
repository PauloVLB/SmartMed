package br.ufrn.DASH.mapper.quesito;

import java.util.List;

import br.ufrn.DASH.model.enums.TipoResposta;

public record QuesitoOutput(
    Long id,
    String enunciado,
    Boolean obrigatorio,
    Integer ordem,
    Integer nivel,
    TipoResposta tipoResposta,
    Long superQuesitoId,
    Long secaoId,
    Long respostaId,
    List<Long> opcoesHabilitadorasIds,
    List<Long> subQuesitosIds,
    List<Long> opcoesIds
    )
{}