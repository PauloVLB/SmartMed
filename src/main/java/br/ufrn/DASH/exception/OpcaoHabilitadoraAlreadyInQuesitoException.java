package br.ufrn.DASH.exception;

public class OpcaoHabilitadoraAlreadyInQuesitoException extends DashException {
    private final Long idQuesito;
    private final Long idOpcao;

    public OpcaoHabilitadoraAlreadyInQuesitoException(Long idQuesito, Long idOpcao) {
        super();
        this.idQuesito = idQuesito;
        this.idOpcao = idOpcao;
    }
    
    @Override
    public String getMessage() {
        return "Quesito com id " + idQuesito + " ja é habilitado por opção de id " + idOpcao + ".";
    }
    
}
