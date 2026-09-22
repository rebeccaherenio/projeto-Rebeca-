# Casos de Teste

Todos os casos seguem o formato definido em `docs/especificacao.md`: 9
linhas de 9 dígitos, onde `0` representa célula vazia. A saída esperada
segue o mesmo formato, ou a string `SEM SOLUCAO` quando aplicável.

Todos os gabaritos dos casos normais e dos casos-limite 2 e 3 derivam do
mesmo tabuleiro-base resolvido (gerado e verificado programaticamente),
garantindo consistência entre os casos e evitando erro humano na
conferência manual das 81 células.

---

## Casos Normais (10)

### CT-01
**Entrada:**
```
400957260
905362708
067184953
198405032
652893100
374621500
031046807
840010305
729530416
```
**Saída esperada:**
```
483957261
915362748
267184953
198475632
652893174
374621589
531246897
846719325
729538416
```
**Descrição:** puzzle com 61 células preenchidas (20 vazias) — caso de
dificuldade baixa, usado como referência principal.

### CT-02
**Entrada:**
```
000957000
905302748
200104953
198475600
652893104
374621509
501240007
040710325
709508410
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** 25 células vazias, dificuldade baixa-média.

### CT-03
**Entrada:**
```
080907000
915362740
200004003
198405600
652803170
074600080
531246897
840709300
720538006
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** 30 células vazias, dificuldade média.

### CT-04
**Entrada:**
```
403050260
915060740
267100053
098005032
602890100
074021000
530040800
046700300
729030416
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** 35 células vazias, dificuldade média.

### CT-05
**Entrada:**
```
400900061
005362040
267180900
108070600
050800174
300001509
530000800
006019320
029500016
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** 40 células vazias, dificuldade média-alta.

### CT-06
**Entrada:**
```
003057061
005000008
200184900
190070602
602003000
070600000
031206890
800010005
020030416
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** 45 células vazias, dificuldade alta.

### CT-07
**Entrada:**
```
000050061
910000708
000004900
100075032
050890000
004000000
500006090
046009305
029008016
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** 50 células vazias.
### CT-08
**Entrada:**
```
083950060
015362740
007184950
190475032
602893174
374621009
530200000
800709320
009500010
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** 30 células vazias, distribuição diferente de CT-03.

### CT-09
**Entrada:**
```
400050061
915300700
067100050
198400602
002890100
070020009
531246007
846709020
709038416
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** 35 células vazias, distribuição diferente de CT-04.

### CT-10
**Entrada:**
```
400007201
910302048
260004003
108075602
050090170
000021589
001200090
046010305
709530000
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** 40 células vazias, distribuição diferente de CT-05.

---

## Casos-Limite (3)

### CL-01 — Tabuleiro vazio
**Entrada:**
```
000000000
000000000
000000000
000000000
000000000
000000000
000000000
000000000
000000000
```
**Saída esperada:** qualquer tabuleiro completo e válido (não há gabarito
único — este caso não deve ser comparado por igualdade literal, e sim
validado programaticamente contra as pós-condições).
**Descrição:** maior espaço de busca possível; usado para avaliar se a
implementação eventualmente termina, e para medir desempenho no pior
caso de ausência de pistas.

### CL-02 — Tabuleiro já completo
**Entrada:**
```
483957261
915362748
267184953
198475632
652893174
374621589
531246897
846719325
729538416
```
**Saída esperada:** idêntica à entrada (nenhuma célula vazia a preencher).
**Descrição:** verifica se a implementação reconhece corretamente um
tabuleiro já resolvido, sem alterá-lo e sem entrar em nenhum ramo de
backtracking.

### CL-03 — Número mínimo de pistas
**Entrada:**
```
400057000
010000008
007004900
000005030
000000174
004620000
530200000
000000000
000000416
```
**Saída esperada:** igual ao gabarito de CT-01.
**Descrição:** apenas 22 células preenchidas — próximo do limite prático
para manter solução única (o mínimo teórico conhecido para Sudoku 9x9 é 17
pistas; este caso foi obtido programaticamente removendo o máximo de
células possível do tabuleiro-base enquanto a unicidade da solução era
verificada a cada remoção). Avalia o comportamento das implementações com
poucas restrições iniciais.

---

## Entradas Inválidas (2)

### EI-01 — Contradição entre células preenchidas
**Entrada:**
```
550000000
000000000
000000000
000000000
000000000
000000000
000000000
000000000
000000000
```
**Saída esperada:** `SEM SOLUCAO` a única exigência do contrato é que nenhuma implementaçãodevolva um tabuleiro 
parcialmente preenchido ou inválido).

**Descrição:** dígito `5` repetido na mesma linha entre as células já
preenchidas — viola a pré-condição da seção 3 de `docs/especificacao.md`
antes mesmo de qualquer tentativa de resolução.

### EI-02 — Formato malformado
**Entrada:**
```
40005700
010000008
007004900
000005030
000000174
004620000
530200000
000000000
000000416
```
**Saída esperada:** rejeição da entrada (comportamento de erro definido
livremente por cada implementação, e documentado em `docs/decisoes.md` —
o contrato não garante um resultado de resolução para entradas
malformadas).
**Descrição:** a primeira linha tem apenas 8 dígitos em vez de 9,
violando a pré-condição de dimensão da seção 3 de
`docs/especificacao.md`. Usado para verificar que cada implementação ao
menos não trava, não lança exceção não tratada, nem produz um resultado
incorreto silencioso.

---

## Critério de Avaliação dos Testes

- **CT-01 a CT-10, CL-02, CL-03**: comparação **exata** com o gabarito
  (solução única conhecida).
- **CL-01**: validação programática das pós-condições (sem dígito
  repetido em linha/coluna/bloco, todas as 81 células preenchidas), não
  comparação literal com um gabarito fixo.
- **EI-01, EI-02**: verifica-se apenas que a implementação **não**
  produz um tabuleiro inválido ou incompleto como se fosse uma solução
  válida — o formato exato da mensagem/erro de rejeição é decisão de
  cada implementação, desde que documentada.
