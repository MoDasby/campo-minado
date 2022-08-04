package com.modasby.campoMinado.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

public class BotaoCampo extends JButton implements BiConsumer<Campo, CampoEvento> {

    private final Color BG_PADRAO = new Color(184, 184, 184);
    private final Color BG_MARCAR = new Color(8, 179, 247);
    private final Color BG_EXPLODIR = new Color(189, 66, 68);
    private final Color TEXTO_VERDE = new Color(0, 100, 0);
    private Campo campo;
    public BotaoCampo(Campo campo) {
        campo.addObserver(this);
        this.campo = campo;

        setBorder(BorderFactory.createBevelBorder(0));
        setBackground(BG_PADRAO);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) campo.alternarMarcacao();
                else if (e.getButton() == 1) campo.abrir();
            }
        });
    }

    @Override
    public void accept(Campo campo, CampoEvento evento) {
        switch (evento) {
            case ABRIR:
                aplicarEstiloAbrir();
                break;
            case MARCAR:
                aplicarEstiloMarcar();
                break;
            case EXPLODIR:
                aplicarEstiloExplodir();
                break;
            case REINICIAR:
                aplicarEstiloReiniciar();
                break;
            default:
                aplicarEstiloPadrao();
        }
    }

    private void aplicarEstiloReiniciar() {
        setBorder(BorderFactory.createBevelBorder(0));
        setBackground(BG_PADRAO);
        setText("");
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCAR);
        setText("M");
    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setText("");
    }

    private void aplicarEstiloAbrir() {

        if (campo.isMinado()) {
            setBackground(BG_EXPLODIR);
            return;
        }

        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        switch (campo.minasNaVizinhanca()) {
            case 1:
                setForeground(TEXTO_VERDE);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }

        String valor = !campo.vizinhançaSegura() && !campo.isMinado() ? campo.minasNaVizinhanca() + "" : "";
        setText(valor);
    }
}
