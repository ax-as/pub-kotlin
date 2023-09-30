package com.github.ax_as.datapro.data;

import java.util.ArrayList;
import java.util.Collections;

public class Cores {
    public static void main(String args[]) {
        ArrayList<String> lista = new ArrayList<>();
        lista.add("VERDE");
        lista.add("AZUL");
        lista.add("VERMELHO");
        lista.add("AMARELO");
        lista.add("CINZA");
        // Lista após exclusão: [VERDE, AZUL, AMARELO, CINZA]
        lista.remove(2);
        System.out.println("Lista após exclusão: " + lista);
        // Lista após da ordenação: [AMARELO, AZUL, CINZA, VERDE]
        Collections.sort(lista);
        System.out.println("Lista após da ordenação: " + lista);
        alterar(lista, 2, "BRANCO");
    }

    private static void alterar(ArrayList<String> lista, int indice, String
            elemento) {
        lista.set(indice, elemento);
        System.out.println("Lista após a atualização: " + lista);
    }
}