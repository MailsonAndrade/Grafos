package br.com.unipe;

import java.util.*;
import java.util.stream.Collectors;

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
        for (String nome : nomes) {
            vertices.add(new Vertice(nome));
        }
    }

    public void addAresta(String nomeVertice1, String nomeVertice2) {
        addAresta("", nomeVertice1, nomeVertice2, 1.0);
    }

    public void addAresta(String nomeAresta, String nomeVertice1, String nomeVertice2) {
        addAresta(nomeAresta, nomeVertice1, nomeVertice2, 1.0);
    }

    public void addAresta(String nomeVertice1, String nomeVertice2, double peso) {
        addAresta("", nomeVertice1, nomeVertice2, peso);
    }

    public void addAresta(String nomeAresta, String nomeVertice1, String nomeVertice2, double peso) {
        Vertice v1 = encontraVertice(nomeVertice1).orElseThrow();
        Vertice v2 = encontraVertice(nomeVertice2).orElseThrow();

        Aresta aresta = new Aresta(nomeAresta, v1, v2, peso);
        arestas.add(aresta);

        v1.getAdjacencias().add(v2);
        v2.getAdjacentes().add(v1);

        if (!eDirigido) {
            v2.getAdjacencias().add(v1);
            v1.getAdjacentes().add(v2);
        }
    }

    private Optional<Vertice> encontraVertice(String nome) {
        return vertices.stream().filter(v -> v.getNome().equals(nome)).findFirst();
    }

    public List<Vertice> getVizinhos(Vertice v) {
        return v.getAdjacencias();
    }

    public List<Map.Entry<Vertice, Double>> getVizinhosComPeso(Vertice v) {
        List<Map.Entry<Vertice, Double>> vizinhos = new ArrayList<>();
        for (Aresta a : arestas) {
            if (a.getVerticeOrigem().equals(v)) {
                vizinhos.add(new AbstractMap.SimpleEntry<>(a.getVerticeDestino(), a.getPeso()));
            } else if (!eDirigido && a.getVerticeDestino().equals(v)) {
                vizinhos.add(new AbstractMap.SimpleEntry<>(a.getVerticeOrigem(), a.getPeso()));
            }
        }
        return vizinhos;
    }

    public List<String> buscaEmLargura(String nomeOrigem) {
        Vertice origem = encontraVertice(nomeOrigem).orElseThrow();
        List<String> visitados = new ArrayList<>();
        Queue<Vertice> fila = new LinkedList<>();
        Set<Vertice> descobertos = new HashSet<>();

        fila.add(origem);
        descobertos.add(origem);

        while (!fila.isEmpty()) {
            Vertice atual = fila.poll();
            visitados.add(atual.getNome());

            for (Vertice vizinho : getVizinhos(atual)) {
                if (!descobertos.contains(vizinho)) {
                    descobertos.add(vizinho);
                    fila.add(vizinho);
                }
            }
        }
        return visitados;
    }

    public boolean ehConexo() {
        if (vertices.isEmpty()) return true;
        return buscaEmLargura(vertices.get(0).getNome()).size() == vertices.size();
    }

    public boolean ehFortementeConexo() {
        if (!eDirigido) return ehConexo();
        for (Vertice v : vertices) {
            if (buscaEmLargura(v.getNome()).size() != vertices.size()) {
                return false;
            }
        }
        return true;
    }

    public double comprimentoCaminhoComPercurso(String nomeOrigem, String nomeDestino, boolean imprimir) {
        Vertice vOrigem = encontraVertice(nomeOrigem).orElseThrow();
        Vertice vDestino = encontraVertice(nomeDestino).orElseThrow();
        Map<Vertice, Double> dist = new HashMap<>();
        Map<Vertice, Vertice> pred = new HashMap<>();
        PriorityQueue<Map.Entry<Vertice, Double>> pq = new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));

        for (Vertice v : vertices) dist.put(v, Double.POSITIVE_INFINITY);
        dist.put(vOrigem, 0.0);
        pq.add(new AbstractMap.SimpleEntry<>(vOrigem, 0.0));

        while (!pq.isEmpty()) {
            Vertice atual = pq.poll().getKey();
            double distAtual = dist.get(atual);

            if (atual.equals(vDestino)) {
                if (imprimir) {
                    List<String> caminho = new ArrayList<>();
                    for (Vertice v = vDestino; v != null; v = pred.get(v)) {
                        caminho.add(0, v.getNome());
                    }
                    System.out.println("Caminho (Dijkstra): " + String.join(" -> ", caminho));
                    System.out.println("Custo total: " + distAtual);
                }
                return distAtual;
            }

            for (Map.Entry<Vertice, Double> vizPeso : getVizinhosComPeso(atual)) {
                double novaDist = distAtual + vizPeso.getValue();
                if (novaDist < dist.get(vizPeso.getKey())) {
                    dist.put(vizPeso.getKey(), novaDist);
                    pred.put(vizPeso.getKey(), atual);
                    pq.add(new AbstractMap.SimpleEntry<>(vizPeso.getKey(), novaDist));
                }
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    public List<Vertice> buscaGananciosa(String origem, String destino, Map<String, Double> heuristica) {
        Vertice vOrigem = encontraVertice(origem).orElseThrow(() -> new IllegalArgumentException("Vértice não existe."));
        Vertice vDestino = encontraVertice(destino).orElseThrow(() -> new IllegalArgumentException("Vértice não existe."));

        if (vOrigem.equals(vDestino)) {
            return List.of(vOrigem);
        }

        PriorityQueue<Map.Entry<Vertice, Double>> filaPrioridade = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );

        Map<Vertice, Vertice> predecessor = new HashMap<>();
        Set<Vertice> visitados = new HashSet<>();

        double hOrigem = heuristica.getOrDefault(vOrigem.getNome(), Double.MAX_VALUE);
        filaPrioridade.add(new AbstractMap.SimpleEntry<>(vOrigem, hOrigem));
        visitados.add(vOrigem);
        predecessor.put(vOrigem, null);

        while (!filaPrioridade.isEmpty()) {
            Vertice atual = filaPrioridade.poll().getKey();

            if (atual.equals(vDestino)) {
                List<Vertice> caminho = new ArrayList<>();
                for (Vertice v = atual; v != null; v = predecessor.get(v)) {
                    caminho.add(0, v);
                }
                return caminho;
            }

            for (Vertice vizinho : getVizinhos(atual)) {
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    predecessor.put(vizinho, atual);
                    double hVizinho = heuristica.getOrDefault(vizinho.getNome(), Double.MAX_VALUE);
                    filaPrioridade.add(new AbstractMap.SimpleEntry<>(vizinho, hVizinho));
                }
            }
        }

        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return """
                Grafo{\s
                   direcionado = %s,\s
                   vertices = %s,\s
                   arestas = %s\s
                }""".formatted(eDirigido, vertices, arestas);
    }
}