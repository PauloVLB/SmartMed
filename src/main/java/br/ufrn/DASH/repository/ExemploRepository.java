package br.ufrn.DASH.repository;

import br.ufrn.DASH.model.ExemploModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExemploRepository extends JpaRepository<ExemploModel, Long> {
   
}