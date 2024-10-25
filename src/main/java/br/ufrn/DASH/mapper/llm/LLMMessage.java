package br.ufrn.DASH.mapper.llm;

public record LLMMessage(
    String role,
    String content
) {}
