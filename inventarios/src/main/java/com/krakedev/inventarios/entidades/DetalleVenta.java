package com.krakedev.inventarios.entidades;

import java.math.BigDecimal;

public class DetalleVenta {
	private int codigo;
	private Venta cabeceraVenta;
	private Producto producto;
	private int cantidad;
	private BigDecimal precioVenta;
	private BigDecimal subTotal;
	private BigDecimal subTotalIva;
	
	public DetalleVenta() {
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public Venta getCabeceraVenta() {
		return cabeceraVenta;
	}
	public void setCabeceraVenta(Venta cabeceraVenta) {
		this.cabeceraVenta = cabeceraVenta;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}
	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getSubTotalIva() {
		return subTotalIva;
	}
	public void setSubTotalIva(BigDecimal subTotalIva) {
		this.subTotalIva = subTotalIva;
	}
	
	public DetalleVenta(int codigo, Venta cabeceraVenta, Producto producto, int cantidad, BigDecimal precioVenta,
			BigDecimal subTotal, BigDecimal subTotalIva) {
		super();
		this.codigo = codigo;
		this.cabeceraVenta = cabeceraVenta;
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioVenta = precioVenta;
		this.subTotal = subTotal;
		this.subTotalIva = subTotalIva;
	}
	
	@Override
	public String toString() {
		return "DetalleVenta [codigo=" + codigo + ", cabeceraVenta=" + cabeceraVenta + ", producto=" + producto
				+ ", cantidad=" + cantidad + ", precioVenta=" + precioVenta + ", subTotal=" + subTotal
				+ ", subTotalIva=" + subTotalIva + "]";
	}
	
}