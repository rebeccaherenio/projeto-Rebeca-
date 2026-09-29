# [P4-ETAPA-04] Reflexão — Do Imperativo ao Orientado a Objetos

## Como o modelo mudou

Na versão imperativa (C), o problema inteiro é representado por uma
**única matriz global** e um conjunto de funções soltas que operam sobre
ela. Não há nenhum conceito de "quem é dono" de qual regra — todas as
verificações (linha, coluna, bloco) vivem juntas, misturadas, dentro de
uma única função (`posicao_segura`).

Na versão orientada a objetos, o problema foi remodelado em torno de
**responsabilidades separadas e comunicantes**: o `Tabuleiro` não sabe
mais *como* cada regra é verificada, ele apenas pergunta a uma coleção de
objetos `Restricao` se alguma delas seria violada. Essa é a mudança mais
importante: a versão imperativa pergunta "como eu verifico isso?"; a
versão OOP pergunta "quem é responsável por saber isso?".

## Representação do estado

- **Imperativo**: o estado é uma matriz `int[9][9]` global, acessada e
  modificada diretamente por qualquer função do programa.
- **OOP**: o estado (`celulas`) está **encapsulado** dentro da classe
  `Tabuleiro`, como campo `private`. Nenhuma outra classe acessa a matriz
  diretamente, tudo passa pelos métodos públicos (`valorEm`, `definir`,
  `limpar`). O estado ainda é mutável (o solver muda o tabuleiro durante o
  backtracking, igual à versão em C), mas agora está **protegido** atrás
  de uma interface, em vez de exposto livremente.

## Responsabilidades

Na versão imperativa, a "responsabilidade" de validar uma célula estava
implicitamente distribuída entre `posicao_segura` (checa as três regras
juntas) e `entrada_valida` (reusa a mesma função). Não havia fronteiras
claras.

Na versão OOP, cada classe tem uma responsabilidade única e nomeada:

| Classe | Responsabilidade |
|---|---|
| `Restricao` (abstrata) | definir o algoritmo comum de verificação de uma regra |
| `RestricaoLinha` / `RestricaoColuna` / `RestricaoBloco` | dizer quais células pertencem ao seu grupo |
| `Tabuleiro` | manter o estado e delegar a verificação às restrições |
| `SudokuSolver` | aplicar backtracking, sem saber como as regras funcionam |
| `LeitorDeEntrada` | validar e converter a entrada em um `Tabuleiro` |
| `Sudoku` (main) | orquestrar a execução |

## Relacionamento entre componentes

- **Herança**: `RestricaoLinha`, `RestricaoColuna` e `RestricaoBloco`
  **estendem** `Restricao`. ela resolve um problema real de
  duplicação: as três regras compartilham exatamente o mesmo algoritmo de
  verificação (percorrer um grupo de células e checar repetição),
  diferindo apenas em qual grupo é esse. Colocar esse algoritmo comum uma
  única vez na classe-mãe (`violada()`), e deixar cada subclasse
  responsável só por definir seu grupo (`celulasRelacionadas()`), é o
  padrão *Template Method* um uso legítimo de herança, não decorativo.
- **Polimorfismo**: o `Tabuleiro` guarda uma lista de `Restricao` e chama
  `r.violada(...)` para cada uma, **sem saber qual subclasse concreta**
  está sendo chamada em cada iteração. Isso é polimorfismo em ação: o
  mesmo código (`for (Restricao r : restricoes) { r.violada(...) }`)
  funciona identicamente para as três regras, e para qualquer regra nova
  que venha a ser adicionada.
- **Composição/Agregação**: `Tabuleiro` **compõe** suas `Restricao`s elas
  são criadas junto com o tabuleiro e não fazem sentido fora dele. Já
  `SudokuSolver` se relaciona com `Tabuleiro` por **uso** (recebe como
  parâmetro), não por composição, o solver não é dono do tabuleiro, só
  opera sobre um que já existe. Essa distinção (posse vs. uso) não existe
  na versão imperativa, onde toda função "usa" a mesma matriz global sem
  distinção alguma.

### Por que não usamos herança em `Tabuleiro`/`SudokuSolver`

Poderíamos ter forçado herança em outro lugar só para "aparecer mais herança" no projeto,
mas isso não corresponderia a nenhuma relação real de "é um" (*is-a*) entre esses conceitos.
`Tabuleiro` e `SudokuSolver` não compartilham comportamento comum que justifique uma
superclasse; a relação entre eles é de colaboração (um usa o outro), não
de especialização. Por isso, mantivemos composição/uso nesses casos, e
reservamos a herança para o único lugar onde ela resolve um problema
genuíno: as `Restricao`s.

## Reutilização

Na versão imperativa, reutilizar a lógica de "checar repetição num grupo
de células" para uma regra nova, exigiria copiar e adaptar o corpo de `posicao_segura`. 
Na versão OOP, isso significa apenas criar `class RestricaoDiagonal extends Restricao` e adicioná-la
à lista, nenhum código existente precisa ser tocado. Esse é o ganho concreto de
reutilização que a herança + polimorfismo trazem aqui.

## Encapsulamento

O campo `celulas` de `Tabuleiro` é `private`; o campo `restricoes`
também. Nenhuma classe externa manipula esses dados diretamente, tudo
passa por métodos públicos com nomes que expressam intenção
(`aceitaValor`, `entradaValida`), em vez de expor a estrutura interna.
Isso contrasta diretamente com a versão em C, onde `tabuleiro` é uma
variável global acessível (e alterável) por qualquer função do arquivo.

## Extensão do sistema

Se o projeto precisasse evoluir para suportar uma variante do Sudoku
(por exemplo, Sudoku-X, com restrições adicionais nas diagonais), a
versão OOP suportaria isso com uma mudança localizada: criar uma nova
subclasse de `Restricao` e registrá-la no construtor de `Tabuleiro`. Na
versão imperativa, a mesma extensão exigiria editar a função
`posicao_segura` diretamente, misturando a lógica nova com a antiga
dentro do mesmo bloco de código, um ponto único de risco para introduzir
bugs em regras que já funcionavam.

## Validação contra os Casos de Teste (Etapa 2)

A lógica de verificação de regras (agora distribuída entre as três
subclasses de `Restricao`) foi validada em Java, reproduzindo o mesmo algoritmo estrutural
(restrição abstrata + subclasses concretas). O resultado bateu
exatamente com o gabarito do caso CT-01, a mudança de design não altera
o comportamento observável do programa, apenas sua organização interna,
como esperado (o contrato de entrada/saída, definido em
`docs/especificacao.md`, permanece o mesmo).
