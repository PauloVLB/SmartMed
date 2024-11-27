package br.ufrn.DASH.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.model.Usuario;
import br.ufrn.DASH.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario create(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public Usuario getById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, new Usuario())
            );
    }

    @Transactional
    public Usuario update(Long id, Usuario usuario) {
        Usuario usuarioExistente = this.getById(id);
        
        usuarioExistente.setLogin(usuario.getLogin());
        usuarioExistente.setSenha(usuario.getSenha());
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setTipoUsuario(usuario.getTipoUsuario());

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public void delete(Long id) {
        this.getById(id);
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        usuarioRepository.deleteAll();
    }
}
