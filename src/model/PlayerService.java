package model;

import java.io.File;
import java.util.Random;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlayerService {
    private PlaylistDupla playlist;
    private boolean tocando;
    private boolean pausado;
    private String modoRepeat;
    private MediaPlayer mediaPlayer; // ← ADICIONADO

    public PlayerService(PlaylistDupla playlist) {
        this.playlist = playlist;
        this.tocando = false;
        this.pausado = false;
        this.modoRepeat = "OFF";
    }

    public PlaylistDupla getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistDupla playlist) {
        this.playlist = playlist;
    }

    public boolean isTocando() {
        return tocando;
    }

    public boolean isPausado() {
        return pausado;
    }

    public String getModoRepeat() {
        return modoRepeat;
    }

    // ← MÉTODO ADICIONADO
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public NoMusica play() {
        if (playlist.getAtualMusica() == null) {
            System.out.println("Não existe música para reproduzir");
            return null;
        }

        // para o player anterior
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        // toca o ficheiro real
        try {
            File f = new File(playlist.getAtualMusica().getCaminhoFicheiro());
            Media media = new Media(f.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();

            // quando acaba passa para a proxima
            mediaPlayer.setOnEndOfMedia(() -> next());

        } catch (Exception e) {
            System.out.println("Erro ao tocar: " + e.getMessage());
        }

        this.tocando = true;
        this.pausado = false;
        System.out.println("Tocando: " + playlist.getAtualMusica());
        return playlist.getAtualMusica();
    }

    public NoMusica pause() {
        if (!tocando) {
            System.out.println("Nao existe musica tocando");
            return null;
        }

        if (mediaPlayer != null) mediaPlayer.pause(); // ← ADICIONADO

        this.tocando = false;
        this.pausado = true;
        System.out.println("Música pausada: " + playlist.getAtualMusica());
        return playlist.getAtualMusica();
    }

    public void stop() {
        if (mediaPlayer != null) { // ← ADICIONADO
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        this.tocando = false;
        this.pausado = false;
        System.out.println("Reprodução parada");
    }

    public NoMusica next() {
        this.playlist.next();
        this.play();
        return playlist.getAtualMusica();
    }

    public NoMusica previous() {
        this.playlist.previous();
        this.play();
        return playlist.getAtualMusica();
    }

    public String repeat() {
        if (modoRepeat.equalsIgnoreCase("OFF")) {
            modoRepeat = "ONE";
        } else if (modoRepeat.equalsIgnoreCase("ONE")) {
            modoRepeat = "ALL";
        } else {
            modoRepeat = "OFF";
        }
        System.out.println("Modo repeat : " + modoRepeat);
        return modoRepeat;
    }

    public NoMusica shuffle() {
        if (playlist.getTamanho() == 0) {
            System.out.println("Playlist vazia");
            return null;
        }

        if (playlist.getTamanho() == 1) {
            System.out.println("Existe apenas uma música na playlist.");
            return playlist.getAtualMusica();
        }

        stop();

        Random random = new Random();
        NoMusica musicaSorteada;

        do {
            int posicao = random.nextInt(playlist.getTamanho());
            musicaSorteada = playlist.getInicio();
            for (int i = 0; i < posicao; i++) {
                musicaSorteada = musicaSorteada.getProximo();
            }
        } while (musicaSorteada == playlist.getAtualMusica());

        playlist.setAtualMusica(musicaSorteada);
        System.out.println("Shuffle selecionou: " + musicaSorteada);
        play();
        return musicaSorteada;
    }

    public void carregarMP3DaPasta(String caminhoPasta) {
        File pasta = new File(caminhoPasta);

        if (!pasta.exists() || !pasta.isDirectory()) {
            System.out.println("Pasta invalida.");
            return;
        }

        File[] ficheiros = pasta.listFiles();

        if (ficheiros == null || ficheiros.length == 0) {
            System.out.println("Nenhum ficheiro encontrado.");
            return;
        }

        int contador = 0;

        for (File ficheiro : ficheiros) {
            if (ficheiro.isFile() && ficheiro.getName().toLowerCase().endsWith(".mp3")) {
                String titulo = ficheiro.getName().substring(0, ficheiro.getName().length() - 4);
                String artista = "Desconhecido";
                String caminhoCompleto = ficheiro.getAbsolutePath();
                playlist.adicionarMusica(titulo, artista, caminhoCompleto);
                contador++;
            }
        }

        System.out.println(contador + " musica(s) carregada(s).");
    }
}