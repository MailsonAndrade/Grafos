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

    //Construtor para arestas sem nome
    public Aresta(Vertice v1, Vertice v2) {
        this.verticeOrigem = v1;
        this.verticeDestino = v2;
    }

    @Override
    public String toString() {
        String nomeAresta = nome != null ? nome : "";
        return nomeAresta + "{" + verticeOrigem + "," + verticeDestino + "}";
    }
}