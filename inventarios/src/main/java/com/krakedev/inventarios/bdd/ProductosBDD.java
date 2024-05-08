package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.CategoriaUDM;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.UnidadDeMedida;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class ProductosBDD {
	public ArrayList<Producto> buscar(String subcadena) throws KrakeDevException {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto producto = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(" select prod.codigo_producto, prod.nombre as nombre_producto, "
					+ " udm.nombre_udm as nombre_udm, udm.descripcion as descripcion_udm, "
					+ " prod.precio_venta, prod.tiene_iva,prod.coste, "
					+ " prod.categoria,cat.nombre as nombre_categoria,stock "
					+ " from productos prod,unidades_de_medida udm, categorias cat "
					+ " where prod.udm = udm.nombre_udm " + " and prod.categoria = cat.codigo_cat "
					+ " and upper(prod.nombre) like ? ");
			ps.setString(1, "%" + subcadena.toUpperCase() + "%");
			// System.out.println(">>>>>>>>>"+ps);
			rs = ps.executeQuery();

			while (rs.next()) {
				int codigoProducto = rs.getInt("codigo_producto");
				String nombreProducto = rs.getString("nombre_producto");
				String nombreUnidadMedida = rs.getString("nombre_udm");
				String descripcionUnidadMedida = rs.getString("descripcion_udm");
				BigDecimal precioVenta = rs.getBigDecimal("precio_venta");
				boolean tieneIva = rs.getBoolean("tiene_iva");
				BigDecimal coste = rs.getBigDecimal("coste");
				int codigoCategoria = rs.getInt("categoria");
				String nombreCategoria = rs.getString("nombre_categoria");
				int stock = rs.getInt("stock");

				UnidadDeMedida udm = new UnidadDeMedida();
				udm.setNombre(nombreUnidadMedida);
				udm.setDescripcion(descripcionUnidadMedida);

				Categoria categoria = new Categoria();
				categoria.setCodigo(codigoCategoria);
				categoria.setNombre(nombreCategoria);

				producto = new Producto();
				producto.setCodigo(codigoProducto);
				producto.setNombre(nombreProducto);
				producto.setUnidadMedida(udm);
				producto.setPrecioVenta(precioVenta);
				producto.setTieneIva(tieneIva);
				producto.setCoste(coste);
				producto.setCategoria(categoria);
				producto.setStock(stock);

				productos.add(producto);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar. Detalle:" + e.getMessage());
		}
		return productos;
	}

	public void insertar(Producto producto) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					" insert into productos (codigo_producto,nombre,udm,precio_venta,tiene_iva,coste,categoria,stock) "
							+ " values(?,?,?,?,?,?,?,?) ");
			ps.setInt(1, producto.getCodigo());
			ps.setString(2, producto.getNombre());
			ps.setString(3, producto.getUnidadMedida().getNombre());
			ps.setBigDecimal(4, producto.getPrecioVenta());
			ps.setBoolean(5, producto.isTieneIva());
			ps.setBigDecimal(6, producto.getCoste());
			ps.setInt(7, producto.getCategoria().getCodigo());
			ps.setInt(8, producto.getStock());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar el cliente. Detalle: " + e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void actualizar(Producto producto) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(" update productos set nombre =?, udm=?, "
					+ " precio_venta=?, tiene_iva=?, coste=?, categoria=? "
					+ " where codigo_producto=? ");
			ps.setString(1, producto.getNombre());
			ps.setString(2, producto.getUnidadMedida().getNombre());
			ps.setBigDecimal(3, producto.getPrecioVenta());
			ps.setBoolean(4, producto.isTieneIva());
			ps.setBigDecimal(5, producto.getCoste());
			ps.setInt(6, producto.getCategoria().getCodigo());
			ps.setInt(7, producto.getCodigo());
			ps.executeUpdate();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al actualizar. Detalle: "+e.getMessage());
		}
	}
	
	public Producto buscarId(String codigo) throws KrakeDevException {
		int codiguillo =Integer.parseInt(codigo);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto producto = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					" select prod.codigo_producto, prod.nombre, udm.nombre_udm, udm.descripcion as descripcion_udm, udm.categoria_udm, cast(prod.precio_venta as decimal(6,2)), "
							+ " prod.tiene_iva, cast(prod.coste as decimal(5,4)), prod.categoria, cate.nombre as nombre_categoria, prod.stock "
							+ " from productos prod, categorias cate, unidades_de_medida udm "
							+ " where prod.categoria = cate.codigo_cat and prod.udm = udm.nombre_udm and codigo_producto=? ");
			ps.setInt(1, codiguillo);
			rs = ps.executeQuery();
			if (rs.next()) {
				String codigoUdm = rs.getString("nombre_udm");
				String descripcion = rs.getString("descripcion_udm");
				CategoriaUDM categoriaUdm = new CategoriaUDM(rs.getString("categoria_udm"), null);
				UnidadDeMedida udm = new UnidadDeMedida(codigoUdm, descripcion, categoriaUdm);

				int codigoCat = rs.getInt("categoria");
				String nombreCategoria = rs.getString("nombre_categoria");
				Categoria cate = new Categoria(codigoCat, nombreCategoria, null);

				int codigoProducto = rs.getInt("codigo_producto");
				String nombreProducto = rs.getString("nombre");
				BigDecimal precioVenta = rs.getBigDecimal("precio_venta");
				boolean tieneIva = rs.getBoolean("tiene_iva");
				BigDecimal coste = rs.getBigDecimal("coste");
				int stock = rs.getInt("stock");
				
				producto = new Producto(codigoProducto, nombreProducto, udm, precioVenta, tieneIva, coste, cate, stock);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar: " + e.getMessage());
		}

		return producto;
	}
}
