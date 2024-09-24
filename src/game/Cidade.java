package game; 

// importar classes/pacotes
import br.com.mvbos.lgj.base.Elemento; 
import javax.swing.ImageIcon; 
import java.awt.Graphics2D; 
import java.awt.Image; 

public class Cidade extends Elemento { 

    private ImageIcon imagem; // a imagem da cidade
    private Image imagemRedimensionada; // a imagem redimensionada da cidade

    // construtor
    public Cidade(int px, int py, int largura, int altura, String imagemPath) {
        super(px, py, largura, altura); 
        this.imagem = new ImageIcon(imagemPath); // imageIcon com o caminho da cidade
        // redimensionando a imagem com o tamanho específicado
        this.imagemRedimensionada = imagem.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);

        this.setAtivo(true);
    }

    // desenha a cidade na tela
    @Override
    public void desenha(Graphics2D g) {
        if (isAtivo()) {
        // desenha a imagem redimensionada na posição específicada
        g.drawImage(imagemRedimensionada, getPx(), getPy(), null);
    }
    }
}
