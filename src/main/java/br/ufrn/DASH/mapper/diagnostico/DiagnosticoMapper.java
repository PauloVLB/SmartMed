package br.ufrn.DASH.mapper.diagnostico;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import br.ufrn.DASH.model.Diagnostico;
import br.ufrn.DASH.model.Opcao;
import static br.ufrn.DASH.model.interfaces.Generics.TToIds;

@Mapper(componentModel  =  MappingConstants.ComponentModel.SPRING)
public interface DiagnosticoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "opcoesMarcadas", ignore = true)
    @Mapping(target = "prontuario", ignore = true)
    @Mapping(target = "descricao")
    Diagnostico toDiagnosticoFromCreate(DiagnosticoCreate diagnosticoCreate);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "opcoesMarcadas", ignore = true)
    @Mapping(target = "prontuario", ignore = true)
    @Mapping(target = "descricao")
    Diagnostico toDiagnosticoFromUpdate(DiagnosticoUpdate diagnosticoUpdate);
    
    @Mapping(target = "id")
    @Mapping(target = "descricao")
    @Mapping(target = "prontuarioId", source = "prontuario.id")
    @Mapping(target = "opcoesMarcadasIds", source = "opcoesMarcadas", qualifiedByName = "opcoesToIds")
    DiagnosticoOutput toDiagnosticoOutput(Diagnostico diagnostico);

    @Named("opcoesToIds")
    default List<Long> quesitosToIds(List<Opcao> opcoes) {
        return TToIds(opcoes);
    }
}
