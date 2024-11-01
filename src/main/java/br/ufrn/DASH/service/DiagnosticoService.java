package br.ufrn.DASH.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.model.Diagnostico;
import br.ufrn.DASH.repository.DiagnosticoRepository;

@Service
public class DiagnosticoService {
    
    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    public Diagnostico create(Diagnostico diagnostico) {
        return diagnosticoRepository.save(diagnostico);
    }

    public List<Diagnostico> getAll() {
        return diagnosticoRepository.findAll();
    }

    public Diagnostico getById(Long id) {
        return diagnosticoRepository.findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(id, new Diagnostico())
        );
    }

    public Diagnostico update(Long id, Diagnostico diagnostico) {
        Diagnostico diagnosticoExistente = this.getById(id);
        
        diagnosticoExistente.setDescricao(diagnostico.getDescricao());

        return diagnosticoRepository.save(diagnosticoExistente);
    }

    public void delete(Long id) {
        diagnosticoRepository.deleteById(id);
    }

    public void deleteAll() {
        diagnosticoRepository.deleteAll();
    }
}
