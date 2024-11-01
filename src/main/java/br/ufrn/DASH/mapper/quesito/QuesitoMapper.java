package br.ufrn.DASH.mapper.quesito;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import br.ufrn.DASH.mapper.opcao.OpcaoMapper;
import br.ufrn.DASH.mapper.resposta.RespostaMapper;
import br.ufrn.DASH.model.Quesito;
import static br.ufrn.DASH.model.interfaces.Generics.TToIds;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {OpcaoMapper.class, RespostaMapper.class}
)
public interface QuesitoMapper {

    @Mapping(target = "enunciado")
    @Mapping(target = "obrigatorio")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "tipoResposta")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "opcoes", ignore = true)
    @Mapping(target = "opcoesHabilitadoras", ignore = true) 
    @Mapping(target = "resposta", ignore = true) 
    @Mapping(target = "secao", ignore = true)
    @Mapping(target = "subQuesitos", ignore = true) 
    @Mapping(target = "superQuesito", ignore = true)
    @Mapping(target = "subItens", ignore = true)
    Quesito toQuesitoFromCreate(QuesitoCreate quesitoCreate);

    @Mapping(target = "enunciado")
    @Mapping(target = "obrigatorio")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "tipoResposta")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "opcoes", ignore = true)
    @Mapping(target = "opcoesHabilitadoras", ignore = true) 
    @Mapping(target = "resposta", ignore = true) 
    @Mapping(target = "secao", ignore = true)
    @Mapping(target = "subQuesitos", ignore = true) 
    @Mapping(target = "superQuesito", ignore = true)
    @Mapping(target = "subItens", ignore = true)
    Quesito toQuesitoFromUpdate(QuesitoUpdate quesitoUpdate);

    @Mapping(target = "id")
    @Mapping(target = "enunciado")
    @Mapping(target = "obrigatorio")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "tipoResposta")
    @Mapping(target = "superQuesitoId", source = "superQuesito.id")
    @Mapping(target = "secaoId", source = "secao.id")
    @Mapping(target = "respostaId", source = "resposta.id")
    @Mapping(target = "opcoesHabilitadorasIds", source = "opcoesHabilitadoras", qualifiedByName = "opcoesToIds")
    @Mapping(target = "subQuesitosIds", source = "subQuesitos", qualifiedByName = "subQuesitosToIds")
    @Mapping(target = "opcoesIds", source = "opcoes", qualifiedByName = "opcoesToIds")
    QuesitoOutput toQuesitoOutput(Quesito quesito);

    @Mapping(target = "id")
    @Mapping(target = "enunciado")
    @Mapping(target = "obrigatorio")
    @Mapping(target = "ordem")
    @Mapping(target = "nivel")
    @Mapping(target = "tipoResposta")
    @Mapping(target = "superQuesitoId", source = "superQuesito.id")
    @Mapping(target = "secaoId", source = "secao.id")
    @Mapping(target = "resposta", source = "resposta")
    @Mapping(target = "opcoesHabilitadorasIds", source = "opcoesHabilitadoras", qualifiedByName = "opcoesToIds")
    @Mapping(target = "subQuesitos", source = "subQuesitos")
    @Mapping(target = "opcoes", source = "opcoes")
    QuesitoCompleteOutput toQuesitoCompleteOutput(Quesito quesito);

    @Named("subQuesitosToIds")
    default List<Long> subQuesitosToIds(List<Quesito> opcoes) {
        return TToIds(opcoes);
    }
}
