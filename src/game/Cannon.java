package game; 

// importar pacote e classes
import br.com.mvbos.lgj.base.Elemento; 
import javax.swing.ImageIcon; 
import java.awt.Graphics2D; 

public class Cannon extends Elemento { 

    private ImageIcon imagem; // para armazenar a imagem do canhão
    private boolean destrutivel; // verificar se um dos canhões é destrutível

    // construtor
    public Cannon(int px, int py, int largura, int altura, String imagemPath, boolean destrutivel) {
        super(px, py, largura, altura); 
        this.imagem = new ImageIcon(imagemPath); // imageIcon com o caminho da imagem
        setAtivo(true);
        this.destrutivel = destrutivel;
    }

    public ImageIcon getImagem() {
        return imagem;
    }

    public void setImagem(ImageIcon imagem) {
        this.imagem = imagem;
    }

    public boolean isDestrutivel() {
        return destrutivel;
    }

    public void setDestrutivel(boolean destrutivel) {
        this.destrutivel = destrutivel;
    }

    // desenhar o canhão na tela
    @Override
    public void desenha(Graphics2D g) {
        if (isAtivo()) {
        // desenha na posição especificada
            imagem.paintIcon(null, g, getPx(), getPy());
        }
    }
}
