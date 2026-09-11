// [P4-ETAPA-04] Implementacao orientada a objetos
// Paradigma: Orientacao a Objetos
// Linguagem: Java
//
// Contrato (ver docs/especificacao.md):
//   Entrada: 9 linhas de 9 digitos (0 = celula vazia) via stdin.
//   Saida:   9 linhas de 9 digitos (solucao completa) via stdout,
//            ou a linha "SEM SOLUCAO".
//
// Estilo OOP: o estado (o tabuleiro) e' encapsulado numa classe,
// que expoe metodos de consulta e alteracao controlada, em vez de
// arrays manipulados livremente por funcoes externas. A logica de
// resolucao fica separada, atuando sobre a interface do Tabuleiro.

import java.util.Scanner;

/** Representa o tabuleiro do Sudoku e encapsula suas regras de validade. */
class Tabuleiro {
    private static final int N = 9;
    private final int[][] celulas = new int[N][N];

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

    /** verificação de regras do sudoku */
    public boolean aceitaValor(int linha, int coluna, int valor) {
        for (int i = 0; i < N; i++) {
            if (celulas[linha][i] == valor) return false;
            if (celulas[i][coluna] == valor) return false;
        }
        int blocoLinha = (linha / 3) * 3;
        int blocoColuna = (coluna / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (celulas[blocoLinha + i][blocoColuna + j] == valor) return false;
            }
        }
        return true;
    }

    /** Confirma que as celulas ja preenchidas nao se contradizem entre si. */
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

/** interage apenas pela interface publica da classe Tabuleiro. */
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

public class Sudoku {
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        Scanner leitor = new Scanner(System.in);

        for (int linha = 0; linha < tabuleiro.tamanho(); linha++) {
            String linhaTexto = leitor.next();
            for (int coluna = 0; coluna < tabuleiro.tamanho(); coluna++) {
                tabuleiro.definir(linha, coluna, linhaTexto.charAt(coluna) - '0');
            }
        }
        leitor.close();

        SudokuSolver solver = new SudokuSolver();
        if (!tabuleiro.entradaValida() || !solver.resolver(tabuleiro)) {
            System.out.println("SEM SOLUCAO");
            return;
        }

        System.out.print(tabuleiro);
    }
}
