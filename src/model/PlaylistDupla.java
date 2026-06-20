package model;

public class PlaylistDupla {
    private NoMusica inicio;
    private NoMusica fim;
    private NoMusica atualMusica;
    private int tamanho;
    
    public PlaylistDupla() {
        this.inicio = null;
        this.fim = null;
        this.atualMusica = null;
        this.tamanho = 0;
    }

    public NoMusica getAtualMusica() {
        return atualMusica;
    }

    public int getTamanho() {
        return tamanho;
    }

     public NoMusica getInicio() {
        return inicio;
    }

    public NoMusica getFim() {
        return fim;
    }

    public void setAtualMusica(NoMusica atualMusica) {
        this.atualMusica = atualMusica;
    }

    //verificar se a lista esta vazia
    private boolean isVazia(){
        if (inicio == null || fim == null) {
            return true;
        }
        return false;
    }

    public void adicionarMusica(String tituloMusica,String artistaMusica,String caminhoFicheiro){
        //criar um NO
        NoMusica novaMusica = new NoMusica(tituloMusica, artistaMusica, caminhoFicheiro); 
        //se a lista estiver vazia quer dizer que o NO a inserir sera o primeiro NO e o ultimo NO e nao tera um anterior e proximo 
        if (isVazia()) {
            this.inicio = novaMusica;
            this.fim = novaMusica;
            this.atualMusica = novaMusica;
        }else{
            //vamos ligar o ponteiro do ultimo No ao novo no 
            fim.setProximo(novaMusica);
            //ligar o ponteiro do novo No que agora é o ultimo ao No que era o ultimo no 
            novaMusica.setAnterior(fim);
            //atualizar o valor do fim
            this.fim = novaMusica;
        }
        this.tamanho++;
    }

    public void removerMusica(String titulo){
        if (isVazia()) {
            System.out.println("Lista esta vazia");
            return;
        }

        NoMusica cursor = inicio;
        NoMusica encontrado = null;
        while (cursor != null) {
            if (cursor.getTituloMusica().equalsIgnoreCase(titulo)) {
                 encontrado = cursor;
                 break;
            }
            cursor = cursor.getProximo();
        }

        if (encontrado == null) {
            System.out.println("Musica nao encontrada");
            return;
        }

        if (inicio == fim) {
             inicio = null;
             fim = null;
             atualMusica = null;
             this.tamanho--;   
            return;
        }

        if (encontrado == inicio) {//caso for primeiro no 
            NoMusica segundoNo = encontrado.getProximo();
            this.inicio = segundoNo;
            segundoNo.setAnterior(null);
            if (encontrado == atualMusica) {//caso musica a eliminar for atual musica
                atualMusica = segundoNo;
            }
            this.tamanho--;
            return;
        }

        if (encontrado == fim) {//caso for ultimo no
            NoMusica penultimo = encontrado.getAnterior();
            this.fim = penultimo;
            penultimo.setProximo(null);
            if (encontrado == atualMusica) {
                atualMusica = penultimo;
            }
            this.tamanho--;
            return;
        }

        NoMusica anterior = encontrado.getAnterior();
        NoMusica proximo = encontrado.getProximo();
        anterior.setProximo(proximo);
        proximo.setAnterior(anterior);
       if (encontrado == atualMusica) {
            atualMusica = proximo;
       }
        this.tamanho--;
    }

    public void listar(){
        if (isVazia()) {
            System.out.println("Lista esta vazia");
            return;
        }

        NoMusica cursor = inicio;
        int contador = 1;
        while (cursor != null) {
            if (cursor == atualMusica) {
                System.out.println(contador + " - " + cursor + " <-- TOCANDO AGORA");
            }else{
                 System.out.println(contador + " - " +cursor);
            }
            cursor = cursor.getProximo();
            contador++;
        }
    }

    public void next(){
        if (atualMusica == null) {
            System.out.println("Nao existe musica para avançar");
            return;
        }

        if (atualMusica.getProximo() != null) {
           atualMusica = atualMusica.getProximo();
        }else{
            atualMusica = inicio;
        }
        System.out.println("Musica a tocar : " + atualMusica);
    }

    public void previous(){
        if (atualMusica == null) {
            System.out.println("Nao existe musica atual");
            return;
        }

        if (atualMusica.getAnterior() != null) {
            atualMusica = atualMusica.getAnterior();
        }
         System.out.println("Musica a tocar : " + atualMusica);
    }

    public NoMusica getMusicas() {
    return inicio;
    }

    //no main por exemplo
    /*NoMusicaPrincipal cursor = playlist.getMusicas();

     while (cursor != null) {
     System.out.println(cursor);
     cursor = cursor.getProximo();
} */
}