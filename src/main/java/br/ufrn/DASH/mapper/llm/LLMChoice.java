package br.ufrn.DASH.mapper.llm;

public record LLMChoice(
    int index,
    LLMMessage message,
    String logprobs,
    String finish_reason
) {}
