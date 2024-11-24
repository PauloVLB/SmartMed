package br.ufrn.DASH.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufrn.DASH.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
}
