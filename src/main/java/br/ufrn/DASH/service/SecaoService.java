package br.ufrn.DASH.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.model.Prontuario;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Secao;
import br.ufrn.DASH.repository.SecaoRepository;

@Service
public class SecaoService {

    @Autowired
    private SecaoRepository secaoRepository;

    public Secao create(Secao secao) {
        return secaoRepository.save(secao);
    }

    public List<Secao> getAll() {
        return secaoRepository.findAll();
    }

    public Secao getById(Long id) {
        return secaoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, new Secao())
            );
    }

    public Secao update(Long id, Secao secao) {
        Secao secaoExistente = this.getById(id);
        
        // if (secaoExistente == null) {
        //     return null;
        // }
        
        secaoExistente.setTitulo(secao.getTitulo());
        secaoExistente.setOrdem(secao.getOrdem());
        secaoExistente.setNivel(secao.getNivel());

        return secaoRepository.save(secaoExistente);
    }

    public void delete(Long id) {
        this.getById(id);
        secaoRepository.deleteById(id);
    }

    public void deleteAll() {
        secaoRepository.deleteAll();
    }

    public Secao addSubSecao(Long idSuperSecao, Secao subSecao) {
        Secao superSecao = this.getById(idSuperSecao);
        
        // if (superSecao == null) {
        //     return null;
        // }
        
        subSecao.setOrdem(superSecao.getSubSecoes().size());
        subSecao.setNivel(superSecao.getNivel() + 1);

        subSecao.setSuperSecao(superSecao);
        superSecao.getSubSecoes().add(subSecao);
        secaoRepository.save(superSecao);
        
        return superSecao.getSubSecoes().get(superSecao.getSubSecoes().size() - 1);
    }

    public Quesito addQuesito(Long idSecao, Quesito quesito) {
        Secao secao = this.getById(idSecao);
        
        // if (secao == null) {
        //     return null;
        // }
        
        quesito.setOrdem(secao.getQuesitos().size());
        quesito.setNivel(secao.getNivel() + 1);
        quesito.setSecao(secao);
        secao.getQuesitos().add(quesito);
        secaoRepository.save(secao);
        
        return secao.getQuesitos().get(secao.getQuesitos().size() - 1);
    }

    public List<Quesito> getQuesitos(Long idSecao) {
        Secao secao = this.getById(idSecao);
        
        // if (secao == null) {
        //     return null;
        // }
        
        return secao.getQuesitos();
    }

    public List<Secao> getSubSecoes(Long idSecao) {
        Secao secao = this.getById(idSecao);
        
        // if (secao == null) {
        //     return null;
        // }
        
        return secao.getSubSecoes();
    }

    protected Prontuario findProntuario(Secao secao) {
        if(secao.getSuperSecao() != null){
            return this.findProntuario(secao.getSuperSecao());
        }else{
            return secao.getProntuario();
        }
    }
}
