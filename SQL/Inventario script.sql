--tabla de categorias
drop table if exists categorias cascade;
create table categorias(
	codigo_cat serial not null,
	nombre varchar(100) not null,
	categoria_padre int,
	constraint categorias_pk primary key (codigo_cat),
	constraint categorias_fk foreign key (categoria_padre)
	references categorias(codigo_cat)
);

--inserts
insert into categorias(nombre,categoria_padre)
values('Materia Prima',null);
insert into categorias(nombre,categoria_padre)
values('Proteina',1);
insert into categorias(nombre,categoria_padre)
values('Salsas',1);
insert into categorias(nombre,categoria_padre)
values('Punto de Venta',null);
insert into categorias(nombre,categoria_padre)
values('Bebidas',4);
insert into categorias(nombre,categoria_padre)
values('Con alcohol',5);
insert into categorias(nombre,categoria_padre)
values('Sin alcohol',5);
--tabla de categorias_unidad_medida
drop table if exists categorias_unidad_medida cascade;
create table categorias_unidad_medida(
	codigo_udm char(1) not null,
	nombre varchar(100) not null,
	constraint categorias_unidad_medida_pk primary key (codigo_udm)
);
--inserts de categorias_unidad_medida
insert into categorias_unidad_medida(codigo_udm,nombre)
values('U','Unidades');
insert into categorias_unidad_medida(codigo_udm,nombre)
values('V','Volumen');
insert into categorias_unidad_medida(codigo_udm,nombre)
values('P','Peso');
--table de unidades_de_medida
--cambie la primary key en este sql y en el diagrama ya que segun las tablas de excel
--necesitaba mostrar el nombre relacionado con una clave foranea, no el codigo
--checar la tabla productos
drop table if exists unidades_de_medida cascade;
create table unidades_de_medida(
	codigo_udm char(1) not null,
	nombre_udm varchar(3) not null,
	descripcion varchar(100) not null,
	constraint unidades_de_medida_pk primary key (nombre_udm)
	
);
alter table unidades_de_medida add categoria_udm char(1) not null,
	add constraint unidades_de_medida_fk foreign key (categoria_udm)
	references categorias_unidad_medida (codigo_udm);
--inserts de unidades_de_medida
insert into unidades_de_medida(codigo_udm,nombre_udm,descripcion,categoria_udm)
values (1,'ml','mililitros','V');
insert into unidades_de_medida(codigo_udm,nombre_udm,descripcion,categoria_udm)
values (2,'l','litros','V');
insert into unidades_de_medida(codigo_udm,nombre_udm,descripcion,categoria_udm)
values (3,'u','unidad','U');
insert into unidades_de_medida(codigo_udm,nombre_udm,descripcion,categoria_udm)
values (4,'d','docena','U');
insert into unidades_de_medida(codigo_udm,nombre_udm,descripcion,categoria_udm)
values (5,'g','gramos','P');
insert into unidades_de_medida(codigo_udm,nombre_udm,descripcion,categoria_udm)
values (6,'kg','kilogramos','P');
insert into unidades_de_medida(codigo_udm,nombre_udm,descripcion,categoria_udm)
values (7,'lb','libras','P');
--tabla de tipo de documento
drop table if exists tipo_de_documento cascade;
create table tipo_de_documento(
	codigo_doc char(1) not null,
	descripcion varchar(100) not null,
	constraint tipo_de_documento_pk primary key (codigo_doc)
);
--inserts de tipo de documento
insert into tipo_de_documento(codigo_doc,descripcion)
values('C','CEDULA');
insert into tipo_de_documento(codigo_doc,descripcion)
values('R','RUC');
--crear la tabla de proveedor
drop table if exists proveedor cascade;
create table proveedor(
	identificador varchar(13) not null,
	tipo_documento char(1) not null,
	nombre varchar(100) not null,
	telefono varchar(10) not null,
	correo varchar(30) not null,
	direccion varchar(30) not null,
	constraint proveedor_pk primary key (identificador),
	constraint proveedor_fk foreign key (tipo_documento)
	references tipo_de_documento (codigo_doc)
);
--insert 
insert into proveedor(identificador,tipo_documento,nombre,telefono,correo,direccion)
values('1792285747','C','SANTIAGO MOSQUERA','0992920306','zantycb89@gmail.com','Cumbayork');
insert into proveedor(identificador,tipo_documento,nombre,telefono,correo,direccion)
values('1792285747001','R','SNACKS SA','0992920398','snacks@gmail.com','La Tola');
--tabla de estado_pedidos
drop table if exists estado_pedidos cascade;
create table estado_pedidos(
	codigo_estado char(1) not null,
	descripcion varchar(100) not null,
	constraint estado_pedidos_pk primary key (codigo_estado)
);
--inserts
insert into estado_pedidos(codigo_estado,descripcion) 
values('S','Solicitado');
insert into estado_pedidos(codigo_estado,descripcion) 
values('R','Recibido');
--tabla de cabecera_pedidos
drop table if exists cabecera_pedidos cascade;
create table cabecera_pedidos(
	codigo_cabecera serial not null,
	proveedor varchar(13) not null,
	fecha timestamp not null,
	estado char(1) not null,
	constraint cabecera_pedidos_pk primary key(codigo_cabecera),
	constraint cabecera_pedidos_fk foreign key(proveedor)
	references proveedor(identificador)
);
--inserts 
insert into cabecera_pedidos(proveedor,fecha,estado)
values('1792285747','20/11/2023','R');
insert into cabecera_pedidos(proveedor,fecha,estado)
values('1792285747','20/11/2023','R');
--tabla cabecera_ventas
drop table if exists cabecera_ventas cascade;
create table cabecera_ventas(
	codigo_cabe_vent serial not null,
	fecha_venta timestamp,
	total_no_iva money not null,
	iva money not null,
	total money not null,
	constraint cabecera_ventas_pk primary key (codigo_cabe_vent)
);
--inserts
insert into cabecera_ventas(fecha_venta,total_no_iva,iva,total)
values('20/11/2023 20:00',3.26,0.39,3.65);
--tabla productos
drop table if exists productos cascade;
create table productos(
	codigo_producto serial not null,
	nombre varchar(100) not null,
	udm char(2) not null,
	precio_Venta money not null,
	tiene_IVA boolean,
	coste money not null,
	categoria int not null,
	stock int not null,
	constraint productos_pk primary key (codigo_producto),
	constraint productos_fk foreign key (udm)
	references unidades_de_medida (nombre_udm),
	constraint productos_categoria_fk foreign key (categoria)
	references categorias (codigo_cat)
);
--inserts
insert into productos(nombre,udm,precio_Venta,tiene_IVA,coste,categoria,stock)
values('Coca cola pequeña','u',0.5804,'true',0.3729,7,105);
insert into productos(nombre,udm,precio_Venta,tiene_IVA,coste,categoria,stock)
values('Salsa de tomate','kg',0.95,'true',0.8736,3,0);
insert into productos(nombre,udm,precio_Venta,tiene_IVA,coste,categoria,stock)
values('Mostaza','kg',0.95,'true',0.89,3,0);
insert into productos(nombre,udm,precio_Venta,tiene_IVA,coste,categoria,stock)
values('Fuze Tea','u',0.8,'true',0.7,3,49);
--tabla historial_stock
drop table if exists historial_stock cascade;
create table historial_stock(
	codigo_historial serial not null,
	fecha timestamp not null,
	referencia_pedidos varchar(30) not null,
	producto int not null,
	cantidad int not null,
	constraint historial_stock_pk primary key (codigo_historial),
	constraint historial_stock_fk foreign key (producto)
	references productos(codigo_producto)
);
--inserts
insert into historial_stock(fecha,referencia_pedidos,producto,cantidad)
values('20/11/2023 19:59','PEDIDO 1',1,100);
insert into historial_stock(fecha,referencia_pedidos,producto,cantidad)
values('20/11/2023 19:59','PEDIDO 1',4,50);
insert into historial_stock(fecha,referencia_pedidos,producto,cantidad)
values('20/11/2023 20:00','PEDIDO 2',1,10);
insert into historial_stock(fecha,referencia_pedidos,producto,cantidad)
values('20/11/2023 20:00','VENTA 1',1,-5);
insert into historial_stock(fecha,referencia_pedidos,producto,cantidad)
values('20/11/2023 20:00','VENTA 1',4,1);
--tabla detalle_ventas
drop table if exists detalle_ventas cascade;
create table detalle_ventas(
	codigo_detalle serial not null,
	cabecera_ventas int not null,
	producto int not null,
	cantidad int not null,
	precio_venta money not null,
	subtotal money not null,
	subtotal_con_iva money not null,
	constraint detalle_ventas_pk primary key (codigo_detalle),
	constraint detalle_ventas_fk foreign key (cabecera_ventas)
	references cabecera_ventas(codigo_cabe_vent),
	constraint detalle_ventas_product_fk foreign key(producto)
	references productos(codigo_producto)
);
--inserts
insert into detalle_ventas(cabecera_ventas,producto,cantidad,precio_venta,subtotal,subtotal_con_iva)
values(1,1,5,0.58,2.9,3.25);
insert into detalle_ventas(cabecera_ventas,producto,cantidad,precio_venta,subtotal,subtotal_con_iva)
values(1,4,1,0.36,0.36,0.4);
--tabla detalle_pedidos
drop table if exists detalle_pedidos cascade;
create table detalle_pedidos(
	codigo_pedido serial not null,
	cabecera_pedido int not null,
	producto int not null,
	cantidad int not null,
	subtotal money not null,
	cantidad_recibida int not null,
	constraint detalle_pedidos_pk primary key (codigo_pedido),
	constraint detalle_pedidos_fk foreign key (cabecera_pedido)
	references cabecera_pedidos(codigo_cabecera),
	constraint detalle_pedidos_product_fk foreign key (producto)
	references productos(codigo_producto)
);
--selects
insert into detalle_pedidos(cabecera_pedido,producto,cantidad,subtotal,cantidad_recibida)
values(1,1,100,37.29,100);
insert into detalle_pedidos(cabecera_pedido,producto,cantidad,subtotal,cantidad_recibida)
values(1,4,50,11.8,50);
insert into detalle_pedidos(cabecera_pedido,producto,cantidad,subtotal,cantidad_recibida)
values(2,1,10,3.73,10);
--selects
select * from categorias;
select * from categorias_unidad_medida;
select * from unidades_de_medida;
select * from tipo_de_documento;
select * from proveedor;
select * from estado_pedidos;
select * from cabecera_pedidos;
select * from cabecera_ventas;
select * from productos;
select * from historial_stock;
select * from detalle_ventas;
select * from detalle_pedidos;