package br.ufrn.DASH.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufrn.DASH.model.Prontuario;

public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {

}
