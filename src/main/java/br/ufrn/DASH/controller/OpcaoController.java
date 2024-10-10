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

import br.ufrn.DASH.mapper.opcao.OpcaoCreate;
import br.ufrn.DASH.mapper.opcao.OpcaoMapper;
import br.ufrn.DASH.mapper.opcao.OpcaoOutput;
import br.ufrn.DASH.mapper.opcao.OpcaoUpdate;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.service.OpcaoService;

@RestController
@RequestMapping("/opcao")
public class OpcaoController {

    @Autowired
    private OpcaoService opcaoService;

    @Autowired
    private OpcaoMapper opcaoMapper;

    @PostMapping
    public ResponseEntity<OpcaoOutput> create(@RequestBody OpcaoCreate opcaoCreate) {
        Opcao opcao = opcaoMapper.toOpcaoFromCreate(opcaoCreate);
        Opcao opcaoCriado = opcaoService.create(opcao);
        OpcaoOutput opcaoOutput = opcaoMapper.toOpcaoOutput(opcaoCriado);
        return new ResponseEntity<OpcaoOutput>(opcaoOutput, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpcaoOutput> getById(@PathVariable Long id) {
        Opcao opcao = opcaoService.getById(id);
        OpcaoOutput opcaoOutput = opcaoMapper.toOpcaoOutput(opcao);
        return new ResponseEntity<OpcaoOutput>(opcaoOutput, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OpcaoOutput>> getAll() {
        List<Opcao> secoes = opcaoService.getAll();
        List<OpcaoOutput> secoesOutput = secoes
                .stream()
                .map(opcaoMapper::toOpcaoOutput)
                .toList();
        return new ResponseEntity<List<OpcaoOutput>>(secoesOutput, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpcaoOutput> update(@PathVariable Long id, @RequestBody OpcaoUpdate opcaoUpdate) {
        Opcao opcao = opcaoMapper.toOpcaoFromUpdate(opcaoUpdate);
        Opcao opcaoAtualizado = opcaoService.update(id, opcao);
        OpcaoOutput opcaoOutput = opcaoMapper.toOpcaoOutput(opcaoAtualizado);
        return new ResponseEntity<OpcaoOutput>(opcaoOutput, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        opcaoService.delete(id);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAll() {
        opcaoService.deleteAll();
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
}
