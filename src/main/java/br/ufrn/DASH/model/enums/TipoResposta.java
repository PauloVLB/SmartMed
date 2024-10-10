package br.ufrn.DASH.model.enums;

public enum TipoResposta {
    DISSERTATIVA_LONGA("Dissertativa Longa"),
    DISSERTATIVA_CURTA("Dissertativa Curta"),
    OBJETIVA_SIMPLES("Objetiva Simples"),
    OBJETIVA_MULTIPLA("Objetiva Múltipla"),
    DATA("Data"),;

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    TipoResposta(String descricao) {
        this.descricao = descricao;
    }
}
