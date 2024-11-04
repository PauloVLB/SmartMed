package br.ufrn.DASH.model.interfaces;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Generics {
    public static <T extends GenericEntity> List<Long> TToIds(List<T> input){
        if(input == null) {
            // nunca entra aqui (?)
            return null; 
        }
        return input.stream().map(T::getId).toList();
    }
    public static <T extends GenericEntity> void sortById(List<T> ordenaveis) {
        Collections.sort(ordenaveis, Comparator.comparing(T::getId));
    }
}
