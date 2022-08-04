package com.modasby.campoMinado.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Getter @Setter @SuppressWarnings("all")
public class Campo {
    private final int linha;
    private final int coluna;
    private boolean minado;
    private boolean aberto;
    private boolean marcado;
    private final List<Campo> vizinhos = new ArrayList<Campo>();
    private final List<BiConsumer<Campo, CampoEvento>> observers = new ArrayList<>();

    public Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

   public void addObserver(BiConsumer<Campo, CampoEvento> observer) {
        observers.add(observer);
    }

    public void notifyObservers(CampoEvento evento) {
        observers.forEach(o -> o.accept(this, evento));
    }

    boolean addVizinho(Campo vizinho) {
        boolean linhaDiferente = this.linha != vizinho.getLinha();
        boolean colunaDiferente = this.coluna != vizinho.getColuna();

        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.getLinha());
        int deltaColuna = Math.abs(coluna - vizinho.getColuna());
        int deltaGeral = deltaColuna + deltaLinha;

        if (deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if (deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else return false;
    }

    void addVizinho(List<Campo> vizinhos) {
        vizinhos.forEach(this::addVizinho);
    }

    void alternarMarcacao() {
        if (!aberto) marcado = !marcado;

        if (marcado) notifyObservers(CampoEvento.MARCAR);
        else notifyObservers(CampoEvento.DESMARCAR);
    }

    boolean abrir() {
        if (!aberto && !marcado) {
            if (minado) {
                notifyObservers(CampoEvento.EXPLODIR);
                return true;
            }

            setAberto(true);
            notifyObservers(CampoEvento.ABRIR);

            if (vizinhançaSegura()) {
                vizinhos.forEach(v -> v.abrir());
            }
        }

        return false;
    }

    boolean vizinhançaSegura() {
        return vizinhos.stream().noneMatch(v -> v.isMinado());
    }

    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    public int minasNaVizinhanca() {
        return (int) vizinhos.stream().filter(v -> v.isMinado()).count();
    }

    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
        notifyObservers(CampoEvento.REINICIAR);
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;

        if (isAberto()) notifyObservers(CampoEvento.ABRIR);
    }
}

