package ui;

import java.util.Scanner;
import model.NoMusica;
import model.PlaylistDupla;

public class MenuConsole {
    private PlaylistDupla playlist; // lista com todas as musicas
    private Scanner ler; // para ler o que o utilizador escreve

    public MenuConsole() {
        this.playlist = new PlaylistDupla(); // cria a lista vazia
        this.ler = new Scanner(System.in); // prepara para ler do teclado
    }

    public void iniciar() {
        int opcao = 0;

        // loop que repete o menu ate o utilizador escolher sair
        do {
            System.out.println("\n===== LEITOR MP3 =====");
            System.out.println("[1] Carregar pasta de músicas");
            System.out.println("[2] Play");
            System.out.println("[3] Pause");
            System.out.println("[4] Next");
            System.out.println("[5] Previous");
            System.out.println("[6] Repeat");
            System.out.println("[7] Shuffle");
            System.out.println("[8] Listar músicas");
            System.out.println("[9] Sair");
            System.out.print("Escolha uma opção: ");

            // le o numero que o utilizador escreveu
            opcao = ler.nextInt();
            ler.nextLine(); // limpa o enter que fica no buffer

            // executa a acao consoante a opcao escolhida
            switch (opcao) {
                case 1:
                    System.out.print("Caminho da pasta: ");
                    String caminho = ler.nextLine();
                    carregarPasta(caminho); // vai buscar os mp3 da pasta
                    break;
                case 2:
                    play(); // mostra a musica atual
                    break;
                case 3:
                    System.out.println("⏸ Pausado.");
                    break;
                case 4:
                    playlist.next(); // avanca para a proxima musica
                    break;
                case 5:
                    playlist.previous(); // volta para a musica anterior
                    break;
                case 6:
                    System.out.println("🔁 Repetir.");
                    break;
                case 7:
                    System.out.println("🔀 Shuffle.");
                    break;
                case 8:
                    playlist.listar(); // mostra todas as musicas da lista
                    break;
                case 9:
                    System.out.println("A sair...");
                    break;
                default:
                    System.out.println("Essa opção não existe, tenta outra vez!");
            }
        } while (opcao != 9); // continua enquanto nao for 9

        ler.close(); // fecha o scanner no fim
    }

    private void carregarPasta(String caminho) {
        java.io.File pasta = new java.io.File(caminho);

        // verifica se a pasta existe e e mesmo uma pasta
        if (!pasta.exists() || !pasta.isDirectory()) {
            System.out.println("Pasta inválida!");
            return;
        }

        // filtra apenas os ficheiros mp3
        java.io.File[] ficheiros = pasta.listFiles((dir, name) -> name.endsWith(".mp3"));

        if (ficheiros == null || ficheiros.length == 0) {
            System.out.println("Nenhum ficheiro MP3 encontrado.");
            return;
        }

        // adiciona cada mp3 a playlist
        for (java.io.File f : ficheiros) {
            String titulo = f.getName().replace(".mp3", ""); // tira o .mp3 do nome
            playlist.adicionarMusica(titulo, "Desconhecido", f.getAbsolutePath());
        }
        System.out.println(ficheiros.length + " músicas carregadas!");
    }

    private void play() {
        NoMusica atual = playlist.getAtualMusica();

        // verifica se ha alguma musica na lista
        if (atual == null) {
            System.out.println("Nenhuma música na playlist!");
            return;
        }
        System.out.println("▶ A tocar: " + atual.getTituloMusica() + " - " + atual.getArtistaMusica());
    }

    public static void main(String[] args) {
        new MenuConsole().iniciar(); // arranca o menu
    }
}