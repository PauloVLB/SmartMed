package br.ufrn.DASH.mapper.resposta;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Resposta;
import static br.ufrn.DASH.model.interfaces.Generics.TToIds;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING)
public interface RespostaMapper {
    @Mapping(target = "conteudo")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "opcoesMarcadas", ignore = true)
    @Mapping(target = "quesito", ignore = true)
    Resposta toRespostaFromCreate(RespostaCreate respostaCreate);

    @Mapping(target = "conteudo")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "opcoesMarcadas", ignore = true)
    @Mapping(target = "quesito", ignore = true)
    Resposta toRespostaFromUpdate(RespostaUpdate respostaUpdate);

    @Mapping(target = "conteudo")
    @Mapping(target = "id")
    @Mapping(target = "opcoesMarcadasIds", source = "opcoesMarcadas", qualifiedByName = "opcoesToIds")
    @Mapping(target = "idQuesito", source = "quesito", qualifiedByName="quesitoToId")
    RespostaOutput toRespostaOutput(Resposta resposta);

    @Named("opcoesToIds")
    default List<Long> opcoesToIds(List<Opcao> opcoes) {
        return TToIds(opcoes);
    }

    @Named("quesitoToId")
    default Long quesitoToId(Quesito quesito) {
        if(quesito == null){
            return null;
        }
        return quesito.getId();
    }
}
