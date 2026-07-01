package ui;

import java.util.Scanner;
import model.NoMusica;
import model.PlayerService;
import model.PlaylistDupla;

public class MenuConsole {
    private PlaylistDupla playlist;
    private PlayerService player;
    private Scanner ler;

    public MenuConsole() {
        this.playlist = new PlaylistDupla();
        this.player = new PlayerService(playlist);
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
                    player.carregarMP3DaPasta(caminho);
                    break;
                case 2:
                    player.play();
                    break;
                case 3:
                    player.pause();
                    break;
                case 4:
                    player.next();
                    break;
                case 5:
                    player.previous();
                    break;
                case 6:
                    System.out.println("🔁 Repeat: " + player.repeat());
                    break;
                case 7:
                    player.shuffle();
                    break;
                case 8:
                    playlist.listar();
                    break;
                case 9:
                    player.stop();
                    System.out.println("A sair...");
                    break;
                default:
                    System.out.println("Essa opção não existe, tenta outra vez!");
            }
        } while (opcao != 9);

        ler.close();
    }

    public static void main(String[] args) {
        new MenuConsole().iniciar();
    }
}