package br.ufrn.DASH.mapper.quesito;

import java.util.List;

import br.ufrn.DASH.mapper.opcao.OpcaoCompleteOutput;
import br.ufrn.DASH.mapper.resposta.RespostaCompleteOutput;
import br.ufrn.DASH.model.enums.TipoResposta;

public record QuesitoCompleteOutput(
    Long id,
    String enunciado,
    Boolean obrigatorio,
    Integer ordem,
    Integer nivel,
    TipoResposta tipoResposta,
    Long superQuesitoId,
    Long secaoId,
    RespostaCompleteOutput resposta,
    List<Long> opcoesHabilitadorasIds,
    List<QuesitoCompleteOutput> subQuesitos,
    List<OpcaoCompleteOutput> opcoes
    )
{}