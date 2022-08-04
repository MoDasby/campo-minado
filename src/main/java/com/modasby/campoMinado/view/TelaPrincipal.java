package com.modasby.campoMinado.view;

import com.modasby.campoMinado.model.Tabuleiro;

import javax.swing.*;

public class TelaPrincipal extends JFrame  {

    public TelaPrincipal() {
        Tabuleiro tabuleiro = new Tabuleiro(16, 30, 5);

        add(new PainelTabuleiro(tabuleiro));
        
        setVisible(true);
        setTitle("Campo Minado");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
