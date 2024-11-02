package br.ufrn.DASH.mapper.prontuario;

import java.util.List;

import br.ufrn.DASH.mapper.secao.SecaoCompleteOutput;

public record ProntuarioCompleteOutput(
    Long id,    
    String nome, 
    String descricao, 
    Boolean finalizado, 
    Boolean ehPublico,
    Boolean ehTemplate,
    Long usuarioId,
    List<SecaoCompleteOutput> secoes,
    String diagnosticoLLM
    )
{}