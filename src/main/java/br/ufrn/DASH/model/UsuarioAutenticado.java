package br.ufrn.DASH.model;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioAutenticado implements UserDetails {

    @Autowired
    private Usuario usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> usuario.getTipoUsuario().getDescricao());
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }
    
    public UsuarioAutenticado(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
