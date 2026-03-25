package br.com.unipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Vertice {
    private String nome;

    private List<Vertice> adjacencias;
    private List<Vertice> adjacentes;

    public Vertice(String nome) {
        this.nome = nome;
        this.adjacencias = new ArrayList<>();
        this.adjacentes = new ArrayList<>();
    }

    public int getGrauEntrada() {
        return adjacentes.size();
    }

    public int getGrauSaida() {
        return adjacencias.size();
    }

    public int getGrauTotal() {
        return getGrauEntrada() + getGrauSaida();
    }

    @Override
    public String toString() {
        return nome + "(Grau:" + getGrauTotal() + ")";
    }
}