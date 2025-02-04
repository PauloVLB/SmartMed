package br.ufrn.DASH.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.DiagnosticoNotInProntuarioException;
import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.exception.ProntuarioInconsistenteException;
import br.ufrn.DASH.exception.ProntuarioNotTemplateException;
import br.ufrn.DASH.exception.ProntuarioTemplateException;
import br.ufrn.DASH.exception.QuesitoNotInProntuarioException;
import br.ufrn.DASH.mapper.llm.LLMResponse;
import br.ufrn.DASH.model.Diagnostico;
import br.ufrn.DASH.model.Opcao;
import br.ufrn.DASH.model.Prontuario;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Resposta;
import br.ufrn.DASH.model.Secao;
import br.ufrn.DASH.model.Usuario;
import br.ufrn.DASH.model.interfaces.Item;

import static br.ufrn.DASH.model.interfaces.GenericEntitySortById.sortById;
import br.ufrn.DASH.repository.ProntuarioRepository;
import br.ufrn.DASH.utils.Pair;
import jakarta.transaction.Transactional;

@Service
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RespostaService respostaService;

    @Autowired
    private QuesitoService quesitoService;

    @Autowired
    private SecaoService secaoService;

    @Autowired
    private OpcaoService opcaoService;

    @Autowired
    private LLMService llmService;

    @Autowired
    private DiagnosticoService diagnosticoService;

    @Transactional
    public Prontuario create(Prontuario prontuario) {
        return prontuarioRepository.save(prontuario);
    }

    public List<Prontuario> getAll() {
        return prontuarioRepository.findAll();
    }

    public Prontuario getById(Long id) {
        return prontuarioRepository.findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException(id, new Prontuario())
            );
    }

    public Prontuario getById(Long id, boolean incluirDesabilitados) {
        Prontuario prontuario = this.getById(id);

        if(incluirDesabilitados) {
           return prontuario;
        } else {
            Prontuario prontuarioSemDesabilitados = prontuario;
            List<Secao> secoesSemDesabilitados = retirarDesabilitadosSecoes(prontuario.getSecoes());
            prontuarioSemDesabilitados.setSecoes(secoesSemDesabilitados);
            return prontuarioSemDesabilitados;
        }
    }

    private List<Secao> retirarDesabilitadosSecoes(List<Secao> secoes) {
        List<Secao> secoesSemDesabilitados = new ArrayList<>();
        
        for(Secao secao : secoes) {
            secao.setSubSecoes(retirarDesabilitadosSecoes(secao.getSubSecoes()));
            secao.setQuesitos(retirarDesabilitadosQuesitos(secao.getQuesitos()));
            secoesSemDesabilitados.add(secao);
        }

        return secoesSemDesabilitados;
    }

    private List<Quesito> retirarDesabilitadosQuesitos(List<Quesito> quesitos) {
        List<Quesito> quesitosSemDesabilitados = new ArrayList<>();

        for(Quesito quesito : quesitos) {
            if(quesitoService.estaHabilitado(quesito)) {
                quesitosSemDesabilitados.add(quesito);
            }
            quesito.setSubQuesitos(retirarDesabilitadosQuesitos(quesito.getSubQuesitos()));
        }

        return quesitosSemDesabilitados;
    }

    @Transactional
    public Prontuario update(Long id, Prontuario prontuario) {
        
        Prontuario prontuarioExistente = this.getById(id);
        
        prontuarioExistente.setNome(prontuario.getNome());
        prontuarioExistente.setDescricao(prontuario.getDescricao());
        prontuarioExistente.setEhPublico(prontuario.getEhPublico());
        prontuarioExistente.setFinalizado(prontuario.getFinalizado());

        return prontuarioRepository.save(prontuarioExistente);
    }

    @Transactional
    public Prontuario tornarPublico(Long id) {
        Prontuario prontuario = this.getById(id);
        prontuario.setEhPublico(true);
        return prontuarioRepository.save(prontuario);
    }

    @Transactional
    public Prontuario tornarPrivado(Long id) {
        Prontuario prontuario = this.getById(id);
        prontuario.setEhPublico(false);
        return prontuarioRepository.save(prontuario);
    }

    @Transactional
    public void delete(Long id) {
        this.getById(id);
        prontuarioRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        prontuarioRepository.deleteAll();
    }

    @Transactional
    public Secao addSecao(Long idProntuario, Secao secaoNova) {
        Prontuario prontuario = this.getById(idProntuario);
        
        secaoNova.setOrdem(prontuario.getSecoes().size());
        secaoNova.setNivel(1);
        secaoNova.setProntuario(prontuario);

        prontuario.getSecoes().add(secaoNova);

        prontuarioRepository.save(prontuario);

        return prontuario.getSecoes().get(prontuario.getSecoes().size() - 1);
    }

    @Transactional
    public Prontuario duplicar(Long idProntuario, Long idUsuario) {
        Prontuario prontuarioToDuplicate = this.getById(idProntuario);

        Usuario novoUsuario = usuarioService.getById(idUsuario);

        Prontuario prontuarioDuplicado = new Prontuario();

        prontuarioDuplicado.setNome(prontuarioToDuplicate.getNome() + " - Cópia");
        prontuarioDuplicado.setDescricao(prontuarioToDuplicate.getDescricao());
        prontuarioDuplicado.setEhPublico(prontuarioToDuplicate.getEhPublico());
        prontuarioDuplicado.setEhTemplate(prontuarioToDuplicate.getEhTemplate());
        prontuarioDuplicado.setUsuario(novoUsuario);

        Map<Opcao, Opcao> opcoesDuplicadas = new HashMap<Opcao, Opcao>();
        for (Secao secao : prontuarioToDuplicate.getSecoes()) {
            Pair<Secao, Map<Opcao, Opcao>> pairSecaoMapa = secaoService.duplicar(opcoesDuplicadas, secao);
            Secao novaSecao = pairSecaoMapa.getFirst();
            opcoesDuplicadas = pairSecaoMapa.getSecond();
            
            novaSecao.setProntuario(prontuarioDuplicado);
            prontuarioDuplicado.getSecoes().add(novaSecao);
        }

        return prontuarioRepository.save(prontuarioDuplicado);
    }
    
    @Transactional
    public Resposta addResposta(Long idProntuario, Long idQuesito, Resposta respostaNova) {
        Prontuario prontuario = this.getById(idProntuario);
        if(prontuario.getEhTemplate()) {
            throw new ProntuarioTemplateException(idProntuario);
        }
        Quesito quesito = quesitoService.getById(idQuesito);
        
        Prontuario prontuarioDoQuesito = quesito.getProntuario();
        if(prontuarioDoQuesito == null || !prontuarioDoQuesito.getId().equals(idProntuario)) {
            throw new QuesitoNotInProntuarioException(idProntuario, idQuesito);
        }
        Resposta respostaCriada;
        if(quesito.getResposta() == null){
            respostaCriada = respostaService.create(respostaNova);
            quesito.setResposta(respostaCriada);
            respostaCriada.setQuesito(quesito);            
        }else{
            respostaCriada = respostaService.getById(quesito.getResposta().getId());

            respostaCriada.setConteudo(respostaNova.getConteudo());
        }
        
        // talvez mudar isso para sintaxe melhor
        quesitoService.create(quesito);
        return respostaService.create(respostaCriada);
    }

    @Transactional
    public Prontuario addProntuarioFromTemplate(Long idTemplate) {
        Prontuario prontuarioTemplate = this.getById(idTemplate);
        if(!prontuarioTemplate.getEhTemplate()) throw new ProntuarioNotTemplateException(idTemplate);
        Prontuario prontuarioCriado = this.duplicar(prontuarioTemplate.getId(), null);
        prontuarioCriado.setEhTemplate(false);
        return prontuarioRepository.save(prontuarioCriado);
    }

    @Transactional
    public Map<String, String> getDiagnosticoLLM(Long idProntuario) {
        String prompt = 
        "Com base no seguinte JSON, que corresponde a um prontuário de um paciente, faça um diagnóstico do paciente. " + 
        "Você não precisa se ater a divisão de seções e quesitos, apenas faça um diagnóstico geral do paciente. " +
        "Seu diagnóstico será avaliado por um médico especialista, que pode ou não concordar com o diagnóstico gerado. " +
        "Portanto, pode dar sugestões de exames, tratamentos, ou qualquer outra informação que julgar relevante. " +
        "Pode assumir que o paciente é real, e que você está fazendo um diagnóstico real.\n" + 
        "Sua mensagem será mostrada ao usuário, deve ser transparente para ele que você está lendo as informações " +
        "de um JSON. Para ele deve ser apenas uma sugestão de diagnóstico.\n" +
        "Além disso, escreva sua resposta como plain text. Não use formatação, nem imagens.\n\n";

        Prontuario prontuario = this.getById(idProntuario);
        prompt += toJson(prontuario);

        Map<String, String> respostas = new HashMap<>();
        LLMResponse response = llmService.getRespostaFromPrompt(prompt);
        respostas.put("content", response.choices().get(0).message().content());

        prontuario.setDiagnosticoLLM(respostas.get("content"));
        this.update(idProntuario, prontuario);
        
        return respostas;
    }

    private String toJson(Prontuario prontuario) {
        StringBuilder json = new StringBuilder("{\n");

        json.append("\t\"nome\": \"").append(prontuario.getNome()).append("\",\n");
        json.append("\t\"descricao\": \"").append(prontuario.getDescricao()).append("\",\n");
        json.append("\t\"secoes\": [\n");

        for (Secao secao : prontuario.getSecoes()) {
            json.append("\t\t{\n");
            json.append("\t\t\t\"nome\": \"").append(secao.getTitulo()).append("\",\n");
            json.append("\t\t\t\"quesitos\": [\n");

            for (Quesito quesito : secao.getQuesitos()) {
                json.append("\t\t\t\t{\n");
                json.append("\t\t\t\t\t\"nome\": \"").append(quesito.getEnunciado()).append("\",\n");
                if(quesito.getResposta() != null) {
                    json.append("\t\t\t\t\t\"resposta\": \"").append(quesito.getResposta().getConteudo()).append("\"\n");
                } else {
                    json.append("\t\t\t\t\t\"resposta\": \"\"\n");
                }
                json.append("\t\t\t\t},\n");
            }

            json.append("\t\t\t]\n");
            json.append("\t\t},\n");
        }

        json.append("\t]\n");
        json.append("}");

        return json.toString();
    }

    @Transactional
    public Diagnostico addDiagnostico(Long idProntuario, Diagnostico diagnostico) {
        Prontuario prontuario = this.getById(idProntuario);
        diagnostico.setProntuario(prontuario);
        
        diagnostico = diagnosticoService.create(diagnostico);

        prontuario.getDiagnosticos().add(diagnostico);

        this.create(prontuario);
        return diagnostico;
    }

    @Transactional
    public void removeDiagnostico(Long idProntuario, Long idDiagnostico) {
        Prontuario prontuario = this.getById(idProntuario);
        Diagnostico diagnostico = diagnosticoService.getById(idDiagnostico);
        
        if(prontuario.getDiagnosticos().contains(diagnostico)){
            prontuario.getDiagnosticos().remove(diagnostico);
            this.create(prontuario);
            diagnosticoService.delete(idDiagnostico);
        }else{
            throw new DiagnosticoNotInProntuarioException(idProntuario, idDiagnostico);
        }
    }

    public Diagnostico getDiagnostico(Long idProntuario) {
        Prontuario prontuario = this.getById(idProntuario);
        List<Opcao> opcoesMarcadas = this.getOpcoesMarcadas(prontuario);

        int qntDiagnosticos = 0;
        Diagnostico diagnosticoToReturn = Diagnostico.inconclusivo();
        for (Diagnostico diagnostico : prontuario.getDiagnosticos()) {
            if(ehSubsequencia(diagnostico.getOpcoesMarcadas(), opcoesMarcadas)) {
                qntDiagnosticos++;
                diagnosticoToReturn = diagnostico;
            }
        }
        
        if(qntDiagnosticos > 1) {
            diagnosticoToReturn = Diagnostico.inconclusivo();
        } 

        return diagnosticoToReturn;
    }
    
    private List<Opcao> getOpcoesMarcadas(Prontuario prontuario) {
        List<Opcao> retorno = new ArrayList<>();
        
        for (Secao secao : prontuario.getSecoes()) {
            retorno.addAll(secaoService.getOpcoesMarcadas(secao));
        }
        
        if(!retorno.isEmpty())
            sortById(retorno);
        
        return retorno;
    }
    
    private boolean ehSubsequencia(List<Opcao> opcoesDiagnostico, List<Opcao> opcoesResposta) {
        int slow = 0;
        int fast = 0;
        int sizeFast = opcoesResposta.size();
        int sizeSlow = opcoesDiagnostico.size();

        while (fast < sizeFast && slow < sizeSlow) {
            if(opcoesDiagnostico.get(slow).getId().compareTo(opcoesResposta.get(fast).getId()) < 0){
                return false;
            }
            if(opcoesDiagnostico.get(slow).getId().equals(opcoesResposta.get(fast).getId())){
                slow++;
            }
            fast++;
        }

        return true;
    }

    @Transactional
    public Prontuario finalizarProntuario(Long idProntuario) {
        
        Prontuario prontuario = this.getById(idProntuario);       
        Queue<Secao> filaSecoes = new LinkedList<>(prontuario.getSecoes());
        List<Secao> listaTodasSecoes = new ArrayList<>();
        Queue<Quesito> filaQuesitos = new LinkedList<>();
        List<Quesito> listaTodosQuesitos = new ArrayList<>();
        
        // Percorre as Secoes
        while (!filaSecoes.isEmpty()) {
            Secao secao = filaSecoes.poll();
            listaTodasSecoes.add(secao);
            filaSecoes.addAll(secao.getSubSecoes());
            filaQuesitos.addAll(secao.getQuesitos());
        }
        
        // Percorre os Quesitos
        while (!filaQuesitos.isEmpty()) {
            Quesito quesito = filaQuesitos.poll();
            listaTodosQuesitos.add(quesito);
            filaQuesitos.addAll(quesito.getSubQuesitos());
        }
        
        StringBuilder erros = new StringBuilder("");
        erros = verificaCamposObrigatoriosDeEntidades(prontuario, listaTodasSecoes, listaTodosQuesitos, erros);
        erros = verificaProntuarioSemSecao(prontuario, erros);
        erros = secaoService.verificaSecoesVazias(listaTodasSecoes, erros);
        erros = quesitoService.verificaQuesitosObjetivosSemOpcao(listaTodosQuesitos, erros);
        erros = opcaoService.verificaOpcoesComMesmoNome(listaTodosQuesitos, erros);
        erros = verificaOrdemOpcoesHabilitadoras(prontuario, erros);

        if (erros.length() > 0) {
            throw new ProntuarioInconsistenteException(erros.toString());
        }

        prontuario.setFinalizado(true);
        return this.create(prontuario);
    }

    private StringBuilder verificaProntuarioSemSecao(Prontuario prontuario, StringBuilder erros) {
        if (prontuario.getSecoes().isEmpty()) {
            erros.append("O prontuário deve ter pelo menos uma seção.\n");
        }
        return erros;
    }

    private StringBuilder verificaCamposObrigatoriosDeEntidades(Prontuario prontuario, List<Secao> listaTodasSecoes, List<Quesito>listaTodosQuesitos, StringBuilder erros) {

        if (prontuario.getNome() == null || prontuario.getNome().isEmpty()) {
            erros.append("O prontuário deve possuir um nome.\n");
        }

        if (prontuario.getDescricao() == null || prontuario.getDescricao().isEmpty()) {
            erros.append("O prontuário deve possuir uma descrição.\n");
        }
        
        List<Secao> secoesSemNome = new ArrayList<>();
        List<Quesito> quesitosSemEnunciado = new ArrayList<>();
        List<Opcao> opcoesSemTextoAlternativa = new ArrayList<>();
        for (Secao secao : listaTodasSecoes) {
            if (secao.getTitulo() == null || secao.getTitulo().isEmpty()) {
                secoesSemNome.add(secao);
            }
        }
        for (Quesito quesito : listaTodosQuesitos) {
            if (quesito.getEnunciado() == null || quesito.getEnunciado().isEmpty()) {
                quesitosSemEnunciado.add(quesito);
            }
            for (Opcao opcao : quesito.getOpcoes()) {
                if (opcao.getTextoAlternativa() == null || opcao.getTextoAlternativa().isEmpty()) {
                    opcoesSemTextoAlternativa.add(opcao);
                }
            }
        }
        
        if(!secoesSemNome.isEmpty()) {
            erros.append("As seguintes seções devem possuir um título: ");
            for(Secao secao : secoesSemNome) {
                erros.append(secao.getId() + ", ");
            }
            if (erros.length() > 0) {
                erros.setLength(erros.length() - 2); // Remove the last ", "
            }
            erros.append("\n");
        }
        if(!quesitosSemEnunciado.isEmpty()) {
            erros.append("Os seguintes quesitos devem possuir um enunciado: ");
            for(Quesito quesito : quesitosSemEnunciado) {
                erros.append(quesito.getId() + ", ");
            }
            if (erros.length() > 0) {
                erros.setLength(erros.length() - 2); // Remove the last ", "
            }
            erros.append("\n");
        }

        if(!opcoesSemTextoAlternativa.isEmpty()) {
            erros.append("As seguintes opções devem possuir um nome: ");
            for(Opcao opcao : opcoesSemTextoAlternativa) {
                erros.append(opcao.getId() + ", ");
            }
            if (erros.length() > 0) {
                erros.setLength(erros.length() - 2); // Remove the last ", "
            }
            erros.append("\n");
        }

        return erros;
    }

    private StringBuilder verificaOrdemOpcoesHabilitadoras(Prontuario prontuario, StringBuilder erros) {
        List<Quesito> quesitosJaVistos = new ArrayList<>();

        for(Item secao : prontuario.getSecoes()) {
            quesitosJaVistos.addAll(percorreProntuario(secao.getSubItens()));
        }

        List<Opcao> opcoesJaVistas = new ArrayList<>();

        for(Quesito quesito : quesitosJaVistos) {

            List<Long> opcoesHabilitadorasInvalidas = new ArrayList<>();
            for(Opcao opcao : quesito.getOpcoesHabilitadoras()) {
                if(!opcoesJaVistas.contains(opcao)) {
                    opcoesHabilitadorasInvalidas.add(opcao.getId());
                }
            }

            if(!opcoesHabilitadorasInvalidas.isEmpty()) {
                erros.append("As opções habilitadoras ");
                erros.append(
                    String.join(",", opcoesHabilitadorasInvalidas.stream()
                                                                           .map(Object::toString)
                                                                           .toArray(String[]::new))
                );
                erros.append(" aparecem após o quesito habilitado ").append(quesito.getId()).append("\n");
            }

            opcoesJaVistas.addAll(quesito.getOpcoes());
        }
        
        return erros;
    }

    private List<Quesito> percorreProntuario(List<Item> itens) {

        List<Quesito> quesitos = new ArrayList<>();

        for(Item item : itens) {
            if(item instanceof Quesito) {
                quesitos.add((Quesito) item);
            } else {
                quesitos.addAll(percorreProntuario(item.getSubItens()));
            }
        }

        return quesitos;
    }

}