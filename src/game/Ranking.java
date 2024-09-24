package game;

import java.io.*;
import java.util.*;

public class Ranking {

    private List<Jogador> jogadores;

    public Ranking() {
        this.jogadores = new ArrayList<>();
        carregarRanking(); // ao iniciar, carrega o ranking do arquivo
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void adicionarJogador(Jogador jogador) {
        jogadores.add(jogador); // ad. o jogador a lista
        Collections.sort(jogadores); // ordena a lista de jogadores
        salvarRanking(); // salva a lista atualizada no arquivo
    }

    private void carregarRanking() {
        File file = new File("ranking.txt");
        
        // se o arquivo não existir cria um novo arquivo
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // tenta ler o arquivo linha por linha
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(" - ");
                if (dados.length == 2) {
                    String nome = dados[0];
                    int pontuacao = Integer.parseInt(dados[1]);
                    jogadores.add(new Jogador(nome, pontuacao));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarRanking() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("ranking.txt"))) {
            for (Jogador jogador : jogadores) {
                pw.println(jogador.getNome() + " - " + jogador.getPontuacao());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // exibir o ranking no console
    public void exibirRanking() {
        for (int i = 0; i < Math.min(jogadores.size(), 10); i++) {
            System.out.println((i + 1) + ". " + jogadores.get(i)); // exibe os top 10 jogadores
        }
    }

    // obter o ranking como uma string formatada
    public String getRankingString() {
        StringBuilder rankingStr = new StringBuilder("Ranking:\n");
        for (int i = 0; i < Math.min(jogadores.size(), 10); i++) {
            rankingStr.append((i + 1)).append(". ").append(jogadores.get(i)).append("\n");
        }
        return rankingStr.toString();
    }
}
