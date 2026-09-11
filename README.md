# Projeto P4 — Um Problema, Quatro Paradigmas

## Visão Geral

Este projeto implementa uma solução para o mesmo problema — **resolução de
um Sudoku 9x9** — utilizando quatro paradigmas de programação diferentes:
Imperativo, Orientação a Objetos, Funcional e Lógico.

O objetivo não é traduzir um programa de uma linguagem para outra, mas
reformular a solução de acordo com o modelo de cada paradigma, observando
como isso muda a modelagem do problema, a representação de dados e estado,
o controle de fluxo, a decomposição da solução e o tratamento de efeitos
colaterais.

O problema permanece conceitualmente o mesmo em todas as etapas — o
contrato de entrada/saída está documentado em `docs/especificacao.md`.

## O Problema

**Sudoku Solver**: dado um tabuleiro 9x9 parcialmente preenchido, determinar
o preenchimento completo que satisfaça as regras do Sudoku (sem repetição em
linhas, colunas e blocos 3x3), ou indicar que não há solução.

Descrição completa em [`docs/problema.md`](docs/problema.md).

## Estrutura do Repositório

```
projeto-p4-rebecca/
│
├── README.md
│
├── docs/
│   ├── problema.md          — descrição do problema e escopo
│   ├── especificacao.md     — contrato semântico (entrada/saída)
│   ├── decisoes.md          — justificativas de linguagem e design
│   └── comparacao-final.md  — comparação entre os quatro paradigmas
│
├── testes/
│   └── casos.md             — casos de teste usados em todas as implementações
│
├── imperativo/    — implementação em C
├── poo/           — implementação em Java
├── funcional/     — implementação em Haskell
├── logico/        — implementação em Prolog
└── integrado/     — (se aplicável) integração entre implementações
```

## Linguagens por Paradigma

| Paradigma | Linguagem | Pasta |
|---|---|---|
| Imperativo | C | `imperativo/` |
| Orientação a Objetos | Java | `poo/` |
| Funcional | Haskell | `funcional/` |
| Lógico | Prolog | `logico/` |

A justificativa de cada escolha está em [`docs/decisoes.md`](docs/decisoes.md).

## Status das Etapas

- [x] `[P4-ETAPA-01]` Proposta do problema
- [x] `[P4-ETAPA-02]` Contrato semântico e testes
- [ ] `[P4-ETAPA-03]` Implementação imperativa (C)
- [ ] `[P4-ETAPA-04]` Implementação orientada a objetos (Java)
- [ ] `[P4-ETAPA-05]` Implementação funcional (Haskell)
- [ ] `[P4-ETAPA-06]` Implementação lógica (Prolog)
- [ ] `[P4-ETAPA-07]` Comparação final entre paradigmas

## Como Executar

### Imperativo (C)
```bash
gcc imperativo/sudoku.c -o sudoku
./sudoku < testes/entrada_exemplo.txt
```

### Orientação a Objetos (Java)
```bash
javac poo/Sudoku.java -d poo/
java -cp poo/ Sudoku < testes/entrada_exemplo.txt
```

### Funcional (Haskell)
```bash
runghc funcional/Sudoku.hs < testes/entrada_exemplo.txt
```

Todas as implementações seguem o mesmo formato de entrada/saída definido em
`docs/especificacao.md`: 9 linhas de 9 dígitos (`0` para célula vazia), e a
saída é a grade resolvida ou a linha `SEM SOLUCAO`.
