package br.ufrn.DASH.exception;

import br.ufrn.DASH.model.interfaces.GenericEntity;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends DashException {
    private final Long id;
    private final String entityName;

    public EntityNotFoundException(Long id, GenericEntity cause) {
        super();
        this.id = id;
        this.entityName = cause.getClass().getSimpleName();
    }
    
    @Override
    public String getMessage() {
        return "Entidade " + entityName + " com o id " + id + " inexistente";
    }
}
