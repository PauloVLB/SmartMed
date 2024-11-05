package br.ufrn.DASH.mapper.opcao;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import br.ufrn.DASH.model.Diagnostico;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Quesito;
import static br.ufrn.DASH.model.interfaces.GenericEntityToId.TToIds;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OpcaoMapper {

    @Mapping(target = "textoAlternativa")
    @Mapping(target = "ordem")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quesito", ignore = true)
    @Mapping(target = "quesitosHabilitados", ignore = true) 
    @Mapping(target = "diagnosticos", ignore = true)
    Opcao toOpcaoFromCreate(OpcaoCreate opcaoCreate);

    @Mapping(target = "textoAlternativa")
    @Mapping(target = "ordem")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quesito", ignore = true)
    @Mapping(target = "quesitosHabilitados", ignore = true)
    @Mapping(target = "diagnosticos", ignore = true)
    Opcao toOpcaoFromUpdate(OpcaoUpdate opcaoUpdate);

    @Mapping(target = "id")
    @Mapping(target = "textoAlternativa")
    @Mapping(target = "ordem")
    @Mapping(target = "quesitoId", source = "quesito.id")
    @Mapping(target = "diagnosticosIds", source = "diagnosticos", qualifiedByName = "diagnosticosToIds")
    @Mapping(target = "quesitosHabilitadosIds", source = "quesitosHabilitados", qualifiedByName = "quesitosToIds")
    OpcaoOutput toOpcaoOutput(Opcao opcao);


    @Mapping(target = "id")
    @Mapping(target = "textoAlternativa")
    @Mapping(target = "ordem")
    @Mapping(target = "quesitoId", source = "quesito.id")
    OpcaoCompleteOutput toOpcaoCompleteOutput(Opcao opcao);

    @Named("quesitosToIds")
    default List<Long> quesitosToIds(List<Quesito> quesitos) {
        return TToIds(quesitos);
    }

    @Named("diagnosticosToIds")
    default List<Long> diagnosticosToIds(List<Diagnostico> diagnosticos) {
        return TToIds(diagnosticos);
    }
}
