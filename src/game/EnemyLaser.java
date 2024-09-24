package game; 

// importar classes/pacotes
import br.com.mvbos.lgj.base.Elemento; 
import javax.swing.ImageIcon;
import java.awt.Graphics2D; 

public class EnemyLaser extends Elemento { 

    private ImageIcon imagem; // lista que vai armazenar os frames do laser inimigo
    private int velX; // vel. horizontal do laser
    private int velY; // vel. vertical do laser

    // construtor
    public EnemyLaser(int px, int py, String imagemPath, int velX, int velY) {
        super(px, py, 0, 0); 
        this.imagem = new ImageIcon(imagemPath); // para a imagem path do laser inimigo
        this.velX = velX; 
        this.velY = velY; 
        setLargura(imagem.getIconWidth());
        setAltura(imagem.getIconHeight());
    }

    // usando esse método para atualizar o estado do laser a cada quadro do jogo
    @Override
    public void atualiza() {
        incPx(velX); // movendo o laser na direção horizontal
        incPy(velY); // movendo o laser na direção vertical

        // vai verificar se o laser saiu da tela ou atingiu a parte inferior
        if (getPy() > 600 || getPx() < 0 || getPx() > 800) {
            setAtivo(false); // setando/desativando o laser
        }
    }

    // desenhar o laser na tela
    @Override
    public void desenha(Graphics2D g) {
         // se o laser tiver ativo
         // a imagem do laser vai ser desenhada
        if (isAtivo()) {
            imagem.paintIcon(null, g, getPx(), getPy()); 
        }
    }
}
