package br.ufrn.DASH.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.DASH.exception.DiagnosticoNotInProntuarioException;
import br.ufrn.DASH.exception.EntityNotFoundException;
import br.ufrn.DASH.exception.ProntuarioNotTemplateException;
import br.ufrn.DASH.exception.ProntuarioTemplateException;
import br.ufrn.DASH.exception.QuesitoNotInProntuarioException;
import br.ufrn.DASH.mapper.llm.LLMResponse;
import br.ufrn.DASH.model.Diagnostico;
import br.ufrn.DASH.model.Prontuario;
import br.ufrn.DASH.model.Quesito;
import br.ufrn.DASH.model.Resposta;
import br.ufrn.DASH.model.Secao;
import br.ufrn.DASH.model.Usuario;
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

    public Prontuario update(Long id, Prontuario prontuario) {
        
        Prontuario prontuarioExistente = this.getById(id);
        
        // if (prontuarioExistente == null) {
        //     throw new EntityNotFoundException(id, new Prontuario());
        // }
        
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
        
        // if (prontuario == null) {
        //     throw new EntityNotFoundException(id, new Prontuario());
        // }
        
        secaoNova.setOrdem(prontuario.getSecoes().size());
        secaoNova.setNivel(1);
        secaoNova.setProntuario(prontuario);

        prontuario.getSecoes().add(secaoNova);

        prontuarioRepository.save(prontuario);

        return prontuario.getSecoes().get(prontuario.getSecoes().size() - 1);
    }

    public Prontuario duplicar(Long idProntuario, Long idUsuario) {
        Prontuario prontuarioToDuplicate = this.getById(idProntuario);

        // if(prontuarioToDuplicate == null) {
        //     return null;
        // }

        Usuario novoUsuario = usuarioService.getById(idUsuario);
        // if(novoUsuario == null) {
        //     return null;
        // }

        Prontuario prontuarioDuplicado = prontuarioToDuplicate.duplicar(novoUsuario);
        return prontuarioRepository.save(prontuarioDuplicado);
    }
    
    public Resposta addResposta(Long idProntuario, Long idQuesito, Resposta respostaNova) {
        if(this.getById(idProntuario).getEhTemplate())throw new ProntuarioTemplateException(idProntuario);
        Quesito quesito = quesitoService.getById(idQuesito);
        // if(!relacionadas(idProntuario, quesito))
        Prontuario acomparar = quesitoService.findProntuario(quesito);
        // System.out.println("-----------------------------------------------------------");
        // System.out.println(acomparar.getId());
        // System.out.println("-----------------------------------------------------------");
        if(acomparar == null || !Objects.equals(acomparar.getId(), idProntuario))
            throw new QuesitoNotInProntuarioException(idProntuario, idQuesito);
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
        if(/*prontuarioTemplate == null ||*/ !prontuarioTemplate.getEhTemplate()) throw new ProntuarioNotTemplateException(idTemplate);
        Prontuario prontuarioCriado = prontuarioTemplate.duplicar(null);
        prontuarioCriado.setEhTemplate(false);
        return prontuarioRepository.save(prontuarioCriado);
    }

    private boolean relacionadas(Long idProntuario, Quesito quesito){
        Quesito busca = quesito;
        Secao secao = null;
        // while(true){
        //     if(secao == null){
        //         if(busca.getSuperQuesito() != null)busca = busca.getSuperQuesito();
        //         else secao = busca.getSecao();
        //     }else{
        //         if(secao.getSuperSecao() != null)secao = secao.getSuperSecao();
        //         else return Objects.equals(secao.getProntuario().getId(), idProntuario);
        //     }
        // }

        while(secao == null || secao.getSuperSecao() != null){
            if(busca.getSuperQuesito() != null)busca = busca.getSuperQuesito();
            else secao = busca.getSecao();
            if(secao != null){
                if(secao.getSuperSecao() != null)secao = secao.getSuperSecao();
            }
        }
        return Objects.equals(secao.getProntuario().getId(), idProntuario);
    }

    public Map<String, String> getDiagnosticoLLM(Long idProntuario) {
        String prompt = 
        "Com base no seguinte JSON, que corresponde a um prontuário de um paciente, faça um diagnóstico do paciente. " + 
        "Você não precisa se ater a divisão de seções e quesitos, apenas faça um diagnóstico geral do paciente. " +
        "Seu diagnóstico será avaliado por um médico especialista, que pode ou não concordar com o diagnóstico gerado. " +
        "Portanto, pode dar sugestões de exames, tratamentos, ou qualquer outra informação que julgar relevante. " +
        "Pode assumir que o paciente é real, e que você está fazendo um diagnóstico real.\n";

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
                json.append("\t\t\t\t\t\"resposta\": \"").append(quesito.getResposta().getConteudo()).append("\"\n");
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
        // diagnostico.getOpcoesMarcadas().add(prontuario);
        // diagnosticoRepository.save(diagnostico);
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

}
