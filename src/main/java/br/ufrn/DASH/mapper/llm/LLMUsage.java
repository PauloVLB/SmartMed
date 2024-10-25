package br.ufrn.DASH.mapper.llm;

public record LLMUsage(
    double queue_time,
    int prompt_tokens,
    double prompt_time,
    int completion_tokens,
    double completion_time,
    int total_tokens,
    double total_time
) {}
