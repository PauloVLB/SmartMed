package br.ufrn.DASH.model.interfaces;

import java.util.List;

public class ItemOrdenavelUtils {
    public static <T extends ItemOrdenavel> void ordenar(List<T> itens) {
        itens.sort((i1, i2) -> i1.getOrdem().compareTo(i2.getOrdem()));
    }
}
