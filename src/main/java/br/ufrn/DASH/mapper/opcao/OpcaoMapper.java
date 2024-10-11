package br.ufrn.DASH.mapper.opcao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import br.ufrn.DASH.model.Opcao;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OpcaoMapper {

    @Mapping(target = "textoAlternativa")
    @Mapping(target = "ordem")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quesito", ignore = true)
    Opcao toOpcaoFromCreate(OpcaoCreate opcaoCreate);

    @Mapping(target = "textoAlternativa")
    @Mapping(target = "ordem")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quesito", ignore = true)
    Opcao toOpcaoFromUpdate(OpcaoUpdate opcaoUpdate);

    @Mapping(target = "id")
    @Mapping(target = "textoAlternativa")
    @Mapping(target = "ordem")
    @Mapping(target = "quesitoId", source = "quesito.id")
    OpcaoOutput toOpcaoOutput(Opcao opcao);
}
