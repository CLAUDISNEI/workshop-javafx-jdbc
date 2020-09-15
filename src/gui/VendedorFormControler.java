package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.AlterarDadosListeners;
import gui.util.Alerts;
import gui.util.Utilidades;
import gui.util.ValidacaoTxtField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Vendedor;
import model.exceptions.ValidacaoExcecoes;
import model.services.VendedorServicos;

public class VendedorFormControler implements Initializable {

	private Vendedor vendedor;

	private VendedorServicos vendedorServicos;

	private List<AlterarDadosListeners> alterarDadosListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpNascimento;

	@FXML
	private TextField txtSalarioBase;

	@FXML
	private Label lblErrorNome;

	@FXML
	private Label lblErrorEmail;

	@FXML
	private Label lblErrorNascimento;

	@FXML
	private Label lblErrorSalarioBase;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void subscriverDataChangeListeners(AlterarDadosListeners listeners) {
		alterarDadosListeners.add(listeners);
	}

	@FXML
	private void onBtSalvarAction(ActionEvent evento) {
		if (vendedor == null) {
			throw new IllegalStateException("Vendedor é nullo");
		}
		if (vendedorServicos == null) {
			throw new IllegalStateException("Servicos é nullo");
		}

		try {
			vendedor = pegarDadosFormulario();
			vendedorServicos.salvarOUAtualizar(vendedor);
			notificarAlteracaoDadosListeners();
			Utilidades.palcoAtual(evento).close();

		} catch (ValidacaoExcecoes e) {
			setarMensagensErros(e.getErros());
		} catch (DbException e) {
			Alerts.showAlerts("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificarAlteracaoDadosListeners() {
		for (AlterarDadosListeners listeners : alterarDadosListeners) {
			listeners.dadosAlterados();
		}

	}

	private Vendedor pegarDadosFormulario() {
		Vendedor vendedor = new Vendedor();

		ValidacaoExcecoes excecao = new ValidacaoExcecoes("Erro de validação!");

		vendedor.setId(Utilidades.converterParaInteiro(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excecao.adicionaErros("nome", "O campo não pode ser vazio");
		}
		vendedor.setName(txtNome.getText());

		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		return vendedor;
	}

	@FXML
	private void onBtCancelarAction(ActionEvent evento) {
		Utilidades.palcoAtual(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unused")
	private void inicializandoControles() {
		ValidacaoTxtField.setarCampoInteger(txtId);
		ValidacaoTxtField.setarTamanhoMaximoTextField(txtNome, 70);
		ValidacaoTxtField.setarCampoDouble(txtSalarioBase);
		ValidacaoTxtField.setarTamanhoMaximoTextField(txtEmail, 60);
		Utilidades.formatarDatePicker(dpNascimento, "dd/MM/yyyy");
	}

	public void setarVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public void setarVendedorServicos(VendedorServicos vendedorServicos) {
		this.vendedorServicos = vendedorServicos;
	}

	public void atualizarDadosFormulario() {
		if (vendedor == null) {
			throw new IllegalStateException("Vendedor não foi fornecido!");
		}
		txtId.setText(String.valueOf(vendedor.getId()));
		txtNome.setText(vendedor.getName());
		txtEmail.setText(vendedor.getEmail());
		Locale.setDefault(Locale.US);
		txtSalarioBase.setText(String.format("%.2f", vendedor.getBaseSalary()));
		if (vendedor.getBirthDate() != null) {
			dpNascimento.setValue(LocalDate.ofInstant(vendedor.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
	}

	private void setarMensagensErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();

		if (campos.contains("nome")) {
			lblErrorNome.setText(erros.get("nome"));
		}

	}

}
