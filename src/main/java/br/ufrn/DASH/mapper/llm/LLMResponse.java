package br.ufrn.DASH.mapper.llm;

import java.util.List;

public record LLMResponse(
    String id,
    String object,
    Long created,
    String model,
    List<LLMChoice> choices,
    LLMUsage usage,
    String system_fingerprint,
    LLMGroq x_groq
) {}
