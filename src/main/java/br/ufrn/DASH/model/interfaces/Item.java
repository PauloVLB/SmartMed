package br.ufrn.DASH.model.interfaces;

import java.util.List;

public interface Item {
    Integer getOrdem();
    List<Item> getSubItens();
} 
