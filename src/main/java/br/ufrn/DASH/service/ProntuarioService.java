package br.ufrn.DASH.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.model.Prontuario;
import br.ufrn.DASH.model.Secao;
import br.ufrn.DASH.repository.ProntuarioRepository;

@Service
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    public Prontuario create(Prontuario prontuario) {
        return prontuarioRepository.save(prontuario);
    }

    public List<Prontuario> getAll() {
        return prontuarioRepository.findAll();
    }

    public Prontuario getById(Long id) {
        return prontuarioRepository.findById(id).orElse(null);
    }

    public Prontuario update(Long id, Prontuario prontuario) {
        
        Prontuario prontuarioExistente = this.getById(id);
        
        if (prontuarioExistente == null) {
            return null;
        }
        
        prontuarioExistente.setNome(prontuario.getNome());
        prontuarioExistente.setDescricao(prontuario.getDescricao());
        prontuarioExistente.setEhPublico(prontuario.getEhPublico());
        prontuarioExistente.setFinalizado(prontuario.getFinalizado());

        return prontuarioRepository.save(prontuarioExistente);
    }

    public void delete(Long id) {
        prontuarioRepository.deleteById(id);
    }

    public void deleteAll() {
        prontuarioRepository.deleteAll();
    }

    public Secao addSecao(Long idProntuario, Secao secaoNova) {
        Prontuario prontuario = this.getById(idProntuario);
        
        if (prontuario == null) {
            return null;
        }
        
        secaoNova.setOrdem(prontuario.getSecoes().size());
        secaoNova.setNivel(1);
        secaoNova.setProntuario(prontuario);

        prontuario.getSecoes().add(secaoNova);

        prontuarioRepository.save(prontuario);

        return prontuario.getSecoes().get(prontuario.getSecoes().size() - 1);
    }

}
