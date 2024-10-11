package br.ufrn.DASH.model.interfaces;

import java.util.List;

public class Generics {
    //    default List<Long> quesitosToIds(List<Quesito> quesitos) {
        public static <T extends GenericEntity> List<Long> TToIds(List<T> input){
            if(input == null) {
                return null;
        }
        return input.stream().map(T::getId).toList();
    }
}
