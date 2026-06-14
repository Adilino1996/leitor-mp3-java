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
            System.out.println("[1] Carregar música");
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
                    System.out.print("Caminho da música (.mp3): ");
                    String caminho = ler.nextLine();
                    carregarMusica(caminho);
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
                    System.out.println("🔁 Repetir.");
                    break;
                case 7:
                    System.out.println("🔀 Shuffle.");
                    break;
                case 8:
                    playlist.listar();
                    break;
                case 9:
                    System.out.println("A sair...");
                    break;
                default:
                    System.out.println("Essa opção não existe, tenta outra vez!");
            }
        } while (opcao != 9);

        ler.close();
    }

    private void carregarMusica(String caminho) {
        java.io.File ficheiro = new java.io.File(caminho);
        if (!ficheiro.exists() || !ficheiro.getName().endsWith(".mp3")) {
            System.out.println("Ficheiro inválido! Tem de ser um ficheiro .mp3");
            return;
        }
        String titulo = ficheiro.getName().replace(".mp3", "");
        playlist.adicionarMusica(titulo, "Desconhecido", ficheiro.getAbsolutePath());
        System.out.println("Música adicionada: " + titulo);
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