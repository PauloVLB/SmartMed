package br.ufrn.DASH.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
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

        // TODO: checar se a opcao é do prontuário do diagnóstico
        
        if(diagnostico.getOpcoesMarcadas().contains(opcao)){
            // TODO: exceção para a opção já está no diagnóstico
            // throw new exception(idDiagnostico, idOpcao);
        }

        diagnostico.getOpcoesMarcadas().add(opcao);
        diagnosticoRepository.save(diagnostico);
        return opcao;
    }

    public Opcao removeOpcao(Long idDiagnostico, Long idOpcao) {
        Diagnostico diagnostico = this.getById(idDiagnostico);
        Opcao opcao = opcaoService.getById(idOpcao);
        
        if(!diagnostico.getOpcoesMarcadas().contains(opcao)){
            // TODO: exceção para a opção não está no diagnóstico
            // throw new exception(idDiagnostico, idOpcao);
        }else{
            diagnostico.getOpcoesMarcadas().remove(opcao);
            diagnosticoRepository.save(diagnostico);
        }
        return opcao;
    }
}
