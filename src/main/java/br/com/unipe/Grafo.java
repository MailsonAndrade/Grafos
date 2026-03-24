package br.com.unipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Grafo {
    private final List<Aresta> arestas;
    private final List<Vertice> vertices;
    private boolean eDirigido;

    public Grafo() {
        this(false);
    }

    public Grafo(boolean eDirigido) {
        this.eDirigido = eDirigido;
        arestas = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    public void adicionaVertices(String... nomes) {
        for (String nome : nomes) { //for-each
            vertices.add(new Vertice(nome));
        }
    }

    public void addAresta(String nomeVertice1, String nomeVertice2) {
        arestas.add(criaAresta("", nomeVertice1, nomeVertice2));
    }

    public void addAresta(String nomeAresta, String nomeVertice1, String nomeVertice2) {
        arestas.add(criaAresta(nomeAresta, nomeVertice1, nomeVertice2));
    }

    private Aresta criaAresta(String nomeAresta, String nomeVertice1, String nomeVertice2) {
        Vertice v1 = encontraVertice(nomeVertice1)
                .orElseThrow(() -> new IllegalArgumentException("Vertice " + nomeVertice1 + " não encontrado."));
        Vertice v2 = encontraVertice(nomeVertice2)
                .orElseThrow(() -> new IllegalArgumentException("Vertice " + nomeVertice2 + " não encontrado."));
        infereSeGrafoEDirecionado(v1, v2);
        return nomeAresta.isEmpty() ? new Aresta(v1, v2) : new Aresta(nomeAresta, v1, v2);
    }

    private void infereSeGrafoEDirecionado(Vertice v1, Vertice v2) {
        if (!eDirigido) { //eDirigido == false
            if (eSelfLoop(v1, v2)) {
                eDirigido = true;
            } else {
                for (Aresta aresta : arestas) { //for-each
                    if (eViaMaoDupla(v1, v2, aresta)) {
                        eDirigido = true;
                        break;
                    }
                    if (eArestaDuplicada(v1, v2, aresta)) {
                        eDirigido = true;
                        break;
                    }
                }
            }
        }
    }

    private static boolean eArestaDuplicada(Vertice v1, Vertice v2, Aresta aresta) {
        return aresta.getVerticeOrigem().equals(v1) && aresta.getVerticeDestino().equals(v2);
    }

    private static boolean eViaMaoDupla(Vertice v1, Vertice v2, Aresta aresta) {
        return aresta.getVerticeOrigem().equals(v2) && aresta.getVerticeDestino().equals(v1);
    }

    private static boolean eSelfLoop(Vertice v1, Vertice v2) {
        return v1.getNome().equals(v2.getNome());
    }

    public Optional<Vertice> encontraVertice(String nome) {
        for (Vertice vertice : vertices) {
            if (vertice.getNome().equalsIgnoreCase(nome)) {
                return Optional.of(vertice);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return """
                Grafo{
                   direcionado = %s,
                   vertices = %s,
                   arestas = %s
                }""".formatted(eDirigido ? "sim" : "não", vertices, arestas);
    }
}