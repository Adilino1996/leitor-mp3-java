# Leitor MP3 em Java

Trabalho de Turma — Conceção e Análise de Algoritmos  

---

## A nossa equipa

| Equipa | O que vai fazer |
|--------|-----------------|
| Líderes | Organiza o GitHub |
| DEV Backend | As classes Java da lista de músicas |
| DEV GUI | O ecrã gráfico e o menu de texto |
| QA / Testes | Testar se o código funciona bem |
| Documentação | Diagramas, relatório e slides |

---

## Onde cada equipa trabalha

Cada equipa tem uma **pasta própria** no projeto.  
Só mexes nos ficheiros da tua pasta — não toques nas pastas dos outros!

```
leitor-mp3-java/
│
├── src/
│   ├── model/     ← DEV BACKEND trabalha aqui
│   └── ui/        ← DEV GUI trabalha aqui
│
├── tests/         ← QA trabalha aqui
│
├── docs/
│   ├── diagramas/ ← DOCUMENTAÇÃO trabalha aqui
│   ├── relatorio/ ← DOCUMENTAÇÃO trabalha aqui
│   └── slides/    ← DOCUMENTAÇÃO trabalha aqui
│
├── config/        ← ficheiros de configuração
├── resources/     ← imagens e ícones
│
├── README.md      ← este ficheiro
└── .gitignore     ← diz ao Git o que ignorar
```

> Os ficheiros `.gitkeep` que vês dentro das pastas vazias são só para o Git guardar a pasta.
> Quando começares a colocar os teus ficheiros lá, podes apagá-los.

---

## Cada equipa tem a sua branch

> **O que é uma branch?**  
> Imagina que o projeto é um documento Word partilhado.  
> Em vez de todos editarem ao mesmo tempo e baralhar tudo,  
> cada pessoa tem a sua **cópia própria** para trabalhar — isso é a branch.  
> No final, o Líder junta tudo.

| Equipa | Branch (cópia de trabalho) |
|--------|---------------------------|
| DEV Backend | `backend` |
| DEV GUI | `gui` |
| QA / Testes | `qa` |
| Documentação | `docs` |
| Líder | `main` |

---

## Como começar — passo a passo (faz isto UMA VEZ)

### Passo 1 — Instalar o Git
Se ainda não tens o Git instalado:  
https://git-scm.com/download/win  
(Clica em "Download for Windows" e instala com as opções padrão)

### Passo 2 — Clonar o projeto para o teu computador

Abre o **Command Prompt** (tecla Windows → escreve `cmd` → Enter) e copia este comando:

```
git clone https://github.com/Adilino1996/leitor-mp3-java.git
```

Isto vai criar uma pasta `leitor-mp3-java` no teu computador com todo o projeto dentro.

### Passo 3 — Entrar na pasta do projeto

```
cd leitor-mp3-java
```

### Passo 4 — Ir para a tua branch

Escolhe o comando da TUA equipa:

```
git checkout backend
```
*(DEV Backend)*

```
git checkout gui
```
*(DEV GUI)*

```
git checkout qa
```
*(QA / Testes)*

```
git checkout docs
```
*(Documentação)*

> Pronto! Agora já estás na tua área de trabalho.

---

## Como guardar o teu trabalho no GitHub (faz isto sempre que acabas de trabalhar)

São sempre estes 3 comandos, nesta ordem:

**1. Dizer ao Git quais ficheiros queres guardar**
```
git add .
```
*(o ponto significa "todos os ficheiros que alterei")*

**2. Criar um registo do que fizeste (escreve uma descrição curta)**
```
git commit -m "escreve aqui o que fizeste"
```

Exemplos de boas descrições:
```
git commit -m "criar classe NoMusica com anterior e proximo"
git commit -m "adicionar botão play na interface"
git commit -m "criar testes para PlaylistDupla"
git commit -m "adicionar diagrama de Use Case"
```

**3. Enviar para o GitHub**
```
git push
```

> Faz isto no fim de cada sessão de trabalho, mesmo que não esteja 100% acabado.  
> É melhor guardar com frequência do que perder trabalho!

---

## Erros comuns e como resolver

**"git não é reconhecido como comando"**  
→ O Git não está instalado. Vai ao Passo 1 acima.

**"Please tell me who you are"**  
→ Tens de te identificar no Git. Corre estes dois comandos:
```
git config --global user.name "O teu nome"
git config --global user.email "o teu email do GitHub"
```

**"rejected — not authorized"**  
→ Não aceitaste o convite de colaborador. Verifica o teu email e aceita o convite do GitHub.

**"merge conflict"**  
→ Dois ficheiros entraram em conflito. Avisa o Líder (Adilino) para resolver.

---

## O que já está feito e o que falta

| Funcionalidade | Estado | Equipa responsável |
|---------------|--------|-------------------|
| Lista duplamente ligada (NoMusica, PlaylistDupla) |  A fazer | DEV Backend |
| PlayerService (play, pause, next...) |  A fazer | DEV Backend |
| Interface gráfica JavaFX |  A fazer | DEV GUI |
| Menu modo consola |  A fazer | DEV GUI |
| Testes JUnit |  A fazer | QA |
| Diagrama Use Case + Atividades |  A fazer | Documentação |
| Relatório |  A fazer | Documentação |
| Slides apresentação |  A fazer | Documentação |

---

## Professor

Valério Santos — valerio.santos@us.edu.cv

