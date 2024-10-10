package br.ufrn.DASH.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.repository.QuesitoRepository;

@Service
public class QuesitoService {

    @Autowired
    private QuesitoRepository quesitoRepository;

    public Quesito create(Quesito quesito) {
        return quesitoRepository.save(quesito);
    }

    public List<Quesito> getAll() {
        return quesitoRepository.findAll();
    }

    public Quesito getById(Long id) {
        return quesitoRepository.findById(id).orElse(null);
    }

    public Quesito update(Long id, Quesito quesito) {
        Quesito quesitoExistente = this.getById(id);
        
        if (quesitoExistente == null) {
            return null;
        }
        
        quesitoExistente.setEnunciado(quesito.getEnunciado());
        quesitoExistente.setOrdem(quesito.getOrdem());
        quesitoExistente.setNivel(quesito.getNivel());
        quesitoExistente.setObrigatorio(quesito.getObrigatorio());
        quesitoExistente.setTipoResposta(quesito.getTipoResposta());

        return quesitoRepository.save(quesitoExistente);
    }

    public void delete(Long id) {
        quesitoRepository.deleteById(id);
    }

    public void deleteAll() {
        quesitoRepository.deleteAll();
    }
}
