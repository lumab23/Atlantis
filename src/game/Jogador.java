package game;

public class Jogador implements Comparable<Jogador> {

    // atributos do jogador
    private String nome;
    private int pontuacao;

    // construtor
    public Jogador(String nome, int pontuacao) {
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    // getters 
    public String getNome() {
        return nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    @Override
    public int compareTo(Jogador outroJogador) {
        return Integer.compare(outroJogador.getPontuacao(), this.pontuacao);
    }

    @Override
    public String toString() {
        return nome + " - " + pontuacao;
    }
}
