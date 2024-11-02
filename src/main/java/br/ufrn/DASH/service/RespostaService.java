package br.ufrn.DASH.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.exception.OpcaoMarcadaAlreadyInRespostaException;
import br.ufrn.DASH.exception.RespostaAndOpcaoIncompatibleException;
import br.ufrn.DASH.exception.RespostaFullOfOpcaoException;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Resposta;
import br.ufrn.DASH.model.enums.TipoResposta;
import br.ufrn.DASH.repository.RespostaRepository;

@Service
public class RespostaService {
    
    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private OpcaoService opcaoService;

    public Resposta create(Resposta resposta) {
        return respostaRepository.save(resposta);
    }

    public List<Resposta> getAll() {
        return respostaRepository.findAll();
    }

    public Resposta getById(Long id) {
        return respostaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, new Resposta())
            );
    }

    public Resposta update(Long id, Resposta resposta) {
        Resposta respostaExistente = this.getById(id);
        // if(respostaExistente == null){
        //     return null;
        // }
        
        respostaExistente.setConteudo(resposta.getConteudo());
        
        return respostaRepository.save(respostaExistente);
    }

    public void delete(Long id) {
        this.getById(id);
        respostaRepository.deleteById(id);
    }

    public void deleteAll() {
        respostaRepository.deleteAll();
    }

    public Opcao addOpcaoMarcada(Long idResposta, Long idOpcao) {
        Resposta resposta = this.getById(idResposta);
        Opcao opcao = opcaoService.getById(idOpcao);

        // if(resposta == null){
        //     return null;
        // }
        // if(opcao == null){
        //     return null;
        // }

        if(resposta.getQuesito() != opcao.getQuesito()){
            throw new RespostaAndOpcaoIncompatibleException(idResposta, idOpcao);
        }
        if(resposta.getQuesito().getTipoResposta() == TipoResposta.OBJETIVA_SIMPLES && resposta.getOpcoesMarcadas().isEmpty()){
            resposta.getOpcoesMarcadas().add(opcao);
        } else if(resposta.getQuesito().getTipoResposta() == TipoResposta.OBJETIVA_MULTIPLA){
            if(resposta.getOpcoesMarcadas().contains(opcao)){
                throw new OpcaoMarcadaAlreadyInRespostaException(idResposta, idOpcao);
            }else{
                resposta.getOpcoesMarcadas().add(opcao);
            }
        } else{
            throw new RespostaFullOfOpcaoException(idResposta);
        }

        respostaRepository.save(resposta);

        return resposta.getOpcoesMarcadas().get(resposta.getOpcoesMarcadas().size() - 1);

    }

}
