package br.ufrn.DASH.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.repository.OpcaoRepository;
import jakarta.transaction.Transactional;

@Service
public class OpcaoService {

    @Autowired
    private OpcaoRepository opcaoRepository;

    @Autowired
    private QuesitoService quesitoService;

    @Transactional
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

    public List<Opcao> getAllByIds(List<Long> ids) {
        return opcaoRepository.findAllById(ids);
    }

    @Transactional
    public Opcao update(Long id, Opcao opcao) {
        Opcao opcaoExistente = this.getById(id);
        
        opcaoExistente.setTextoAlternativa(opcao.getTextoAlternativa());
        opcaoExistente.setOrdem(opcao.getOrdem());

        return opcaoRepository.save(opcaoExistente);
    }

    @Transactional
    public void delete(Long id) {
        opcaoRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        List<Quesito> quesitos = quesitoService.getAll();
        for (Quesito quesito : quesitos) {
            quesito.getOpcoesHabilitadoras().clear();
        }
        opcaoRepository.deleteAll();
    }

    public StringBuilder verificaOpcoesComMesmoNome(List<Quesito> listaTodosQuesitos, StringBuilder erros) {
        for (Quesito quesito : listaTodosQuesitos) {
            Map<String, Integer> nomeOpcaoCount = new HashMap<>();
            for (Opcao opcao : quesito.getOpcoes()) {
                nomeOpcaoCount.put(opcao.getTextoAlternativa(), nomeOpcaoCount.getOrDefault(opcao.getTextoAlternativa(), 0) + 1);
            }

            List<String> opcoesRepetidas = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : nomeOpcaoCount.entrySet()) {
                if (entry.getValue() > 1) {
                    opcoesRepetidas.add(entry.getKey());
                }
            }

            if (!opcoesRepetidas.isEmpty()) {
                erros.append("O quesito '").append(quesito.getId()).append("' possui opções com nomes repetidos: ");
                for (String nomeOpcao : opcoesRepetidas) {
                    erros.append(nomeOpcao).append(", ");
                }
                if (erros.length() > 0) {
                    erros.setLength(erros.length() - 2); // Remove the last ", "
                }
                erros.append("\n");
            }
        }
        return erros;
    }

}
