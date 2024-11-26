package br.ufrn.DASH.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Secao;
import br.ufrn.DASH.model.interfaces.Item;
import br.ufrn.DASH.repository.SecaoRepository;

@Service
public class SecaoService {

    @Autowired
    private SecaoRepository secaoRepository;

    @Autowired
    @Lazy
    private QuesitoService quesitoService;

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
        
        List<Item> subItems = superSecao.getSubItens();
        if(subItems.isEmpty()) {
            subSecao.setOrdem(1);
        }
        else {
            subSecao.setOrdem(subItems.get(subItems.size() - 1).getOrdem() + 1);
        }
        subSecao.setNivel(superSecao.getNivel() + 1);

        subSecao.setSuperSecao(superSecao);
        superSecao.getSubSecoes().add(subSecao);
        secaoRepository.save(superSecao);
        
        return superSecao.getSubSecoes().get(superSecao.getSubSecoes().size() - 1);
    }

    public Quesito addQuesito(Long idSecao, Quesito quesito) {
        Secao secao = this.getById(idSecao);
        
        List<Item> subItems = secao.getSubItens();
        if(subItems.isEmpty()) {
            quesito.setOrdem(1);
        }
        else {
            quesito.setOrdem(subItems.get(subItems.size() - 1).getOrdem() + 1);
        }
        quesito.setNivel(secao.getNivel() + 1);
        quesito.setSecao(secao);
        secao.getQuesitos().add(quesito);
        secaoRepository.save(secao);
        
        return secao.getQuesitos().get(secao.getQuesitos().size() - 1);
    }

    public List<Quesito> getQuesitos(Long idSecao) {
        Secao secao = this.getById(idSecao);
        
        return secao.getQuesitos();
    }

    public List<Secao> getSubSecoes(Long idSecao) {
        Secao secao = this.getById(idSecao);
        
        return secao.getSubSecoes();
    }

    protected List<Opcao> getOpcoesMarcadas(Secao secao) {
        List<Opcao> retorno = new ArrayList<>();

        for (Quesito quesito : secao.getQuesitos()) {
            retorno.addAll(quesitoService.getOpcoesMarcadas(quesito));
        }
        for (Secao subSecao : secao.getSubSecoes()) {
            retorno.addAll(this.getOpcoesMarcadas(subSecao));
        }

    
        return retorno;
    }
}
