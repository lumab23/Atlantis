package game; 

// importar classes/pacotes
import br.com.mvbos.lgj.base.Elemento; 
import br.com.mvbos.lgj.base.Texto;
import javax.swing.*; 
import java.awt.*; 


public class GamePanel extends JPanel {

    private Game game;

    //construtor
    public GamePanel() {
        setPreferredSize(new Dimension(800, 600)); // 800x600 pixels
        setBackground(Color.BLACK); 
        game = new Game(this); // cria um novo objeto do jogo
    }

    // método para exibir a pontuação na tela
    public void mostrarPontuacao(Graphics2D g) {
        Texto texto = new Texto();
        texto.setCor(Color.YELLOW);

        int larguraTela = 800;
        int alturaTela = 600;

        String pontuacao = "" + game.getScore();
        int x = larguraTela / 2 - g.getFontMetrics(texto.getFonte()).stringWidth(pontuacao) / 2; // centraliza horizontalmente
        int y = alturaTela - 30; // posição Y para ficar próximo à parte inferior da tela

        texto.desenha(g, pontuacao, x, y); // desenhando a pontuação na tela
    }

    // desenhar os componentes do jogo no painel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawCityStructures(g); 
        mostrarPontuacao((Graphics2D) g); 
    }

    // desenhar as estruturas da cidade no painel
    private void drawCityStructures(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; // converte o objeto Graphics em Graphics2D

        // desenha a água
        g2d.setColor(new Color(0, 49, 99, 255)); 
        g2d.fillRect(0, 410, 800, 600 - 410); 

        // desenha o force field
        ImageIcon forceField = new ImageIcon("resources/linha_do_mar.png"); // cria um ImageIcon com a imagem do force field
        forceField.paintIcon(this, g2d, 0, 5); // desenha o force field no painel

        // desenha os tiros do jogador
        for (Laser l : game.getTiros()) {
            l.desenha(g2d);
        }

        // desenha as naves inimigas
        for (InvaderShip inimigo : game.getInvaderShips()) {
            inimigo.desenha(g2d); 
        }

        // desenha o chão
        ImageIcon ground = new ImageIcon("resources/ground.png"); // cria um ImageIcon com a imagem do chão
        ground.paintIcon(this, g2d, 0, 0); // Desenha a imagem do chão no painel

        // desenha todos os elementos do jogo
        for (Elemento elem : game.getElementos()) {
            elem.desenha(g2d); 
        }

        // desenha todas as explosões
        for (Explosao explosion : game.getExplosoes()) {
            explosion.desenha(g2d); 
        }

        // Remove os tiros inativos da lista
        game.getTiros().removeIf(tiro -> !tiro.isAtivo());
    }

    

}
