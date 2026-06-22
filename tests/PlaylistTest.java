import model.NoMusica;
import model.PlayerService;
import model.PlaylistDupla;

public class PlaylistTest {
    public static void main(String[] args) {
        Teste[] testes = {
            new Teste("adicionarSegundaMusicaLigaOsDoisNos", PlaylistTest::adicionarSegundaMusicaLigaOsDoisNos),
            new Teste("removerUltimaMusicaAtualizaFim", PlaylistTest::removerUltimaMusicaAtualizaFim),
            new Teste("adicionarVariasMusicasMantemOrdemELigacoesDuplas", PlaylistTest::adicionarVariasMusicasMantemOrdemELigacoesDuplas),
            new Teste("removerUnicaMusicaDeixaListaVazia", PlaylistTest::removerUnicaMusicaDeixaListaVazia),
            new Teste("removerPrimeiraMusicaAtualizaInicio", PlaylistTest::removerPrimeiraMusicaAtualizaInicio),
            new Teste("nextNoFimVoltaParaInicio", PlaylistTest::nextNoFimVoltaParaInicio),
            new Teste("previousNoMeioVoltaParaMusicaAnterior", PlaylistTest::previousNoMeioVoltaParaMusicaAnterior),
            new Teste("previousNoInicioMantemMesmaMusica", PlaylistTest::previousNoInicioMantemMesmaMusica),
            new Teste("removerMusicaDeListaVaziaNaoAlteraTamanho", PlaylistTest::removerMusicaDeListaVaziaNaoAlteraTamanho),
            new Teste("adicionarMusicaEmListaVaziaDefineInicioFimEAtual", PlaylistTest::adicionarMusicaEmListaVaziaDefineInicioFimEAtual),
            new Teste("removerMusicaDoMeioLigaAnteriorAoProximo", PlaylistTest::removerMusicaDoMeioLigaAnteriorAoProximo),
            new Teste("removerMusicaQueNaoExisteNaoAlteraLista", PlaylistTest::removerMusicaQueNaoExisteNaoAlteraLista),
            new Teste("nextNoMeioAvancaParaProximaMusica", PlaylistTest::nextNoMeioAvancaParaProximaMusica)
        };

        executar("PlaylistTest", testes);
    }

    public static void executar(String nomeClasse, Teste[] testes) {
        System.out.println("|-- " + nomeClasse + " OK");
        for (Teste teste : testes) {
            teste.executar();
            System.out.println("|   |-- " + teste.nome + "() OK");
        }
    }

    private static void adicionarSegundaMusicaLigaOsDoisNos() {
        PlaylistDupla playlist = new PlaylistDupla();
        playlist.adicionarMusica("Musica 1", "Artista 1", "musica1.mp3");
        playlist.adicionarMusica("Musica 2", "Artista 2", "musica2.mp3");

        assertEquals(playlist.getFim(), playlist.getInicio().getProximo(), "inicio liga ao fim");
        assertEquals(playlist.getInicio(), playlist.getFim().getAnterior(), "fim liga ao inicio");
    }

    private static void removerUltimaMusicaAtualizaFim() {
        PlaylistDupla playlist = criarPlaylist();
        playlist.removerMusica("Musica 3");

        assertEquals("Musica 2", playlist.getFim().getTituloMusica(), "fim apos remover ultima");
        assertNull(playlist.getFim().getProximo(), "fim sem proximo");
    }

    private static void adicionarVariasMusicasMantemOrdemELigacoesDuplas() {
        PlaylistDupla playlist = criarPlaylist();

        assertEquals(3, playlist.getTamanho(), "tamanho apos adicionar");
        assertEquals("Musica 1", playlist.getInicio().getTituloMusica(), "primeira musica");
        assertEquals("Musica 2", playlist.getInicio().getProximo().getTituloMusica(), "segunda musica");
        assertEquals("Musica 3", playlist.getFim().getTituloMusica(), "ultima musica");
    }

    private static void removerUnicaMusicaDeixaListaVazia() {
        PlaylistDupla playlist = new PlaylistDupla();
        playlist.adicionarMusica("Musica 1", "Artista 1", "musica1.mp3");
        playlist.removerMusica("Musica 1");

        assertEquals(0, playlist.getTamanho(), "tamanho vazio");
        assertNull(playlist.getInicio(), "inicio vazio");
        assertNull(playlist.getFim(), "fim vazio");
        assertNull(playlist.getAtualMusica(), "atual vazio");
    }

    private static void removerPrimeiraMusicaAtualizaInicio() {
        PlaylistDupla playlist = criarPlaylist();
        playlist.removerMusica("Musica 1");

        assertEquals("Musica 2", playlist.getInicio().getTituloMusica(), "novo inicio");
        assertNull(playlist.getInicio().getAnterior(), "inicio sem anterior");
    }

    private static void nextNoFimVoltaParaInicio() {
        PlaylistDupla playlist = criarPlaylist();

        playlist.next();
        playlist.next();
        playlist.next();
        assertEquals("Musica 1", playlist.getAtualMusica().getTituloMusica(), "next no fim volta ao inicio");
    }

    private static void previousNoMeioVoltaParaMusicaAnterior() {
        PlaylistDupla playlist = criarPlaylist();
        playlist.next();
        playlist.previous();

        assertEquals("Musica 1", playlist.getAtualMusica().getTituloMusica(), "previous volta para anterior");
    }

    private static void previousNoInicioMantemMesmaMusica() {
        PlaylistDupla playlist = criarPlaylist();

        playlist.previous();
        assertEquals("Musica 1", playlist.getAtualMusica().getTituloMusica(), "previous no inicio mantem primeira");
    }

    private static void removerMusicaDeListaVaziaNaoAlteraTamanho() {
        PlaylistDupla playlist = new PlaylistDupla();
        playlist.removerMusica("Musica 1");

        assertEquals(0, playlist.getTamanho(), "lista vazia continua vazia");
    }

    private static void adicionarMusicaEmListaVaziaDefineInicioFimEAtual() {
        PlaylistDupla playlist = new PlaylistDupla();
        playlist.adicionarMusica("Musica 1", "Artista 1", "musica1.mp3");

        assertEquals(playlist.getInicio(), playlist.getFim(), "inicio e fim iguais");
        assertEquals(playlist.getInicio(), playlist.getAtualMusica(), "atual igual ao inicio");
    }

    private static void removerMusicaDoMeioLigaAnteriorAoProximo() {
        PlaylistDupla playlist = criarPlaylist();
        playlist.removerMusica("Musica 2");

        assertEquals(2, playlist.getTamanho(), "tamanho apos remover");
        assertEquals("Musica 3", playlist.getInicio().getProximo().getTituloMusica(), "inicio liga ao proximo correto");
        assertEquals("Musica 1", playlist.getFim().getAnterior().getTituloMusica(), "fim liga ao anterior correto");
    }

    private static void removerMusicaQueNaoExisteNaoAlteraLista() {
        PlaylistDupla playlist = criarPlaylist();
        playlist.removerMusica("Musica X");

        assertEquals(3, playlist.getTamanho(), "tamanho nao muda");
        assertEquals("Musica 1", playlist.getInicio().getTituloMusica(), "inicio nao muda");
        assertEquals("Musica 3", playlist.getFim().getTituloMusica(), "fim nao muda");
    }

    private static void nextNoMeioAvancaParaProximaMusica() {
        PlaylistDupla playlist = criarPlaylist();
        playlist.next();

        assertEquals("Musica 2", playlist.getAtualMusica().getTituloMusica(), "next para segunda");
    }

    static void tocarPausarERepeatFuncionam() {
        PlaylistDupla playlist = criarPlaylist();
        PlayerService player = new PlayerService(playlist);

        NoMusica tocada = player.play();
        assertEquals("Musica 1", tocada.getTituloMusica(), "player toca musica atual");
        assertTrue(player.isTocando(), "player fica tocando");

        player.pause();
        assertTrue(player.isPausado(), "player fica pausado");

        assertEquals("ONE", player.repeat(), "repeat muda para ONE");
        assertEquals("ALL", player.repeat(), "repeat muda para ALL");
        assertEquals("OFF", player.repeat(), "repeat volta para OFF");
    }

    static void stopDesativaTocarEPausa() {
        PlayerService player = new PlayerService(criarPlaylist());

        player.play();
        player.pause();
        player.stop();

        assertFalse(player.isTocando(), "stop desativa tocando");
        assertFalse(player.isPausado(), "stop desativa pausa");
    }

    static void pauseSemMusicaTocandoRetornaNull() {
        PlayerService player = new PlayerService(criarPlaylist());

        assertNull(player.pause(), "pause sem tocar retorna null");
    }

    static void nextNoPlayerAvancaMusica() {
        PlayerService player = new PlayerService(criarPlaylist());

        assertEquals("Musica 2", player.next().getTituloMusica(), "player next avanca");
    }

    static void repeatAlternaEntreTodosOsModos() {
        PlayerService player = new PlayerService(criarPlaylist());

        assertEquals("ONE", player.repeat(), "repeat one");
        assertEquals("ALL", player.repeat(), "repeat all");
        assertEquals("OFF", player.repeat(), "repeat off");
    }

    static void shuffleEmPlaylistComUmaMusicaMantemAtual() {
        PlaylistDupla playlist = new PlaylistDupla();
        playlist.adicionarMusica("Faixa 1", "Artista 1", "faixa1.mp3");
        PlayerService player = new PlayerService(playlist);

        assertEquals("Faixa 1", player.shuffle().getTituloMusica(), "shuffle unica musica");
    }

    static PlaylistDupla criarPlaylist() {
        PlaylistDupla playlist = new PlaylistDupla();
        playlist.adicionarMusica("Musica 1", "Artista 1", "musica1.mp3");
        playlist.adicionarMusica("Musica 2", "Artista 2", "musica2.mp3");
        playlist.adicionarMusica("Musica 3", "Artista 3", "musica3.mp3");
        return playlist;
    }

    private static void assertEquals(Object esperado, Object atual, String mensagem) {
        if (!esperado.equals(atual)) {
            throw new AssertionError(mensagem + " - esperado: " + esperado + ", atual: " + atual);
        }
    }

    private static void assertTrue(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }

    private static void assertFalse(boolean condicao, String mensagem) {
        if (condicao) {
            throw new AssertionError(mensagem);
        }
    }

    private static void assertNull(Object atual, String mensagem) {
        if (atual != null) {
            throw new AssertionError(mensagem + " - esperado null, atual: " + atual);
        }
    }

    public static class Teste {
        private final String nome;
        private final Runnable acao;

        public Teste(String nome, Runnable acao) {
            this.nome = nome;
            this.acao = acao;
        }

        public void executar() {
            acao.run();
        }

        public String getNome() {
            return nome;
        }
    }
}
