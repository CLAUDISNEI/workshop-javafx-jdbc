package model.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.DAO.VendedorDAO;
import model.entities.Departamento;
import model.entities.Vendedor;

public class VendedorDaoJDBC implements VendedorDAO {
	
	private Connection conn;
	
	public VendedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Vendedor obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, "
					+ "BaseSalary, DepartmentId) "
					+ "VALUES(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartamento().getId());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultset(rs);
			}
			else {
				throw new DbException("Erro inesperado: Nenhuma linha foi criada");
			}
			
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
		
	}

	@Override
	public void update(Vendedor obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, "
					+ "BirthDate = ?, BaseSalary = ?, "
					+ "DepartmentId = ?	WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		}catch(SQLException e){
			throw new DbException(e.getMessage());
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
					"DELETE FROM seller	WHERE Id = ?");
			st.setInt(1, id);
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas == 0) {
				throw new SQLException("Vendendor não encontrado!");
			}
			
			conn.setAutoCommit(true);
		}
		catch(SQLException e) {
			
			try {
				conn.rollback();
				throw new DbException("Operação não realizada devido: "+e.getMessage());
			}catch(SQLException e2) {
				throw new DbException("Erro ao tentar voltar a operação!"+e2.getMessage());
			}
			
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Vendedor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Departamento dp = instanciaDepartamento(rs);				
				Vendedor obj = instanciaVendedor(rs, dp);				
				return obj;
				
			}
			return null;
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultset(rs);
		}
		
	}

	private Vendedor instanciaVendedor(ResultSet rs, Departamento dp) throws SQLException {
		Vendedor obj = new Vendedor();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartamento(dp);
		
		return obj;
	}

	private Departamento instanciaDepartamento(ResultSet rs) throws SQLException {
		 Departamento dep = new Departamento();
		 dep.setId(rs.getInt("DepartmentId"));
		 dep.setNome(rs.getString("DepName"));
		 
		 return dep;
	}

	@Override
	public List<Vendedor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Vendedor> lista = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while(rs.next()) {
				//se o departamento não existir no map ele será instanciado
				Departamento dp = map.get(rs.getInt("DepartmentId"));
				
				if(dp == null) {
					dp = instanciaDepartamento(rs);
					map.put(rs.getInt("DepartmentId"), dp);
				}
							
				Vendedor obj = instanciaVendedor(rs, dp);				
				lista.add(obj);				
			}
			return lista;
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultset(rs);
		}
	}

	@Override
	public List<Vendedor> findByDepartment(Departamento department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Vendedor> lista = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while(rs.next()) {
				//se o departamento não existir no map ele será instanciado
				Departamento dp = map.get(rs.getInt("DepartmentId"));
				
				if(dp == null) {
					dp = instanciaDepartamento(rs);
					map.put(rs.getInt("DepartmentId"), dp);
				}
							
				Vendedor obj = instanciaVendedor(rs, dp);				
				lista.add(obj);				
			}
			return lista;
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultset(rs);
		}
	}

}
