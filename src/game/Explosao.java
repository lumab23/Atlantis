package game; 

// importar classes/pacotes
import br.com.mvbos.lgj.base.Elemento; 
import javax.swing.*; 
import java.awt.*; 
import java.util.ArrayList; 
import java.util.List; 

public class Explosao extends Elemento { 

    private List<ImageIcon> imagens; // armazena os frames da explosão
    private int currentFrame = 0; // index do frame atual
    private int frameDelay; // atraso entre os frames
    private int frameDelayCounter = 0; // contador para controlar o atraso entre frama

    // construtor
    public Explosao(int px, int py, int largura, int altura, List<String> imagemPaths, int frameDelay) {
        super(px, py, largura, altura); 
        this.imagens = new ArrayList<>(); 
        // carrega cada imagem da explosão a partir dos caminhos 
        for (String path : imagemPaths) {
            this.imagens.add(new ImageIcon(path)); // adiciona um imageIcon à lista
        }
        this.frameDelay = frameDelay; 
        setAtivo(true); // definindo a explosão como ativa
    }

    // atualizar a explosão a cada quadro do jogo
    @Override
    public void atualiza() {
        frameDelayCounter++; // incrementação do cont. de atraso entre as frames
        // se o atraso entre frames foi alcançado
        if (frameDelayCounter >= frameDelay) { 
            currentFrame++; // vai avançar para a próxima frame
            frameDelayCounter = 0; // reiniciando o contador de atraso
            // se todos os frames forem exibidos
            if (currentFrame >= imagens.size()) { 
                setAtivo(false); // a explosão vai ser desativada
        }

    }
    }

    // desenha a explosão natela
    @Override
    public void desenha(Graphics2D g) {
        // se a explosão tiver ativa
        if (isAtivo()) { 
            // desenha o frame atual na posição especificada
            imagens.get(currentFrame).paintIcon(null, g, getPx(), getPy());
        }
    }
}
