package game; // Pacote do jogo

import br.com.mvbos.lgj.base.Elemento; // Importa a classe Elemento de um pacote externo
import javax.swing.ImageIcon; // Importa a classe ImageIcon do pacote javax.swing
import java.awt.Graphics2D; // Importa a classe Graphics2D do pacote java.awt

public class Gerador extends Elemento { // Declaração da classe Gerador, que estende a classe Elemento

    private ImageIcon imagemOn; // Armazena a imagem do gerador quando está ligado
    private ImageIcon imagemOff; // Armazena a imagem do gerador quando está desligado
    private boolean estado; // Estado atual do gerador (ligado/desligado)

    // Construtor da classe Gerador
    public Gerador(int px, int py, int largura, int altura, String imagemOnPath, String imagemOffPath) {
        super(px, py, largura, altura); // Chama o construtor da classe pai (Elemento)
        this.imagemOn = new ImageIcon(imagemOnPath); // Cria um ImageIcon com o caminho da imagem do gerador ligado
        this.imagemOff = new ImageIcon(imagemOffPath); // Cria um ImageIcon com o caminho da imagem do gerador desligado
        this.estado = true; // Inicializa o gerador como ligado por padrão
        this.setAtivo(true);
    }

    // Método para alternar o estado do gerador (ligado <-> desligado)
    public void alternarEstado() {
        estado = !estado; // Inverte o estado atual do gerador
    }

    // Método para desenhar o gerador na tela
    @Override
    public void desenha(Graphics2D g) {
        // Desenha a imagem correspondente ao estado atual do gerador na posição especificada
        if (estado) {
            imagemOn.paintIcon(null, g, getPx(), getPy()); // Desenha a imagem do gerador ligado
        } else {
            imagemOff.paintIcon(null, g, getPx(), getPy()); // Desenha a imagem do gerador desligado
        }
    }
}
