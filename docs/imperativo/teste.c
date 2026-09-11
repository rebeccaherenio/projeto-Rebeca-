/*
 * [P4-ETAPA-03] Implementacao imperativa
 * Paradigma: Imperativo
 * Linguagem: C
 *
 * Contrato (ver docs/especificacao.md):
 *   Entrada: 9 linhas de 9 digitos (0 = celula vazia) via stdin.
 *   Saida:   9 linhas de 9 digitos (solucao completa) via stdout,
 *            ou a linha "SEM SOLUCAO".
 *
 * Estilo imperativo: estado mutavel explicito (matriz global),
 * controle de fluxo por lacos e recursao com backtracking, sem
 * abstracoes de dados alem de arrays.
 */

#include <stdio.h>

#define N 9

static int tabuleiro[N][N];

/* Verifica se e' seguro colocar 'valor' na posicao (linha, coluna),
 * dado o estado atual do tabuleiro (mutavel, consultado diretamente). */
static int posicao_segura(int linha, int coluna, int valor) {
    int i, j, bloco_linha, bloco_coluna;

    for (i = 0; i < N; i++) {
        if (tabuleiro[linha][i] == valor) return 0;   /* linha */
        if (tabuleiro[i][coluna] == valor) return 0;  /* coluna */
    }

    bloco_linha = (linha / 3) * 3;
    bloco_coluna = (coluna / 3) * 3;
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            if (tabuleiro[bloco_linha + i][bloco_coluna + j] == valor) return 0;
        }
    }
    return 1;
}

/* Valida a entrada: confere se as celulas ja preenchidas nao violam
 * as regras entre si (pre-condicao do contrato). */
static int entrada_valida(void) {
    int linha, coluna, valor;
    for (linha = 0; linha < N; linha++) {
        for (coluna = 0; coluna < N; coluna++) {
            valor = tabuleiro[linha][coluna];
            if (valor != 0) {
                tabuleiro[linha][coluna] = 0;
                if (!posicao_segura(linha, coluna, valor)) {
                    tabuleiro[linha][coluna] = valor;
                    return 0;
                }
                tabuleiro[linha][coluna] = valor;
            }
        }
    }
    return 1;
}

/* Backtracking classico: percorre celulas vazias em ordem, tenta
 * valores de 1 a 9, retrocede em caso de falha. Estado mutado
 * diretamente na matriz global (efeito colateral controlado). */
static int resolver(void) {
    int linha, coluna, valor;

    for (linha = 0; linha < N; linha++) {
        for (coluna = 0; coluna < N; coluna++) {
            if (tabuleiro[linha][coluna] == 0) {
                for (valor = 1; valor <= 9; valor++) {
                    if (posicao_segura(linha, coluna, valor)) {
                        tabuleiro[linha][coluna] = valor;
                        if (resolver()) return 1;
                        tabuleiro[linha][coluna] = 0; /* backtrack */
                    }
                }
                return 0; /* nenhum valor funcionou nesta celula */
            }
        }
    }
    return 1; /* nenhuma celula vazia restante */
}

static void ler_entrada(void) {
    int linha, coluna;
    char c;
    for (linha = 0; linha < N; linha++) {
        for (coluna = 0; coluna < N; coluna++) {
            scanf(" %c", &c);
            tabuleiro[linha][coluna] = c - '0';
        }
    }
}

static void imprimir_tabuleiro(void) {
    int linha, coluna;
    for (linha = 0; linha < N; linha++) {
        for (coluna = 0; coluna < N; coluna++) {
            printf("%d", tabuleiro[linha][coluna]);
        }
        printf("\n");
    }
}

int main(void) {
    ler_entrada();

    if (!entrada_valida() || !resolver()) {
        printf("SEM SOLUCAO\n");
        return 0;
    }

    imprimir_tabuleiro();
    return 0;
}
