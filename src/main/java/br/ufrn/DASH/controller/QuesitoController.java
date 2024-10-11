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
import br.ufrn.DASH.mapper.quesito.QuesitoCreate;
import br.ufrn.DASH.mapper.quesito.QuesitoMapper;
import br.ufrn.DASH.mapper.quesito.QuesitoOutput;
import br.ufrn.DASH.mapper.quesito.QuesitoUpdate;
import br.ufrn.DASH.mapper.secao.SecaoOutput;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Secao;
import br.ufrn.DASH.service.QuesitoService;

@RestController
@RequestMapping("/quesito")
public class QuesitoController {

    @Autowired
    private QuesitoService quesitoService;

    @Autowired
    private QuesitoMapper quesitoMapper;

    @Autowired
    private OpcaoMapper opcaoMapper;

    @PostMapping
    public ResponseEntity<QuesitoOutput> create(@RequestBody QuesitoCreate quesitoCreate) {
        Quesito quesito = quesitoMapper.toQuesitoFromCreate(quesitoCreate);
        Quesito quesitoCriado = quesitoService.create(quesito);
        QuesitoOutput quesitoOutput = quesitoMapper.toQuesitoOutput(quesitoCriado);
        return new ResponseEntity<QuesitoOutput>(quesitoOutput, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuesitoOutput> getById(@PathVariable Long id) {
        Quesito quesito = quesitoService.getById(id);
        QuesitoOutput quesitoOutput = quesitoMapper.toQuesitoOutput(quesito);
        return new ResponseEntity<QuesitoOutput>(quesitoOutput, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<QuesitoOutput>> getAll() {
        List<Quesito> secoes = quesitoService.getAll();
        List<QuesitoOutput> secoesOutput = secoes
                .stream()
                .map(quesitoMapper::toQuesitoOutput)
                .toList();
        return new ResponseEntity<List<QuesitoOutput>>(secoesOutput, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuesitoOutput> update(@PathVariable Long id, @RequestBody QuesitoUpdate quesitoUpdate) {
        Quesito quesito = quesitoMapper.toQuesitoFromUpdate(quesitoUpdate);
        Quesito quesitoAtualizado = quesitoService.update(id, quesito);
        QuesitoOutput quesitoOutput = quesitoMapper.toQuesitoOutput(quesitoAtualizado);
        return new ResponseEntity<QuesitoOutput>(quesitoOutput, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        quesitoService.delete(id);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAll() {
        quesitoService.deleteAll();
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @PostMapping("/{idQuesito}/addSubQuesito")
    public ResponseEntity<QuesitoOutput> addSubQuesito(@PathVariable Long idQuesito,@RequestBody QuesitoCreate quesitoCreate){
        Quesito quesitoNovo = quesitoMapper.toQuesitoFromCreate(quesitoCreate);
        Quesito quesitoCriado = quesitoService.addSubQuesito(idQuesito, quesitoNovo);
        QuesitoOutput quesitoOutput = quesitoMapper.toQuesitoOutput(quesitoCriado);
        return new ResponseEntity<QuesitoOutput>(quesitoOutput, HttpStatus.CREATED);
    }

    @PostMapping("/{idQuesito}/addOpcao")
    public ResponseEntity<OpcaoOutput> addOpcao(@PathVariable Long idQuesito, @RequestBody OpcaoCreate opcaoCreate) {
        Opcao opcaoNovo = opcaoMapper.toOpcaoFromCreate(opcaoCreate);
        Opcao opcaoCriado = quesitoService.addOpcao(idQuesito, opcaoNovo);
        OpcaoOutput opcaoOutput = opcaoMapper.toOpcaoOutput(opcaoCriado);
        return new ResponseEntity<OpcaoOutput>(opcaoOutput, HttpStatus.CREATED);
    }
}
