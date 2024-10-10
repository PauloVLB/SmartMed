package br.ufrn.DASH.mapper.usuario;

import java.util.List;

import br.ufrn.DASH.model.enums.TipoUsuario;

public record UsuarioOutput(
    Long id,
    String nome,
    String login,
    String senha,
    TipoUsuario tipoUsuario,
    List<Long> prontuariosIds)
{}