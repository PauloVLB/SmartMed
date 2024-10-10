package br.ufrn.DASH.mapper.secao;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import br.ufrn.DASH.model.Secao;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SecaoMapper {

    @Mapping(target = "titulo")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subSecoes", ignore = true)
    @Mapping(target = "prontuario", ignore = true)
    @Mapping(target = "quesitos", ignore = true)
    @Mapping(target = "superSecao", ignore = true)
    Secao toSecaoFromCreate(SecaoCreate SecaoCreate);

    @Mapping(target = "titulo")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subSecoes", ignore = true)
    @Mapping(target = "prontuario", ignore = true)
    @Mapping(target = "quesitos", ignore = true)
    @Mapping(target = "superSecao", ignore = true)
    Secao toSecaoFromUpdate(SecaoUpdate SecaoUpdate);

    @Mapping(target = "id")
    @Mapping(target = "titulo")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "subSecoesIds", source = "subSecoes", qualifiedByName = "secoesToIds")
    @Mapping(target = "superSecaoId", source = "superSecao.id")
    @Mapping(target = "prontuarioId", source = "prontuario.id")
    SecaoOutput toSecaoOutput(Secao secao);

    @Named("secoesToIds")
    default List<Long> secoesToIds(List<Secao> secoes) {
        if(secoes == null) {
            return null;
        }
        return secoes.stream().map(Secao::getId).toList();
    }
}
