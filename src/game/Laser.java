package game; 

// importar classes/pacotes
import br.com.mvbos.lgj.base.Elemento; 
import javax.swing.ImageIcon; 
import java.awt.Graphics2D; 
import java.util.ArrayList; 
import java.util.List; 

public class Laser extends Elemento { 

    private List<ImageIcon> imagens; // armazenar os frames do laser
    private int currentFrame = 0; // index do frame atual
    private int frameDelay = 1; // atraso entre os frames da animação
    private int frameDelayCounter = 0; // contador que controla o atraso entre os frames
    private int velX; // vel. horizontal do laser
    private int velY; // vel. vertical do laser
    private String cannonType; // tipo de canhão

    // construtor
    public Laser(int px, int py, int largura, int altura, List<String> imagemPaths, int velX, int velY, String cannonType) {
        super(px, py, largura, altura); 
        this.imagens = new ArrayList<>();
        
        for (String path : imagemPaths) {
            this.imagens.add(new ImageIcon(path)); 
        }
        
        this.cannonType = cannonType;
        this.velX = velX; 
        this.velY = velY;
        setAtivo(true); // define o laser como ativo quando é criado
    }

    public String getCannonType() {
        return cannonType;
    }

    // atualizar o estado do laser a cada quadro do jogo
    @Override
    public void atualiza() {
        incPx(velX); // move o laser na direção horizontal
        incPy(velY); // move o laser na direção vertical

        // atualiza o frame da animação
        frameDelayCounter++;
        if (frameDelayCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % imagens.size(); // avança para o próximo frame
            frameDelayCounter = 0; // reinicia o contador de atraso
        }

        // verifica se o laser saiu da tela
        if (getPy() < 0 || getPx() < 0 || getPx() > 800) {
            setAtivo(false); // desativa o laser se sair da tela
        }
    }


    // desenhar o laser na tela
    @Override
    public void desenha(Graphics2D g) {
        // se o laser estiver ativo
        if (isAtivo()) { 
            // desenha o frame atual na posição especificada
            imagens.get(currentFrame).paintIcon(null, g, getPx(), getPy());
        }
    }
}
