package com.krakedev.inventarios.entidades;

public class EstadoPedido {
	private String codigo_estado;
	private String descripcion;
	
	public EstadoPedido() {
	}

	public String getCodigo_estado() {
		return codigo_estado;
	}

	public void setCodigo_estado(String codigo_estado) {
		this.codigo_estado = codigo_estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoPedido(String codigo_estado, String descripcion) {
		super();
		this.codigo_estado = codigo_estado;
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "EstadoPedido [codigo_estado=" + codigo_estado + ", descripcion=" + descripcion + "]";
	}
	
}
