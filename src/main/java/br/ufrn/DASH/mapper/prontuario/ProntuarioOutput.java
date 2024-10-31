package br.ufrn.DASH.mapper.prontuario;

import java.util.List;

public record ProntuarioOutput(
    Long id,    
    String nome, 
    String descricao, 
    Boolean finalizado, 
    Boolean ehPublico,
    Boolean ehTemplate,
    Long usuarioId,
    List<Long> secoesIds,
    String diagnosticoLLM
    )
{}