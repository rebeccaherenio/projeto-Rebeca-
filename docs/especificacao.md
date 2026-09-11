# [P4-ETAPA-02] Contrato Semântico

## 1. Objetivo

Definir, de forma independente de linguagem e paradigma, o contrato que toda
implementação deve respeitar: o que é uma entrada válida, o que é uma saída
válida, e quais garantias o programa oferece. Este contrato é o que permite
comparar as quatro implementações de forma justa — todas resolvem exatamente
o mesmo problema, com a mesma interface conceitual.

## 2. Representação dos Dados

### 2.1 Tabuleiro

Um tabuleiro é uma matriz 9x9 de inteiros, onde cada célula contém um valor
no conjunto `{0, 1, 2, ..., 9}`:

- valores de `1` a `9` representam dígitos já preenchidos;
- o valor `0` representa uma célula vazia (a ser preenchida pela solução).

### 2.2 Formato de entrada (texto)

Para permitir entrada/saída uniforme entre todas as implementações, adota-se
uma representação textual de 9 linhas, cada uma com 9 dígitos (sem
separadores), por exemplo:

```
530070000
600195000
098000060
800060003
400803001
700020006
060000280
000419005
000080079
```

### 2.3 Formato de saída (texto)

Mesma representação de 9 linhas com 9 dígitos, mas sem nenhum `0` — todas as
células preenchidas com o valor resolvido. Caso não exista solução, a saída
deve ser a string literal:

```
SEM SOLUCAO
```

## 3. Pré-condições (entrada válida)

Uma entrada é considerada **válida** para o contrato se:

1. possui exatamente 9 linhas e 9 colunas;
2. cada célula contém um valor em `{0, ..., 9}`;
3. as células já preenchidas (≠ 0) **não violam** as regras do Sudoku entre
   si (não há dígito repetido numa mesma linha, coluna ou bloco 3x3 apenas
   entre as células fixas).

O comportamento do programa para entradas que violem a pré-condição (ex.:
grade de tamanho errado, valores fora do intervalo) **não é garantido** pelo
contrato, cada implementação pode tratar isso como erro de forma própria.

## 4. Pós-condições (saída válida)

Dada uma entrada válida, a saída produzida deve satisfazer:

1. **Completude**: todas as 81 células possuem um valor em `{1, ..., 9}`
   (nenhum `0` na saída), **exceto** no caso "sem solução".
2. **Consistência com a entrada**: toda célula que já estava preenchida na
   entrada mantém o mesmo valor na saída.
3. **Validade**: nenhuma linha, coluna ou bloco 3x3 contém dígito repetido.
4. **Caso sem solução**: se não existir nenhum preenchimento que satisfaça
   as regras, a saída é `SEM SOLUCAO` — nenhuma implementação deve retornar
   um tabuleiro parcialmente preenchido ou inválido.

## 5. Contrato de Interface (independente de linguagem)

Toda implementação deve expor, no mínimo, uma função/predicado/método
equivalente a:

```
resolver(tabuleiro_entrada) -> tabuleiro_solucao | SEM_SOLUCAO
```

- **Entrada**: um tabuleiro no formato da seção 2.1, satisfazendo a seção 3.
- **Saída**: um tabuleiro satisfazendo a seção 4, ou o indicador de ausência
  de solução.

Cada paradigma poderá adaptar a *assinatura* dessa função ao seu próprio
estilo (por exemplo, em Prolog será um predicado com unificação; em Haskell,
uma função pura `Tabuleiro -> Maybe Tabuleiro`), mas o comportamento
observável (entrada → saída) deve ser idêntico entre as implementações.

## 6. Casos de teste

Os casos de teste detalhados estão em `testes/casos.md`, cobrindo:

- tabuleiro com solução única (caso comum);
- tabuleiro quase completo (apenas 1-2 células vazias);
- tabuleiro vazio (nenhuma célula preenchida — grande espaço de busca);
- tabuleiro sem solução (entrada inconsistente proposital);
- tabuleiro já completo e válido na entrada (caso trivial).
