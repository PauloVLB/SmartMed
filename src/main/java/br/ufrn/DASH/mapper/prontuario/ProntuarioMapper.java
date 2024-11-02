package br.ufrn.DASH.mapper.prontuario;

import static br.ufrn.DASH.model.interfaces.Generics.TToIds;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import br.ufrn.DASH.mapper.opcao.OpcaoCompleteOutput;
import br.ufrn.DASH.mapper.quesito.QuesitoCompleteOutput;
import br.ufrn.DASH.mapper.resposta.RespostaCompleteOutput;
import br.ufrn.DASH.mapper.secao.SecaoCompleteOutput;
import br.ufrn.DASH.mapper.secao.SecaoMapper;
import br.ufrn.DASH.mapper.subItem.ItemOutput;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Prontuario;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Resposta;
import br.ufrn.DASH.model.Secao;
import br.ufrn.DASH.model.interfaces.Item;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {SecaoMapper.class}
)
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
    Prontuario toProntuarioFromUpdate(ProntuarioUpdate prontuarioUpdate);

    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "ehTemplate")
    @Mapping(target = "finalizado")
    @Mapping(target = "id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "secoesIds", source = "secoes", qualifiedByName = "secoesToIds")
    ProntuarioOutput toProntuarioOutput(Prontuario prontuario);

    @Mapping(target = "nome")
    @Mapping(target = "descricao")
    @Mapping(target = "ehPublico")
    @Mapping(target = "ehTemplate")
    @Mapping(target = "finalizado")
    @Mapping(target = "id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "secoes", source = "secoes")
    ProntuarioCompleteOutput toProntuarioCompleteOutput(Prontuario prontuario);
    
    default List<SecaoCompleteOutput> toSecoesCompleteOutput(List<Secao> secoes) {
        List<SecaoCompleteOutput> secoesCompleteOutput = new ArrayList<>();

        int ordem = 1;
        for (Secao secao : secoes) {
            secoesCompleteOutput.add(toSecaoCompleteOutput(secao, ordem + "."));
            ordem++;
        }

        return secoesCompleteOutput;
    }

    default SecaoCompleteOutput toSecaoCompleteOutput(Secao secao, String numeracao) {
        List<ItemOutput> subItensOutput = new ArrayList<>();

        List<Item> subItens = secao.getSubItens();
        int ordem = 1;
        for (Item subItem : subItens) {
            if (subItem instanceof Secao) {
                subItensOutput.add(toSecaoCompleteOutput((Secao) subItem, numeracao + ordem + "."));
            } else {
                subItensOutput.add(toQuesitoCompleteOutput((Quesito) subItem, numeracao + ordem + "."));
            }
            ordem++;
        }


        return new SecaoCompleteOutput(
            secao.getId(),
            "secao",
            numeracao,
            secao.getTitulo(),
            secao.getOrdem(),
            secao.getNivel(),
            subItensOutput,
            secao.getSuperSecao() != null ? secao.getSuperSecao().getId() : null,
            secao.getProntuario() != null ? secao.getProntuario().getId() : null
        );
    }

    default QuesitoCompleteOutput toQuesitoCompleteOutput(Quesito quesito, String numeracao) {
        List<QuesitoCompleteOutput> subQuesitosOutput = new ArrayList<>();

        int ordem = 1;
        for (Quesito subQuesito : quesito.getSubQuesitos()) {
            subQuesitosOutput.add(toQuesitoCompleteOutput(subQuesito, numeracao + ordem + "."));
            ordem++;
        }

        return new QuesitoCompleteOutput(
            quesito.getId(),
            "quesito",
            numeracao,
            quesito.getEnunciado(),
            quesito.getObrigatorio(),
            quesito.getOrdem(),
            quesito.getNivel(),
            quesito.getTipoResposta(),
            quesito.getSuperQuesito() != null ? quesito.getSuperQuesito().getId() : null,
            quesito.getSecao() != null ? quesito.getSecao().getId() : null,
            toRespostaCompleteOutput(quesito.getResposta()),
            opcoesToIds(quesito.getOpcoesHabilitadoras()),
            subQuesitosOutput,
            quesito.getOpcoes().stream().map(this::toOpcaoCompleteOutput).toList()
        );
    }

    @Mapping(target = "id")
    @Mapping(target = "conteudo")
    @Mapping(target = "opcoesMarcadas", source = "opcoesMarcadas")
    @Mapping(target = "idQuesito", source = "quesito.id")
    RespostaCompleteOutput toRespostaCompleteOutput(Resposta resposta);

    @Mapping(target = "id")
    @Mapping(target = "textoAlternativa")
    @Mapping(target = "ordem")
    @Mapping(target = "quesitoId", source = "quesito.id")
    @Mapping(target = "quesitosHabilitadosIds", source = "quesitosHabilitados", qualifiedByName = "quesitosToIds")
    OpcaoCompleteOutput toOpcaoCompleteOutput(Opcao opcao);

    default List<Long> opcoesToIds(List<Opcao> opcoes) {
        return TToIds(opcoes);
    }
}
