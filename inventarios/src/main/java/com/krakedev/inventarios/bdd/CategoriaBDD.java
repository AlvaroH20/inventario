package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class CategoriaBDD {
	public void insertar(Categoria categoria) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(" insert into categorias (nombre,categoria_padre) "
					+ " values (?,?) ");
			ps.setString(1, categoria.getNombre());
			if(categoria.getCategoriaPadre()!=null) {
				ps.setInt(2, categoria.getCategoriaPadre().getCodigo());
			}else {
				ps.setNull(2, Types.INTEGER);
			}
			ps.executeUpdate();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar la categoria. Detalle: "+e.getMessage());
		}
	}
	public void actualizar(Categoria c) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(" update categorias set nombre=?, categoria_padre=? where codigo_cat=? ");
			ps.setString(1, c.getNombre());
			if(c.getCategoriaPadre()!=null) {
				ps.setInt(2, c.getCategoriaPadre().getCodigo());
			}else {
				ps.setNull(2, Types.INTEGER);
			}
			ps.setInt(3, c.getCodigo());
			ps.executeUpdate();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al actualizar la categoria. Detalle: "+e.getMessage());
		}
	}
	public ArrayList<Categoria> recuperarTodo() throws KrakeDevException{
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs= null;
		Categoria categori = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(" select  * from categorias ");
			rs = ps.executeQuery();
			while (rs.next()) {
				int codigo = rs.getInt("codigo_cat");
				String nombre = rs.getString("nombre");
				Categoria cate = new Categoria();
				cate.setCodigo(rs.getInt("categoria_padre"));;
				categori = new Categoria(codigo, nombre, cate);
				categorias.add(categori);
			}
			
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar: "+e.getMessage());
		}
		return categorias;
	}
}