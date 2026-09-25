# [P4-ETAPA-03] Decisões de Implementação — Imperativo (C)

## 1. Quais estados são mantidos

O único estado do programa é a matriz `tabuleiro[9][9]`, uma variável
global (`static`) que representa o Sudoku em cada instante da execução.
Não há nenhuma outra estrutura de estado persistente, o programa não
mantém histórico de tentativas, contadores externos, nem qualquer dado
auxiliar fora dessa matriz. Esse é o estado mutável central em torno do
qual toda a solução imperativa gira.

## 2. Quais operações modificam estado

- `ler_entrada()`: preenche `tabuleiro` pela primeira vez, célula a célula,
  a partir da entrada padrão.
- `resolver()`: modifica `tabuleiro` diretamente a cada tentativa de
  preenchimento (`tabuleiro[linha][coluna] = valor`) e também a desfaz
  explicitamente no backtracking (`tabuleiro[linha][coluna] = 0`) quando a
  tentativa falha.

Nenhuma outra função modifica o tabuleiro: `posicao_segura()`,
`entrada_valida()` e `imprimir_tabuleiro()` apenas **leem** o estado, nunca
o alteram — essa separação entre funções que leem e funções que escrevem
foi uma escolha deliberada para deixar os efeitos colaterais localizados e
fáceis de rastrear.

## 3. Onde aparecem efeitos colaterais

O efeito colateral central do programa é a mutação da matriz global dentro
de `resolver()`. Cada chamada recursiva de `resolver()` depende do estado
deixado pela chamada anterior (o tabuleiro parcialmente preenchido) e, ao
retroceder, desfaz sua própria alteração antes de retornar, esse
"desfazer" (`tabuleiro[linha][coluna] = 0`) é o efeito colateral mais
importante da solução, pois é ele quem implementa o backtracking. Sem esse
efeito colateral explícito, tentativas malsucedidas "sujariam" o tabuleiro
permanentemente.

Além disso, `imprimir_tabuleiro()` tem o efeito colateral de escrever em
`stdout`, e `ler_entrada()` tem o efeito colateral de consumir `stdin`.

## 4. Quais estruturas de controle foram escolhidas

- **Laços `for` aninhados**: usados em `posicao_segura()`,
  `entrada_valida()`, `resolver()` e `imprimir_tabuleiro()` para percorrer
  linhas, colunas e blocos 3x3.
- **Recursão controlando o fluxo de busca**: `resolver()` chama a si mesma
  para avançar para a próxima célula vazia; o `return 0`/`return 1` de cada
  chamada informa ao chamador se deve continuar tentando outros valores ou
  se pode propagar sucesso. A recursão aqui não é usada no sentido
  funcional (transformação de dados imutáveis), mas como mecanismo de
  controle de fluxo sobre um estado mutável compartilhado.
- **Retorno antecipado (`return`)**: usado extensivamente para interromper
  a busca assim que uma condição de falha é detectada, evitando
  aninhamento excessivo de `if`.

## 5. Como os subprogramas foram organizados

A solução foi decomposta em seis funções, cada uma com uma única
responsabilidade:

| Função | Responsabilidade |
|---|---|
| `posicao_segura` | consulta se um valor pode ser colocado numa posição |
| `entrada_valida` | valida consistência das células fixas da entrada |
| `resolver` | busca com backtracking, altera o estado global |
| `ler_entrada` | le e valida o formato da entrada |
| `imprimir_tabuleiro` | formata e imprime a saída |
| `main` | orquestra a ordem de chamada das demais funções |

Os parâmetros são usados para passar **coordenadas e valores a testar**
(`linha`, `coluna`, `valor`), nunca o próprio tabuleiro, como ele é uma
variável global, as funções o acessam diretamente. Essa é uma escolha
característica do imperativo: o estado compartilhado é acessado
implicitamente por todas as funções, em vez de ser passado explicitamente
como em uma solução funcional.

## 6. Por que a solução pode ser considerada predominantemente imperativa

- Não há nenhuma `struct` com comportamento associado, nem qualquer
  tentativa de encapsular o tabuleiro atrás de uma interface, ele é uma
  matriz de inteiros pura, manipulada diretamente por funções externas a
  ela.
- O estado é mutável e compartilhado globalmente, e a lógica de
  backtracking depende inteiramente de mutação e desfazimento explícitos.
- O controle de fluxo é feito por comandos (`for`, `if`, `return`), não por
  composição de funções de alta ordem, recursão sobre estruturas
  imutáveis ou correspondência de padrões (o que caracterizaria funcional
  ou lógico).
- A linguagem C não oferece coleta de lixo, classes ou abstrações de alto
  nível, não há como o código "escorregar" para outro paradigma mesmo sem
  intenção.

## Validação contra os Casos de Teste (Etapa 2)

Todos os 15 casos definidos em `testes/casos.md` foram executados contra
esta implementação:

- **CT-01 a CT-10**: todos produzem saída idêntica ao gabarito. 
- **CL-01** (tabuleiro vazio): o programa retorna um tabuleiro completo e
  válido (verificado programaticamente contra as pós-condições, não por
  igualdade literal, como previsto no critério de avaliação). 
- **CL-02** (já completo): saída idêntica à entrada, sem alterações. 
- **CL-03** (poucas pistas): saída idêntica ao gabarito. 
- **EI-01** (contradição entre células fixas): `SEM SOLUCAO`. 
- **EI-02** (formato malformado): `ENTRADA INVALIDA`. 
