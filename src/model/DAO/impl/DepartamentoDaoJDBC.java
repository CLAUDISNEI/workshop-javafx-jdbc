package model.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import db.DB;
import db.DbException;
import model.DAO.DepartamentoDAO;
import model.entities.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDAO {
	
	private Connection conn;
	
	public DepartamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Departamento obj) {
		PreparedStatement st = null;
		
		try {
			
			conn.setAutoCommit(false);
			
			st = conn.prepareStatement(
					"INSERT INTO Department (Name) VALUES(?)",Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getNome());
			
			int linhaIncluida = st.executeUpdate();
			
			//código para obter o Id criado no banco de dados e associalo ao objeto 
			//fornecido como parâmetro
			if(linhaIncluida > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultset(rs);
			}
			else {
				throw new SQLException("Erro ao criar novo Departamento");
			}
			conn.commit();					
		}catch(SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Erro: "+e.getMessage());
			}catch(SQLException e2) {
				throw new DbException("Erro ao cancelar criação!: "+e2.getMessage());
			}
		}finally {
			DB.closeStatement(st);
		
		}	
	}

	@Override
	public void update(Departamento obj) {
		PreparedStatement st = null;
		
		try {
			conn.setAutoCommit(false);
			
			st = conn.prepareStatement(
					"UPDATE Department SET Name = ? WHERE Id = ?");
			
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());
			
			int resultado = st.executeUpdate();
			
			if(resultado == 0) {
				throw new SQLException("Departamento não encontrado!");
			}
			conn.commit();
		}catch(SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Erro : " + e.getMessage());
			}catch(SQLException e2 ) {
				throw new DbException("Erro ao voltar a transação no B. Dados!");
			}
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			conn.setAutoCommit(false);
			
			st = conn.prepareStatement(
					"DELETE FROM Department WHERE Id = ? ");
			st.setInt(1, id);
			
			int registroApagado = st.executeUpdate();
			
			if(registroApagado == 0) {
				throw new SQLException("Departamento não existe!");
			}
			conn.commit();
		}catch(SQLException e){
			try {
				conn.rollback();
				throw new DbException("Erro: "+e.getMessage());
			}catch(SQLException e2) {
				throw new DbException("Erro ao cancelar exclusão! \n\n"+e2.getMessage() );
			}
		}finally {
			DB.closeStatement(st);
		}
				
		
	}

	@Override
	public Departamento findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT Department.* FROM Department WHERE Id= ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if(rs.next()) {
				Departamento dep = new Departamento(rs.getInt("Id"), rs.getString("Name"));
				return dep;
			}
			else {
				throw new SQLException("Departamento não encontrado!");
			}
		}catch(SQLException e) {
			throw new DbException("Erro: "+e.getMessage());
		}
		finally {
			DB.closeResultset(rs);
			DB.closeStatement(st);
		}
		
	}

	@Override
	public List<Departamento> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT Department.* FROM Department ORDER BY Name ");
			rs = st.executeQuery();
			List<Departamento> lista = new ArrayList<>();
			if(rs.next()) {
				while(rs.next()) {
					Departamento dp = new Departamento();
					dp.setId(rs.getInt("Id"));
					dp.setNome(rs.getString("Name"));
					lista.add(dp);
				}
				return lista;				
			}else {
				throw new DbException("Lista de Departamento não criada!");
			}
		
		}catch(SQLException e) {
			throw new DbException("Erro: "+e.getMessage());
		}finally {
			DB.closeResultset(rs);
			DB.closeStatement(st);
		}
		
	}

}
