package tcc.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    @FXML
    private ComboBox<String> sexo;
    @FXML
    private ComboBox<String> pais;
    @FXML
    private javafx.scene.control.TextField idade;
    @FXML
    private javafx.scene.control.TextField anosCliente;
    @FXML
    private javafx.scene.control.TextField saldo;
    @FXML
    private javafx.scene.control.TextField servicosAdquiridos;
    @FXML
    private javafx.scene.control.TextField score;
    @FXML
    private javafx.scene.control.TextField salario;
    @FXML
    private javafx.scene.control.CheckBox temCartao;
    @FXML
    private javafx.scene.control.CheckBox membroAtivo;
    @FXML
    private javafx.scene.control.Label returnLabel;
    @FXML
    private javafx.scene.control.Label returnLabel1;


    // Função para inicializar o formulário
    public void initialize() {
        // Criação das listas para os ComboBoxes
        ObservableList<String> paises = FXCollections.observableArrayList("País", "Alemanha", "Espanha", "França");
        ObservableList<String> sexos = FXCollections.observableArrayList("Sexo", "Homem", "Mulher");
        sexo.setItems(sexos);
        pais.setItems(paises);
        sexo.getSelectionModel().selectFirst();
        pais.getSelectionModel().selectFirst();

        // Aplica a validação de número inteiro em tempo real para os campos do formulário
        applyValidateInteger(idade, anosCliente, saldo, servicosAdquiridos, score, salario);
        sexo.valueProperty().addListener((observable, oldValue, newValue) -> validateComboBox(sexo));
        pais.valueProperty().addListener((observable, oldValue, newValue) -> validateComboBox(pais));
    }

    // Função para aplicar a validação de número inteiro em tempo real para os campos do formulário
    private void applyValidateInteger(TextField... textFields) {
        Arrays.asList(textFields).forEach(textField -> textField.setOnKeyReleased(event -> validateInteger(textField)));
    }

    // Função para validar se o campo é um número inteiro. Aplica uma borda vermelha caso algum caractere não seja um dígito. No caso do campo "saldo", aceita o caractere "-" para números negativos
    private void validateInteger(TextField textField) {
        String text = textField.getText();

        if (!text.isEmpty()) {
            if ((textField != saldo && !text.matches("\\d+")) || (textField == saldo && !text.matches("-?\\d*"))) {
                textField.setStyle("-fx-border-color: red;");
            } else {
                textField.setStyle("");
            }
        } else {
            textField.setStyle("");
        }
    }

    // Como a função validateInputs() já verifica se as ComboBoxes estão selecionadas, esta função aqui apenas remove o efeito vermelho da borda, pois ao esvaziar os campos através do botão "Esvaziar Campos", a borda vermelha não era removida
    private void validateComboBox(ComboBox<String> comboBox) {
        if (comboBox.getValue().matches("Alemanha") || comboBox.getValue().matches("Espanha") || comboBox.getValue().matches("França") || comboBox.getValue().matches("Homem") || comboBox.getValue().matches("Mulher")) {
            comboBox.setStyle("-fx-font-size: 12px;");
        }
    }

    // Função para validar os campos do formulário. Caso algum não esteja corretamente preenchido, a função retorna false e a requisição não é efetuada. Aplica alguns efeitos visuais para indicar que a validação falhou
    public boolean validateInputs() {
        // Inicializa a variável de validação
        boolean isValid = true;

        // Idade deve ser um número inteiro entre 0 e 120
        if (idade.getText().isEmpty() || !idade.getText().matches("\\d+") || Integer.parseInt(idade.getText()) < 0 || Integer.parseInt(idade.getText()) > 120) {
            idade.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            idade.setStyle("");
        }

        // Anos de cliente deve ser um número inteiro entre 0 e 120
        if (anosCliente.getText().isEmpty() || !anosCliente.getText().matches("\\d+") || Integer.parseInt(anosCliente.getText()) < 0 || Integer.parseInt(anosCliente.getText()) > 120) {
            anosCliente.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            anosCliente.setStyle("");
        }

        // Saldo deve ser um número inteiro, positivo ou negativo
        if (saldo.getText().isEmpty() || !saldo.getText().matches("-?\\d*")) {
            saldo.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            saldo.setStyle("");
        }

        // Serviços adquiridos deve ser um número inteiro positivo
        if (servicosAdquiridos.getText().isEmpty() || !servicosAdquiridos.getText().matches("\\d+") || Integer.parseInt(servicosAdquiridos.getText()) < 0) {
            servicosAdquiridos.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            servicosAdquiridos.setStyle("");
        }

        // Score de crédito deve ser um número inteiro entre 0 e 1000
        if (score.getText().isEmpty() || !score.getText().matches("\\d+") || Integer.parseInt(score.getText()) < 0 || Integer.parseInt(score.getText()) > 1000) {
            score.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            score.setStyle("");
        }

        // Salário estimado deve ser um número inteiro positivo
        if (salario.getText().isEmpty() || !salario.getText().matches("\\d+") || Integer.parseInt(salario.getText()) < 0) {
            salario.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            salario.setStyle("");
        }

        // Verifica se o sexo foi selecionado
        if (sexo.getValue().matches("Sexo")) {
            sexo.setStyle("-fx-border-color: red; -fx-font-size: 12px;");
            isValid = false;
        } else {
            sexo.setStyle("-fx-font-size: 12px;");
        }

        // Verifica se o país foi selecionado
        if (pais.getValue().matches("País")) {
            pais.setStyle("-fx-border-color: red; -fx-font-size: 12px;");
            isValid = false;
        } else {
            pais.setStyle("-fx-font-size: 12px;");
        }

        // Caso a validação falhe, a label de retorno é atualizada com uma mensagem de erro e a imagem de informação recebe um efeito de cor vermelha
        if (!isValid) {
            returnLabel.setText("Preencha os campos corretamente");
        }

        // Retorna se a validação foi bem sucedida. Caso sim, a função retorna true e a requisição é efetuada
        return isValid;
    }

    public void confirmarBtn() throws IOException {
        // Realiza uma validação nos campos. Caso a validação falhe, a função é encerrada aqui mesmo
        if (!validateInputs()) {
            return;
        }

        // Cria um mapa com os dados do formulário
        Map<String, List<Object>> data = new HashMap<>();

        // Adiciona os dados ao mapa
        data.put("score_credito", Arrays.asList(score.getText()));
        data.put("pais", Arrays.asList(pais.getValue()));
        data.put("sexo_biologico", Arrays.asList(sexo.getValue()));
        data.put("idade", Arrays.asList(idade.getText()));
        data.put("anos_de_cliente", Arrays.asList(anosCliente.getText()));
        data.put("saldo", Arrays.asList(saldo.getText()));
        data.put("servicos_adquiridos", Arrays.asList(servicosAdquiridos.getText()));
        data.put("tem_cartao_credito", Arrays.asList(temCartao.isSelected() ? 1 : 0));
        data.put("membro_ativo", Arrays.asList(membroAtivo.isSelected() ? 1 : 0));
        data.put("salario_estimado", Arrays.asList(salario.getText()));

        // Converte os dados para JSON
        String requestBody = new ObjectMapper().writeValueAsString(data);

        // URL para o endpoint /predict
        URL url = new URL("http://localhost:5000/predict");

        // Criação da conexão HTTP
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Configuração do método HTTP para POST
        connection.setRequestMethod("POST");

        // Habilitando envio de dados
        connection.setDoOutput(true);

        // Configurando cabeçalhos da requisição
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));

        // Enviando dados no corpo da requisição
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        // Obtendo a resposta do servidor
        int responseCode = connection.getResponseCode();
        System.out.println("Código de resposta: " + responseCode);

        // Lê a resposta do Python do recall
        InputStream inputStream = connection.getInputStream();
        String responsePython = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Map<String,Object> result = new ObjectMapper().readValue(responsePython, HashMap.class);

        // Imprime a resposta do Python na returnLabel e remove o efeito da imagem de informação para o caso de estar com cor vermelha
        String acuracia = String.valueOf(result.get("acuracia"));
        String precisao = String.valueOf(result.get("precisao"));
        String recall = String.valueOf(result.get("recall"));
        String predicao = String.valueOf(result.get("predicao"));

        returnLabel.setText(" Acurácia: " + acuracia + "%" + "\n" + " Precisão:  " + precisao + "%" + "\n" + " Recall:      " + recall + "%");
        returnLabel1.setText(predicao);
        
        // returnLabel.setText(String.valueOf(result));
        //returnLabel.setText(String.valueOf(result.get("recall")));
    }

    public void esvaziarBtn() {
        // Reseta todos os campos
        idade.clear();
        anosCliente.clear();
        saldo.clear();
        servicosAdquiridos.clear();
        score.clear();
        salario.clear();
        temCartao.setSelected(false);
        membroAtivo.setSelected(false);
        sexo.getSelectionModel().selectFirst();
        pais.getSelectionModel().selectFirst();

        // Reseta a label de retorno e a imagem de informação
        returnLabel.setText("Informações aqui");

        // Caso haja alguma borda vermelha, ela será removida
        idade.setStyle("");
        anosCliente.setStyle("");
        saldo.setStyle("");
        servicosAdquiridos.setStyle("");
        score.setStyle("");
        salario.setStyle("");
        pais.setStyle("-fx-font-size: 12px;");
        sexo.setStyle("-fx-font-size: 12px;");
    }
}
