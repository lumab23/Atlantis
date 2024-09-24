package game;

import br.com.mvbos.lgj.base.Elemento;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InvaderShip extends Elemento {

    private EnemyLaser laser;
    private List<ImageIcon> imagens;
    private boolean isLeftToRight;
    private tipos tipo;

    public enum tipos {
        // tipos de cada gorgon ship e suas dif. direções
        // junto ao caminho da imagem
        GORGON1_LEFT("resources/spr_longship_0.png", 100),
        GORGON2_LEFT("resources/spr_orbship_0.png", 200),
        GORGON3_LEFT("resources/spr_speedship_0.png", 1000),
        GORGON1_RIGHT("resources/spr_longship_0_right.png", 100),
        GORGON2_RIGHT("resources/spr_orbship_0_right.png", 200),
        GORGON3_RIGHT("resources/spr_speedship_0_right.png", 1000);

        private final String imagePath;
        private final int score;

        tipos(String imagePath, int score) {
            this.imagePath = imagePath;
            this.score = score;
        }

        public String getImagePath() {
            return imagePath;
        }

        public int getScore() {
            return score;
        }
    }

    // construtor
    public InvaderShip(int px, int py, int largura, int altura, tipos tipo, int velX, int velY, boolean isLeftToRight) {
        super(px, py, largura, altura);
        this.imagens = new ArrayList<>();
        this.imagens.add(new ImageIcon(tipo.getImagePath()));
        this.setVel(velX);
        this.isLeftToRight = isLeftToRight;
        this.tipo = tipo;
        this.laser = new EnemyLaser(px, py, "resources/spr_laser_0.png", velX, velY); // cria o laser com a mesma velocidade da nave invasora 
        setAtivo(true);
    }

    // getter methods
    public boolean isLeftToRight() {
        return isLeftToRight;
    }

    public tipos getTipo() {
        return tipo;
    }

    public EnemyLaser getLaser() {
        return laser;
    }

    @Override
    public void atualiza() {
        incPx(getVel()); // incrementa a posição horizontal (px) da nave pela quantidade de sua velocidade atual
        laser.incPx(getVel()); // atualiza a posição do laser com base na velocidade da nave

        // verifica se a nave está fora dos limites da tela (esquerda ou direita)
        if (getPx() < -getLargura() || getPx() > 800) {
            // move a nave para baixo
            setPy(getPy() + 40);
            // se a nava estava fora do limite esquerdo
            // a reposiciona no lado direito e vice-versa
            setPx((getPx() < -getLargura()) ? 800 : -getLargura());
            // se a nave desceu além de 300 pixels, a nave é desativada
            if (getPy() > 300) {
                setAtivo(false);
            }
        }
    }


    @Override
    public void desenha(Graphics2D g) {

        /// se tiver ativo
        if (isAtivo()) {
            laser.desenha(g); // desenha o laser
            ImageIcon imagem = imagens.get(0); // obtém a imagem da nave
            if (getPy() > 240) {
                System.out.println("laserrr"); // verificar se a colisão está acontecendo
                // posiciona o laser na frente da nave e o ativa
                laser.setPx(getPx() + 22);
                laser.setPy(getPy() + 12);
                laser.setAtivo(true);
            } else {
                // desativa o laser se a nave estiver acima de 240 pixels 
                laser.setAtivo(false);
            }
            
            // desenha a nave na posição atual
            imagem.paintIcon(null, g, getPx(), getPy());
        }
    }

}
