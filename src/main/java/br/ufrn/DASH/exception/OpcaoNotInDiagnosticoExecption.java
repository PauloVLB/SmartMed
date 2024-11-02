package br.ufrn.DASH.exception;

public class OpcaoNotInDiagnosticoExecption extends DashException{
    private final Long idDiagnostico;
    private final Long idOpcao;

    public OpcaoNotInDiagnosticoExecption(Long idDiagnostico, Long idOpcao) {
        super();
        this.idOpcao = idOpcao;
        this.idDiagnostico = idDiagnostico;
    }
    
    @Override
    public String getMessage() {
        return "Opcao com id " + idDiagnostico + " nao encontrada em um diagnostico com id " + idOpcao + ".";
    }
}