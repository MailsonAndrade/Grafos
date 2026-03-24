package br.com.unipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Vertice {
    private String nome;

    @Override
    public String toString() {
        return nome;
    }
}