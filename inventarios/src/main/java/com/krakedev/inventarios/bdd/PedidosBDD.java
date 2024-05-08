package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.CategoriaUDM;
import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.EstadoPedido;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.entidades.UnidadDeMedida;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class PedidosBDD {
	public void insertar(Pedido pedido) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		ResultSet rsClave = null;
		int codigoCabecera = 0;

		Date fechaActual = new Date();
		java.sql.Date fechaSQL = new java.sql.Date(fechaActual.getTime());
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(" insert into cabecera_pedidos(proveedor,fecha,estado)" + " values(?,?,?) ",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, pedido.getProveedor().getIdentificador());
			ps.setDate(2, fechaSQL);
			ps.setString(3, "S");

			ps.executeUpdate();

			rsClave = ps.getGeneratedKeys();

			if (rsClave.next()) {
				codigoCabecera = rsClave.getInt(1);
			}

			ArrayList<DetallePedido> detallesPedido = pedido.getDetalle();
			DetallePedido det;
			for (int i = 0; i < detallesPedido.size(); i++) {
				det = detallesPedido.get(i);
				psDet = con.prepareStatement(
						" insert into detalle_pedidos(cabecera_pedido,producto,cantidad,subtotal,cantidad_recibida)"
								+ " values(?,?,?,?,?)");
				psDet.setInt(1, codigoCabecera);
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidadSolicitada());
				psDet.setInt(5, 0);
				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadSolicitada());
				BigDecimal subtotal = pv.multiply(cantidad);
				psDet.setBigDecimal(4, subtotal);

				psDet.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar el pedido. Detalle: " + e.getMessage());
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

	public void actualizar(Pedido pedido) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		PreparedStatement psH = null;

		Date fechaActual = new Date();
		Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime());
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(" update cabecera_pedidos set estado='R' where codigo_cabecera=? ");
			ps.setInt(1, pedido.getCodigo());

			ps.executeUpdate();

			ArrayList<DetallePedido> detallesPedido = pedido.getDetalle();
			DetallePedido det;
			psH = con.prepareStatement(" insert into historial_stock (fecha, referencia_pedidos, producto, cantidad) values (?, ?, ?, ?)");
			for (int i = 0; i < detallesPedido.size(); i++) {
				det = detallesPedido.get(i);
				psDet = con.prepareStatement(
						" update detalle_pedidos set cantidad_recibida=?, subtotal=? where codigo_pedido=? ");
				psDet.setInt(1, det.getCantidadRecibida());
				psDet.setInt(3, det.getCodigo());
				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadRecibida());
				BigDecimal subtotal = pv.multiply(cantidad);
				psDet.setBigDecimal(2, subtotal);

				psDet.executeUpdate();

				psH.setTimestamp(1, fechaHoraActual);
				psH.setString(2, "PEDIDO " + pedido.getCodigo());
				psH.setInt(3, det.getProducto().getCodigo());
				psH.setInt(4, det.getCantidadRecibida());

				psH.executeUpdate();

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar el pedido. Detalle: " + e.getMessage());
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
	public ArrayList<Pedido> buscarPorProveedor(String identificador) throws KrakeDevException{
		ArrayList<Pedido> Pedidos = new ArrayList<Pedido>();
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDP = null;
		ResultSet rs= null;
		ResultSet rsDP= null;
		Pedido pedido= null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(" select prod.identificador, prod.nombre, prod.telefono, prod.correo, prod.direccion, "
					+ " td.codigo_doc, td.descripcion as descipcion_doc, "
					+ " cabeP.codigo_cabecera, cabeP.fecha, cabeP.estado, estadop.descripcion as descripcion_estado "
					+ " from proveedor prod,cabecera_pedidos cabeP, estado_pedidos estadop, tipo_de_documento td "
					+ " where cabeP.proveedor = prod.identificador and estadop.codigo_estado = cabeP.estado and td.codigo_doc = prod.tipo_documento "
					+ " and cabeP.proveedor=? ");
			ps.setString(1, identificador);
			
			rs = ps.executeQuery();
			
			while (rs.next()) {			
				String codigoDoc = rs.getString("codigo_doc");
				String descripcionDoc = rs.getString("descipcion_doc");
				TipoDocumento td = new TipoDocumento(codigoDoc,descripcionDoc);
				String nombre = rs.getString("nombre");
				String telefono = rs.getString("telefono");
				String correo = rs.getString("correo");
				String direccion = rs.getString("direccion");
				Proveedor proveedor = new Proveedor(identificador, td, nombre, telefono, correo, direccion);
				String codigoEstado = rs.getString("estado");
				String descripcionEstado = rs.getString("descripcion_estado");
				EstadoPedido ep = new EstadoPedido(codigoEstado,descripcionEstado);
				
				int codigo = rs.getInt("codigo_cabecera");
				Date fecha = rs.getDate("fecha");
				pedido = new Pedido(codigo, proveedor, fecha, ep);
				
				psDP= con.prepareStatement(" select detalleP.codigo_pedido as codigo_detalle , detalleP.cabecera_pedido, prod.codigo_producto, "
						+ " prod.nombre, udm.nombre_udm, udm.descripcion as descripcion_udm, udm.categoria_udm, "
						+ " cast(prod.precio_venta as decimal(6,2)), "
						+ " prod.tiene_iva, cast(prod.coste as decimal(5,4)), prod.categoria, cate.nombre as nombre_categoria, prod.stock, "
						+ " detalleP.cantidad as cantidad_solicitada, cast(detalleP.subtotal as decimal(10,4)), detalleP.cantidad_recibida "
						+ " from detalle_pedidos detalleP, productos prod, categorias cate, unidades_de_medida udm "
						+ " where detalleP.producto =prod.codigo_producto and prod.categoria = cate.codigo_cat and prod.udm = udm.nombre_udm "
						+ " and detalleP.cabecera_pedido=? ");
				psDP.setInt(1, codigo);
				rsDP = psDP.executeQuery();
				ArrayList<DetallePedido> listDetalle = new ArrayList<DetallePedido>();
				while(rsDP.next()) {
					String codigoUdm = rsDP.getString("nombre_udm");
					String descripcion = rsDP.getString("descripcion_udm");
					CategoriaUDM categoriaUdm = new CategoriaUDM(rsDP.getString("categoria_udm"), null);
					UnidadDeMedida udm = new UnidadDeMedida(codigoUdm, descripcion, categoriaUdm);
					int codigoCat = rsDP.getInt("categoria");
					String nombreCategoria = rsDP.getString("nombre_categoria");
					Categoria cate = new Categoria(codigoCat, nombreCategoria, null);
					int codigoProducto = rsDP.getInt("codigo_producto");
					String nombreProducto = rsDP.getString("nombre");
					BigDecimal precioVenta = rsDP.getBigDecimal("precio_venta");
					boolean tieneIva = rsDP.getBoolean("tiene_iva");
					BigDecimal coste = rsDP.getBigDecimal("coste");;
					int stock = rsDP.getInt("stock");
					Producto producto = new Producto(codigoProducto, nombreProducto, udm, precioVenta, tieneIva, coste, cate, stock);
					int codigoDetalle = rsDP.getInt("codigo_detalle");
					int cantidadSolicitada = rsDP.getInt("cantidad_solicitada");
					BigDecimal Subtotal = rsDP.getBigDecimal("subtotal");
					int cantidadRecibida = rsDP.getInt("cantidad_recibida");
					DetallePedido  detalle = new DetallePedido(codigoDetalle, null, producto, cantidadSolicitada, Subtotal, cantidadRecibida);
					listDetalle.add(detalle);
					
				}
				
				pedido.setDetalle(listDetalle);
				Pedidos.add(pedido);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar: "+e.getMessage());
		}
		return Pedidos;
	}
}
