package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.exceptions.ValidacaoExcecoes;
import model.services.DepartamentoServicos;

public class DepartamentoFormControler implements Initializable {

	private Departamento departamento;

	private DepartamentoServicos departamentoServicos;

	private List<AlterarDadosListeners> alterarDadosListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private Label lblError;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void subscriverDataChangeListeners(AlterarDadosListeners listeners) {
		alterarDadosListeners.add(listeners);
	}

	@FXML
	private void onBtSalvarAction(ActionEvent evento) {
		if (departamento == null) {
			throw new IllegalStateException("Departamento é nullo");
		}
		if (departamentoServicos == null) {
			throw new IllegalStateException("Servicos é nullo");
		}

		try {
			departamento = pegarDadosFormulario();
			departamentoServicos.salvarOUAtualizar(departamento);
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

	private Departamento pegarDadosFormulario() {
		Departamento departamento = new Departamento();

		ValidacaoExcecoes excecao = new ValidacaoExcecoes("Erro de validação!");

		departamento.setId(Utilidades.converterParaInteiro(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excecao.adicionaErros("nome", "O campo não pode ser vazio");
		}
		departamento.setNome(txtNome.getText());

		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		return departamento;
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
		ValidacaoTxtField.setarTamanhoMaximoTextField(txtNome, 30);
	}

	public void setarDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public void setarDepartamentoServicos(DepartamentoServicos departamentoServicos) {
		this.departamentoServicos = departamentoServicos;
	}

	public void atualizarDadosFormulario() {
		if (departamento == null) {
			throw new IllegalStateException("Departamento não foi fornecido!");
		}
		txtId.setText(String.valueOf(departamento.getId()));
		txtNome.setText(departamento.getNome());
	}

	private void setarMensagensErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();

		if (campos.contains("nome")) {
			lblError.setText(erros.get("nome"));
		}

	}

}
