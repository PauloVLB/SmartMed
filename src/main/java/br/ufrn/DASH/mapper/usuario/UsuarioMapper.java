package br.ufrn.DASH.mapper.usuario;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import br.ufrn.DASH.model.Prontuario;
import br.ufrn.DASH.model.Usuario;
import static br.ufrn.DASH.model.interfaces.Generics.TToIds;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper{

    @Mapping(target = "nome")
    @Mapping(target = "login")
    @Mapping(target = "senha")
    @Mapping(target = "tipoUsuario")    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prontuarios", ignore = true)
    Usuario toUsuarioFromCreate(UsuarioCreate usuarioCreate);

    @Mapping(target = "nome")
    @Mapping(target = "login")
    @Mapping(target = "senha")
    @Mapping(target = "tipoUsuario")    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prontuarios", ignore = true)
    Usuario toUsuarioFromUpdate(UsuarioUpdate usuarioUpdate);
    
    @Mapping(target = "id")
    @Mapping(target = "nome")
    @Mapping(target = "login")
    @Mapping(target = "senha")
    @Mapping(target = "tipoUsuario")    
    @Mapping(target = "prontuariosIds", source = "prontuarios", qualifiedByName = "prontuariosToIds")
    UsuarioOutput toUsuarioOutput(Usuario usuario);

    @Named("prontuariosToIds")
    default List<Long> prontuariosToIds(List<Prontuario> secoes) {
        return TToIds(secoes);
    }
}
