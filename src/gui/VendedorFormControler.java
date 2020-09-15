package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.AlterarDadosListeners;
import gui.util.Alerts;
import gui.util.Utilidades;
import gui.util.ValidacaoTxtField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departamento;
import model.entities.Vendedor;
import model.exceptions.ValidacaoExcecoes;
import model.services.DepartamentoServicos;
import model.services.VendedorServicos;

public class VendedorFormControler implements Initializable {

	private Vendedor vendedor;

	private DepartamentoServicos departamentoServicos;

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
	private ComboBox<Departamento> cbxDepartamento;
	private ObservableList<Departamento> obslistaDepartamento;

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
		iniciarCbxDepartamento();
	}

	public void setarVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public void setarVendedorServicosDepartamentoServicos(VendedorServicos vendedorServicos,
			DepartamentoServicos departamentoServicos) {
		this.vendedorServicos = vendedorServicos;
		this.departamentoServicos = departamentoServicos;
	}

	public void atualizarDadosFormulario() {
		if (vendedor == null) {
			throw new IllegalStateException("Vendedor não foi fornecido!");
		}
		txtId.setText(String.valueOf(vendedor.getId()));
		txtNome.setText(vendedor.getName());
		txtEmail.setText(vendedor.getEmail());
		Locale.setDefault(Locale.US);
		
		if(vendedor.getBaseSalary() == null) {
			txtSalarioBase.setText("");
		}else {
			txtSalarioBase.setText(String.format("%.2f", vendedor.getBaseSalary()));
		}	
		
		if (vendedor.getBirthDate() != null) {
			dpNascimento.setValue(LocalDate.ofInstant(vendedor.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		
		// quando estiver cadastrando o vendedor
		// o cbox irá carregar o campo vazio
		if (vendedor.getDepartamento() == null) {
			cbxDepartamento.getSelectionModel().selectFirst();
		} else {
			cbxDepartamento.setValue(vendedor.getDepartamento());
			;
		}
	}

	private void setarMensagensErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();

		if (campos.contains("nome")) {
			lblErrorNome.setText(erros.get("nome"));
		}

	}

	/*
	 * metodo criado para associar os departamento no combobox de departamentos
	 */
	public void carregarDeparpatamentosNoCombobox() {
		if (departamentoServicos == null) {
			throw new IllegalStateException("DepartamentoServicos é nullo");
		}
		List<Departamento> lista = departamentoServicos.encontrarTodosDepartamentos();
		obslistaDepartamento = FXCollections.observableArrayList(lista);
		cbxDepartamento.setItems(obslistaDepartamento);
	}

	public void iniciarCbxDepartamento() {
		Callback<ListView<Departamento>, ListCell<Departamento>> fabrica = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean vazio) {
				super.updateItem(item, vazio);
				setText(vazio ? "" : item.getNome());

			}
		};
		
		cbxDepartamento.setCellFactory(fabrica);
		cbxDepartamento.setButtonCell(fabrica.call(null));
	}
}
