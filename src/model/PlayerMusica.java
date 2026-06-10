
import java.io.File;

class Media {
    private final String source;

    public Media(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}

class MediaPlayer {
    private final Media media;
    private Runnable onEndOfMedia;
    private Runnable onReady;
    private boolean playing;
    private boolean paused;
    private double volume = 1.0;

    public MediaPlayer(Media media) {
        this.media = media;
        if (onReady != null) {
            onReady.run();
        }
    }

    public void dispose() {
        stop();
    }

    public void play() {
        playing = true;
        paused = false;
    }

    public void pause() {
        if (playing) {
            paused = true;
            playing = false;
        }
    }

    public void stop() {
        playing = false;
        paused = false;
    }

    public void setOnEndOfMedia(Runnable runnable) {
        this.onEndOfMedia = runnable;
    }

    public void setOnReady(Runnable runnable) {
        this.onReady = runnable;
        if (this.onReady != null) {
            this.onReady.run();
        }
    }

    public void setOnError(Runnable runnable) {
        // no-op
    }

    public void setVolume(double value) {
        this.volume = value;
    }

    public Media getMedia() {
        return media;
    }

    public Runnable getOnEndOfMedia() {
        return onEndOfMedia;
    }

    public boolean isPaused() {
        return paused;
    }

    public double getVolume() {
        return volume;
    }
}

public class PlayerMusica {

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Estado getEstado() {
        return estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getArtista() {
        return artista;
    }

    // 1. Gerar os estados que o David pediu
    public enum Estado {
        PARADO,
        A_TOCAR,
        PAUSADO
    }

    private MediaPlayer mediaPlayer;
    private Estado estado = Estado.PARADO;
    private String titulo = "Nenhuma música";
    private String artista = "Desconhecido";

    // 2. Carregar MP3 antes de tocar
    public void carregar(String caminhoMP3) {
        try {
            if (mediaPlayer!= null) {
                mediaPlayer.dispose();
            }

            var media = new Media(new File(caminhoMP3).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            titulo = new File(caminhoMP3).getName();
            artista = "Desconhecido";
            estado = Estado.PARADO;
        } catch (Exception e) {
        }
    }
}
;

   