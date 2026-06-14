package ui;  

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class JanelaPrincipal extends Application {

    // Método auxiliar para criar botões com tooltip
    private Button criarBotao(String texto, String descricao) {
        Button btn = new Button(texto);
        btn.setTooltip(new Tooltip(descricao));
        return btn;
    }

    @Override
    public void start(Stage stage) {
        
        Label lblVolume = new Label("Volume:");
        Slider sliderVolume = new Slider(0, 100, 50);

        // Botões usando o método auxiliar
        Button btnanterior = criarBotao("⏮", "Anterior");
        Button btnplay = criarBotao("▶", "Play");
        Button btnpause = criarBotao("⏸", "Pause");
        Button btnstop = criarBotao("⏹", "Stop");
        Button btnproximo = criarBotao("⏭", "Próximo");
        Button btnrepeat = criarBotao("🔁", "Repetir");
        Button btnshuffle = criarBotao("🔀", "Shuffle");
        Button btncarregar = criarBotao("Carregar música", "Carregar música");
    }

    public static void main(String[] args) {
        launch(args); 
    }
}