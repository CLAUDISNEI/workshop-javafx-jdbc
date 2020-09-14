package model.services;

import java.util.List;

import model.DAO.DepartamentoDAO;
import model.DAO.FabricaDAO;
import model.entities.Departamento;

public class DepartamentoServicos {
	
	private DepartamentoDAO dao = FabricaDAO.criarDepartmentoDao();
	
	public List<Departamento> encontrarTodosDepartamentos(){
		return dao.findAll();
	}
	
	public void salvarOUAtualizar(Departamento departamento) {
		if(departamento.getId()==null) {
			dao.insert(departamento);
		}else {
			dao.update(departamento);
		}
	}
	
	public void remover(Departamento departamento) {
		dao.deleteById(departamento.getId());
	}
}
