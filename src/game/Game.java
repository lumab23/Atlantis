package game; 

// importar classes/pacotes
import br.com.mvbos.lgj.base.Elemento;
import br.com.mvbos.lgj.base.Util; 
import javax.swing.*;
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent; 
import java.util.*; 
import javax.sound.sampled.*;
import javax.swing.Timer;
import java.io.File;

public class Game {

    // variaveis para armazenar as estruturas de atlantis
    private Cidade cidade1, cidade2, cidade3;
    private Gerador gerador1, gerador2, gerador3;
    private Cannon cannon1, cannon2, cannon3;

    private List<Elemento> elementos = new ArrayList<>(); // lista de elementos
    private List<Laser> tiros = new ArrayList<>(); // lista de tiros 
    private List<EnemyLaser> lasers = new ArrayList<>(); // lista de lasers do inimigo
    private List<InvaderShip> inimigos = new ArrayList<>(); // lista de naves inimigas 
    private List<Integer> yPositionsOccupied = new ArrayList<>(); // lista de pos. ocupadas no eixo Y
    private List<Explosao> explosoes = new ArrayList<>(); // lista de explosões 
    private Map<String, Long> lastShotTime = new HashMap<>(); // mapa para registrar o último momento de disparo em cada direção
    private static final int COOLDOWN_TIME = 300; // tempo de recarga do disparo
    private static final int INVADER_SPAWN_TIMER_INTERVAL = 2000;
    private Ranking ranking;
    private Timer invaderSpawnTimer;
    private int elementosDestruidos = 0; // inicializando as estruturas destruídas com 0
    private boolean gameStarted = false; // indica se o jogo está em andamento
    private int score = 0; // pontuação do jogador
    private boolean isShootingCenter = false;
    private boolean isShootingLeft = false;
    private boolean isShootingRight = false;

    // listas de imagens para os tiros e explosões
    private List<String> imagensTiros = List.of(
            "resources/spr_bulllet_0.png",
            "resources/spr_bulllet_1.png",
            "resources/spr_bulllet_2.png",
            "resources/spr_bulllet_3.png"
    );

    private List<String> imagensExplosao = List.of(
            "resources/spr_shipExplosion_0.png",
            "resources/spr_shipExplosion_1.png",
            "resources/spr_shipExplosion_2.png"
    );


    // game construtor
    public Game(GamePanel gamePanel) {

        // inicializa os tempos de disparo
        // 3L : canhão pronto para disparar imediatamente
        lastShotTime.put("CENTER", 0L);
        lastShotTime.put("LEFT", 0L);
        lastShotTime.put("RIGHT", 0L);

        ranking = new Ranking();
        
        // inicializa as cidades 
        cidade1 = new Cidade(80, 490, 80, 32, "resources/spr_city1_0.png");
        cidade2 = new Cidade(185, 433, 80, 32, "resources/spr_city2_0.png");
        cidade3 = new Cidade(478, 464, 82, 32, "resources/spr_city3_0.png");

        elementos.add(cidade1);
        elementos.add(cidade2);
        elementos.add(cidade3);
        
        // inicializa os geradores
        gerador1 = new Gerador(310, 416, 50, 50, "resources/spr_gen1_0.png", "resources/spr_gen1_1.png");
        gerador2 = new Gerador(408, 384, 50, 50, "resources/spr_gen2_0.png", "resources/spr_gen2_1.png");
        gerador3 = new Gerador(710, 414, 50, 50, "resources/spr_gen3_0.png", "resources/spr_gen3_1.png");

        elementos.add(gerador1);
        elementos.add(gerador2);
        elementos.add(gerador3);

        // inicializa os canhões
        cannon1 = new Cannon(355, 353, 50, 50, "resources/spr_mainCannon_0.png", true);
        cannon2 = new Cannon(758, 354, 50, 50, "resources/spr_rightCannon_0.png", false);
        cannon3 = new Cannon(2, 384, 50, 50, "resources/spr_leftCannon_0.png", false);

        elementos.add(cannon1);
        elementos.add(cannon2);
        elementos.add(cannon3);

        // temporizador para alternar os estados e atualizar o jogo
        Timer timer = new Timer(100, e -> {
            if (gameStarted) { // se o jogo esiver em andamento
                for (Elemento elem : elementos) {
                    if (elem instanceof Gerador) {
                        ((Gerador) elem).alternarEstado(); // alterna o estado dos geradores
                    }
                }
                for (Laser l : tiros) {
                    l.atualiza(); // atualiza a posição dos tiros
                }

                
                for (InvaderShip inimigo : inimigos) {
                    inimigo.atualiza(); // atualiza a posição das naves inimigas
                }
                 
                atualizarInimigos(); // atualize os inimigos

                for (Explosao explosion : explosoes) {
                    explosion.atualiza(); // atualiza a animação das explosões
                }

                verificarColisoes();
                criarNovosInimigos(); 
            }
            gamePanel.repaint(); // redesenha o painel do jogo
        });
        timer.start(); // inicia o temporizador

        // tempo para controlar o intervalo de aparição dos invasores
        invaderSpawnTimer = new Timer(INVADER_SPAWN_TIMER_INTERVAL, e -> {
            if (gameStarted) {
                criarNovosInimigos(); // cria novas naves inimigas
            }
        });
        invaderSpawnTimer.start(); // inicia o tempo de aparição dos invasores

        // listener de teclado para lidar com os disparos
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_SPACE) {
                    if (!gameStarted) {
                        gameStarted = true; // inicia o jogo 
                        playSound("audios/sound_audio_snd_backgroundnoise.wav");
                    } else {
                        shoot("CENTER"); // dispara a partir do canhão central/command post
                    }
                } else if (key == KeyEvent.VK_LEFT && gameStarted) {
                    shoot("RIGHT"); // dispara a partir do canhão direito
                } else if (key == KeyEvent.VK_RIGHT && gameStarted) {
                    shoot("LEFT"); // dispara a partir do canhão esquerdo
                }
            }
        });
        gamePanel.setFocusable(true); 
    }

    // getters
    public List<Elemento> getElementos() {
        return elementos;
    }

    public List<Laser> getTiros() {
        return tiros;
    }

    public List<InvaderShip> getInvaderShips() {
        return inimigos;
    }

    public List<Explosao> getExplosoes() {
        return explosoes;
    }


    public List<EnemyLaser> getLasers() {
        return lasers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    // método de tiro
    private void shoot(String direction) {
        long currentTime = System.currentTimeMillis(); // Obter o tempo atual em milissegundos
        boolean canShoot = false;
        
        // Verifica se o canhão correspondente não está disparando atualmente
        if (direction.equals("CENTER") && !isShootingCenter && (currentTime - lastShotTime.get(direction) >= COOLDOWN_TIME)) {
            isShootingCenter = true;
            canShoot = true;
        } else if (direction.equals("LEFT") && !isShootingLeft && (currentTime - lastShotTime.get(direction) >= COOLDOWN_TIME)) {
            isShootingLeft = true;
            canShoot = true;
        } else if (direction.equals("RIGHT") && !isShootingRight && (currentTime - lastShotTime.get(direction) >= COOLDOWN_TIME)) {
            isShootingRight = true;
            canShoot = true;
        }

        if (!canShoot) {
            return; // Se não puder disparar, sai do método
        }

        lastShotTime.put(direction, currentTime); // Atualiza o último tempo de disparo

        for (Elemento elem : elementos) {
            if (elem instanceof Cannon) {
                Cannon cannon = (Cannon) elem;
                int bulletX = cannon.getPx() + cannon.getLargura() / 2 - 5; // Centralizar a bala no canhão
                int bulletY = cannon.getPy();
                int velX = 0;
                int velY = -50; // Canhões disparam verticalmente para cima
                String cannonType = "";

                if (direction.equals("CENTER") && cannon.getPx() == 355) {
                    // Canhão central dispara diretamente para cima
                    cannonType = "COMMAND";
                    tiros.add(new Laser(bulletX, bulletY, 10, 20, imagensTiros, velX, velY, cannonType));
                    playSound("audios/sound_audio_snd_fire.wav");
                } else if (direction.equals("LEFT") && cannon.getPx() == 2) {
                    // Canhão esquerdo dispara para a direita
                    cannonType = "SENTRY";
                    velX = 120;
                    tiros.add(new Laser(bulletX, bulletY, 10, 20, imagensTiros, velX, velY, cannonType));
                    playSound("audios/sound_audio_snd_fire.wav");
                } else if (direction.equals("RIGHT") && cannon.getPx() == 758) {
                    // Canhão direito dispara para a esquerda
                    cannonType = "SENTRY";
                    velX = -120;
                    tiros.add(new Laser(bulletX, bulletY, 10, 20, imagensTiros, velX, velY, cannonType));
                    playSound("audios/sound_audio_snd_fire.wav");
                }
            }
        }

        // Definir um temporizador para redefinir o estado de disparo após o tempo de recarga
        Timer cooldownTimer = new Timer(COOLDOWN_TIME, e -> {
            if (direction.equals("CENTER")) {
                isShootingCenter = false;
            } else if (direction.equals("LEFT")) {
                isShootingLeft = false;
            } else if (direction.equals("RIGHT")) {
                isShootingRight = false;
            }
        });
        cooldownTimer.setRepeats(false); // O temporizador deve disparar apenas uma vez
        cooldownTimer.start(); // Inicia o temporizador
    }


    // método para criar novos inimigo
    private void criarNovosInimigos() {
        // limite máximo de inimigos na tela
        int MAX_INVADERS_ON_SCREEN = 3;

        // se o número de inimigos atuais é menor que o limite máximo
        if (inimigos.size() >= MAX_INVADERS_ON_SCREEN) {
            return; // não cria novos inimigos se já houver o número máximo na tela
        }

        boolean spaceFree = true;
        for (InvaderShip inimigo : inimigos) {
            if ((inimigo.getPx() >= -40 && inimigo.getPx() <= 80) ||
                    (inimigo.getPx() >= 720 && inimigo.getPx() <= 800)) {
                spaceFree = false;
                break;
            }
        }

        if (!spaceFree) {
            return;
        }

        if (Math.random() < 0.05) { // 5% chance to create a new invader each update
            int px;
            int velX;
            boolean isLeftToRight;
            boolean isBanditBomber = score >= 3000 && (Math.random() < 0.2); // 20% chance de criar um Bandit Bomber após 3000 pontos

            InvaderShip.tipos tipo;

            if (Math.random() < 0.5) {
                // invasores vindo da esquerda para a direita
                px = -40; // inicia fora da tela à esquerda
                if (isBanditBomber) {
                    velX = 22; // velocidade maior para Bandit Bomber
                    tipo = InvaderShip.tipos.GORGON3_LEFT;
                } else if (Math.random() < 0.5) {
                    velX = (int) (2.0 + (score / 500)); // velocidade para gorgon 1
                    tipo = InvaderShip.tipos.GORGON1_LEFT;
                } else {
                    velX = (int) (2.0 + (score / 500)); // velocidade para Gorgon 2
                    tipo = InvaderShip.tipos.GORGON2_LEFT;
                }
                isLeftToRight = true;
            } else {
                // invasores vindo da direita para a esquerda
                px = 800; // inicia fora da tela à direita
                if (isBanditBomber) {
                    velX = -22; // celocidade maior para Bandit Bomber
                    tipo = InvaderShip.tipos.GORGON3_RIGHT;
                } else if (Math.random() < 0.5) {
                    velX = (int) (-2.0 - (score / 500)); // celocidade para Gorgon 1
                    tipo = InvaderShip.tipos.GORGON1_RIGHT;
                } else {
                    velX = (int) (-2.0 - (score / 500)); // celocidade para Gorgon 2
                    tipo = InvaderShip.tipos.GORGON2_RIGHT;
                }
                isLeftToRight = false;
            }

            // posição única do Y
            int py;
            do {
                py = (int) (Math.random() * 100) + 20; // random Y position between 20 and 120
            } while (yPositionsOccupied.contains(py));

            // adiciona a nova posição Y à lista de ocupadas
            yPositionsOccupied.add(py);

            InvaderShip novoInimigo = new InvaderShip(px, py, 40, 40, tipo, velX, 0, isLeftToRight);
            inimigos.add(novoInimigo);
        }
    }

    // método para atualizar a posição dos inimigos e remover os que saírem da tela
    private void atualizarInimigos() {
        List<InvaderShip> inimigosParaRemover = new ArrayList<>();

        for (InvaderShip inimigo : inimigos) {
            inimigo.atualiza();
            if (inimigo.getPx() < -40 || inimigo.getPx() > 800) {
                // atualiza a posição vertical quando o inimigo sai da tela
                inimigo.setPy(inimigo.getPy() + 40);
                if (inimigo.getPy() > 400) { // limite para não ficar muito perto da cidade
                    inimigosParaRemover.add(inimigo);
                } else {
                    // reposiciona horizontalmente
                    inimigo.setPx((inimigo.getPx() < -40) ? 800 : -40);
                }
            }
        }

        // remover inimigos e liberar as posições Y ocupadas
        for (InvaderShip inimigo : inimigosParaRemover) {
            yPositionsOccupied.remove(Integer.valueOf(inimigo.getPy()));
        }
        inimigos.removeAll(inimigosParaRemover);
    }

    private void destruirEstrutura() {
        elementosDestruidos++;
        if (elementosDestruidos == 7) {
            gameOver();
        }
    }



    private void verificarColisoes() {
        // lista para armazenar os tiros que devem ser removidos
        List<Laser> tirosParaRemover = new ArrayList<>();
        // lista para armazenar os inimigos que devem ser removidos
        List<InvaderShip> inimigosParaRemover = new ArrayList<>();
        List<EnemyLaser> lasersParaRemover = new ArrayList<>();
        List<Elemento> elementosParaRemover = new ArrayList<>();
        boolean banditBomberHit = false;
    
        // loop através de todos os tiros no jogo
        for (Laser tiro : new ArrayList<>(tiros)) { // itera sobre uma cópia da lista
            // loop através de todas as naves inimigas no jogo
            for (InvaderShip inimigo : new ArrayList<>(inimigos)) { // itera sobre uma cópia da lista
                // Verifica se há colisão entre o tiro e o inimigo
                if (Util.colide(tiro, inimigo)) {
                    // Adiciona o tiro à lista de remoção
                    tirosParaRemover.add(tiro);
                    // Adiciona o inimigo à lista de remoção
                    inimigosParaRemover.add(inimigo);
                    // Cria uma explosão na posição do inimigo
                    criarExplosao(inimigo.getPx(), inimigo.getPy());
                    // Toca o som de explosão
                    playSound("audios/sound_audio_snd_explosion1.wav");
                    // Incrementa a pontuação do jogador com o valor da pontuação do inimigo
                    score += calculatePoints(tiro, inimigo);
    
                    // Verifica se o inimigo é um Bandit Bomber
                    if (inimigo.getTipo() == InvaderShip.tipos.GORGON3_LEFT || inimigo.getTipo() == InvaderShip.tipos.GORGON3_RIGHT) {
                        banditBomberHit = true;
                    }
                }
            }
        }
    
        // Se um Bandit Bomber foi atingido, explode todas as naves restantes
        if (banditBomberHit) {
            for (InvaderShip inimigo : new ArrayList<>(inimigos)) { // Itera sobre uma cópia da lista
                criarExplosao(inimigo.getPx(), inimigo.getPy());
                playSound("audios/sound_audio_snd_explosion1.wav");
                inimigosParaRemover.add(inimigo);
                score += calculatePoints(null, inimigo); // Adiciona a pontuação para todas as naves atingidas
            }
        }
    
        // Remove todos os tiros que colidiram
        tiros.removeAll(tirosParaRemover);
        // Remove todos os inimigos que foram atingidos
        inimigos.removeAll(inimigosParaRemover);
    
    // Colisão de lasers inimigos com estruturas da cidade
    for (InvaderShip inimigo : new ArrayList<>(inimigos)) { // Itera sobre uma cópia da lista
        if (inimigo.getLaser().isAtivo()) {
            playSound("audios/sound_audio_snd_laser.wav");
            for (Elemento elem : new ArrayList<>(elementos)) { // Itera sobre uma cópia da lista
                if (Util.colide(inimigo.getLaser(), elem)) {
                    System.out.println("Colidiu");
                    if (elem instanceof Cidade || elem instanceof Gerador || 
                        (elem instanceof Cannon && ((Cannon) elem).isDestrutivel())) { // Verifica se o canhão é destrutível
                        if (elem.isAtivo()) {
                            elem.setAtivo(false);
                            elementosParaRemover.add(elem);
                            destruirEstrutura();
                        }
                    }
                    // marca o laser para remover
                    lasersParaRemover.add(inimigo.getLaser());
                    inimigo.getLaser().setAtivo(false); // desativa o laser
                    break; // quebra o loop para prevenir outras colisões
                }
            }
        }
    }

    
        // Remove collided elements from the game
        elementos.removeAll(elementosParaRemover);
        // Remove collided lasers from the game
        lasers.removeAll(lasersParaRemover);
    
        if (elementosParaRemover.size() == 7) {
            gameOver();
        }
    }
    
    

    private int calculatePoints(Laser tiro, InvaderShip inimigo) {
        int points = 0;
        String cannonType = (tiro != null) ? tiro.getCannonType() : "COMMAND"; // assume "command" se tiro for nulo

        switch (inimigo.getTipo()) {
            case GORGON1_LEFT:
            case GORGON1_RIGHT:
            case GORGON2_LEFT:
            case GORGON2_RIGHT:
                points = cannonType.equals("COMMAND") ? 100 : 200;
                break;
            case GORGON3_LEFT:
            case GORGON3_RIGHT:
                points = cannonType.equals("COMMAND") ? 1000 : 2000;
                break;
        }
        return points;
    }

    // Método para criar uma explosão
    private void criarExplosao(int px, int py) {
        explosoes.add(new Explosao(px, py, 40, 40, imagensExplosao, 2));
    }

    private void gameOver() {
        gameStarted = false;
        String nomeJogador = JOptionPane.showInputDialog(null, "Game Over! Sua pontuação: " + score + ". \nInforme seu nome:");
        if (nomeJogador != null && !nomeJogador.trim().isEmpty()) {
            ranking.adicionarJogador(new Jogador(nomeJogador.trim(), score));
        }
        mostrarRanking();

        int option = JOptionPane.showConfirmDialog(null, "Deseja jogar novamente?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.NO_OPTION) {
            System.exit(0); // sai do jogo
        } else {
            resetGame(); // reinicia o jogo
        }
    
    }

    // mostrar o ranking dos jogadores
    private void mostrarRanking() {
        String rankingStr = ranking.getRankingString();
        JOptionPane.showMessageDialog(null, rankingStr);
    }

    private void resetGame() {
        // reinicia todas as variáveis e listas para os valores iniciais
        elementos.clear();
        tiros.clear();
        lasers.clear();
        inimigos.clear();
        yPositionsOccupied.clear();
        explosoes.clear();
        elementosDestruidos = 0;
        score = 0;
    
        cidade1 = new Cidade(80, 490, 80, 32, "resources/spr_city1_0.png");
        cidade2 = new Cidade(185, 433, 80, 32, "resources/spr_city2_0.png");
        cidade3 = new Cidade(478, 464, 82, 32, "resources/spr_city3_0.png");
    
        elementos.add(cidade1);
        elementos.add(cidade2);
        elementos.add(cidade3);
    
        gerador1 = new Gerador(310, 416, 50, 50, "resources/spr_gen1_0.png", "resources/spr_gen1_1.png");
        gerador2 = new Gerador(408, 384, 50, 50, "resources/spr_gen2_0.png", "resources/spr_gen2_1.png");
        gerador3 = new Gerador(710, 414, 50, 50, "resources/spr_gen3_0.png", "resources/spr_gen3_1.png");
    
        elementos.add(gerador1);
        elementos.add(gerador2);
        elementos.add(gerador3);
    
        cannon1 = new Cannon(355, 353, 50, 50, "resources/spr_mainCannon_0.png", true);
        cannon2 = new Cannon(758, 354, 50, 50, "resources/spr_rightCannon_0.png", false);
        cannon3 = new Cannon(2, 384, 50, 50, "resources/spr_leftCannon_0.png", false);
    
        elementos.add(cannon1);
        elementos.add(cannon2);
        elementos.add(cannon3);
    
        lastShotTime.put("CENTER", 0L);
        lastShotTime.put("LEFT", 0L);
        lastShotTime.put("RIGHT", 0L);
    
        gameStarted = true; // reinicia o jogo
        playSound("audios/sound_audio_snd_backgroundnoise.wav");
    }
    

    // Método para tocar o som
    private void playSound(String soundFile) {
        try {
            // Cria um objeto File para o arquivo de áudio especificado pelo caminho soundFile
            File audioFile = new File(soundFile);
            // Obtém um fluxo de entrada de áudio a partir do arquivo de áudio
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            // Obtém um Clip de áudio, que é uma linha que pode ser carregada com dados de áudio
            Clip clip = AudioSystem.getClip();
            // Abre o Clip para carregar o fluxo de entrada de áudio
            clip.open(audioStream);
            // Inicia a reprodução do áudio
            clip.start();
        } catch (Exception e) {
            // Em caso de qualquer exceção, imprime a stack trace para ajudar na depuração
            e.printStackTrace();
        }
    }
}
