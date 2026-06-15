import static java.lang.System.out;

public class PlayerTest {
    public static void main(String[] args) {
        executar();
    }

    public static void executar() {
        PlaylistTest.Teste[] testes = {
            new PlaylistTest.Teste("tocarPausarERepeatFuncionam", PlaylistTest::tocarPausarERepeatFuncionam),
            new PlaylistTest.Teste("stopDesativaTocarEPausa", PlaylistTest::stopDesativaTocarEPausa),
            new PlaylistTest.Teste("pauseSemMusicaTocandoRetornaNull", PlaylistTest::pauseSemMusicaTocandoRetornaNull),
            new PlaylistTest.Teste("nextNoPlayerAvancaMusica", PlaylistTest::nextNoPlayerAvancaMusica),
            new PlaylistTest.Teste("repeatAlternaEntreTodosOsModos", PlaylistTest::repeatAlternaEntreTodosOsModos),
            new PlaylistTest.Teste("shuffleEmPlaylistComUmaMusicaMantemAtual", PlaylistTest::shuffleEmPlaylistComUmaMusicaMantemAtual)
        };

        out.println("|-- PlayerTest OK");
        for (PlaylistTest.Teste teste : testes) {
            teste.executar();
            out.println("|   |-- " + teste.getNome() + "() OK");
        }
    }
}
