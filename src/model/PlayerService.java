package model;

import java.io.File;
import java.util.Random;

public class PlayerService {
    private PlaylistDupla playlist;
    private boolean tocando;
    private boolean pausado;
    private String modoRepeat;

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

    public NoMusica play(){
        if (playlist.getAtualMusica() == null) {
            System.out.println("Não existe música para reproduzir");
            return null;
        }

        this.tocando = true;
        this.pausado =false;
        System.out.println("Tocando: " + playlist.getAtualMusica());
        return playlist.getAtualMusica();
    }

    public NoMusica pause(){
        if (!tocando) {
           System.out.println("Nao existe musica tocando");
           return null;
        }

        this.tocando = false;
        this.pausado = true;
        System.out.println("Música pausada: " + playlist.getAtualMusica());
        return playlist.getAtualMusica();
    }

    public void stop(){
        this.tocando = false;
        this.pausado = false;
        System.out.println("Reprodução parada");
    }

    public NoMusica next(){
        this.stop();
        this.playlist.next();
        this.play();
        return playlist.getAtualMusica();
    }

    public NoMusica previous(){
        this.stop();
        this.playlist.previous();
        this.play();
        return playlist.getAtualMusica();
    }

    public String repeat(){
        if (modoRepeat.equalsIgnoreCase("OFF")) {
            modoRepeat = "ONE";
        }else if(modoRepeat.equalsIgnoreCase("ONE")){
            modoRepeat = "ALL";
        }else{
            modoRepeat = "OFF";
        }
        System.out.println("Modo repeat : " + modoRepeat);
        return modoRepeat;
    }

    public NoMusica shuffle() {

    //como estou a usar lista duplamente ligada entao nao posso acessar posiçao por posiçao svou gerar posiçao aleatoria e percorrer a lista ate a posiçao    
    if (playlist.getTamanho() == 0) {
        System.out.println("Playlist vazia");
        return null;
    }

    // Se só existir uma música
    if (playlist.getTamanho() == 1) {
        System.out.println("Existe apenas uma música na playlist.");
        return playlist.getAtualMusica();
    }


    stop();

    //"gerar" numero aleatorio
    Random random = new Random();

    NoMusica musicaSorteada;

    do {//para nao deixar que a musica escolhida seja o que ja esta a tocar entao vai ficar gerando uma nova posiçao e percorendo a lista ate nao ser a que ja esta sendo reproduzida

        int posicao = random.nextInt(playlist.getTamanho());

        //para poder percorer
        musicaSorteada = playlist.getInicio();

        for (int i = 0; i < posicao; i++) {
            musicaSorteada = musicaSorteada.getProximo();
        }

    } while (musicaSorteada == playlist.getAtualMusica());

    playlist.setAtualMusica(musicaSorteada);

    System.out.println("Shuffle selecionou:");
    System.out.println(musicaSorteada);

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

        if (ficheiro.isFile()
                && ficheiro.getName().toLowerCase().endsWith(".mp3")) {

            String titulo = ficheiro.getName()
                    .substring(0, ficheiro.getName().length() - 4);

            String artista = "Desconhecido";

            String caminhoCompleto = ficheiro.getAbsolutePath();

            playlist.adicionarMusica(
                    titulo,
                    artista,
                    caminhoCompleto
            );

            contador++;
        }
    }

    System.out.println(contador + " musica(s) carregada(s).");
}

}