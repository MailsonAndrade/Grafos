package br.com.unipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Aresta {
    private String nome;
    private Vertice verticeOrigem;
    private Vertice verticeDestino;
    private double peso;


    public Aresta(Vertice v1, Vertice v2) {
        this(null, v1, v2, 1.0);
    }


    public Aresta(Vertice v1, Vertice v2, double peso) {
        this(null, v1, v2, peso);
    }


    public Aresta(String nome, Vertice v1, Vertice v2) {
        this(nome, v1, v2, 1.0);
    }

    @Override
    public String toString() {
        String nomeAresta = nome != null ? nome : "";
        return String.format("%s{%s,%s,peso=%.2f}", nomeAresta, verticeOrigem, verticeDestino, peso);
    }
}