# Tabela de Testes Manuais — Leitor MP3 em Java

**Equipa QA** · Data: 05/07/2026  
**Testado por:** Elizeu

---

## Modo Gráfico (JanelaPrincipal)

| Nº | Teste | O que esperava | O que aconteceu | Passou? |
|----|-------|----------------|-----------------|---------|
| 1 | Abrir a aplicação | A janela abre sem erros | A janela abriu corretamente | ✅ |
| 2 | Carregar pasta de músicas | As músicas aparecem na lista | As músicas foram carregadas na lista | ✅ |
| 3 | Clicar Play numa música | A música começa a tocar (ouve-se som) | A música tocou com som | ✅ |
| 4 | Clicar Pause | A música pausa e mantém a posição | A música pausou na posição certa | ✅ |
| 5 | Clicar Play depois de Pause | A música continua de onde parou | Continuou do ponto onde parou | ✅ |
| 6 | Clicar Stop | A música para completamente | A música parou | ✅ |
| 7 | Clicar Next (Seguinte) | Passa para a próxima música | Passou à próxima música | ✅ |
| 8 | Clicar Previous (Anterior) | Volta à música anterior | Voltou à música anterior | ✅ |
| 9 | Next na última música | Volta à primeira música | Voltou à primeira música | ✅ |
| 10 | Clicar Shuffle | Toca uma música aleatória | Tocou uma música aleatória | ✅ |
| 11 | Clicar Repeat | Alterna entre OFF, ONE, ALL | Alternou entre os três modos | ✅ |
| 12 | Mexer no slider de volume | O som fica mais alto/baixo | O volume mudou corretamente | ✅ |
| 13 | Remover uma música da lista | A música desaparece da lista | A música foi removida | ✅ |
| 14 | Fechar a aplicação | A app fecha sem erros | A app fechou sem erros | ✅ |

---

## Modo Consola (MenuConsole)

| Nº | Teste | O que esperava | O que aconteceu | Passou? |
|----|-------|----------------|-----------------|---------|
| 15 | Opção 1 — Carregar pasta | As músicas são carregadas | As músicas foram carregadas | ✅ |
| 16 | Opção 2 — Play | A música toca | A música tocou | ✅ |
| 17 | Opção 3 — Pause | A música pausa | A música pausou | ✅ |
| 18 | Opção 4 — Next | Passa à próxima música | Passou à próxima | ✅ |
| 19 | Opção 5 — Previous | Volta à música anterior | Voltou à anterior | ✅ |
| 20 | Opção 6 — Repeat | Muda o modo de repetição | O modo mudou | ✅ |
| 21 | Opção 7 — Shuffle | Toca música aleatória | Tocou música aleatória | ✅ |
| 22 | Opção 8 — Listar músicas | Mostra todas as músicas | Mostrou a lista de músicas | ✅ |
| 23 | Opção 9 — Sair | O programa fecha | O programa fechou | ✅ |
| 24 | Opção inválida (ex: 99) | Mostra mensagem de erro | Mostrou mensagem de opção inválida | ✅ |

---

## Testes de casos especiais

| Nº | Teste | O que esperava | O que aconteceu | Passou? |
|----|-------|----------------|-----------------|---------|
| 25 | Play com playlist vazia | Mensagem "sem música", não crasha | Mostrou mensagem, não crashou | ✅ |
| 26 | Carregar pasta sem MP3 | Mensagem "nenhum ficheiro", não crasha | Mostrou mensagem, não crashou | ✅ |
| 27 | Carregar pasta que não existe | Mensagem "pasta inválida", não crasha | Mostrou mensagem, não crashou | ✅ |

---

## Bugs encontrados

| Bug | Descrição | Equipa responsável | Estado |
|-----|-----------|-------------------|--------|
| — | Nenhum bug encontrado durante os testes | — | — |

---

## Conclusão

Total de testes: 27  
Passaram: 27  
Falharam: 0  

**Observações finais:**  
Todos os testes passaram com sucesso. A aplicação funciona corretamente nos dois modos (gráfico e consola), sem erros nem crashes. O projeto está pronto para entrega.
