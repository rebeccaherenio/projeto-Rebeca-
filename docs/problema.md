# [P4-ETAPA-01] Proposta do Problema

## 1. Descrição do Problema

O problema escolhido é o **Sudoku Solver**: dado um tabuleiro 9x9 parcialmente
preenchido, o programa deve determinar o preenchimento completo que satisfaça
as regras do Sudoku, ou indicar que o tabuleiro não possui solução.

### Regras do Sudoku

- O tabuleiro é uma grade 9x9, dividida em 9 subgrades (blocos) de 3x3.
- Cada célula deve conter um dígito de 1 a 9.
- Nenhum dígito pode se repetir na mesma **linha**.
- Nenhum dígito pode se repetir na mesma **coluna**.
- Nenhum dígito pode se repetir no mesmo **bloco 3x3**.
- Um tabuleiro válido de entrada já respeita essas regras nas células
  preenchidas; as células vazias são representadas por `0` (ou `.`).

## 2. Por que este problema é adequado ao projeto

O Sudoku foi escolhido por reunir características que se expressam de forma
distinta em cada paradigma:

- **Estado mutável e iterativo**: o preenchimento progressivo do tabuleiro
  se presta naturalmente à modelagem imperativa.
- **Entidades com identidade e comportamento**: o tabuleiro, as células, as
  linhas/colunas/blocos podem ser modelados como objetos com responsabilidades
  próprias, favorecendo a Orientação a Objetos.
- **Transformações e recursão sobre estruturas imutáveis**: a resolução pode
  ser expressa como uma função pura que recebe um tabuleiro e devolve outro
  (ou `Nothing`/`Nenhuma solução`), sem mutação de estado, sendo um encaixe natural
  para a Programação Funcional.
- **Restrições e relações declarativas**: as regras do Sudoku são um conjunto de restrições de não-repetição, deixando a busca da solução a cargo do motor de inferência/backtracking da Programação Lógica (uso natural de
  `all_different`/`fd` em Prolog).

Dessa forma, o mesmo problema conceitual permite observar, de forma concreta,
como cada paradigma resolve a mesma questão com modelos de dados, controle de
fluxo e técnicas de abstração completamente diferentes.

## 3. Escopo do Projeto

Para manter o projeto viável e focado na comparação entre paradigmas, o escopo foi definido como:

- **Tamanho do tabuleiro**: 9x9 clássico.
- **Funcionalidade**: apenas **resolução** (solver) de um tabuleiro dado.
  A geração de puzzles novos está **fora do escopo**.
- **Entrada**: representação textual da grade, com `0` (ou `.`) indicando
  células vazias, formato consistente entre todas as implementações, para
  permitir comparação direta.
- **Saída**: a grade completa resolvida, ou uma mensagem indicando que o
  tabuleiro não possui solução.
- **Garantia de unicidade**: os casos de teste utilizarão tabuleiros com
  solução única, para simplificar a comparação de resultados entre
  paradigmas.

## 4. Linguagens Candidatas por Paradigma

| Paradigma | Linguagem candidata | Justificativa preliminar |
|---|---|---|
| Imperativo | C | Controle explícito de laços, arrays e estado mutável; C reforça a manipulação direta de memória e fluxo. |
| Orientação a Objetos | Java | Suporte maduro a classes, encapsulamento e modelagem de entidades (Tabuleiro, Célula, Restrição). |
| Funcional | Haskell | Tipagem forte, imutabilidade por padrão, recursão e funções de alta ordem como mecanismo natural de solução. |
| Lógico | Prolog (SWI-Prolog) | Motor de inferência e backtracking nativos; biblioteca `clpfd` (finite domain constraints) é praticamente desenhada para problemas como o Sudoku. |
