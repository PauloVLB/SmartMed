package br.ufrn.DASH.mapper.secao;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import br.ufrn.DASH.mapper.quesito.QuesitoMapper;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Secao;
import static br.ufrn.DASH.model.interfaces.GenericEntityToId.TToIds;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {QuesitoMapper.class}    
)
public interface SecaoMapper {

    @Mapping(target = "titulo")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subSecoes", ignore = true)
    @Mapping(target = "prontuario", ignore = true)
    @Mapping(target = "quesitos", ignore = true)
    @Mapping(target = "superSecao", ignore = true)
    @Mapping(target = "subItens", ignore = true)
    Secao toSecaoFromCreate(SecaoCreate secaoCreate);

    @Mapping(target = "titulo")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subSecoes", ignore = true)
    @Mapping(target = "prontuario", ignore = true)
    @Mapping(target = "quesitos", ignore = true)
    @Mapping(target = "superSecao", ignore = true)
    @Mapping(target = "subItens", ignore = true)
    Secao toSecaoFromUpdate(SecaoUpdate secaoUpdate);

    @Mapping(target = "id")
    @Mapping(target = "titulo")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "subSecoesIds", source = "subSecoes", qualifiedByName = "secoesToIds")
    @Mapping(target = "superSecaoId", source = "superSecao.id")
    @Mapping(target = "prontuarioId", source = "prontuario.id")
    @Mapping(target = "quesitosIds", source = "quesitos", qualifiedByName = "quesitosToIds")
    SecaoOutput toSecaoOutput(Secao secao);

    @Named("secoesToIds")
    default List<Long> secoesToIds(List<Secao> secoes) {
        return TToIds(secoes);
    }

    @Named("quesitosToIds")
    default List<Long> quesitosToIds(List<Quesito> quesitos) {        
        return TToIds(quesitos);
    }
}
