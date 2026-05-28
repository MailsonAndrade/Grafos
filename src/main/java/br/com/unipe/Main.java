package br.com.unipe;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Grafo mapa = new Grafo(true);
        mapa.adicionaVertices("A", "B", "C", "D");

        mapa.addAresta("A", "B", 2.0);
        mapa.addAresta("A", "C", 5.0);
        mapa.addAresta("B", "D", 10.0);
        mapa.addAresta("C", "D", 2.0);

        Map<String, Double> heuristicaAteD = new HashMap<>();
        heuristicaAteD.put("A", 10.0);
        heuristicaAteD.put("B", 3.0);
        heuristicaAteD.put("C", 6.0);
        heuristicaAteD.put("D", 0.0);

        System.out.println("--- Teste de Busca Gananciosa ---");
        var caminhoGreedy = mapa.buscaGananciosa("A", "D", heuristicaAteD);

        String resultado = caminhoGreedy.stream()
                .map(Vertice::getNome)
                .reduce((v1, v2) -> v1 + " -> " + v2).orElse("Nenhum");
        System.out.println("Caminho escolhido: " + resultado);

        System.out.println("\n--- Comparação com Dijkstra ---");
        mapa.comprimentoCaminhoComPercurso("A", "D", true);
    }
}