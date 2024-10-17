package br.ufrn.DASH.model.interfaces;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Generics {
    public static <T extends DashEntity> List<Long> TToIds(List<T> input){
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

    public static <T extends Ordenavel> void ordenar(List<T> ordenaveis) {
        Collections.sort(ordenaveis, Comparator.comparing(T::getOrdem));
    }
}
