package br.ufrn.DASH.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.ufrn.DASH.mapper.llm.LLMMessage;
import br.ufrn.DASH.mapper.llm.LLMRequest;
import br.ufrn.DASH.mapper.llm.LLMResponse;

import java.util.List;

@Service
public class LLMService {

    @Qualifier("llmRestTemplate")
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${llm.api-url}")
    private String apiUrl;

    @Value("${llm.model}")
    private String model;

    public LLMResponse getRespostaFromPrompt(String prompt) {
        LLMMessage message = new LLMMessage("user", prompt);
        LLMRequest request = new LLMRequest(List.of(message), model, 1);

        LLMResponse response = restTemplate.postForObject(apiUrl, request, LLMResponse.class);

        return response;
    }
}
