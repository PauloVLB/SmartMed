package br.ufrn.DASH.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.DASH.mapper.diagnostico.DiagnosticoCreate;
import br.ufrn.DASH.mapper.diagnostico.DiagnosticoMapper;
import br.ufrn.DASH.mapper.diagnostico.DiagnosticoOutput;
import br.ufrn.DASH.mapper.diagnostico.DiagnosticoUpdate;
import br.ufrn.DASH.mapper.opcao.OpcaoMapper;
import br.ufrn.DASH.mapper.opcao.OpcaoOutput;
import br.ufrn.DASH.model.Diagnostico;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.service.DiagnosticoService;

@RestController
@RequestMapping("/diagnostico")
public class DiagnosticoController {
    @Autowired
    private DiagnosticoService diagnosticoService;

    @Autowired
    private DiagnosticoMapper diagnosticoMapper;

    @Autowired
    private OpcaoMapper opcaoMapper;

    @PostMapping
    public ResponseEntity<DiagnosticoOutput> create(@RequestBody DiagnosticoCreate diagnosticoCreate) {
        Diagnostico diagnostico = diagnosticoMapper.toDiagnosticoFromCreate(diagnosticoCreate);
        Diagnostico diagnosticoCriado = diagnosticoService.create(diagnostico);
        DiagnosticoOutput diagnosticoOutput = diagnosticoMapper.toDiagnosticoOutput(diagnosticoCriado);
        return new ResponseEntity<DiagnosticoOutput>(diagnosticoOutput, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoOutput> getById(@PathVariable Long id) {
        Diagnostico diagnostico = diagnosticoService.getById(id);
        DiagnosticoOutput diagnosticoOutput = diagnosticoMapper.toDiagnosticoOutput(diagnostico);
        return new ResponseEntity<DiagnosticoOutput>(diagnosticoOutput, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DiagnosticoOutput>> getAll() {
        List<Diagnostico> secoes = diagnosticoService.getAll();
        List<DiagnosticoOutput> secoesOutput = secoes
                .stream()
                .map(diagnosticoMapper::toDiagnosticoOutput)
                .toList();
        return new ResponseEntity<List<DiagnosticoOutput>>(secoesOutput, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticoOutput> update(@PathVariable Long id, @RequestBody DiagnosticoUpdate diagnosticoUpdate) {
        Diagnostico diagnostico = diagnosticoMapper.toDiagnosticoFromUpdate(diagnosticoUpdate);
        Diagnostico diagnosticoAtualizado = diagnosticoService.update(id, diagnostico);
        DiagnosticoOutput diagnosticoOutput = diagnosticoMapper.toDiagnosticoOutput(diagnosticoAtualizado);
        return new ResponseEntity<DiagnosticoOutput>(diagnosticoOutput, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        diagnosticoService.delete(id);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAll() {
        diagnosticoService.deleteAll();
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @PostMapping("/{idDiagnostico}/opcao/{idOpcao}/addOpcao")
    public ResponseEntity<OpcaoOutput> addOpcao(@PathVariable Long idDiagnostico, @PathVariable Long idOpcao) {
        Opcao opcaoCriado = diagnosticoService.addOpcao(idDiagnostico, idOpcao);
        OpcaoOutput opcaoOutput = opcaoMapper.toOpcaoOutput(opcaoCriado);
        return new ResponseEntity<OpcaoOutput>(opcaoOutput, HttpStatus.OK);
    }

    @PostMapping("/{idDiagnostico}/opcao/{idOpcao}/removeOpcao")
    public ResponseEntity<OpcaoOutput> removeOpcao(@PathVariable Long idDiagnostico, @PathVariable Long idOpcao) {
        Opcao opcaoCriado = diagnosticoService.removeOpcao(idDiagnostico, idOpcao);
        OpcaoOutput opcaoOutput = opcaoMapper.toOpcaoOutput(opcaoCriado);
        return new ResponseEntity<OpcaoOutput>(opcaoOutput, HttpStatus.OK);
    }

}
