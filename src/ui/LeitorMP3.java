package ui;

import model.NoMusica;
import model.PlayerService;
import model.PlaylistDupla;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;

public class LeitorMP3 extends JFrame {
    private final PlaylistDupla playlist;
    private final PlayerService player;
    private final DefaultListModel<String> playlistModel;
    private final JList<String> playlistList;
    private final JLabel repeatLabel;
    private final JLabel estadoLabel;

    public LeitorMP3() {
        super("Leitor MP3");
        playlist = new PlaylistDupla();
        player = new PlayerService(playlist);
        playlistModel = new DefaultListModel<>();
        playlistList = new JList<>(playlistModel);
        repeatLabel = new JLabel("Repeat: OFF");
        estadoLabel = new JLabel("Estado: parado | Atual: nenhuma musica");

        configurarJanela();
        atualizarInterface();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LeitorMP3().setVisible(true));
    }

    private void configurarJanela() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Mantem o visual padrao caso o sistema nao permita alterar.
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 460);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(16, 14));

        JPanel topo = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Leitor MP3");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 24f));
        repeatLabel.setFont(repeatLabel.getFont().deriveFont(Font.BOLD));
        topo.add(titulo, BorderLayout.WEST);
        topo.add(repeatLabel, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(playlistList);
        scroll.setBorder(BorderFactory.createTitledBorder("Playlist"));
        add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout(0, 10));
        estadoLabel.setFont(estadoLabel.getFont().deriveFont(Font.BOLD));
        rodape.add(estadoLabel, BorderLayout.NORTH);
        rodape.add(criarBotoes(), BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    private JPanel criarBotoes() {
        JPanel botoes = new JPanel(new GridLayout(3, 4, 20, 12));

        botoes.add(criarBotao("Play", () -> {
            player.play();
            atualizarInterface();
        }));
        botoes.add(criarBotao("Pause", () -> {
            player.pause();
            atualizarInterface();
        }));
        botoes.add(criarBotao("Stop", () -> {
            player.stop();
            atualizarInterface();
        }));
        botoes.add(criarBotao("Next", () -> {
            player.next();
            atualizarInterface();
        }));
        botoes.add(criarBotao("Previous", () -> {
            player.previous();
            atualizarInterface();
        }));
        botoes.add(criarBotao("Repeat", () -> {
            player.repeat();
            atualizarInterface();
        }));
        botoes.add(criarBotao("Shuffle", () -> {
            player.shuffle();
            atualizarInterface();
        }));
        botoes.add(criarBotao("Carregar pasta", this::carregarPasta));
        botoes.add(criarBotao("Adicionar demo", this::adicionarDemo));
        botoes.add(criarBotao("Remover atual", this::removerAtual));
        botoes.add(criarBotao("Limpar lista", () -> {
            playlist.limpar();
            player.stop();
            atualizarInterface();
        }));
        botoes.add(new JLabel(""));

        return botoes;
    }

    private JButton criarBotao(String texto, Runnable acao) {
        JButton botao = new JButton(texto);
        botao.addActionListener(e -> acao.run());
        return botao;
    }

    private void carregarPasta() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Selecionar pasta de musicas");

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            player.carregarMP3DaPasta(chooser.getSelectedFile().getAbsolutePath());
            atualizarInterface();
        }
    }

    private void adicionarDemo() {
        int numero = playlist.getTamanho() + 1;
        playlist.adicionarMusica("Musica " + numero, "Artista " + numero, "musica" + numero + ".mp3");
        atualizarInterface();
    }

    private void removerAtual() {
        NoMusica atual = playlist.getAtualMusica();
        if (atual == null) {
            JOptionPane.showMessageDialog(this, "Nao existe musica atual para remover.");
            return;
        }

        playlist.removerMusica(atual.getTituloMusica());
        atualizarInterface();
    }

    private void atualizarInterface() {
        playlistModel.clear();

        NoMusica cursor = playlist.getInicio();
        while (cursor != null) {
            String prefixo = cursor == playlist.getAtualMusica() ? "-> " : "   ";
            playlistModel.addElement(prefixo + cursor.getTituloMusica() + " - " + cursor.getArtistaMusica());
            cursor = cursor.getProximo();
        }

        repeatLabel.setText("Repeat: " + player.getModoRepeat());

        NoMusica atual = playlist.getAtualMusica();
        String estado;
        if (player.isTocando()) {
            estado = "tocando";
        } else if (player.isPausado()) {
            estado = "pausado";
        } else {
            estado = "parado";
        }

        String musicaAtual = atual == null ? "nenhuma musica" : atual.getTituloMusica();
        estadoLabel.setText("Estado: " + estado + " | Atual: " + musicaAtual);

        int indiceAtual = obterIndiceAtual();
        if (indiceAtual >= 0) {
            playlistList.setSelectedIndex(indiceAtual);
        }
    }

    private int obterIndiceAtual() {
        NoMusica cursor = playlist.getInicio();
        int indice = 0;
        while (cursor != null) {
            if (cursor == playlist.getAtualMusica()) {
                return indice;
            }
            cursor = cursor.getProximo();
            indice++;
        }
        return -1;
    }
}
