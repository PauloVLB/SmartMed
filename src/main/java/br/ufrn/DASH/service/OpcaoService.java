package br.ufrn.DASH.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.repository.OpcaoRepository;

@Service
public class OpcaoService {

    @Autowired
    private OpcaoRepository opcaoRepository;

    @Autowired
    private QuesitoService quesitoService;

    public Opcao create(Opcao opcao) {
        return opcaoRepository.save(opcao);
    }

    public List<Opcao> getAll() {
        return opcaoRepository.findAll();
    }

    public Opcao getById(Long id) {
        return opcaoRepository.findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(id, new Opcao())
        );
    }

    public Opcao update(Long id, Opcao opcao) {
        Opcao opcaoExistente = this.getById(id);
        
        // if (opcaoExistente == null) {
        //     throw new EntityNotFoundException(id, new Opcao());
        // }
        
        opcaoExistente.setTextoAlternativa(opcao.getTextoAlternativa());
        opcaoExistente.setOrdem(opcao.getOrdem());

        return opcaoRepository.save(opcaoExistente);
    }

    public void delete(Long id) {
        Opcao opcaoHabilitadora = this.getById(id);
        List<Quesito> quesitos = opcaoHabilitadora.getQuesitosHabilitados();
        for (Quesito quesito : quesitos) {
            quesito.getOpcoesHabilitadoras().removeIf(opcao -> opcao.getId().equals(id));
        }
        opcaoRepository.deleteById(id);
    }

    public void deleteAll() {
        List<Quesito> quesitos = quesitoService.getAll();
        for (Quesito quesito : quesitos) {
            quesito.getOpcoesHabilitadoras().clear();
        }
        opcaoRepository.deleteAll();
    }
}
