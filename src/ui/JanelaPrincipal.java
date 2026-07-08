package ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.NoMusica;
import model.PlayerService;
import model.PlaylistDupla;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JanelaPrincipal extends Application {
    private PlaylistDupla playlist;
    private PlayerService player;
    private VBox playlistCardsBox;
    private Label labelTituloMusica;
    private Label labelArtista;
    private Label labelStatus;
    private Label labelTime;
    private Label albumArtLabel;
    private Button btnPlayPause;
    private Button btnRepeat;
    private Label countLabel;
    private List<Region> waveBars;
    private Timeline animacaoEqualizador;
    private Random rng;
    private StackPane albumWrapper;
    private Slider progressSlider;

    // ── Neon Noir Palette ───────────────────────────────────
    private static final String BG_ABYSS     = "#05050E";
    private static final String BG_PANEL     = "#0A0A1A";
    private static final String BG_CARD      = "#0E0E28";
    private static final String BG_CARD_HOV  = "#181842";
    private static final String BG_INSET     = "#080818";
    private static final String CYAN         = "#00F5FF";
    private static final String CYAN_DIM     = "#0088AA";
    private static final String NEON_PINK    = "#FF0080";
    private static final String NEON_PURPLE  = "#8B5CF6";
    private static final String TEXT_GLOW    = "#E8EEFF";
    private static final String TEXT_MUTED   = "#4A5580";
    private static final String TEXT_SEC     = "#7888B0";
    private static final String BORDER_DIM   = "#181840";
    private static final String BORDER_GLOW  = "#282868";
    private static final String SUCCESS      = "#00FF88";
    private static final String WARN         = "#FF8800";

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        playlist = new PlaylistDupla();
        player   = new PlayerService(playlist);
        rng      = new Random();

        stage.setTitle("Leitor MP3");
        stage.setMinWidth(1100);
        stage.setMinHeight(700);

        VBox leftPane = buildControlsPane(stage);
        leftPane.setPrefWidth(300);
        leftPane.setMinWidth(280);

        VBox centerPane = buildHeroPane();

        VBox rightPane = buildPlaylistPane(stage);
        rightPane.setPrefWidth(360);
        rightPane.setMinWidth(320);

        HBox mainContent = new HBox(leftPane, centerPane, rightPane);
        HBox.setHgrow(centerPane, Priority.ALWAYS);

        Scene scene = new Scene(mainContent, 1100, 720);
        scene.setFill(Color.web(BG_ABYSS));

        stage.setScene(scene);
        stage.show();

        atualizarPlaylistCards();
        iniciarAnimacaoEqualizador();
    }

    // ══════════════════════════════════════════════════════════
    //  EQUALIZER ANIMATION
    // ══════════════════════════════════════════════════════════
    private void iniciarAnimacaoEqualizador() {
        animacaoEqualizador = new Timeline();
        animacaoEqualizador.setCycleCount(Timeline.INDEFINITE);
        animacaoEqualizador.getKeyFrames().add(
            new KeyFrame(Duration.millis(180), e -> animarBarras())
        );
        animacaoEqualizador.play();
    }

    private void animarBarras() {
        if (waveBars == null) return;
        boolean tocando = player.isTocando();

        if (!progressSlider.isPressed() && !progressSlider.isValueChanging()
            && player.getMediaPlayer() != null) {
            Duration total = player.getDuracaoTotal();
            Duration atual = player.getTempoAtual();
            if (total != null && !total.isUnknown() && total.toMillis() > 0) {
                progressSlider.setValue(Math.min(atual.toMillis() / total.toMillis(), 1.0));
            }
        }

        for (Region bar : waveBars) {
            double base = 8.0 + rng.nextInt(42);
            double h = tocando ? base : 12.0 + rng.nextInt(8);
            bar.setMinHeight(h);
            bar.setMaxHeight(h);
            String cor = tocando ? CYAN : CYAN_DIM;
            double op = tocando ? 0.5 + rng.nextDouble() * 0.5 : 0.25;
            bar.setStyle(
                "-fx-background-color: " + cor + ";" +
                "-fx-background-radius: 3;" +
                "-fx-opacity: " + op + ";");
        }
    }

    // ══════════════════════════════════════════════════════════
    //  LEFT — Controls
    // ══════════════════════════════════════════════════════════
    private VBox buildControlsPane(Stage stage) {
        Label brand = new Label("◈ WAV");
        brand.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: 900;" +
            "-fx-text-fill: " + CYAN + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-letter-spacing: 4px;");

        Label appTitle = new Label("PLAYER");
        appTitle.setStyle(
            "-fx-font-size: 20px; -fx-font-weight: 300;" +
            "-fx-text-fill: " + TEXT_GLOW + ";" +
            "-fx-font-family: 'Georgia', serif;");

        HBox brandRow = new HBox(10, brand, appTitle);
        brandRow.setAlignment(Pos.CENTER_LEFT);

        // ── Now Playing ──
        labelStatus = new Label("✦ Nenhuma música");
        labelStatus.setStyle(
            "-fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;");
        labelStatus.setWrapText(true);

        labelTime = new Label("--:-- / --:--");
        labelTime.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold;" +
            "-fx-text-fill: " + CYAN + ";" +
            "-fx-font-family: 'Consolas', monospace;");

        HBox timeRow = new HBox(labelTime);
        timeRow.setAlignment(Pos.CENTER_LEFT);

        VBox nowPlayingBox = new VBox(6, labelStatus, timeRow);
        nowPlayingBox.setStyle(
            "-fx-background-color: " + BG_INSET + ";" +
            "-fx-background-radius: 14;" +
            "-fx-padding: 14 16 14 16;" +
            "-fx-border-color: " + BORDER_DIM + ";" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,245,255,0.06), 20, 0, 0, 0);");

        // ── PLAY / PAUSE ──
        btnPlayPause = new Button("▶  PLAY");
        btnPlayPause.setStyle(stylePlayBtn(false));
        btnPlayPause.setMinHeight(60);
        btnPlayPause.setMaxWidth(Double.MAX_VALUE);
        btnPlayPause.setOnAction(e -> alternarPlayPause());

        Button btnAnterior = new Button("⏮");
        btnAnterior.setStyle(styleNavBtn());
        btnAnterior.setMinHeight(48);
        btnAnterior.setMaxWidth(Double.MAX_VALUE);
        btnAnterior.setOnAction(e -> acionarAnterior());

        Button btnProximo = new Button("⏭");
        btnProximo.setStyle(styleNavBtn());
        btnProximo.setMinHeight(48);
        btnProximo.setMaxWidth(Double.MAX_VALUE);
        btnProximo.setOnAction(e -> acionarProximo());

        HBox prevNextRow = new HBox(10, btnAnterior, btnProximo);
        prevNextRow.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnAnterior, Priority.ALWAYS);
        HBox.setHgrow(btnProximo, Priority.ALWAYS);

        // ── REPEAT / SHUFFLE ──
        btnRepeat = new Button("⟳  Repeat");
        btnRepeat.setStyle(stylePillBtn(false));
        btnRepeat.setMinHeight(42);
        btnRepeat.setMaxWidth(Double.MAX_VALUE);
        btnRepeat.setOnAction(e -> alternarRepeat());

        Button btnShuffle = new Button("⇄  Shuffle");
        btnShuffle.setStyle(stylePillBtn(false));
        btnShuffle.setMinHeight(42);
        btnShuffle.setMaxWidth(Double.MAX_VALUE);
        btnShuffle.setOnAction(e -> acionarShuffle());

        HBox shuffleRepeatRow = new HBox(10, btnRepeat, btnShuffle);
        shuffleRepeatRow.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnRepeat, Priority.ALWAYS);
        HBox.setHgrow(btnShuffle, Priority.ALWAYS);

        Region sep = new Region();
        sep.setMinHeight(1);
        sep.setMaxWidth(Double.MAX_VALUE);
        sep.setStyle("-fx-background-color: " + BORDER_DIM + ";");

        Region sepMid = new Region();
        sepMid.setMinHeight(1);
        sepMid.setMaxWidth(Double.MAX_VALUE);
        sepMid.setStyle("-fx-background-color: " + BORDER_DIM + ";");

        // ── VOLUME ──
        Label volIcon = new Label("◉");
        volIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CYAN + ";");

        Label volLabel = new Label("Volume");
        volLabel.setStyle(
            "-fx-font-size: 12px; -fx-font-weight: 600;" +
            "-fx-text-fill: " + TEXT_SEC + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;");

        Label volPercent = new Label("70%");
        volPercent.setStyle(
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-text-fill: " + CYAN + ";" +
            "-fx-font-family: 'Consolas', monospace;");

        Region volSpacer = new Region();
        HBox.setHgrow(volSpacer, Priority.ALWAYS);
        HBox volRow = new HBox(8, volIcon, volLabel, volSpacer, volPercent);
        volRow.setAlignment(Pos.CENTER_LEFT);
        volRow.setMaxWidth(Double.MAX_VALUE);

        Slider volumeSlider = new Slider(0, 100, 70);
        volumeSlider.setMaxWidth(Double.MAX_VALUE);
        volumeSlider.setStyle(
            "-fx-control-inner-background: " + BORDER_GLOW + ";" +
            "-fx-background-color: transparent;");
        volumeSlider.valueProperty().addListener((obs, o, n) -> {
            volPercent.setText(n.intValue() + "%");
            player.setVolume(n.doubleValue() / 100.0);
        });

        VBox volumeBox = new VBox(10, volRow, volumeSlider);
        volumeBox.setMaxWidth(Double.MAX_VALUE);
        volumeBox.setStyle(
            "-fx-background-color: " + BG_INSET + ";" +
            "-fx-background-radius: 14;" +
            "-fx-padding: 14 16 14 16;" +
            "-fx-border-color: " + BORDER_DIM + ";" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1;");

        Region sep2 = new Region();
        sep2.setMinHeight(1);
        sep2.setMaxWidth(Double.MAX_VALUE);
        sep2.setStyle("-fx-background-color: " + BORDER_DIM + ";");

        Label addLabel = new Label("──  ADICIONAR  ──");
        addLabel.setStyle(
            "-fx-font-size: 10px; -fx-text-fill: " + TEXT_MUTED + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-letter-spacing: 3px;");

        Button btnArquivo = new Button("+  Arquivo MP3");
        btnArquivo.setStyle(styleAddBtn());
        btnArquivo.setMinHeight(42);
        btnArquivo.setMaxWidth(Double.MAX_VALUE);
        btnArquivo.setOnAction(e -> adicionarArquivo(stage));

        Button btnPasta = new Button("+  Carregar Pasta");
        btnPasta.setStyle(styleAddBtn());
        btnPasta.setMinHeight(42);
        btnPasta.setMaxWidth(Double.MAX_VALUE);
        btnPasta.setOnAction(e -> carregarPasta(stage));

        VBox pane = new VBox(18,
            brandRow,
            nowPlayingBox,
            sep,
            btnPlayPause,
            prevNextRow,
            shuffleRepeatRow,
            sepMid,
            volumeBox,
            sep2,
            addLabel,
            btnArquivo,
            btnPasta
        );
        pane.setPadding(new Insets(28, 22, 28, 22));
        pane.setStyle(
            "-fx-background-color: " + BG_PANEL + ";" +
            "-fx-border-color: " + BORDER_DIM + ";" +
            "-fx-border-width: 0 1 0 0;");

        return pane;
    }

    // ══════════════════════════════════════════════════════════
    //  CENTER — Hero
    // ══════════════════════════════════════════════════════════
    private VBox buildHeroPane() {
        albumArtLabel = new Label("♫");
        albumArtLabel.setStyle(
            "-fx-font-size: 80px;" +
            "-fx-text-fill: rgba(0,245,255,0.15);" +
            "-fx-background-color: #080820;" +
            "-fx-background-radius: 30;" +
            "-fx-border-color: " + BORDER_GLOW + ";" +
            "-fx-border-radius: 30;" +
            "-fx-border-width: 1;" +
            "-fx-min-width: 320;" +
            "-fx-min-height: 320;" +
            "-fx-max-width: 360;" +
            "-fx-max-height: 360;" +
            "-fx-alignment: center;");
        albumArtLabel.setAlignment(Pos.CENTER);

        albumWrapper = new StackPane(albumArtLabel);
        albumWrapper.setAlignment(Pos.CENTER);
        albumWrapper.setStyle(
            "-fx-padding: 8;" +
            "-fx-background-color: rgba(10,10,30,0.8);" +
            "-fx-background-radius: 38;" +
            "-fx-border-color: " + CYAN_DIM + ";" +
            "-fx-border-radius: 38;" +
            "-fx-border-width: 1.5;");

        // Progress bar (seek)
        progressSlider = new Slider(0, 1, 0);
        progressSlider.setMaxWidth(450);
        progressSlider.setMinHeight(6);
        progressSlider.setStyle(
            "-fx-control-inner-background: " + BORDER_DIM + ";" +
            "-fx-background-color: transparent;" +
            "-fx-track-fill: " + CYAN + ";" +
            "-fx-thumb-color: " + CYAN + ";" +
            "-fx-thumb-radius: 8;");
        progressSlider.setOnMouseReleased(e -> {
            if (player.getMediaPlayer() != null) {
                player.seek(progressSlider.getValue());
            }
        });
        progressSlider.setOnTouchReleased(e -> {
            if (player.getMediaPlayer() != null) {
                player.seek(progressSlider.getValue());
            }
        });

        // Wave bars (animated equalizer)
        HBox waveBox = buildWaveBars();

        labelTituloMusica = new Label("Nenhuma música");
        labelTituloMusica.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXT_GLOW + ";" +
            "-fx-font-family: 'Georgia', serif;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;");
        labelTituloMusica.setWrapText(true);
        labelTituloMusica.setMaxWidth(500);
        labelTituloMusica.setAlignment(Pos.CENTER);
        labelTituloMusica.setMaxWidth(Double.MAX_VALUE);

        labelArtista = new Label("—");
        labelArtista.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + TEXT_MUTED + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;");

        Label pillFormat = buildPill("MP3", CYAN);
        Label pillHiFi   = buildPill("WAV", NEON_PINK);
        Label pillStereo = buildPill("STEREO", NEON_PURPLE);
        HBox pillRow = new HBox(8, pillFormat, pillHiFi, pillStereo);
        pillRow.setAlignment(Pos.CENTER);

        VBox textBlock = new VBox(8,
            labelTituloMusica,
            labelArtista,
            pillRow
        );
        textBlock.setAlignment(Pos.CENTER);

        VBox heroContent = new VBox(24,
            albumWrapper,
            progressSlider,
            waveBox,
            textBlock
        );
        heroContent.setAlignment(Pos.CENTER);
        heroContent.setPadding(new Insets(36, 40, 36, 40));

        VBox center = new VBox(heroContent);
        center.setAlignment(Pos.CENTER);
        VBox.setVgrow(heroContent, Priority.ALWAYS);
        center.setStyle("-fx-background-color: " + BG_ABYSS + ";");

        return center;
    }

    private HBox buildWaveBars() {
        waveBars = new ArrayList<>();
        HBox box = new HBox(3);
        box.setAlignment(Pos.BOTTOM_CENTER);

        // Altura fixa: o container nao muda de tamanho mesmo quando as barras animam
        box.setMinHeight(60);
        box.setPrefHeight(60);
        box.setMaxHeight(60);
        int[] heights = {8, 14, 20, 12, 24, 16, 28, 18, 24, 14, 20, 10, 16, 24, 12, 18, 28, 14, 22, 10};
        for (int h : heights) {
            Region bar = new Region();
            bar.setMinWidth(4);
            bar.setMaxWidth(4);
            bar.setMinHeight(h);
            bar.setMaxHeight(h);
            bar.setStyle(
                "-fx-background-color: " + CYAN_DIM + ";" +
                "-fx-background-radius: 3;" +
                "-fx-opacity: 0.3;");
            waveBars.add(bar);
            box.getChildren().add(bar);
        }
        return box;
    }

    private Label buildPill(String text, String color) {
        Label l = new Label(text);
        l.setStyle(
            "-fx-font-size: 9px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-family: 'Consolas', monospace;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-radius: 20;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 3 10 3 10;" +
            "-fx-opacity: 0.7;");
        return l;
    }

    // ══════════════════════════════════════════════════════════
    //  RIGHT — Playlist
    // ══════════════════════════════════════════════════════════
    private VBox buildPlaylistPane(Stage stage) {
        Label title = new Label("FILA");
        title.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 900;" +
            "-fx-text-fill: " + TEXT_GLOW + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-letter-spacing: 4px;");

        countLabel = new Label("0 músicas");
        countLabel.setStyle(
            "-fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;");

        VBox headerBox = new VBox(3, title, countLabel);

        TextField buscaField = new TextField();
        buscaField.setPromptText("Buscar...");
        buscaField.setStyle(
            "-fx-background-color: " + BG_INSET + ";" +
            "-fx-text-fill: " + TEXT_GLOW + ";" +
            "-fx-prompt-text-fill: " + TEXT_MUTED + ";" +
            "-fx-padding: 10 14 10 14;" +
            "-fx-font-size: 12px;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + BORDER_DIM + ";" +
            "-fx-border-width: 1;");
        buscaField.setMaxWidth(Double.MAX_VALUE);
        buscaField.textProperty().addListener((obs, o, n) -> filtrarPlaylist(n));

        playlistCardsBox = new VBox(8);
        playlistCardsBox.setPadding(new Insets(4, 0, 12, 0));

        ScrollPane scroll = new ScrollPane(playlistCardsBox);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox pane = new VBox(16, headerBox, buscaField, scroll);
        pane.setPadding(new Insets(28, 18, 18, 18));
        pane.setStyle(
            "-fx-background-color: " + BG_PANEL + ";" +
            "-fx-border-color: " + BORDER_DIM + ";" +
            "-fx-border-width: 0 0 0 1;");

        return pane;
    }

    // ══════════════════════════════════════════════════════════
    //  Track Card
    // ══════════════════════════════════════════════════════════
    private VBox buildTrackCard(int numero, NoMusica musica, boolean isAtual) {
        Region accentBar = new Region();
        accentBar.setMinWidth(4);
        accentBar.setMaxWidth(4);
        String accentCor = isAtual ? CYAN : "transparent";
        String bgCardAtual = "#0C0C30";
        accentBar.setStyle(
            "-fx-background-color: " + accentCor + ";" +
            "-fx-background-radius: 4 0 0 4;" +
            "-fx-effect: " + (isAtual ? "dropshadow(gaussian, rgba(0,245,255,0.4), 12, 0, 0, 0)" : "none") + ";");

        Label numLabel = new Label(isAtual ? "▶" : String.format("%02d", numero));
        numLabel.setStyle(
            "-fx-font-size: " + (isAtual ? "13px" : "11px") + ";" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + (isAtual ? CYAN : TEXT_MUTED) + ";" +
            "-fx-min-width: 28; -fx-max-width: 28;" +
            (isAtual ? "" : "-fx-font-family: 'Consolas', monospace;"));

        Label tituloLabel = new Label(musica.getTituloMusica());
        tituloLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: " + (isAtual ? "bold" : "normal") + ";" +
            "-fx-text-fill: " + (isAtual ? TEXT_GLOW : TEXT_SEC) + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;");
        tituloLabel.setEllipsisString("…");
        tituloLabel.setMaxWidth(200);

        Label artistaLabel = new Label(musica.getArtistaMusica());
        artistaLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + TEXT_MUTED + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;");

        VBox textBox = new VBox(2, tituloLabel, artistaLabel);
        if (isAtual) {
            Label badge = new Label("◉ NOW");
            badge.setStyle(
                "-fx-font-size: 8px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + CYAN + ";" +
                "-fx-font-family: 'Consolas', monospace;" +
                "-fx-background-color: rgba(0,245,255,0.1);" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 2 6 2 6;");
            textBox.getChildren().add(badge);
        }

        HBox inner = new HBox(10, numLabel, textBox);
        inner.setAlignment(Pos.CENTER_LEFT);
        inner.setPadding(new Insets(10, 12, 10, 10));
        HBox.setHgrow(textBox, Priority.ALWAYS);

        HBox cardRow = new HBox(accentBar, inner);
        cardRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(inner, Priority.ALWAYS);

        VBox card = new VBox(cardRow);
        card.setStyle(
            "-fx-background-color: " + (isAtual ? bgCardAtual : BG_CARD) + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + (isAtual ? CYAN : BORDER_DIM) + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;");

        card.setOnMouseEntered(e -> {
            if (!isAtual) card.setStyle(
                "-fx-background-color: " + BG_CARD_HOV + ";" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + BORDER_GLOW + ";" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;");
        });
        card.setOnMouseExited(e -> {
            if (!isAtual) card.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + BORDER_DIM + ";" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;");
        });
        card.setOnMouseClicked(e -> {
            selecionarMusica(musica);
        });

        return card;
    }

    // ══════════════════════════════════════════════════════════
    //  Button Styles
    // ══════════════════════════════════════════════════════════
    private String stylePlayBtn(boolean isPaused) {
        String bg    = isPaused ? "rgba(0,245,255,0.08)" : CYAN;
        String texto = isPaused ? CYAN : "#05050E";
        String brilho = isPaused ? "0.15" : "0.5";
        return "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: " + texto + ";" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: " + CYAN + ";" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 1.5;" +
            "-fx-cursor: hand;" +
            "-fx-letter-spacing: 2px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,245,255," + brilho + "), 24, 0, 0, 0);";
    }

    private String styleNavBtn() {
        return "-fx-background-color: " + BG_CARD + ";" +
            "-fx-text-fill: " + TEXT_SEC + ";" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + BORDER_DIM + ";" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;";
    }

    private String stylePillBtn(boolean active) {
        String bg   = active ? "rgba(0,245,255,0.12)" : BG_INSET;
        String cor  = active ? CYAN : TEXT_MUTED;
        String borda = active ? CYAN : BORDER_DIM;
        return "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: " + cor + ";" +
            "-fx-font-size: 11px;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + borda + ";" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;";
    }

    private String styleAddBtn() {
        return "-fx-background-color: transparent;" +
            "-fx-text-fill: " + TEXT_MUTED + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + BORDER_DIM + ";" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;";
    }

    // ══════════════════════════════════════════════════════════
    //  Actions
    // ══════════════════════════════════════════════════════════
    private void mostrarErro(String titulo, String mensagem) {
        System.out.println("ERRO: " + titulo + " - " + mensagem);
        labelStatus.setText("⚠ Erro: " + mensagem);
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.show();
    }

    private void adicionarArquivo(Stage stage) {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Selecionar arquivo MP3");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
            File arquivo = fc.showOpenDialog(stage);
            if (arquivo != null) {
                String titulo = arquivo.getName().replaceFirst("(?i)\\.mp3$", "");
                playlist.adicionarMusica(titulo, "Desconhecido", arquivo.getAbsolutePath());
                System.out.println("Adicionado: " + arquivo.getAbsolutePath());
                atualizarPlaylistCards();
                atualizarHero();
            }
        } catch (Exception e) {
            mostrarErro("Erro ao adicionar arquivo", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void carregarPasta(Stage stage) {
        try {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Selecionar pasta de músicas");
            File pasta = dc.showDialog(stage);
            if (pasta != null) {
                player.carregarMP3DaPasta(pasta.getAbsolutePath());
                System.out.println("Pasta carregada: " + pasta.getAbsolutePath());
                atualizarPlaylistCards();
                atualizarHero();
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar pasta", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void alternarPlayPause() {
        try {
            if (player.isTocando()) {
                player.pause();
                btnPlayPause.setText("▶  PLAY");
                btnPlayPause.setStyle(stylePlayBtn(true));
                labelStatus.setText("⏸ Pausado");
            } else {
                player.limparErro();
                NoMusica m = player.isPausado() ? player.resume() : player.play();
                if (m != null) {
                    btnPlayPause.setText("⏸  PAUSE");
                    btnPlayPause.setStyle(stylePlayBtn(false));
                    labelStatus.setText("◉ " + m.getTituloMusica());
                } else {
                    String erro = player.getErro();
                    if (erro != null) {
                        labelStatus.setText("⚠ " + erro);
                    } else {
                        labelStatus.setText("— Nenhuma música disponível.");
                    }
                }
            }
            atualizarHero();
            atualizarPlaylistCards();
        } catch (Exception e) {
            mostrarErro("Erro no Play/Pause", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void acionarAnterior() {
        try {
            player.limparErro();
            NoMusica m = player.previous();
            if (m != null) {
                btnPlayPause.setText("⏸  PAUSE");
                btnPlayPause.setStyle(stylePlayBtn(false));
                labelStatus.setText("◉ " + m.getTituloMusica());
            } else {
                String erro = player.getErro();
                if (erro != null) labelStatus.setText("⚠ " + erro);
            }
            atualizarHero();
            atualizarPlaylistCards();
        } catch (Exception e) {
            mostrarErro("Erro no Anterior", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void acionarProximo() {
        try {
            player.limparErro();
            NoMusica m = player.next();
            if (m != null) {
                btnPlayPause.setText("⏸  PAUSE");
                btnPlayPause.setStyle(stylePlayBtn(false));
                labelStatus.setText("◉ " + m.getTituloMusica());
            } else {
                String erro = player.getErro();
                if (erro != null) labelStatus.setText("⚠ " + erro);
            }
            atualizarHero();
            atualizarPlaylistCards();
        } catch (Exception e) {
            mostrarErro("Erro no Próximo", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void alternarRepeat() {
        try {
            String modo = player.repeat();
            btnRepeat.setText("⟳  Repeat: " + modo);
            boolean active = !modo.equalsIgnoreCase("off");
            btnRepeat.setStyle(stylePillBtn(active));
        } catch (Exception e) {
            mostrarErro("Erro no Repeat", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void acionarShuffle() {
        try {
            player.limparErro();
            NoMusica m = player.shuffle();
            if (m != null) {
                btnPlayPause.setText("⏸  PAUSE");
                btnPlayPause.setStyle(stylePlayBtn(false));
                labelStatus.setText("⇄ " + m.getTituloMusica());
            } else {
                String erro = player.getErro();
                if (erro != null) labelStatus.setText("⚠ " + erro);
            }
            atualizarHero();
            atualizarPlaylistCards();
        } catch (Exception e) {
            mostrarErro("Erro no Shuffle", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void selecionarMusica(NoMusica musica) {
        try {
            playlist.setAtualMusica(musica);
            player.limparErro();
            NoMusica m = player.play();
            if (m != null) {
                btnPlayPause.setText("⏸  PAUSE");
                btnPlayPause.setStyle(stylePlayBtn(false));
                labelStatus.setText("◉ " + musica.getTituloMusica());
            } else {
                String erro = player.getErro();
                labelStatus.setText(erro != null ? "⚠ " + erro : "— Não foi possível reproduzir.");
            }
            atualizarHero();
            atualizarPlaylistCards();
        } catch (Exception e) {
            mostrarErro("Erro ao selecionar música", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════
    //  Update Helpers
    // ══════════════════════════════════════════════════════════
    private void vincularTempo() {
        labelTime.textProperty().unbind();
        if (player.getMediaPlayer() != null) {
            labelTime.textProperty().bind(player.criarStringTempo());
        } else {
            labelTime.setText("--:-- / --:--");
        }
    }

    private void atualizarHero() {
        NoMusica atual = playlist.getAtualMusica();
        if (atual == null) {
            labelTituloMusica.setText("Nenhuma música");
            labelArtista.setText("—");
            albumArtLabel.setText("♫");
        } else {
            labelTituloMusica.setText(atual.getTituloMusica());
            labelArtista.setText(atual.getArtistaMusica());
        }
        vincularTempo();
    }

    private void atualizarPlaylistCards() {
        playlistCardsBox.getChildren().clear();
        NoMusica cursor = playlist.getInicio();
        int i = 1;
        while (cursor != null) {
            boolean isAtual = cursor == playlist.getAtualMusica();
            VBox card = buildTrackCard(i, cursor, isAtual);
            playlistCardsBox.getChildren().add(card);
            cursor = cursor.getProximo();
            i++;
        }
        countLabel.setText(playlist.getTamanho() + " música(s)");
        if (playlistCardsBox.getChildren().isEmpty()) {
            Label empty = new Label("Adicione músicas com os botões");
            empty.setStyle(
                "-fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED + ";" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-wrap-text: true;");
            empty.setMaxWidth(280);
            empty.setWrapText(true);
            playlistCardsBox.getChildren().add(empty);
        }
    }

    private void filtrarPlaylist(String filtro) {
        playlistCardsBox.getChildren().clear();
        NoMusica cursor = playlist.getInicio();
        int i = 1;
        while (cursor != null) {
            String titulo = cursor.getTituloMusica().toLowerCase();
            if (filtro == null || filtro.isBlank() || titulo.contains(filtro.toLowerCase())) {
                boolean isAtual = cursor == playlist.getAtualMusica();
                playlistCardsBox.getChildren().add(buildTrackCard(i, cursor, isAtual));
            }
            cursor = cursor.getProximo();
            i++;
        }
    }
}
