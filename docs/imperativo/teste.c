#include <stdio.h>

#define N 9

static int tabuleiro[N][N];

static int posicao_segura(int linha, int coluna, int valor) {
    int i, j, bloco_linha, bloco_coluna;

    for (i = 0; i < N; i++) {
        if (tabuleiro[linha][i] == valor) return 0;   
        if (tabuleiro[i][coluna] == valor) return 0;
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

static int resolver(void) {
    int linha, coluna, valor;

    for (linha = 0; linha < N; linha++) {
        for (coluna = 0; coluna < N; coluna++) {
            if (tabuleiro[linha][coluna] == 0) {
                for (valor = 1; valor <= 9; valor++) {
                    if (posicao_segura(linha, coluna, valor)) {
                        tabuleiro[linha][coluna] = valor;
                        if (resolver()) return 1;
                        tabuleiro[linha][coluna] = 0;
                    }
                }
                return 0; 
            }
        }
    }
    return 1;
}

static int ler_entrada(void) {
    char buffer[256];
    int linha, coluna, tamanho;

    for (linha = 0; linha < N; linha++) {
        if (fgets(buffer, sizeof(buffer), stdin) == NULL) {
            return 0;
        }

        tamanho = 0;
        while (buffer[tamanho] != '\0' && buffer[tamanho] != '\n' && buffer[tamanho] != '\r') {
            tamanho++;
        }

        if (tamanho != N) {
            return 0;
        }

        for (coluna = 0; coluna < N; coluna++) {
            if (buffer[coluna] < '0' || buffer[coluna] > '9') {
                return 0;
            }
            tabuleiro[linha][coluna] = buffer[coluna] - '0';
        }
    }
    return 1;
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
    if (!ler_entrada()) {
        printf("ENTRADA INVALIDA\n");
        return 0;
    }

    if (!entrada_valida() || !resolver()) {
        printf("SEM SOLUCAO\n");
        return 0;
    }

    imprimir_tabuleiro();
    return 0;
}
