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
	
}
