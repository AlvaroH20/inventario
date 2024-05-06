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

import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.Pedido;
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
			psH = con.prepareStatement(
					"INSERT INTO historial_stock (fecha, referencia_pedidos, producto, cantidad) VALUES (?, ?, ?, ?)");
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
}
