package br.ufrn.DASH.mapper.llm;

import java.util.List;

public record LLMRequest(
    List<LLMMessage> messages,
    String model,
    int temperature
) {}
