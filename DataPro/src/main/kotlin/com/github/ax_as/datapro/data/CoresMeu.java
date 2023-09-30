package com.github.ax_as.datapro.data;

import java.util.ArrayList;

public class CoresMeu {

    public static void main(String args[]) {
        ArrayList<String> lista = new ArrayList<>();
        lista.add("VERDE");
        lista.add("AZUL");
        lista.add("VERMELHO");
        lista.add("AMARELO");
        lista.add("CINZA");
        // Insira na linha imediatamente a seguir a linha de código que atenda ao requisito

        excluir(lista, 2);
        System.out.println("Lista após exclusão: " + lista);
        // Insira na linha imediatamente a seguir a linha de código que atenda ao requisito
        ordenar(lista);
        System.out.println("Lista após da ordenação: " + lista);
        alterar(lista, 2, "BRANCO");

        System.out.println("Lista após da atualização: " + lista);
    }


    private static void alterar(ArrayList<String> lista, int id, String novaCor) {
        String corAntiga = lista.remove(id);
        lista.add(id, novaCor);

    }

    private static void excluir(ArrayList<String> lista, int indice) {
        lista.remove(indice);
    }

    private static void ordenar(ArrayList<String> lista) {
        lista.sort((a, b) -> a.compareTo(b));
    }

    // A partir da próxima linha, crie o método alterar(lista, indice, novaCor).

}