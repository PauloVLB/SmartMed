package br.ufrn.DASH.mapper.resposta;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import br.ufrn.DASH.model.Resposta;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING)
public interface RespostaMapper {
    @Mapping(target = "conteudo")
    @Mapping(target = "id", ignore = true)
    Resposta toRespostaFromCreate(RespostaCreate respostaCreate);

    @Mapping(target = "conteudo")
    @Mapping(target = "id", ignore = true)
    Resposta toRespostaFromUpdate(RespostaUpdate respostaUpdate);

    @Mapping(target = "conteudo")
    @Mapping(target = "id")
    RespostaOutput toRespostaOutput(Resposta resposta);
}
