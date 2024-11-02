package br.ufrn.DASH.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.exception.OpcaoAlreadyInDiagnosticoException;
import br.ufrn.DASH.exception.OpcaoNotInDiagnosticoExecption;
import br.ufrn.DASH.model.Diagnostico;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.repository.DiagnosticoRepository;

@Service
public class DiagnosticoService {
    
    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Autowired
    @Lazy
    private OpcaoService opcaoService;

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

    public Opcao addOpcao(Long idDiagnostico, Long idOpcao) {
        Diagnostico diagnostico = this.getById(idDiagnostico);
        Opcao opcao = opcaoService.getById(idOpcao);
        
        if(diagnostico.getProntuario() == opcaoService.findProntuario(opcao)){
            throw new OpcaoAlreadyInDiagnosticoException(idDiagnostico, idOpcao);
        }


        if(diagnostico.getOpcoesMarcadas().contains(opcao)){
            throw new OpcaoAlreadyInDiagnosticoException(idDiagnostico, idOpcao);
        }

        diagnostico.getOpcoesMarcadas().add(opcao);
        diagnosticoRepository.save(diagnostico);
        return opcao;
    }

    public Opcao removeOpcao(Long idDiagnostico, Long idOpcao) {
        Diagnostico diagnostico = this.getById(idDiagnostico);
        Opcao opcao = opcaoService.getById(idOpcao);
        
        if(!diagnostico.getOpcoesMarcadas().contains(opcao)){
            throw new OpcaoNotInDiagnosticoExecption(idDiagnostico, idOpcao);
        }else{
            diagnostico.getOpcoesMarcadas().remove(opcao);
            diagnosticoRepository.save(diagnostico);
        }
        return opcao;
    }
}
