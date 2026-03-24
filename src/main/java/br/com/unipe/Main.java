package br.com.unipe;

public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo(); //não dirigido
        grafo.adicionaVertices("1", "2", "3", "4", "5", "6");
        grafo.addAresta("1", "3");
        grafo.addAresta("1", "4");
        grafo.addAresta("1", "5");
        grafo.addAresta("2", "3");
        grafo.addAresta("2", "4");
        grafo.addAresta("2", "5");
        grafo.addAresta("3", "5");
        grafo.addAresta("4", "5");

        System.out.println(grafo);
    }
}