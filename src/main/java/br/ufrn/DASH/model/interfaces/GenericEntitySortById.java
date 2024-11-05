package br.ufrn.DASH.model.interfaces;

import java.util.List;

public class GenericEntitySortById {
    public static <T extends GenericEntity> void sortById(List<T> ordenaveis) {
        ordenaveis.sort((i1, i2) -> i1.getId().compareTo(i2.getId()));
    }
}
