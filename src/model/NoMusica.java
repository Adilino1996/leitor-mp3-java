package model;

public class NoMusica {
    private String tituloMusica;
    private String artistaMusica;
    private String caminhoFicheiro;
    private NoMusica anterior;
    private NoMusica proximo;
        
    public NoMusica(String tituloMusica, String artistaMusica, String caminhoFicheiro) {
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

    public NoMusica getAnterior() {
        return anterior;
    }

    public void setAnterior(NoMusica anterior) {
        this.anterior = anterior;
    }

    public NoMusica getProximo() {
        return proximo;
    }

    public void setProximo(NoMusica proximo) {
        this.proximo = proximo;
    }

   @Override
    public String toString() {
      return "Titulo: " + tituloMusica +
           ", Artista: " + artistaMusica +
           ", Ficheiro: " + caminhoFicheiro;
    }
}