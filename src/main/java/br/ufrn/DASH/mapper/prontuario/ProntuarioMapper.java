package br.ufrn.DASH.mapper.prontuario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import br.ufrn.DASH.model.Prontuario;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProntuarioMapper {

    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "finalizado", ignore = true)
    @Mapping(target = "id", ignore = true)
    Prontuario toProntuarioFromCreate(ProntuarioCreate prontuarioCreate);


    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "finalizado")
    @Mapping(target = "id", ignore = true)
    Prontuario toProntuarioFromUpdate(ProntuarioUpdate prontuarioUpdate);

    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "finalizado")
    @Mapping(target = "id")
    ProntuarioOutput toProntuarioOutput(Prontuario prontuario);
    
    
}
