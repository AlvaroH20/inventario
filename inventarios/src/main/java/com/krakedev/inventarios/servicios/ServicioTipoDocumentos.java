package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.TipodocumentoBDD;
import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("tiposdocumento")
public class ServicioTipoDocumentos {
	@Path("recuperar")
	@GET 
	@Produces(MediaType.APPLICATION_JSON)
	public Response recuperar(){
		TipodocumentoBDD TipodocBDD = new TipodocumentoBDD();
		ArrayList<TipoDocumento> documentos=null;
		try {
			documentos = TipodocBDD.recuperar();
			return Response.ok(documentos).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build(); 
		}
	}
}
