package model;

public class NoMusicaPrincipal {
    private String tituloMusica;
    private String artistaMusica;
    private String caminhoFicheiro;
    private NoMusicaPrincipal anterior;
    private NoMusicaPrincipal proximo;
        
    public NoMusicaPrincipal(String tituloMusica, String artistaMusica, String caminhoFicheiro) {
        this.tituloMusica = tituloMusica;
        this.artistaMusica = artistaMusica;
        this.caminhoFicheiro = caminhoFicheiro;
        this.anterior = null;
        this.proximo = null;
    }

    public String getTituloMusica() {
        return tituloMusica;
    }

    public void setTituloMusica(String tituloMusica) {
        this.tituloMusica = tituloMusica;
    }

    public String getArtistaMusica() {
        return artistaMusica;
    }

    public void setArtistaMusica(String artistaMusica) {
        this.artistaMusica = artistaMusica;
    }

    public String getCaminhoFicheiro() {
        return caminhoFicheiro;
    }

    public void setCaminhoFicheiro(String caminhoFicheiro) {
        this.caminhoFicheiro = caminhoFicheiro;
    }

    public NoMusicaPrincipal getAnterior() {
        return anterior;
    }

    public void setAnterior(NoMusicaPrincipal anterior) {
        this.anterior = anterior;
    }

    public NoMusicaPrincipal getProximo() {
        return proximo;
    }

    public void setProximo(NoMusicaPrincipal proximo) {
        this.proximo = proximo;
    }

   @Override
    public String toString() {
      return "Titulo: " + tituloMusica +
           ", Artista: " + artistaMusica +
           ", Ficheiro: " + caminhoFicheiro;
    }
}
