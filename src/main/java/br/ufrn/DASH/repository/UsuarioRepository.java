package br.ufrn.DASH.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufrn.DASH.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
