package br.ufrn.DASH.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufrn.DASH.model.Diagnostico;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

}
