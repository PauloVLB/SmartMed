package br.ufrn.DASH.model.interfaces;

import java.util.List;

public interface ItemOrdenavel {
    Integer getOrdem();
    List<ItemOrdenavel> getSubItens();
} 
