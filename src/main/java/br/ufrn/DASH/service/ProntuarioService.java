package br.ufrn.DASH.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.DiagnosticoNotInProntuarioException;
import br.ufrn.DASH.exception.EntityNotFoundException;
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
import static br.ufrn.DASH.model.interfaces.GenericEntitySortById.sortById;
import br.ufrn.DASH.repository.ProntuarioRepository;

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
    private LLMService llmService;

    @Autowired
    private DiagnosticoService diagnosticoService;

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

    public Prontuario update(Long id, Prontuario prontuario) {
        
        Prontuario prontuarioExistente = this.getById(id);
        
        prontuarioExistente.setNome(prontuario.getNome());
        prontuarioExistente.setDescricao(prontuario.getDescricao());
        prontuarioExistente.setEhPublico(prontuario.getEhPublico());
        prontuarioExistente.setFinalizado(prontuario.getFinalizado());

        return prontuarioRepository.save(prontuarioExistente);
    }

    public void delete(Long id) {
        this.getById(id);
        prontuarioRepository.deleteById(id);
    }

    public void deleteAll() {
        prontuarioRepository.deleteAll();
    }

    public Secao addSecao(Long idProntuario, Secao secaoNova) {
        Prontuario prontuario = this.getById(idProntuario);
        
        secaoNova.setOrdem(prontuario.getSecoes().size());
        secaoNova.setNivel(1);
        secaoNova.setProntuario(prontuario);

        prontuario.getSecoes().add(secaoNova);

        prontuarioRepository.save(prontuario);

        return prontuario.getSecoes().get(prontuario.getSecoes().size() - 1);
    }

    public Prontuario duplicar(Long idProntuario, Long idUsuario) {
        Prontuario prontuarioToDuplicate = this.getById(idProntuario);

        Usuario novoUsuario = usuarioService.getById(idUsuario);

        Prontuario prontuarioDuplicado = prontuarioToDuplicate.duplicar(novoUsuario);
        return prontuarioRepository.save(prontuarioDuplicado);
    }
    
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

    public Prontuario addProntuarioFromTemplate(Long idTemplate) {
        Prontuario prontuarioTemplate = this.getById(idTemplate);
        if(!prontuarioTemplate.getEhTemplate()) throw new ProntuarioNotTemplateException(idTemplate);
        Prontuario prontuarioCriado = prontuarioTemplate.duplicar(null);
        prontuarioCriado.setEhTemplate(false);
        return prontuarioRepository.save(prontuarioCriado);
    }

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

    public Diagnostico addDiagnostico(Long idProntuario, Diagnostico diagnostico) {
        Prontuario prontuario = this.getById(idProntuario);
        diagnostico.setProntuario(prontuario);
        
        diagnostico = diagnosticoService.create(diagnostico);

        prontuario.getDiagnosticos().add(diagnostico);

        this.create(prontuario);
        return diagnostico;
    }

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

}
