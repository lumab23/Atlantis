package br.com.mvbos.lgj.base;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Texto extends Elemento {

    private Font fonte;

    public Texto() {
        fonte = carregarFonte("resources/Minecraft.ttf");
    }

    public Texto(Font fonte) {
        this.fonte = fonte;
    }

    public void desenha(Graphics2D g, String texto) {
        desenha(g, texto, getPx(), getPy());
    }

    public void desenha(Graphics2D g, String texto, int px, int py) {
        if (getCor() != null)
            g.setColor(getCor());

        g.setFont(fonte);
        g.drawString(texto, px, py);
    }

    public Font getFonte() {
        return fonte;
    }

    public void setFonte(Font fonte) {
        this.fonte = fonte;
    }

    private Font carregarFonte(String caminho) {
        try {
            Font fonte = Font.createFont(Font.TRUETYPE_FONT, new File(caminho));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fonte);
            return fonte.deriveFont(22f); // Tamanho 16
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Tahoma", Font.PLAIN, 22); // Fallback para Tahoma
        }
    }
}
