import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

final class Posicao {
    final int linha;
    final int coluna;

    Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    boolean igual(int outraLinha, int outraColuna) {
        return linha == outraLinha && coluna == outraColuna;
    }
}

/**
 * Justificativa da escolha por heranca (em vez de composicao) :
 * as tres restricoes compartilham EXATAMENTE o mesmo algoritmo de
 * verificacao (percorrer um grupo de celulas e checar repeticao) e
 * diferem apenas em COMO esse grupo e' calculado esse e' o caso
 * classico onde heranca com metodo template evita duplicacao de
 * codigo de forma legitima, e nao apenas decorativa.
 */
abstract class Restricao {

    protected abstract List<Posicao> celulasRelacionadas(int linha, int coluna);

    public boolean violada(Tabuleiro tab, int linha, int coluna, int valor) {
        for (Posicao p : celulasRelacionadas(linha, coluna)) {
            if (!p.igual(linha, coluna) && tab.valorEm(p.linha, p.coluna) == valor) {
                return true;
            }
        }
        return false;
    }
}

class RestricaoLinha extends Restricao {
    @Override
    protected List<Posicao> celulasRelacionadas(int linha, int coluna) {
        List<Posicao> posicoes = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            posicoes.add(new Posicao(linha, i));
        }
        return posicoes;
    }
}

class RestricaoColuna extends Restricao {
    @Override
    protected List<Posicao> celulasRelacionadas(int linha, int coluna) {
        List<Posicao> posicoes = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            posicoes.add(new Posicao(i, coluna));
        }
        return posicoes;
    }
}
class RestricaoBloco extends Restricao {
    @Override
    protected List<Posicao> celulasRelacionadas(int linha, int coluna) {
        List<Posicao> posicoes = new ArrayList<>();
        int blocoLinha = (linha / 3) * 3;
        int blocoColuna = (coluna / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                posicoes.add(new Posicao(blocoLinha + i, blocoColuna + j));
            }
        }
        return posicoes;
    }
}

class Tabuleiro {
    private static final int N = 9;
    private final int[][] celulas = new int[N][N];
    private final List<Restricao> restricoes = new ArrayList<>();

    Tabuleiro() {
        restricoes.add(new RestricaoLinha());
        restricoes.add(new RestricaoColuna());
        restricoes.add(new RestricaoBloco());
    }

    public int tamanho() {
        return N;
    }

    public int valorEm(int linha, int coluna) {
        return celulas[linha][coluna];
    }

    public boolean vazia(int linha, int coluna) {
        return celulas[linha][coluna] == 0;
    }

    public void definir(int linha, int coluna, int valor) {
        celulas[linha][coluna] = valor;
    }

    public void limpar(int linha, int coluna) {
        celulas[linha][coluna] = 0;
    }

    public boolean aceitaValor(int linha, int coluna, int valor) {
        for (Restricao r : restricoes) {
            if (r.violada(this, linha, coluna, valor)) {
                return false;
            }
        }
        return true;
    }

    public boolean entradaValida() {
        for (int linha = 0; linha < N; linha++) {
            for (int coluna = 0; coluna < N; coluna++) {
                int valor = celulas[linha][coluna];
                if (valor != 0) {
                    celulas[linha][coluna] = 0;
                    boolean aceita = aceitaValor(linha, coluna, valor);
                    celulas[linha][coluna] = valor;
                    if (!aceita) return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int linha = 0; linha < N; linha++) {
            for (int coluna = 0; coluna < N; coluna++) {
                sb.append(celulas[linha][coluna]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
class SudokuSolver {
    public boolean resolver(Tabuleiro tab) {
        int n = tab.tamanho();
        for (int linha = 0; linha < n; linha++) {
            for (int coluna = 0; coluna < n; coluna++) {
                if (tab.vazia(linha, coluna)) {
                    for (int valor = 1; valor <= 9; valor++) {
                        if (tab.aceitaValor(linha, coluna, valor)) {
                            tab.definir(linha, coluna, valor);
                            if (resolver(tab)) return true;
                            tab.limpar(linha, coluna); // backtrack
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
class LeitorDeEntrada {
    public Tabuleiro ler(Scanner leitor, int tamanho) {
        Tabuleiro tabuleiro = new Tabuleiro();
        for (int linha = 0; linha < tamanho; linha++) {
            if (!leitor.hasNextLine()) return null;
            String linhaTexto = leitor.nextLine();
            if (linhaTexto.length() != tamanho) return null;
            for (int coluna = 0; coluna < tamanho; coluna++) {
                char ch = linhaTexto.charAt(coluna);
                if (ch < '0' || ch > '9') return null;
                tabuleiro.definir(linha, coluna, ch - '0');
            }
        }
        return tabuleiro;
    }
}

public class Sudoku {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        LeitorDeEntrada leitorDeEntrada = new LeitorDeEntrada();
        Tabuleiro tabuleiro = leitorDeEntrada.ler(leitor, 9);
        leitor.close();

        if (tabuleiro == null) {
            System.out.println("ENTRADA INVALIDA");
            return;
        }

        SudokuSolver solver = new SudokuSolver();
        if (!tabuleiro.entradaValida() || !solver.resolver(tabuleiro)) {
            System.out.println("SEM SOLUCAO");
            return;
        }

        System.out.print(tabuleiro);
    }
}
