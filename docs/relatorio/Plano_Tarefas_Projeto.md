# Plano de Tarefas do Projeto

## Documentação (branch `docs`)
Pastas:
- `docs/diagramas/`
- `docs/relatorio/`
- `docs/slides/`

### D1 — Diagrama de Use Case
- Ator: Utilizador
- Ações: Tocar, Pausar, Avançar, Recuar, Repetir, Baralhar, Guardar Playlist
- Entregável: `docs/diagramas/UseCase.png`

### D2 — Diagrama de Atividades
Fluxo:
Início → Carregar MP3 → Mostrar playlist → Utilizador escolhe ação → Executar → Atualizar → Voltar ao menu
- Entregável: `docs/diagramas/Atividades.png`

### D3 — Relatório
1. Introdução
2. Objetivos
3. Diagramas
4. Lista duplamente ligada
5. Testes
6. Conclusão
7. Referências
- Mínimo: 10 páginas com capturas.
- Entregável: `docs/relatorio/Relatorio.pdf`

### D4 — Slides
1. Título + equipa
2. Projeto
3. Lista duplamente ligada
4. Demo
5. Testes
6. Conclusão
- Máx. 10 minutos.
- Entregável: `docs/slides/Slides.pptx`

Ferramentas: draw.io, Word, PowerPoint.

---

## QA / Testes (branch `qa`)
Pasta: `tests/`

### Q1 — JUnit 5
- Adicionar músicas
- Remover (primeiro, último, meio, inexistente)
- `next()`
- `previous()`
- Mínimo 10 testes
- Entregável: `PlaylistTest.java`

### Q2 — Testes manuais
Documentar:
- Ação
- Resultado esperado
- Resultado obtido
- Passou/Falhou
- Entregável: `.md` ou Word

### Q3 — Build final
- Clonar repositório
- Compilar
- Executar
- Confirmar funcionamento

Commit:
```bash
git add .
git commit -m "test: testes à PlaylistDupla"
git push
```

---

## DEV GUI (branch `gui`)
Pasta: `src/ui/`

### G1 — MenuConsola.java
Loop com opções:
1. Carregar pasta
2. Play
3. Pause
4. Next
5. Previous
6. Repeat
7. Shuffle
8. Listar músicas
9. Sair

### G2 — JanelaPrincipal.java
- Tema escuro
- ListView
- Botões
- Slider volume
- Barra progresso
- Música atual
- Ligação ao PlayerService

Commit:
```bash
git add .
git commit -m "feat: criar menu consola"
git push
```

---

## DEV Backend (branch `backend`)
Pasta: `src/model/`

### B1 — NoMusica.java
Campos:
- titulo
- artista
- caminhoFicheiro
- anterior
- proximo
- construtor
- getters/setters
- toString()

### B2 — PlaylistDupla.java
- head
- tail
- atual
- tamanho
- adicionarMusica()
- removerMusica()
- next()
- previous()
- listar()

### B3 — PlayerService.java
- play()
- pause()
- stop()
- next()
- previous()
- repeat()
- shuffle()
- carregar MP3

> O backend é a base do projeto; os restantes grupos dependem dele.
