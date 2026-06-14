package ui;

import java.util.Scanner;
import model.NoMusica;
import model.PlaylistDupla;

public class MenuConsole {
    private PlaylistDupla playlist;
    private Scanner ler;

    public MenuConsole() {
        this.playlist = new PlaylistDupla();
        this.ler = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao = 0;
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

            opcao = ler.nextInt();
            ler.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Caminho da pasta: ");
                    String caminho = ler.nextLine();
                    carregarPasta(caminho);
                    break;
                case 2:
                    play();
                    break;
                case 3:
                    System.out.println("⏸ Pausado.");
                    break;
                case 4:
                    playlist.next();
                    break;
                case 5:
                    playlist.previous();
                    break;
                case 6:
                    System.out.println("🔁 Repeat ativado.");
                    break;
                case 7:
                    System.out.println("🔀 Shuffle ativado.");
                    break;
                case 8:
                    playlist.listar();
                    break;
                case 9:
                    System.out.println("A sair...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 9);

        ler.close();
    }

    private void carregarPasta(String caminho) {
        java.io.File pasta = new java.io.File(caminho);
        if (!pasta.exists() || !pasta.isDirectory()) {
            System.out.println("Pasta inválida!");
            return;
        }
        java.io.File[] ficheiros = pasta.listFiles((dir, name) -> name.endsWith(".mp3"));
        if (ficheiros == null || ficheiros.length == 0) {
            System.out.println("Nenhum ficheiro MP3 encontrado.");
            return;
        }
        for (java.io.File f : ficheiros) {
            String titulo = f.getName().replace(".mp3", "");
            playlist.adicionarMusica(titulo, "Desconhecido", f.getAbsolutePath());
        }
        System.out.println(ficheiros.length + " músicas carregadas!");
    }

    private void play() {
        NoMusica atual = playlist.getAtualMusica();
        if (atual == null) {
            System.out.println("Nenhuma música na playlist!");
            return;
        }
        System.out.println("▶ A tocar: " + atual.getTituloMusica() + " - " + atual.getArtistaMusica());
    }

    public static void main(String[] args) {
        new MenuConsole().iniciar();
    }
}