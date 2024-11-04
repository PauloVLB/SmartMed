package br.ufrn.DASH.model.interfaces;

import java.util.List;

public class GenericEntityToId {
    public static <T extends GenericEntity> List<Long> TToIds(List<T> input){
        if(input == null) {
            // nunca entra aqui (?)
            return null; 
        }
        return input.stream().map(T::getId).toList();
    }
}
