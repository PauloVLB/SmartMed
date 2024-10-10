package br.ufrn.DASH.mapper.usuario;

import br.ufrn.DASH.model.enums.TipoUsuario;

public record UsuarioCreate(
    String nome,
    String login,
    String senha,
    TipoUsuario tipoUsuario)
{}