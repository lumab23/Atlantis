package game; 

import javax.swing.*; 

public class AtlantisGame { 

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> { // inicialização da GUI na thread de despacho de eventos
            JFrame frame = new JFrame("Atlantis"); // definindo o título da janela = Atlantis
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            frame.setResizable(false); // não pode redimensionar a janela

            GamePanel gamePanel = new GamePanel(); 
            frame.add(gamePanel);  
            frame.pack(); // para ajustar o tamanho da janela
            frame.setLocationRelativeTo(null); 
            frame.setVisible(true); // janela fica visível

            gamePanel.requestFocusInWindow(); // foco do painel do jogo
        });
    }
}
