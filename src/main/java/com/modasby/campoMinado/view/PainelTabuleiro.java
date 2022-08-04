package com.modasby.campoMinado.view;

import com.modasby.campoMinado.model.BotaoCampo;
import com.modasby.campoMinado.model.Campo;
import com.modasby.campoMinado.model.CampoEvento;
import com.modasby.campoMinado.model.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {

    PainelTabuleiro(Tabuleiro tabuleiro) {
        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

        int total = tabuleiro.getLinhas() * tabuleiro.getColunas();

        tabuleiro.getCampos()
                .forEach(c -> {
                    add(new BotaoCampo(c));
                });

        tabuleiro.addObserver(o -> {
            SwingUtilities.invokeLater(() -> {
                if (o.isWinner()) {
                    JOptionPane.showMessageDialog(this, "Ganhou");
                } else {
                    JOptionPane.showMessageDialog(this, "Perdeu");
                }
                tabuleiro.reiniciar();
            });
        });

    }
}
