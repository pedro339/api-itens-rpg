package api_itens_rpg.cliente;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InterfaceRpg extends JFrame {

    private static final String API = "http://localhost:8080/api";

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper();

    public InterfaceRpg() {
        super("RPG Itens e Personagens");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("GERENCIADOR RPG", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 23));
        titulo.setBorder(new EmptyBorder(18, 10, 18, 10));

        add(titulo, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();

        abas.addTab("Itens", new PainelItens());
        abas.addTab("Personagens", new PainelPersonagens());

        add(abas, BorderLayout.CENTER);

        setVisible(true);
    }

    // Comunicação HTTP com a API
    private JsonNode requisicao(String metodo, String caminho, Object corpo)
            throws Exception {

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(API + caminho))
                .header("Content-Type", "application/json; charset=UTF-8");

        if (corpo == null) {
            builder.method(metodo, HttpRequest.BodyPublishers.noBody());
        } else {
            String conteudo = corpo instanceof String s
                    ? s : json.writeValueAsString(corpo);

            builder.method(metodo,
                    HttpRequest.BodyPublishers.ofString(
                            conteudo, StandardCharsets.UTF_8));
        }

        HttpResponse<String> resposta = http.send(
                builder.build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (resposta.statusCode() < 200 || resposta.statusCode() >= 300) {
            String detalhe = resposta.body() == null
                    || resposta.body().isBlank()
                    ? "Sem detalhes." : resposta.body();

            throw new RuntimeException(
                    "Erro HTTP " + resposta.statusCode() + ": " + detalhe);
        }

        if (resposta.body() == null || resposta.body().isBlank()) {
            return json.createObjectNode();
        }

        return json.readTree(resposta.body());
    }

    private void erro(Component pai, Exception e) {

    e.printStackTrace();

    JOptionPane.showMessageDialog(
            pai,
            "Não foi possível concluir a operação.\n"
                    + e.toString(),
            "Erro de comunicação",
            JOptionPane.ERROR_MESSAGE);
}

    private JPanel painelBase(
            JTable tabela,
            Runnable atualizar,
            Runnable novo,
            Runnable editar,
            Runnable excluir) {

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(14, 14, 14, 14));

        tabela.setRowHeight(26);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton bNovo = new JButton("Cadastrar");
        JButton bEditar = new JButton("Editar selecionado");
        JButton bExcluir = new JButton("Excluir selecionado");
        JButton bAtualizar = new JButton("Atualizar lista");

        bNovo.addActionListener(e -> novo.run());
        bEditar.addActionListener(e -> editar.run());
        bExcluir.addActionListener(e -> excluir.run());
        bAtualizar.addActionListener(e -> atualizar.run());

        botoes.add(bNovo);
        botoes.add(bEditar);
        botoes.add(bExcluir);
        botoes.add(bAtualizar);

        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }

    private static DefaultTableModel modelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // =====================================================
    // ABA DE ITENS
    // =====================================================

    private class PainelItens extends JPanel {

        private final DefaultTableModel modelo = modelo(
                "ID", "Nome", "Raridade", "Preço", "Nível mínimo");

        private final JTable tabela = new JTable(modelo);

        PainelItens() {
            super(new BorderLayout());

            add(painelBase(
                    tabela,
                    this::carregar,
                    this::novo,
                    this::editar,
                    this::excluir
            ), BorderLayout.CENTER);

            carregar();
        }

        private Long idSelecionado() {
            int linha = tabela.getSelectedRow();

            return linha < 0 ? null
                    : ((Number) modelo.getValueAt(linha, 0)).longValue();
        }

        private void carregar() {
            try {
                modelo.setRowCount(0);

                JsonNode lista = requisicao("GET", "/itens", null);

                for (JsonNode item : lista) {
                    modelo.addRow(new Object[]{
                            item.path("id").asLong(),
                            item.path("nome").asText(),
                            item.path("raridade").asText(),
                            item.path("preco").asText(),
                            item.path("nivelMinimo").asInt()
                    });
                }

            } catch (Exception e) {
                erro(this, e);
            }
        }

        private void formulario(Long id) {

            JTextField nome = new JTextField(20);

            JComboBox<String> raridade = new JComboBox<>(
                    new String[]{
                            "Comum", "Incomum", "Raro",
                            "Épico", "Lendário", "Mítico"
                    });

            JTextField preco = new JTextField(10);

            JSpinner nivel = new JSpinner(
                    new SpinnerNumberModel(1, 1, 100, 1));

            if (id != null) {
                try {
                    JsonNode item = requisicao(
                            "GET", "/itens/" + id, null);

                    nome.setText(item.path("nome").asText());
                    raridade.setSelectedItem(
                            item.path("raridade").asText());
                    preco.setText(item.path("preco").asText());
                    nivel.setValue(
                            item.path("nivelMinimo").asInt(1));

                } catch (Exception e) {
                    erro(this, e);
                    return;
                }
            }

            JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));

            form.add(new JLabel("Nome:"));
            form.add(nome);

            form.add(new JLabel("Raridade:"));
            form.add(raridade);

            form.add(new JLabel("Preço:"));
            form.add(preco);

            form.add(new JLabel("Nível mínimo:"));
            form.add(nivel);

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    id == null ? "Cadastrar item" : "Editar item",
                    JOptionPane.OK_CANCEL_OPTION);

            if (opcao != JOptionPane.OK_OPTION) return;

            try {
                String nomeValor = nome.getText().trim();

                if (nomeValor.length() < 2) {
                    throw new IllegalArgumentException(
                            "O nome deve ter pelo menos 2 caracteres.");
                }

                BigDecimal valor = new BigDecimal(
                        preco.getText().trim().replace(',', '.'));

                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException(
                            "O preço deve ser maior que zero.");
                }

                Map<String, Object> dados = Map.of(
                        "nome", nomeValor,
                        "raridade", raridade.getSelectedItem(),
                        "preco", valor,
                        "nivelMinimo", nivel.getValue()
                );

                requisicao(
                        id == null ? "POST" : "PUT",
                        "/itens" + (id == null ? "" : "/" + id),
                        dados);

                carregar();

            } catch (Exception e) {
                erro(this, e);
            }
        }

        private void novo() {
            formulario(null);
        }

        private void editar() {
            Long id = idSelecionado();

            if (id == null) {
                avisoSelecao();
            } else {
                formulario(id);
            }
        }

        private void excluir() {
            Long id = idSelecionado();

            if (id == null) {
                avisoSelecao();
                return;
            }

            if (JOptionPane.showConfirmDialog(
                    this,
                    "Excluir o item " + id + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION) {

                try {
                    requisicao("DELETE", "/itens/" + id, null);
                    carregar();

                } catch (Exception e) {
                    erro(this, e);
                }
            }
        }
    }

    // =====================================================
    // ABA DE PERSONAGENS
    // =====================================================

    private class PainelPersonagens extends JPanel {

        private final DefaultTableModel modelo = modelo(
                "ID", "Nome", "Nível", "Classe", "Tipo de dano");

        private final JTable tabela = new JTable(modelo);

        PainelPersonagens() {
            super(new BorderLayout());

            add(painelBase(
                    tabela,
                    this::carregar,
                    this::novo,
                    this::editar,
                    this::excluir
            ), BorderLayout.CENTER);

            carregar();
        }

        private Long idSelecionado() {
            int linha = tabela.getSelectedRow();

            return linha < 0 ? null
                    : ((Number) modelo.getValueAt(linha, 0)).longValue();
        }

        private void carregar() {
            try {
                modelo.setRowCount(0);

                JsonNode lista = requisicao(
                        "GET", "/personagens", null);

                for (JsonNode p : lista) {
                    JsonNode classe = p.path("classe");

                    modelo.addRow(new Object[]{
                            p.path("id").asLong(),
                            p.path("nome").asText(),
                            p.path("nivel").asInt(),
                            classe.path("nomeClasse").asText(),
                            classe.path("tipoDano").asText()
                    });
                }

            } catch (Exception e) {
                erro(this, e);
            }
        }

        private void formulario(Long id) {

            JTextField nome = new JTextField(20);

            JSpinner nivel = new JSpinner(
                    new SpinnerNumberModel(1, 1, 100, 1));

            JComboBox<OpcaoClasse> classe = new JComboBox<>();

            try {
                JsonNode classes = requisicao("GET", "/classes", null);

                List<OpcaoClasse> opcoes = new ArrayList<>();

                for (JsonNode c : classes) {
                    opcoes.add(new OpcaoClasse(
                            c.path("id").asLong(),
                            c.path("nomeClasse").asText()));
                }

                for (OpcaoClasse c : opcoes) {
                    classe.addItem(c);
                }

                if (id != null) {
                    JsonNode p = requisicao(
                            "GET", "/personagens/" + id, null);

                    nome.setText(p.path("nome").asText());
                    nivel.setValue(p.path("nivel").asInt(1));

                    long classeId = p.path("classe").path("id").asLong();

                    for (int i = 0; i < classe.getItemCount(); i++) {
                        if (classe.getItemAt(i).id == classeId) {
                            classe.setSelectedIndex(i);
                        }
                    }
                }

            } catch (Exception e) {
                erro(this, e);
                return;
            }

            if (classe.getItemCount() == 0) {
                erro(this,
                        new RuntimeException(
                                "Nenhuma classe cadastrada na API."));
                return;
            }

            JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));

            form.add(new JLabel("Nome:"));
            form.add(nome);

            form.add(new JLabel("Nível:"));
            form.add(nivel);

            form.add(new JLabel("Classe:"));
            form.add(classe);

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    id == null
                            ? "Cadastrar personagem"
                            : "Editar personagem",
                    JOptionPane.OK_CANCEL_OPTION);

            if (opcao != JOptionPane.OK_OPTION) return;

            try {
                if (nome.getText().trim().length() < 2) {
                    throw new IllegalArgumentException(
                            "O nome deve ter pelo menos 2 caracteres.");
                }

                OpcaoClasse selecionada =
                        (OpcaoClasse) classe.getSelectedItem();

                Map<String, Object> dados = Map.of(
                        "nome", nome.getText().trim(),
                        "nivel", nivel.getValue(),
                        "classeId", selecionada.id
                );

                requisicao(
                        id == null ? "POST" : "PUT",
                        "/personagens" + (id == null ? "" : "/" + id),
                        dados);

                carregar();

            } catch (Exception e) {
                erro(this, e);
            }
        }

        private void novo() {
            formulario(null);
        }

        private void editar() {
            Long id = idSelecionado();

            if (id == null) {
                avisoSelecao();
            } else {
                formulario(id);
            }
        }

        private void excluir() {
            Long id = idSelecionado();

            if (id == null) {
                avisoSelecao();
                return;
            }

            if (JOptionPane.showConfirmDialog(
                    this,
                    "Excluir o personagem " + id + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION) {

                try {
                    requisicao(
                            "DELETE", "/personagens/" + id, null);

                    carregar();

                } catch (Exception e) {
                    erro(this, e);
                }
            }
        }
    }

    // Opção apresentada no campo de seleção de classe
    private record OpcaoClasse(long id, String nome) {
        @Override
        public String toString() {
            return nome;
        }
    }

    private void avisoSelecao() {
        JOptionPane.showMessageDialog(
                this,
                "Selecione um registro na tabela primeiro.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InterfaceRpg::new);
    }
}
