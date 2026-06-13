package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.NoMusica;
import model.PlaylistDupla;
import java.io.File;

public class JanelaPrincipal extends Application {

    private PlaylistDupla playlist = new PlaylistDupla();
    private ListView<String> listView = new ListView<>();
    private Label labelMusica = new Label("Nenhuma música selecionada");
    private Slider sliderVolume = new Slider(0, 100, 50);
    private Slider sliderProgresso = new Slider(0, 100, 0);
    private boolean pausado = false;
    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage stage) {
        labelMusica.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        listView.setPrefHeight(300);
        listView.setStyle("-fx-background-color: #2b2b2b; -fx-control-inner-background: #2b2b2b; -fx-text-fill: white;");
        listView.setOnMouseClicked(e -> {
            int idx = listView.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                NoMusica cursor = playlist.getInicio();
                for (int i = 0; i < idx; i++) cursor = cursor.getProximo();
                playlist.setAtualMusica(cursor);
                atualizarLabel();
            }
        });

        Button btnAnterior  = criarBotao("⏮", "Anterior");
        Button btnPlayPause = criarBotao("▶", "Play / Pause");
        Button btnStop      = criarBotao("⏹", "Stop");
        Button btnNext      = criarBotao("⏭", "Próxima");
        Button btnRepeat    = criarBotao("🔁", "Repeat");
        Button btnShuffle   = criarBotao("🔀", "Shuffle");
        Button btnCarregar  = criarBotao("📂 Carregar", "Carregar pasta de músicas");
        Button btnFechar    = criarBotao("❌ Fechar", "Fechar aplicação");

        btnAnterior.setOnAction(e -> { playlist.previous(); atualizarLabel(); });
        btnNext.setOnAction(e -> { playlist.next(); atualizarLabel(); });

        btnStop.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                pausado = true;
                btnPlayPause.setText("▶");
                labelMusica.setText("⏹ Parado");
                sliderProgresso.setValue(0);
            }
        });

        btnRepeat.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(javafx.util.Duration.ZERO);
                mediaPlayer.play();
                labelMusica.setText("🔁 " + playlist.getAtualMusica().getTituloMusica());
            }
        });

        btnShuffle.setOnAction(e -> labelMusica.setText("🔀 Shuffle ativo"));

        btnPlayPause.setOnAction(e -> {
            if (mediaPlayer == null) return;
            if (pausado) {
                mediaPlayer.play();
                pausado = false;
                btnPlayPause.setText("⏸");
            } else {
                mediaPlayer.pause();
                pausado = true;
                btnPlayPause.setText("▶");
            }
        });

        btnCarregar.setOnAction(e -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Escolher pasta de músicas");
            File pasta = dc.showDialog(stage);
            if (pasta != null) carregarPasta(pasta);
        });

        btnFechar.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.stop();
            stage.close();
        });

        sliderVolume.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) mediaPlayer.setVolume(newVal.doubleValue() / 100.0);
        });

        // Progresso
        Label lblProgresso = new Label("Progresso:");
        lblProgresso.setStyle("-fx-text-fill: #aaaaaa;");
        sliderProgresso.setMaxWidth(Double.MAX_VALUE);
        sliderProgresso.setStyle("-fx-control-inner-background: #555;");
        HBox controlosProgresso = new HBox(8, lblProgresso, sliderProgresso);
        controlosProgresso.setAlignment(Pos.CENTER);
        HBox.setHgrow(sliderProgresso, Priority.ALWAYS);

        // Volume
        Label lblVolume = new Label("Volume:");
        lblVolume.setStyle("-fx-text-fill: #aaaaaa;");
        sliderVolume.setStyle("-fx-control-inner-background: #555;");
        HBox controlosVolume = new HBox(8, lblVolume, sliderVolume);
        controlosVolume.setAlignment(Pos.CENTER);

        HBox botoes = new HBox(8,
            btnAnterior, btnPlayPause, btnStop, btnNext, btnRepeat, btnShuffle, btnFechar);
        botoes.setAlignment(Pos.CENTER);

        VBox root = new VBox(12,
            btnCarregar, listView, labelMusica, botoes, controlosProgresso, controlosVolume);
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: #1e1e1e;");

        Scene scene = new Scene(root, 500, 550);
        stage.setTitle("Leitor MP3");
        stage.setScene(scene);
        stage.show();
    }

    private Button criarBotao(String texto, String tooltip) {
        Button btn = new Button(texto);
        btn.setTooltip(new Tooltip(tooltip));
        btn.setStyle("""
            -fx-background-color: #3c3f41;
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
            -fx-padding: 6 12;
            """);
        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: #555555;
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
            -fx-padding: 6 12;
            """));
        btn.setOnMouseExited(e -> btn.setStyle("""
            -fx-background-color: #3c3f41;
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
            -fx-padding: 6 12;
            """));
        return btn;
    }

    private void carregarPasta(File pasta) {
        File[] ficheiros = pasta.listFiles((d, n) -> n.endsWith(".mp3"));
        if (ficheiros == null || ficheiros.length == 0) {
            labelMusica.setText("Nenhum MP3 encontrado.");
            return;
        }
        listView.getItems().clear();
        for (File f : ficheiros) {
            String titulo = f.getName().replace(".mp3", "");
            playlist.adicionarMusica(titulo, "Desconhecido", f.getAbsolutePath());
            listView.getItems().add(titulo);
        }
        atualizarLabel();
    }

    private void atualizarLabel() {
        NoMusica atual = playlist.getAtualMusica();
        if (atual == null) return;

        labelMusica.setText("▶ " + atual.getTituloMusica() + " — " + atual.getArtistaMusica());

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        sliderProgresso.setValue(0);

        try {
            File f = new File(atual.getCaminhoFicheiro());
            Media media = new Media(f.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(sliderVolume.getValue() / 100.0);
            mediaPlayer.play();
            pausado = false;

            // Atualiza barra de progresso
            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                if (!sliderProgresso.isValueChanging()) {
                    double duracao = mediaPlayer.getTotalDuration().toSeconds();
                    if (duracao > 0) {
                        sliderProgresso.setValue((newTime.toSeconds() / duracao) * 100);
                    }
                }
            });

            // Permite arrastar para mudar posição
            sliderProgresso.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
                if (!isChanging) {
                    double duracao = mediaPlayer.getTotalDuration().toSeconds();
                    mediaPlayer.seek(javafx.util.Duration.seconds(sliderProgresso.getValue() / 100 * duracao));
                }
            });

            // Quando acaba passa para a próxima
            mediaPlayer.setOnEndOfMedia(() -> {
                playlist.next();
                atualizarLabel();
            });

        } catch (Exception e) {
            labelMusica.setText("Erro ao tocar: " + atual.getTituloMusica());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}