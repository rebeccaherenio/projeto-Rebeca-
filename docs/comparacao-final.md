# [P4-ETAPA-05] Comparação — Imperativo (C) vs. Orientação a Objetos (Java)

Esta análise é baseada diretamente no código produzido em
`imperativo/sudoku.c` e `poo/Sudoku.java`
## 1. Representação do estado

- **Imperativo**: uma única matriz global `static int tabuleiro[9][9]`,
  acessível e alterável por qualquer função do arquivo, sem nenhuma
  barreira de acesso.
- **OOP**: o estado equivalente (`celulas`) é um campo `private` dentro da
  classe `Tabuleiro`. O mesmo dado existe nos dois casos, uma matriz
  9x9 de inteiros, mas no OOP ele está **encapsulado** atrás de métodos
  (`valorEm`, `definir`, `limpar`), e mais nenhuma classe o acessa
  diretamente.

## 2. Mutabilidade

Em ambas as versões o estado é **mutável** — nenhuma das duas evita
mutação. A diferença não está em *se* o estado muda, mas em *quem*
pode mudá-lo: em C, qualquer função do arquivo; em Java, apenas através
dos métodos públicos de `Tabuleiro`, o que impede alterações acidentais
vindas de código externo à classe.

## 3. Fluxo de controle

- **Imperativo**: `for` aninhados e recursão de controle (`resolver()`
  chamando a si mesma para avançar à próxima célula). O fluxo é
  inteiramente explícito: cada decisão (`if`, `return`) está visível no
  mesmo bloco de código.
- **OOP**: o fluxo de alto nível (`SudokuSolver.resolver`) é quase
  idêntico ao da versão em C, ainda são laços e recursão. A diferença
  aparece na verificação de regras: em vez de um `if` testando linha,
  coluna e bloco em sequência, o fluxo passa por um laço `for (Restricao r
  : restricoes)` que invoca `r.violada(...)` **polimorficamente** — o
  ponto exato de "qual código roda" só é decidido em tempo de execução,
  dependendo da subclasse concreta. Isso torna o fluxo um pouco menos
  rastreável linha a linha (é preciso "pular" para a subclasse correta
  para saber o que realmente acontece), em troca de maior flexibilidade.

## 4. Decomposição do problema

- **Imperativo**: decomposto em **funções** por tarefa
  (`ler_entrada`, `posicao_segura`, `resolver`, `imprimir_tabuleiro`).
- **OOP**: decomposto em **entidades com responsabilidade própria**
  (`Tabuleiro`, `Restricao` e suas subclasses, `SudokuSolver`,
  `LeitorDeEntrada`). A diferença central: no imperativo, a decomposição
  segue a pergunta "que passos o programa executa?"; no OOP, a pergunta é
  "quem é responsável por saber o quê?".

## 5. Reutilização

A versão OOP reutiliza melhor: o algoritmo de "verificar repetição num
grupo de células" está escrito **uma única vez** em `Restricao.violada()`
e reaproveitado pelas três regras. Na versão em C, a mesma lógica de
"percorrer um grupo e checar repetição" aparece **três vezes**, uma para
cada tipo de grupo (linha, coluna, bloco), dentro de `posicao_segura` —
um código praticamente idêntico, repetido.

## 6. Manutenção

Se uma regra do Sudoku precisasse mudar (hipoteticamente), a versão em C
exigiria editar diretamente dentro de `posicao_segura`, com risco de
afetar sem querer a lógica das outras regras, já que estão todas no mesmo
bloco. Na versão OOP, a mudança ficaria isolada na subclasse específica
daquela regra (por exemplo, só em `RestricaoBloco`), sem risco de afetar
`RestricaoLinha` ou `RestricaoColuna`.

## 7. Facilidade de extensão

Na versão OOP significa criar uma classe nova (`RestricaoDiagonal extends Restricao`) e registrá-la
no construtor de `Tabuleiro`, nenhum código existente é tocado. Na versão
em C, seria necessário adicionar mais um laço dentro de
`posicao_segura`, misturando a regra nova com as já existentes no mesmo
corpo de função.

## 8. Tratamento de erros

As duas versões tratam erros de forma equivalente em espírito (validação
explícita de formato de entrada, retornando `ENTRADA INVALIDA` para
entrada malformada e `SEM SOLUCAO` para entrada bem formada mas
impossível), essa decisão de design foi deliberadamente mantida idêntica
entre as duas implementações, para isolar a comparação de paradigma de
diferenças de política de erro. A diferença é só mecânica: C usa valor de
retorno (`int` como booleano) para sinalizar falha de leitura; Java usa
`null` como retorno de `LeitorDeEntrada.ler()` para o mesmo propósito.

## 9. Efeitos colaterais

Em ambas as versões, o backtracking depende de efeitos colaterais
(mutação do tabuleiro seguida de desfazimento). A diferença é o
**alcance** desse efeito colateral: em C, qualquer função pode, em tese,
alterar a matriz global (mesmo que na prática só `resolver` e
`ler_entrada` o façam). Em Java, o efeito colateral só pode ocorrer através dos métodos 
públicos de `Tabuleiro`, tornando mais fácil garantir, por inspeção do código, quais trechos
alteram o estado.

## 10. Facilidade para testar

A versão OOP é mais fácil de testar **em partes isoladas**: dá para
testar uma `RestricaoLinha` sozinha, passando um `Tabuleiro` de teste, sem
precisar montar um cenário completo de resolução. Na versão em C, a lógica
de verificação (`posicao_segura`) está acoplada à matriz global — testá-la
isoladamente exigiria manipular o estado global diretamente antes de cada
chamada, o que é mais frágil (testes podem interferir uns nos outros se
não houver o cuidado de resetar o estado global entre eles).

## 11. Organização do código

- **Imperativo**: um único arquivo, com funções em sequência lógica de
  uso. Fácil de ler de cima a baixo, mas tudo "no mesmo nível".
- **OOP**: múltiplas classes, cada uma menor e mais focada, mas é preciso
  "navegar" entre elas para entender o comportamento completo (por
  exemplo, entender `aceitaValor()` exige olhar também as três subclasses
  de `Restricao`). Há um trade-off real aqui entre coesão por arquivo e
  necessidade de navegação entre arquivos.

## 12. Complexidade

Medindo de forma simples (linhas de código e número de "unidades"
nomeadas): a versão em C tem ~6 funções em 1 arquivo; a versão em Java tem
8 classes distribuídas na mesma lógica. A versão OOP tem **mais
complexidade estrutural** (mais nomes, mais indireção, mais arquivos
conceituais) para implementar exatamente o mesmo comportamento observável.
---

## Perguntas

### 1. Qual problema ficou mais fácil de expressar de forma imperativa?

O **algoritmo de backtracking em si** (a sequência "tente, avance, se
falhar desfaça e tente o próximo valor") ficou mais direto em C: é só
olhar a função `resolver()` de cima a baixo para entender exatamente o
que acontece, sem precisar pular entre arquivos ou entender hierarquia de
classes. Para um algoritmo sequencial e relativamente simples como esse,
a forma imperativa "crua" comunica o processo sem camadas extras.

### 2. Qual problema ficou mais fácil de expressar utilizando orientação a objetos?

A **adição e variação de regras de validação**. Expressar "existem três
tipos de restrição, que compartilham o mesmo algoritmo de checagem mas
diferem em que células agrupam" é natural em OOP (uma abstração + três
implementações) e artificial em C (três blocos de código quase idênticos,
com nenhuma relação explícita entre eles reconhecida pela linguagem).

### 3. Onde a orientação a objetos realmente trouxe vantagem?

Na **extensibilidade das regras de validação** e na
**testabilidade isolada** de cada regra. Essas são vantagens
reais, não apenas teóricas: qualquer alteração futura nas regras do
Sudoku (ou uma variante do jogo) se beneficiaria diretamente dessa
estrutura.

### 4. Em quais situações a utilização de objetos acrescentou complexidade desnecessária?

Na **leitura de entrada** (`LeitorDeEntrada`) e na **orquestração**
(classe `Sudoku`/`main`). Essas partes não têm variação nem necessidade de
extensão, são processos únicos e lineares (ler uma entrada, orquestrar a
chamada de duas outras classes). Transformá-las em classes separadas não
trouxe benefício de polimorfismo ou reutilização; foi mais uma
organização por convenção de OOP do que uma necessidade real do problema.
Dá para argumentar que `LeitorDeEntrada` poderia ter sido um método
estático simples, sem perda de clareza.

### 5. Que partes do problema praticamente não mudaram entre as duas implementações?

O **algoritmo de busca com backtracking** (`SudokuSolver.resolver` vs.
`resolver()` em C) é estruturalmoente quase idêntico nas duas versões:
mesmos laços, mesma ordem de tentativa de valores, mesma lógica de
desfazer.

### 6. Que partes precisaram ser completamente remodeladas?

A **verificação de regras de restrição** (`posicao_segura` → hierarquia
`Restricao`/`RestricaoLinha`/`RestricaoColuna`/`RestricaoBloco`). Essa foi
a única parte onde não bastou "encapsular a mesma lógica numa classe", a
lógica precisou ser genuinamente redesenhada (de um bloco de `if`s
sequenciais para uma abstração com polimorfismo) para que a versão OOP
usasse o paradigma de forma real, e não apenas como sintaxe.
