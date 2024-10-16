package br.ufrn.DASH.model.interfaces;

import java.util.List;

public class Generics {
    public static <T extends GenericEntity> List<Long> TToIds(List<T> input){
        if(input == null) {
            return null;
    }
        return input.stream().map(T::getId).toList();
    }

    public static <T extends Ordenavel> void alterarOrdem(List<T> ordenaveis, Integer x){
        for (T t : ordenaveis) {
            if(t.getOrdem() >= x)t.setOrdem(t.getOrdem()+1);
        }
    }
}
