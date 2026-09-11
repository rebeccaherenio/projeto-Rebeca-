# Casos de Teste — Sudoku Solver

Todos os casos seguem o formato definido em `docs/especificacao.md`
(9 linhas de 9 dígitos, `0` para célula vazia).

## Caso 1 — Solução única (caso comum)

**Entrada:**
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

**Saída esperada:**
```
534678912
672195348
198342567
859761423
426853791
713924856
961537284
287419635
345286179
```

## Caso 2 — Quase completo (1 célula vazia)

**Entrada:** igual à saída do Caso 1, mas com a célula (linha 1, coluna 1)
zerada (valor original `5`).

**Saída esperada:** idêntica à saída do Caso 1 (deve reconstituir o `5`).

## Caso 3 — Tabuleiro vazio

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

**Saída esperada:** qualquer tabuleiro válido completo (não há solução
única — o teste verifica apenas que a saída satisfaz as pós-condições da
seção 4 da especificação, não um valor exato).

## Caso 4 — Sem solução

**Entrada:** duas ocorrências do dígito `5` na mesma linha (viola a
pré-condição de consistência da entrada) — usado para validar o tratamento
de erro/rejeição de entrada inválida:
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

**Saída esperada:** `SEM SOLUCAO`

## Caso 5 — Já completo e válido

**Entrada:** a mesma saída do Caso 1 (nenhuma célula vazia).

**Saída esperada:** idêntica à entrada (o solver deve reconhecer que já é
uma solução válida e devolvê-la sem alterações).

## Critério de avaliação dos testes

Para os Casos 1, 2 e 5 (solução única conhecida), a saída deve ser
**exatamente igual** ao gabarito. Para o Caso 3 (múltiplas soluções
possíveis), a validação deve ser feita programaticamente conferindo as
pós-condições (linhas, colunas e blocos sem repetição, todas as células
preenchidas), não por comparação literal com um gabarito fixo.
