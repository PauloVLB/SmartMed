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

import br.ufrn.DASH.mapper.usuario.UsuarioCreate;
import br.ufrn.DASH.mapper.usuario.UsuarioMapper;
import br.ufrn.DASH.mapper.usuario.UsuarioOutput;
import br.ufrn.DASH.mapper.usuario.UsuarioUpdate;
import br.ufrn.DASH.model.Usuario;
import br.ufrn.DASH.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @PostMapping
    public ResponseEntity<UsuarioOutput> create(@RequestBody UsuarioCreate usuarioCreate) {
        Usuario usuario = usuarioMapper.toUsuarioFromCreate(usuarioCreate);
        Usuario usuarioCriado = usuarioService.create(usuario);
        UsuarioOutput usuarioOutput = usuarioMapper.toUsuarioOutput(usuarioCriado);
        return new ResponseEntity<UsuarioOutput>(usuarioOutput, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioOutput> getById(@PathVariable Long id) {
        Usuario usuario = usuarioService.getById(id);
        UsuarioOutput usuarioOutput = usuarioMapper.toUsuarioOutput(usuario);
        return new ResponseEntity<UsuarioOutput>(usuarioOutput, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioOutput>> getAll() {
        List<Usuario> secoes = usuarioService.getAll();
        List<UsuarioOutput> secoesOutput = secoes
                .stream()
                .map(usuarioMapper::toUsuarioOutput)
                .toList();
        return new ResponseEntity<List<UsuarioOutput>>(secoesOutput, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioOutput> update(@PathVariable Long id, @RequestBody UsuarioUpdate usuarioUpdate) {
        Usuario usuario = usuarioMapper.toUsuarioFromUpdate(usuarioUpdate);
        Usuario usuarioAtualizado = usuarioService.update(id, usuario);
        UsuarioOutput usuarioOutput = usuarioMapper.toUsuarioOutput(usuarioAtualizado);
        return new ResponseEntity<UsuarioOutput>(usuarioOutput, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAll() {
        usuarioService.deleteAll();
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
}
