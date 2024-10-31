package br.ufrn.DASH.mapper.prontuario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import br.ufrn.DASH.mapper.secao.SecaoMapper;
import br.ufrn.DASH.model.Prontuario;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {SecaoMapper.class}
)
public interface ProntuarioMapper {

    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "ehTemplate")
    @Mapping(target = "finalizado", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "secoes", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "diagnosticoLLM", ignore = true)
    @Mapping(target = "duplicar", ignore = true)
    Prontuario toProntuarioFromCreate(ProntuarioCreate prontuarioCreate);


    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "finalizado")
    @Mapping(target = "ehTemplate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "secoes", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "diagnosticoLLM", ignore = true)
    @Mapping(target = "duplicar", ignore = true)
    Prontuario toProntuarioFromUpdate(ProntuarioUpdate prontuarioUpdate);

    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "ehTemplate")
    @Mapping(target = "finalizado")
    @Mapping(target = "id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "secoesIds", source = "secoes", qualifiedByName = "secoesToIds")
    ProntuarioOutput toProntuarioOutput(Prontuario prontuario);

    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "ehTemplate")
    @Mapping(target = "finalizado")
    @Mapping(target = "id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "secoes", source = "secoes")
    ProntuarioCompleteOutput toProntuarioCompleteOutput(Prontuario prontuario);
    
}
