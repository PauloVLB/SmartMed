package br.ufrn.DASH.mapper.prontuario;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import br.ufrn.DASH.model.Diagnostico;
import br.ufrn.DASH.model.Prontuario;
import br.ufrn.DASH.model.Secao;
import static br.ufrn.DASH.model.interfaces.Generics.TToIds;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
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
    @Mapping(target = "diagnosticos", ignore = true)
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
    @Mapping(target = "diagnosticos", ignore = true)
    Prontuario toProntuarioFromUpdate(ProntuarioUpdate prontuarioUpdate);

    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "ehTemplate")
    @Mapping(target = "finalizado")
    @Mapping(target = "id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "secoesIds", source = "secoes", qualifiedByName = "secoesToIds")
    @Mapping(target = "diagnosticosIds", source = "diagnosticos", qualifiedByName = "diagnosticosToIds")
    ProntuarioOutput toProntuarioOutput(Prontuario prontuario);
    
    @Named("secoesToIds")
    default List<Long> secoesToIds(List<Secao> secoes) {
        return TToIds(secoes);
    }

    @Named("diagnosticosToIds")
    default List<Long> diagnosticosToIds(List<Diagnostico> diagnosticos) {
        return TToIds(diagnosticos);
    }
    
}
