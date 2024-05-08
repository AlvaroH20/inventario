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

import com.krakedev.inventarios.entidades.DetalleVenta;
import com.krakedev.inventarios.entidades.Venta;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class VentasBDD {
	public void insertar(Venta venta) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDV = null;
		PreparedStatement psCV = null;
		PreparedStatement psHS = null;
		ResultSet rsClave = null;
		int codigoCabecera = 0;
		BigDecimal TotalNoIva= BigDecimal.ZERO;
		//para inicializar necesito ZERO en BigDecimals
		BigDecimal TotalIVA= BigDecimal.ZERO;
		BigDecimal Total;

		Date fechaActual = new Date();
		Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime());
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					" insert into cabecera_ventas (fecha_venta,total_no_iva,iva,total) " + " values (?,?,?,?) ",
					Statement.RETURN_GENERATED_KEYS);
			ps.setTimestamp(1, fechaHoraActual);
			ps.setBigDecimal(2, new BigDecimal(0));
			ps.setBigDecimal(3, new BigDecimal(0));
			ps.setBigDecimal(4, new BigDecimal(0));

			ps.executeUpdate();

			rsClave = ps.getGeneratedKeys();
			if (rsClave.next()) {
				codigoCabecera = rsClave.getInt(1);
			}

			ArrayList<DetalleVenta> ListDetalle = venta.getDetalles();
			DetalleVenta detalleV = null;
			for (int i = 0; i < ListDetalle.size(); i++) {
				detalleV = ListDetalle.get(i);
				psDV = con.prepareStatement(
						" insert into detalle_ventas (cabecera_ventas,producto,cantidad,precio_venta,subtotal,subtotal_con_iva) "
								+ " values (?,?,?,?,?,?) ");
				psDV.setInt(1, codigoCabecera);
				psDV.setInt(2, detalleV.getProducto().getCodigo());
				psDV.setInt(3, detalleV.getCantidad());
				BigDecimal precioV = detalleV.getProducto().getPrecioVenta();
				psDV.setBigDecimal(4, precioV);
				BigDecimal cantidad = new BigDecimal(detalleV.getCantidad());
				BigDecimal resulSubTotal = precioV.multiply(cantidad);
				psDV.setBigDecimal(5, resulSubTotal);

				if (detalleV.getProducto().isTieneIva()) {
					BigDecimal iva = new BigDecimal(0.12);
					BigDecimal rSubtotalIva = resulSubTotal.multiply(iva);
					BigDecimal ConIva=rSubtotalIva.add(resulSubTotal);
					psDV.setBigDecimal(6, ConIva);
				} else {
					TotalNoIva = TotalNoIva.add(resulSubTotal);
					psDV.setBigDecimal(6, resulSubTotal);
				}
				
				psDV.executeUpdate();

				psHS = con.prepareStatement("insert into historial_stock (fecha,referencia_pedidos,producto,cantidad) "
						+ " values (?,?,?,?) ");
				psHS.setTimestamp(1, fechaHoraActual);
				psHS.setString(2, "VENTA " + codigoCabecera);
				psHS.setInt(3, detalleV.getProducto().getCodigo());
				psHS.setInt(4, 1 * -(detalleV.getCantidad()));

				psHS.executeUpdate();

			}

				psCV = con.prepareStatement(
					" update cabecera_ventas set total_no_iva = ?,iva = ?,total = ? where codigo_cabe_vent=? ");
				psCV.setBigDecimal(1, TotalNoIva);
				psCV.setBigDecimal(2, TotalIVA);
				Total = TotalIVA.add(TotalNoIva);
				psCV.setBigDecimal(3, Total);
				psCV.setInt(4, codigoCabecera);
				
				psCV.executeUpdate();

		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException(
					"Error al realizar el proceso de registro de venta. Detalle: " + e.getMessage());
		}
	}
}