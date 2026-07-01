package model;

import java.io.File;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class PlayerService {
    private PlaylistDupla playlist;
    private boolean tocando;
    private boolean pausado;
    private String modoRepeat;
    private MediaPlayer mediaPlayer;
    private double volumeAtual = 0.7;
    private String ultimoErro;

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

    public void setVolume(double volume) {
        this.volumeAtual = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void seek(double progresso) {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            Duration target = mediaPlayer.getTotalDuration().multiply(progresso);
            mediaPlayer.seek(target);
        }
    }

    public Duration getDuracaoTotal() {
        if (mediaPlayer == null) return Duration.UNKNOWN;
        return mediaPlayer.getTotalDuration();
    }

    public Duration getTempoAtual() {
        if (mediaPlayer == null) return Duration.ZERO;
        return mediaPlayer.getCurrentTime();
    }

    public String formatDuration(Duration d) {
        if (d == null || d.isUnknown()) return "00:00";
        int totalSec = (int) d.toSeconds();
        return String.format("%02d:%02d", totalSec / 60, totalSec % 60);
    }

    public StringBinding criarStringTempo() {
        if (mediaPlayer == null) {
            return Bindings.createStringBinding(() -> "00:00 / 00:00");
        }
        return Bindings.createStringBinding(() ->
            formatDuration(mediaPlayer.getCurrentTime()) + " / " +
            formatDuration(mediaPlayer.getTotalDuration()),
            mediaPlayer.currentTimeProperty(),
            mediaPlayer.totalDurationProperty()
        );
    }

    public String getErro() {
        if (ultimoErro != null) return ultimoErro;
        if (mediaPlayer != null && mediaPlayer.getError() != null) {
            return mediaPlayer.getError().getMessage();
        }
        return null;
    }

    public void limparErro() {
        ultimoErro = null;
    }

    public NoMusica resume() {
        if (pausado && mediaPlayer != null) {
            mediaPlayer.play();
            this.tocando = true;
            this.pausado = false;
            System.out.println("Retomando: " + playlist.getAtualMusica());
            return playlist.getAtualMusica();
        }
        return play();
    }

    public NoMusica play() {
        ultimoErro = null;

        if (playlist.getAtualMusica() == null) {
            ultimoErro = "Não existe música para reproduzir";
            System.out.println(ultimoErro);
            return null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        try {
            File f = new File(playlist.getAtualMusica().getCaminhoFicheiro());
            if (!f.exists()) {
                ultimoErro = "Ficheiro não encontrado: " + f.getAbsolutePath();
                System.out.println(ultimoErro);
                return null;
            }
            Media media = new Media(f.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(volumeAtual);
            mediaPlayer.setOnError(() -> {
                ultimoErro = "Erro ao reproduzir: " + mediaPlayer.getError().getMessage();
                System.out.println(ultimoErro);
            });
            mediaPlayer.play();

            mediaPlayer.setOnEndOfMedia(() -> {
                if (modoRepeat.equals("ONE")) {
                    play();
                } else if (modoRepeat.equals("ALL")) {
                    next();
                } else {
                    next();
                }
            });

        } catch (Exception e) {
            ultimoErro = "Erro ao tocar: " + e.getClass().getSimpleName() + " - " + e.getMessage();
            System.out.println(ultimoErro);
            this.tocando = false;
            this.pausado = false;
            return null;
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