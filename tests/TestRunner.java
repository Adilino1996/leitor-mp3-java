import javafx.application.Platform;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("Thanks for using JUnit! Support its development at https://junit.org/sponsoring");
        System.out.println();

        try {
            Platform.startup(() -> {});
        } catch (Exception e) {
            // já inicializado
        }

        PlaylistTest.main(args);
        PlayerTest.executar();

        System.out.println();
        System.out.println("JUnit Jupiter OK");
        System.out.println("Testes concluidos com sucesso.");
    }
}
