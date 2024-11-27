package br.ufrn.DASH.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Secao;
import br.ufrn.DASH.model.interfaces.Item;
import br.ufrn.DASH.repository.SecaoRepository;
import br.ufrn.DASH.utils.Pair;
import jakarta.transaction.Transactional;

@Service
public class SecaoService {

    @Autowired
    private SecaoRepository secaoRepository;

    @Autowired
    @Lazy
    private QuesitoService quesitoService;

    @Transactional
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

    @Transactional
    public Secao update(Long id, Secao secao) {
        Secao secaoExistente = this.getById(id);
        
        secaoExistente.setTitulo(secao.getTitulo());
        secaoExistente.setOrdem(secao.getOrdem());
        secaoExistente.setNivel(secao.getNivel());

        return secaoRepository.save(secaoExistente);
    }

    @Transactional
    public void delete(Long id) {
        this.getById(id);
        secaoRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        secaoRepository.deleteAll();
    }

    @Transactional
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

    @Transactional
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

    public StringBuilder verificaSecoesVazias(List<Secao> listaTodasSecoes, StringBuilder erros) {
        List<Secao> secoesVazias = new ArrayList<>();
        for (Secao secao : listaTodasSecoes) {
            if(secao.getSubItens().isEmpty()) {
                secoesVazias.add(secao);
            }
        }

        if(!secoesVazias.isEmpty()) {
            erros.append("As seguintes seções devem possuir pelo menos um quesito ou subseção: ");
            for(Secao secao : secoesVazias) {
                erros.append(secao.getId() + ", ");
            }
            if (erros.length() > 0) {
                erros.setLength(erros.length() - 2); // Remove the last ", "
            }
            erros.append("\n");
        }
        return erros;
    }

    public Pair<Secao, Map<Opcao, Opcao>> duplicar(Map<Opcao, Opcao> opcoesDuplicadas, Secao secaoToDuplicate) {
        Secao secao = new Secao();
        secao.setTitulo(secaoToDuplicate.getTitulo());
        secao.setOrdem(secaoToDuplicate.getOrdem());
        secao.setNivel(secaoToDuplicate.getNivel());

        for (Secao subSecao : secaoToDuplicate.getSubSecoes()) {
            Pair<Secao, Map<Opcao, Opcao>> pairSecaoMapa = this.duplicar(opcoesDuplicadas, subSecao);
            Secao novaSubSecao = pairSecaoMapa.getFirst();
            opcoesDuplicadas = pairSecaoMapa.getSecond();
            novaSubSecao.setSuperSecao(secao);
            secao.getSubSecoes().add(novaSubSecao);
        }

        for (Quesito quesito : secaoToDuplicate.getQuesitos()) {
            Pair<Quesito, Map<Opcao, Opcao>> pairQuesitoMapa = quesitoService.duplicar(opcoesDuplicadas, quesito);
            Quesito novoQuesito = pairQuesitoMapa.getFirst();
            opcoesDuplicadas = pairQuesitoMapa.getSecond();
            novoQuesito.setSecao(secao);
            secao.getQuesitos().add(novoQuesito);
        }

        return new Pair<Secao, Map<Opcao, Opcao>>(secao, opcoesDuplicadas);
    }
}
