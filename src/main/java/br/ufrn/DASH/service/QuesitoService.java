package br.ufrn.DASH.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.exception.OpcaoHabilitadoraAlreadyInQuesitoException;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Secao;
import br.ufrn.DASH.model.Resposta;
import br.ufrn.DASH.repository.QuesitoRepository;

@Service
public class QuesitoService {

    @Autowired
    private QuesitoRepository quesitoRepository;

    @Autowired
    @Lazy
    private OpcaoService opcaoService;

    @Autowired
    private SecaoService secaoService;

    @Autowired
    @Lazy
    private RespostaService respostaService;

    public Quesito create(Quesito quesito) {
        return quesitoRepository.save(quesito);
    }

    public List<Quesito> getAll() {
        return quesitoRepository.findAll();
    }

    public Quesito getById(Long id) {
        return quesitoRepository.findById(id)
        .orElseThrow(
                () -> new EntityNotFoundException(id, new Quesito())
            );
    }

    public Quesito update(Long id, Quesito quesito) {
        Quesito quesitoExistente = this.getById(id);
        
        // if (quesitoExistente == null) {
        //     return null;
        // }
        
        quesitoExistente.setEnunciado(quesito.getEnunciado());
        quesitoExistente.setOrdem(quesito.getOrdem());
        quesitoExistente.setNivel(quesito.getNivel());
        quesitoExistente.setObrigatorio(quesito.getObrigatorio());
        quesitoExistente.setTipoResposta(quesito.getTipoResposta());

        return quesitoRepository.save(quesitoExistente);
    }

    public void delete(Long id) {
        Quesito quesitoARemover = this.getById(id);
        Secao secao = quesitoARemover.getSecao();
        if(secao != null){
            secao.getQuesitos().removeIf(quesito -> quesito.getId().equals(id));
            secaoService.update(secao.getId(), secao);
        }
        Quesito superQuesito = quesitoARemover.getSuperQuesito();
        if(superQuesito != null){
            superQuesito.getSubQuesitos().removeIf(quesito -> quesito.getId().equals(id));
            quesitoRepository.save(superQuesito);
        }

        quesitoRepository.deleteById(id);
    }

    public void deleteAll() {
        List<Quesito> quesitos = this.getAll();
        for (Quesito quesito : quesitos) {
            Secao secao = quesito.getSecao();
            if(secao != null){
                secao.getQuesitos().clear();
                secaoService.update(secao.getId(), secao);
            }
            Quesito superQuesito = quesito.getSuperQuesito();
            if(superQuesito != null){
                superQuesito.getSubQuesitos().clear();
                quesitoRepository.save(superQuesito);
            }
        }
        quesitoRepository.deleteAll();
    }

    public Quesito addSubQuesito(Long idQuesito, Quesito subQuesito) {
        Quesito superQuesito = this.getById(idQuesito);

        // if(superQuesito == null){
        //     return null;
        // }
        subQuesito.setOrdem(superQuesito.getSubQuesitos().size());
        subQuesito.setNivel(superQuesito.getNivel() + 1);

        subQuesito.setSuperQuesito(superQuesito);
        superQuesito.getSubQuesitos().add(subQuesito);
        quesitoRepository.save(superQuesito);
        
        return superQuesito.getSubQuesitos().get(superQuesito.getSubQuesitos().size() - 1);
        
    }

    public Opcao addOpcao(Long idQuesito, Opcao opcaoNovo) {
        Quesito quesito = this.getById(idQuesito);
        
        // if (quesito == null) {
        //     return null;
        // }
        
        opcaoNovo.setOrdem(quesito.getOpcoes().size());
        opcaoNovo.setQuesito(quesito);
        quesito.getOpcoes().add(opcaoNovo);
        quesitoRepository.save(quesito);
        
        return quesito.getOpcoes().get(quesito.getOpcoes().size() - 1);
    }

    public List<Opcao> getOpcoes(Long idQuesito) {
        Quesito quesito = this.getById(idQuesito);
        
        // if (quesito == null) {
        //     return null;
        // }
        
        return quesito.getOpcoes();
    }

    public List<Quesito> getSubQuesitos(Long idQuesito) {
        Quesito quesito = this.getById(idQuesito);
        
        // if (quesito == null) {
        //     return null;
        // }
        
        return quesito.getSubQuesitos();
    }

    public Opcao addOpcaoHabilitadora(Long idQuesito, Long idOpcao) {
        Quesito quesito = this.getById(idQuesito);
        Opcao opcao = opcaoService.getById(idOpcao);

        if(quesito.getOpcoesHabilitadoras().contains(opcao)){
            throw new OpcaoHabilitadoraAlreadyInQuesitoException(idQuesito, idOpcao);
        }

        quesito.getOpcoesHabilitadoras().add(opcao);
        quesitoRepository.save(quesito);
        
        return opcao;
    }
    
    public Boolean estaHabilitado(Quesito quesito) {
        List<Opcao> opcoesHabilitadoras = quesito.getOpcoesHabilitadoras();
        if(opcoesHabilitadoras.isEmpty()){
            return true;
        }

        List<Quesito> quesitosPai = opcoesHabilitadoras.stream()
            .map(Opcao::getQuesito)
            .distinct()
            .toList();
        
        List<Resposta> respostas = quesitosPai.stream()
            .filter(q -> q.getResposta() != null)
            .map(Quesito::getResposta)
            .toList();

        for (Resposta resposta : respostas) {
            List<Opcao> opcoesMarcadas = resposta.getOpcoesMarcadas();
            if(opcoesHabilitadoras.stream().anyMatch(opcao -> opcoesMarcadas.contains(opcao))){
                return true;
            }
            
        }
        return false;
    }

    public Boolean estaHabilitado(Long id) {
        Quesito quesito = this.getById(id);
        return estaHabilitado(quesito);
    }
}
