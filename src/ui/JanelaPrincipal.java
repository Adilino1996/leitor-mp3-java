package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.NoMusica;
import model.PlayerService;
import model.PlaylistDupla;

import java.io.File;

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

    // ── Palette ──────────────────────────────────────────────
    private static final String BG_DEEP      = "#060A12";
    private static final String BG_PANEL     = "#0C1120";
    private static final String BG_CARD      = "#111827";
    private static final String BG_CARD_HOV  = "#162030";
    private static final String BG_INSET     = "#0A0F1C";
    private static final String ACCENT       = "#3D6FFF";
    private static final String ACCENT_DARK  = "#1A3FB3";
    private static final String ACCENT_GLOW  = "#2D5CE6";
    private static final String TEXT_PRIMARY  = "#E8EDF5";
    private static final String TEXT_MUTED   = "#5A6E8C";
    private static final String TEXT_SEC     = "#8899BB";
    private static final String BORDER       = "#1A2640";
    private static final String BORDER_LIGHT = "#243050";
    private static final String SUCCESS      = "#22C55E";
    private static final String WARN         = "#F59E0B";

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        playlist = new PlaylistDupla();
        player   = new PlayerService(playlist);

        stage.setTitle("Leitor MP3");
        stage.setMinWidth(1440);
        stage.setMinHeight(860);

        // ════════════════════════════════════════════════════════
        // LEFT PANE — Player Controls
        // ════════════════════════════════════════════════════════
        VBox leftPane = buildControlsPane(stage);
        leftPane.setPrefWidth(300);
        leftPane.setMinWidth(280);

        // ════════════════════════════════════════════════════════
        // CENTER PANE — Hero
        // ════════════════════════════════════════════════════════
        VBox centerPane = buildHeroPane();

        // ════════════════════════════════════════════════════════
        // RIGHT PANE — Playlist
        // ════════════════════════════════════════════════════════
        VBox rightPane = buildPlaylistPane(stage);
        rightPane.setPrefWidth(360);
        rightPane.setMinWidth(320);

        // ════════════════════════════════════════════════════════
        // MAIN LAYOUT
        // ════════════════════════════════════════════════════════
        HBox mainContent = new HBox(leftPane, centerPane, rightPane);
        HBox.setHgrow(centerPane, Priority.ALWAYS);

        Scene scene = new Scene(mainContent, 1440, 860);
        scene.getStylesheets().add("data:text/css," +
            ".scroll-pane { -fx-background: transparent; -fx-background-color: transparent; }" +
            ".scroll-pane .viewport { -fx-background-color: transparent; }" +
            ".scroll-pane .scroll-bar:vertical .track { -fx-background-color: #0C1120; }" +
            ".scroll-pane .scroll-bar:vertical .thumb { -fx-background-color: #243050; -fx-background-radius: 4; }" +
            ".scroll-pane .scroll-bar:vertical .increment-button," +
            ".scroll-pane .scroll-bar:vertical .decrement-button { -fx-background-color: transparent; }" +
            ".scroll-pane .corner { -fx-background-color: #0C1120; }");

        stage.setScene(scene);
        stage.show();

        atualizarPlaylistCards();
    }

    // ══════════════════════════════════════════════════════════════
    //  LEFT — Controls
    // ══════════════════════════════════════════════════════════════
    private VBox buildControlsPane(Stage stage) {
        // ── App logo / brand ──
        Label brand = new Label("MP3");
        brand.setStyle(
            "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + ACCENT + ";" +
            "-fx-background-color: " + BG_CARD + ";" +
            "-fx-padding: 4 10 4 10;" +
            "-fx-background-radius: 6;" +
            "-fx-letter-spacing: 3px;");

        Label appTitle = new Label("Player");
        appTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

        HBox brandRow = new HBox(10, brand, appTitle);
        brandRow.setAlignment(Pos.CENTER_LEFT);

        // ── Now Playing mini info ──
        labelStatus = new Label("Nenhuma música");
        labelStatus.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED + ";");
        labelStatus.setWrapText(true);

        labelTime = new Label("00:00 / 00:00");
        labelTime.setStyle(
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-text-fill: " + ACCENT + ";" +
            "-fx-font-family: monospace;");

        HBox timeRow = new HBox(labelTime);
        timeRow.setAlignment(Pos.CENTER_LEFT);

        VBox nowPlayingBox = new VBox(4, labelStatus, timeRow);
        nowPlayingBox.setStyle(
            "-fx-background-color: " + BG_INSET + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 12 14 12 14;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;");

        // ── PLAY / PAUSE ──
        btnPlayPause = new Button("▶  PLAY");
        btnPlayPause.setStyle(stylePlayBtn(false));
        btnPlayPause.setMinHeight(58);
        btnPlayPause.setMaxWidth(Double.MAX_VALUE);
        btnPlayPause.setOnAction(e -> alternarPlayPause());

        // ── PREVIOUS / NEXT ──
        Button btnAnterior = new Button("⏮  Anterior");
        btnAnterior.setStyle(styleNavBtn());
        btnAnterior.setMinHeight(46);
        btnAnterior.setMaxWidth(Double.MAX_VALUE);
        btnAnterior.setOnAction(e -> acionarAnterior());

        Button btnProximo = new Button("Próximo  ⏭");
        btnProximo.setStyle(styleNavBtn());
        btnProximo.setMinHeight(46);
        btnProximo.setMaxWidth(Double.MAX_VALUE);
        btnProximo.setOnAction(e -> acionarProximo());

        HBox prevNextRow = new HBox(10, btnAnterior, btnProximo);
        prevNextRow.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnAnterior, Priority.ALWAYS);
        HBox.setHgrow(btnProximo, Priority.ALWAYS);

        // ── REPEAT / SHUFFLE ──
        btnRepeat = new Button("🔁  Repeat");
        btnRepeat.setStyle(stylePillBtn(false));
        btnRepeat.setMinHeight(42);
        btnRepeat.setMaxWidth(Double.MAX_VALUE);
        btnRepeat.setOnAction(e -> alternarRepeat());

        Button btnShuffle = new Button("🔀  Shuffle");
        btnShuffle.setStyle(stylePillBtn(false));
        btnShuffle.setMinHeight(42);
        btnShuffle.setMaxWidth(Double.MAX_VALUE);
        btnShuffle.setOnAction(e -> acionarShuffle());

        HBox shuffleRepeatRow = new HBox(10, btnRepeat, btnShuffle);
        shuffleRepeatRow.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnRepeat, Priority.ALWAYS);
        HBox.setHgrow(btnShuffle, Priority.ALWAYS);

        // ── Separator (before play) ──
        Region sep = new Region();
        sep.setMinHeight(1);
        sep.setMaxWidth(Double.MAX_VALUE);
        sep.setStyle("-fx-background-color: " + BORDER + ";");

        // ── Separator (before volume) ──
        Region sepMid = new Region();
        sepMid.setMinHeight(1);
        sepMid.setMaxWidth(Double.MAX_VALUE);
        sepMid.setStyle("-fx-background-color: " + BORDER + ";");

        // ── VOLUME ──
        Label volIcon = new Label("🔊");
        volIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT_SEC + ";");

        Label volLabel = new Label(" Volume");
        volLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SEC + ";");

        Label volPercent = new Label("70%");
        volPercent.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + ACCENT + ";");

        Region volSpacer = new Region();
        HBox.setHgrow(volSpacer, Priority.ALWAYS);
        HBox volRow = new HBox(8, volIcon, volLabel, volSpacer, volPercent);
        volRow.setAlignment(Pos.CENTER_LEFT);
        volRow.setMaxWidth(Double.MAX_VALUE);

        Slider volumeSlider = new Slider(0, 100, 70);
        volumeSlider.setMaxWidth(Double.MAX_VALUE);
        volumeSlider.setStyle(
            "-fx-control-inner-background: " + BORDER_LIGHT + ";" +
            "-fx-background-color: transparent;");
        volumeSlider.valueProperty().addListener((obs, o, n) ->
            volPercent.setText(n.intValue() + "%"));

        VBox volumeBox = new VBox(10, volRow, volumeSlider);
        volumeBox.setMaxWidth(Double.MAX_VALUE);
        volumeBox.setStyle(
            "-fx-background-color: " + BG_INSET + ";" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 14 16 14 16;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1;");

        // ── Add files section ──
        Region sep2 = new Region();
        sep2.setMinHeight(1);
        sep2.setMaxWidth(Double.MAX_VALUE);
        sep2.setStyle("-fx-background-color: " + BORDER + ";");

        Label addLabel = new Label("Adicionar Músicas");
        addLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED + "; -fx-letter-spacing: 1px;");

        Button btnArquivo = new Button("📁  Arquivo MP3");
        btnArquivo.setStyle(styleAddBtn());
        btnArquivo.setMinHeight(40);
        btnArquivo.setMaxWidth(Double.MAX_VALUE);
        btnArquivo.setOnAction(e -> adicionarArquivo(stage));

        Button btnPasta = new Button("📂  Carregar Pasta");
        btnPasta.setStyle(styleAddBtn());
        btnPasta.setMinHeight(40);
        btnPasta.setMaxWidth(Double.MAX_VALUE);
        btnPasta.setOnAction(e -> carregarPasta(stage));

        // ── Assemble left pane ──
        VBox pane = new VBox(20,
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
        pane.setStyle("-fx-background-color: " + BG_PANEL + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 1 0 0;");

        return pane;
    }

    // ══════════════════════════════════════════════════════════════
    //  CENTER — Hero
    // ══════════════════════════════════════════════════════════════
    private VBox buildHeroPane() {
        // Album art / cover placeholder
        albumArtLabel = new Label("♪");
        albumArtLabel.setStyle(
            "-fx-font-size: 96px;" +
            "-fx-text-fill: rgba(61,111,255,0.25);" +
            "-fx-background-color: #0D1628;" +
            "-fx-background-radius: 24;" +
            "-fx-border-color: " + BORDER_LIGHT + ";" +
            "-fx-border-radius: 24;" +
            "-fx-border-width: 1;" +
            "-fx-min-width: 340;" +
            "-fx-min-height: 340;" +
            "-fx-max-width: 380;" +
            "-fx-max-height: 380;" +
            "-fx-alignment: center;");
        albumArtLabel.setAlignment(Pos.CENTER);

        // Glowing ring around album art
        StackPane albumWrapper = new StackPane(albumArtLabel);
        albumWrapper.setAlignment(Pos.CENTER);
        albumWrapper.setStyle(
            "-fx-padding: 6;" +
            "-fx-background-color: linear-gradient(135deg, " + ACCENT_DARK + " 0%, #0C1120 100%);" +
            "-fx-background-radius: 30;" +
            "-fx-effect: dropshadow(gaussian, rgba(61,111,255,0.3), 40, 0, 0, 0);");

        // Wave bars (visual decoration)
        HBox waveBox = buildWaveBars();

        // Song title
        labelTituloMusica = new Label("Nenhuma música selecionada");
        labelTituloMusica.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;");
        labelTituloMusica.setWrapText(true);
        labelTituloMusica.setMaxWidth(500);

        // Artist
        labelArtista = new Label("Artista Desconhecido");
        labelArtista.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: " + TEXT_MUTED + ";");

        // Pill tags row
        Label pillFormat = buildPill("MP3", ACCENT);
        Label pillHiFi   = buildPill("HI-FI", "#10B981");
        Label pillStereo = buildPill("STEREO", "#8B5CF6");
        HBox pillRow = new HBox(8, pillFormat, pillHiFi, pillStereo);
        pillRow.setAlignment(Pos.CENTER);

        VBox textBlock = new VBox(10,
            labelTituloMusica,
            labelArtista,
            pillRow
        );
        textBlock.setAlignment(Pos.CENTER);

        VBox heroContent = new VBox(36,
            albumWrapper,
            waveBox,
            textBlock
        );
        heroContent.setAlignment(Pos.CENTER);
        heroContent.setPadding(new Insets(48, 40, 48, 40));

        VBox center = new VBox(heroContent);
        center.setAlignment(Pos.CENTER);
        VBox.setVgrow(heroContent, Priority.ALWAYS);
        center.setStyle("-fx-background-color: " + BG_DEEP + ";");

        return center;
    }

    private HBox buildWaveBars() {
        int[] heights = {12, 20, 30, 18, 36, 24, 42, 28, 36, 20, 30, 16, 24, 36, 18, 28, 42, 20, 32, 16};
        HBox box = new HBox(3);
        box.setAlignment(Pos.CENTER);
        for (int h : heights) {
            Region bar = new Region();
            bar.setMinWidth(4);
            bar.setMaxWidth(4);
            bar.setMinHeight(h);
            bar.setMaxHeight(h);
            bar.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                "-fx-background-radius: 3;" +
                "-fx-opacity: 0.45;");
            box.getChildren().add(bar);
        }
        return box;
    }

    private Label buildPill(String text, String color) {
        Label l = new Label(text);
        l.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-radius: 20;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 3 10 3 10;" +
            "-fx-opacity: 0.8;");
        return l;
    }

    // ══════════════════════════════════════════════════════════════
    //  RIGHT — Playlist
    // ══════════════════════════════════════════════════════════════
    private VBox buildPlaylistPane(Stage stage) {
        // Header
        Label title = new Label("Fila de Reprodução");
        title.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";");

        Label countLabel = new Label("0 músicas");
        countLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED + ";");

        VBox headerBox = new VBox(3, title, countLabel);

        // Search field
        TextField buscaField = new TextField();
        buscaField.setPromptText("Buscar...");
        buscaField.setStyle(
            "-fx-background-color: " + BG_INSET + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-prompt-text-fill: " + TEXT_MUTED + ";" +
            "-fx-padding: 10 14 10 14;" +
            "-fx-font-size: 13px;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;");
        buscaField.setMaxWidth(Double.MAX_VALUE);
        buscaField.textProperty().addListener((obs, o, n) -> filtrarPlaylist(n));

        // Cards container
        playlistCardsBox = new VBox(8);
        playlistCardsBox.setPadding(new Insets(4, 0, 12, 0));

        ScrollPane scroll = new ScrollPane(playlistCardsBox);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox pane = new VBox(18, headerBox, buscaField, scroll);
        pane.setPadding(new Insets(28, 18, 18, 18));
        pane.setStyle(
            "-fx-background-color: " + BG_PANEL + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 0 0 1;");

        return pane;
    }

    // ══════════════════════════════════════════════════════════════
    //  Card builder for each track
    // ══════════════════════════════════════════════════════════════
    private VBox buildTrackCard(int numero, NoMusica musica, boolean isAtual) {
        // Accent bar on left
        Region accentBar = new Region();
        accentBar.setMinWidth(3);
        accentBar.setMaxWidth(3);
        accentBar.setStyle("-fx-background-color: " + (isAtual ? ACCENT : "transparent") + ";" +
            "-fx-background-radius: 3 0 0 3;");

        // Number / indicator
        Label numLabel = new Label(isAtual ? "▶" : String.format("%02d", numero));
        numLabel.setStyle(
            "-fx-font-size: " + (isAtual ? "14px" : "11px") + ";" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + (isAtual ? ACCENT : TEXT_MUTED) + ";" +
            "-fx-min-width: 28; -fx-max-width: 28;" +
            (isAtual ? "" : "-fx-font-family: monospace;"));

        // Title
        Label tituloLabel = new Label(musica.getTituloMusica());
        tituloLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: " + (isAtual ? "bold" : "normal") + ";" +
            "-fx-text-fill: " + (isAtual ? TEXT_PRIMARY : TEXT_SEC) + ";");
        tituloLabel.setEllipsisString("…");
        tituloLabel.setMaxWidth(200);

        // Artist
        Label artistaLabel = new Label(musica.getArtistaMusica());
        artistaLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + TEXT_MUTED + ";");

        // Playing badge
        VBox textBox = new VBox(2, tituloLabel, artistaLabel);
        if (isAtual) {
            Label badge = new Label("A TOCAR");
            badge.setStyle(
                "-fx-font-size: 9px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + ACCENT + ";" +
                "-fx-background-color: rgba(61,111,255,0.12);" +
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
            "-fx-background-color: " + (isAtual ? "#111C30" : BG_CARD) + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + (isAtual ? ACCENT_GLOW : BORDER) + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: " + (isAtual ? "1" : "1") + ";" +
            "-fx-cursor: hand;");

        // Hover effect via event handlers
        card.setOnMouseEntered(e -> {
            if (!isAtual) card.setStyle(
                "-fx-background-color: " + BG_CARD_HOV + ";" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + BORDER_LIGHT + ";" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;");
        });
        card.setOnMouseExited(e -> {
            if (!isAtual) card.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;");
        });
        card.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                selecionarMusica(musica);
            }
        });

        return card;
    }

    // ══════════════════════════════════════════════════════════════
    //  Button styles
    // ══════════════════════════════════════════════════════════════
    private String stylePlayBtn(boolean isPaused) {
        String color = isPaused ? "#0D1628" : ACCENT;
        String textColor = isPaused ? ACCENT : "#FFFFFF";
        return "-fx-background-color: " + color + ";" +
            "-fx-text-fill: " + textColor + ";" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + ACCENT + ";" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1.5;" +
            "-fx-cursor: hand;" +
            "-fx-letter-spacing: 1px;";
    }

    private String styleNavBtn() {
        return "-fx-background-color: " + BG_CARD + ";" +
            "-fx-text-fill: " + TEXT_SEC + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;";
    }

    private String stylePillBtn(boolean active) {
        return "-fx-background-color: " + (active ? "rgba(61,111,255,0.18)" : BG_INSET) + ";" +
            "-fx-text-fill: " + (active ? ACCENT : TEXT_MUTED) + ";" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + (active ? ACCENT : BORDER) + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;";
    }

    private String styleAddBtn() {
        return "-fx-background-color: transparent;" +
            "-fx-text-fill: " + TEXT_MUTED + ";" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;";
    }

    // ══════════════════════════════════════════════════════════════
    //  Actions
    // ══════════════════════════════════════════════════════════════
    private void adicionarArquivo(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecionar arquivo MP3");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
        File arquivo = fc.showOpenDialog(stage);
        if (arquivo != null) {
            String titulo = arquivo.getName().replaceFirst("(?i)\\.mp3$", "");
            playlist.adicionarMusica(titulo, "Desconhecido", arquivo.getAbsolutePath());
            atualizarPlaylistCards();
            atualizarHero();
        }
    }

    private void carregarPasta(Stage stage) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Selecionar pasta de músicas");
        File pasta = dc.showDialog(stage);
        if (pasta != null) {
            player.carregarMP3DaPasta(pasta.getAbsolutePath());
            atualizarPlaylistCards();
            atualizarHero();
        }
    }

    private void alternarPlayPause() {
        if (player.isTocando()) {
            player.pause();
            btnPlayPause.setText("▶  PLAY");
            btnPlayPause.setStyle(stylePlayBtn(true));
            labelStatus.setText("Pausado");
        } else {
            NoMusica m = player.play();
            if (m != null) {
                btnPlayPause.setText("⏸  PAUSE");
                btnPlayPause.setStyle(stylePlayBtn(false));
                labelStatus.setText("A tocar: " + m.getTituloMusica());
            } else {
                labelStatus.setText("Nenhuma música disponível.");
            }
        }
        atualizarHero();
        atualizarPlaylistCards();
    }

    private void acionarAnterior() {
        NoMusica m = player.previous();
        if (m != null) {
            btnPlayPause.setText("⏸  PAUSE");
            btnPlayPause.setStyle(stylePlayBtn(false));
            labelStatus.setText("A tocar: " + m.getTituloMusica());
        }
        atualizarHero();
        atualizarPlaylistCards();
    }

    private void acionarProximo() {
        NoMusica m = player.next();
        if (m != null) {
            btnPlayPause.setText("⏸  PAUSE");
            btnPlayPause.setStyle(stylePlayBtn(false));
            labelStatus.setText("A tocar: " + m.getTituloMusica());
        }
        atualizarHero();
        atualizarPlaylistCards();
    }

    private void alternarRepeat() {
        String modo = player.repeat();
        btnRepeat.setText("🔁  Repeat: " + modo);
        boolean active = !modo.equalsIgnoreCase("off");
        btnRepeat.setStyle(stylePillBtn(active));
    }

    private void acionarShuffle() {
        NoMusica m = player.shuffle();
        if (m != null) {
            btnPlayPause.setText("⏸  PAUSE");
            btnPlayPause.setStyle(stylePlayBtn(false));
            labelStatus.setText("Shuffle: " + m.getTituloMusica());
        }
        atualizarHero();
        atualizarPlaylistCards();
    }

    private void selecionarMusica(NoMusica musica) {
        playlist.setAtualMusica(musica);
        player.play();
        btnPlayPause.setText("⏸  PAUSE");
        btnPlayPause.setStyle(stylePlayBtn(false));
        labelStatus.setText("A tocar: " + musica.getTituloMusica());
        atualizarHero();
        atualizarPlaylistCards();
    }

    // ══════════════════════════════════════════════════════════════
    //  Update helpers
    // ══════════════════════════════════════════════════════════════
    private void atualizarHero() {
        NoMusica atual = playlist.getAtualMusica();
        if (atual == null) {
            labelTituloMusica.setText("Nenhuma música selecionada");
            labelArtista.setText("Artista Desconhecido");
            labelTime.setText("00:00 / 00:00");
            albumArtLabel.setText("♪");
        } else {
            labelTituloMusica.setText(atual.getTituloMusica());
            labelArtista.setText(atual.getArtistaMusica());
            labelTime.setText("00:00 / 00:00");
        }
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
        if (playlistCardsBox.getChildren().isEmpty()) {
            Label empty = new Label("Adicione músicas usando os botões à esquerda.");
            empty.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED + "; -fx-wrap-text: true;");
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