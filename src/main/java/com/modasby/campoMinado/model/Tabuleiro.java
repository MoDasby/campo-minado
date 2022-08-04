package com.modasby.campoMinado.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Getter @Setter
public class Tabuleiro implements BiConsumer<Campo, CampoEvento> {
    private int linhas;
    private int colunas;
    private int minas;

    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observers = new ArrayList<>();

    public Tabuleiro (int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
        
        gerarCampos();
        associarOsVizinhos();
        sortearMinas();
    }

    public void addObserver(Consumer<ResultadoEvento> observer) {
        observers.add(observer);
    }

    public void notifyObservers(ResultadoEvento result) {
        observers.forEach(o -> o.accept(result));
    }

    public void abrir (int linha, int coluna) {
        try {
            campos.stream()
                    .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                    .findFirst()
                    .ifPresent(Campo::abrir);
        } catch (Exception e) {
            campos.forEach(c -> c.setAberto(true));

            throw e;
        }
    }

    private void mostrarMinas() {
        campos.stream()
                .filter(Campo::isMinado)
                .filter(c -> !c.isMarcado())
                .forEach(c -> c.setAberto(true));
    }

    public void alternarMarcacao (int linha, int coluna) {
        campos.stream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(Campo::alternarMarcacao);
    }
    private void sortearMinas() {
        var random = new Random();

        int minasArmadas = 0;

        while (minasArmadas < minas) {
            int campoGerado = random.nextInt(campos.size());
            Campo campoSorteado = campos.get(campoGerado);

            if (!campoSorteado.isMinado()) {
                campoSorteado.setMinado(true);
                minasArmadas++;
            }
        }
    }
    private void associarOsVizinhos() {
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                c1.addVizinho(c2);
            }
        }
    }

    private void gerarCampos() {
        for (int i = 1; i <= linhas; i++) {
            for (int j = 1; j <= colunas; j++) {
                Campo campo = new Campo(i, j);
                campo.addObserver(this);
                campos.add(campo);
            }
        }
    }

    public boolean objetivoAlcancado() {
        return campos.stream().allMatch(Campo::objetivoAlcancado);
    }

    public void reiniciar() {
        campos.forEach(Campo::reiniciar);
        sortearMinas();
    }

    @Override
    public void accept(Campo campo, CampoEvento evento) {
        if (evento == CampoEvento.EXPLODIR) {
            mostrarMinas();
            notifyObservers(new ResultadoEvento(false));
        } else if (objetivoAlcancado()) {
            notifyObservers(new ResultadoEvento(true));
        }
    }
}
