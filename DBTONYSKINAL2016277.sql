drop database if exists DBTonysKinal2016277;

create database DBTonysKinal2016277;
use DBTonysKinal2016277;

/*------- EMPRESAS -----*/
create table Empresas(
	codigoEmpresa int auto_increment not null,
    nombreEmpresa varchar(150),
    direccion varchar(150),
	telefono varchar(10),
    primary key PK_codigoEmpresa (codigoEmpresa)
);

/*------- Presupuestos -----------*/
create table Presupuesto(
	codigoPresupuesto int auto_increment not null,
    fechaSolicitud date,
    cantidadPresupuesto decimal (10,2),
    codigoEmpresa int,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_codigoEmpresa
		foreign key (codigoEmpresa) references Empresas(codigoEmpresa)
);


/*--------- Servicios-----------*/
create table Servicios(
	codigoServicio int auto_increment not null,
    fechaServicio date,
    tipoServicio varchar(100),
    horaServicio time,
    lugarServicio varchar(100),
    telefonoContacto varchar(100),
    codigoEmpresa int,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_codigoEmpresa1
		foreign key (codigoEmpresa) references Empresas(codigoEmpresa)
);

/*------- Tipo de Empleados -------------*/
create table TipoEmpleado(
	codigoTipoEmpleado int auto_increment not null,
    descripcion varchar(100),
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);
create table Empleados(
	codigoEmpleado int auto_increment not null,
    numeroEmpleado int,
    apellidosEmpleado varchar(150),
    nombresEmpleado varchar(150),
    direccionEmpleado varchar(150),
    telefonoContacto varchar(10),
    gradoCocinero varchar(50),
    codigoTipoEmpleado int,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_codigoTipoEmpleado
		foreign key (codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado)
);

/*----------- Tipo de Platos --------------*/
create table TipoPlato(
	codigoTipoPlato int auto_increment not null,
    descripcionTipo varchar(100),
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

/*--------------Platos --------------------*/
create table Platos(
	codigoPlato int auto_increment not null,
    cantidad int not null,
    nombrePlato varchar(50),
    descripcionPlato varchar(150),
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int,
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_codigoTipoPlato
		foreign key (codigoTipoPlato) references TipoPlato(codigoTipoPlato)
);

/*------------ Productos -------------------*/
create table Productos(
	codigoProducto int auto_increment not null,
    nombreProducto varchar(150),
    cantidad int not null,
    primary key PK_codigoProducto (codigoProducto)
);

/*-------------- Sevicios y Empleados -----------------*/
create table Servicios_has_Empleados(
	Servicios_codigoServicio int,
    Empleados_codigoEmpleado int,
    fechaEvento date,
    horaEvento time,
    lugarEvento varchar(150),
    constraint FK_Servicios_codigoServicio
		foreign key (Servicios_codigoServicio) references Servicios(codigoServicio),
	constraint FK_Empleados_codigoEmpleado
		foreign key (Empleados_codigoEmpleado) references Empleados(codigoEmpleado)
);

/*-----------------Servicios y Platos ---------------------------*/
create table Servicios_has_Platos(
	Servicios_codigoServicio int,
    Platos_codigoPlato int,
    constraint FK_Servicios_codigoServicio1
		foreign key (Servicios_codigoServicio) references Servicios(codigoServicio),
	constraint FK_Servicios_codigoPlato1
		foreign key (Platos_codigoPlato) references Platos(codigoPlato)
);

/*---------------- Productos y Platos ------------------------------*/
create table Productos_has_Platos(
	Productos_codigoProducto int,
    Platos_codigoPlato int,
    constraint Fk_Productos_codigoProducto
		foreign key (Productos_codigoProducto) references Productos(codigoProducto),
	constraint FK_Platos_codigoPlato2
		foreign key (Platos_codigoPlato) references Platos(codigoPlato)
);
#-----------------SP--------------------------------------------------------#
/*E M P R E S A */
/*-------- Agregar Empresas ------*/
Delimiter $$
	create procedure sp_AgregarEmpresa(
		in _nombreEmpresa varchar(150),
		in _direccion varchar(150),
		in _telefono varchar(10)
	)
    Begin
		insert into Empresas(nombreEmpresa, direccion, telefono)
			values (_nombreEmpresa, _direccion, _telefono);
    End $$
Delimiter ;
call sp_agregarEmpresa("Mercasid","15-23 avenida zona 5 de mixco"," 54654349");
call sp_agregarEmpresa("pollo campero","Pollo Campero, avenida 15-01, zona 1","43567645");
call sp_agregarEmpresa("GEPS","20 Calle 25-55, Zona 12 Ofi-Bodega 918 Empresarial  ","86543434");
call sp_agregarEmpresa("Agencia De Viaje Volare","5 avenida 13-82 z.9 Edificio Via Napoli local 33","54532343");
call sp_agregarEmpresa("Laser Care","iagonal 6 11-97 zona 10 Edificio Centro Internaciones","54342323");

/*------Listar Empresas --------*/
drop procedure if exists sp_ListarEmpresa;
Delimiter $$
	create procedure sp_ListarEmpresa()
    Begin
		select 
			Empresas.codigoEmpresa,
            Empresas.nombreEmpresa,
            Empresas.direccion,
            Empresas.telefono
				from Empresas;
    End $$
Delimiter ;
/*-----Actualizar Empresas--------*/
Delimiter $$
	create procedure sp_ActualizarEmpresa(
		in _codigoEmpresa int,
        in _nombreEmpresa varchar(150),
        in _direccion varchar(150),
        in _telefono varchar(10)
    )
    Begin
		Update Empresas set
			nombreEmpresa = _nombreEmpresa,
            direccion = _direccion,
            telefono = _telefono
            where codigoEmpresa = _codigoEmpresa; 
    End $$
Delimiter ;


/*------ Eliminar Empresas -------*/
Delimiter $$
	create procedure sp_EliminarEmpresa(
		in _codigoEmpresa int 
    )
    Begin
		Delete from Empresas 
			where codigoEmpresa = _codigoEmpresa;
    End $$
Delimiter ;
/*------ Buscar Empresas ---------*/
Delimiter $$
	create procedure sp_BuscarEmpresa(
		in _codigoEmpresa int
    )
    Begin
		select 
			Empresas.codigoEmpresa,
            Empresas.nombreEmpresa,
            Empresas.direccion,
            Empresas.telefono
				from Empresas Where codigoEmpresa = _codigoEmpresa;
    End $$
Delimiter ;

/*P r e s u p u e s t o s*/
/*------- Agregar Presupuestos --------*/
Delimiter $$
	create procedure sp_AgregarPresupuesto(
		in _fechaSolicitud date,
        in _cantidadPresupuesto decimal (10,2),
        in _codigoEmpresa int
    )
    Begin
		insert into Presupuesto(fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
			values (_fechaSolicitud, _cantidadPresupuesto, _codigoEmpresa);
    End $$
Delimiter ;
call sp_AgregarPresupuesto("20200422",4323.34,1);
call sp_AgregarPresupuesto("20200621",4333.22,2);
call sp_AgregarPresupuesto("20200225",4300.00,3);
call sp_AgregarPresupuesto("20200924",5400.34,4);
call sp_AgregarPresupuesto("20201221",8974.43,5);
/*-------- Listar Presupuestos ---------*/
Delimiter $$
	create procedure sp_ListarPresupuesto()
    Begin
		select 
			Presupuesto.codigoPresupuesto, 
            Presupuesto.fechaSolicitud,
            Presupuesto.cantidadPresupuesto,
            Presupuesto.codigoEmpresa
				from Presupuesto;
    End $$
Delimiter ;
/*------- Actualizar Presupuestos -------*/
Delimiter $$
	create procedure sp_ActualizarPresupuesto(
		in _codigoPresupuesto int,
        in _fechaSolicitud date,
        in _cantidadPresupuesto decimal(10,2),
        in _codigoEmpresa int
    )
    Begin
		Update Presupuesto set
			fechaSolicitud = _fechaSolicitud,
            cantidadPresupuesto = _cantidadPresupuesto,
            codigoEmpresa = _codigoEmpresa
            where codigoPresupuesto = _codigoPresupuesto;
    End $$
Delimiter ;
/*-------Eliminar Presupuestos ---------*/
Delimiter $$
	create procedure sp_EliminarPresupuesto(
		in _codigoPresupuesto int
    )
    Begin
		Delete from Presupuesto
			where codigoPresupuesto = _codigoPresupuesto;
    End $$
Delimiter ;
/*------Buscar Presupuestos ----------*/
Delimiter $$
	create procedure sp_BuscarPrespuesto(
		in _codigoPresupuesto int
    )
    Begin
		select 
			Presupuesto.codigoPresupuesto, 
            Presupuesto.fechaSolicitud,
            Presupuesto.cantidadPresupuesto,
            Presupuesto.codigoEmpresa
				from Presupuesto where codigoPresupuesto = _codigoPresupuesto;
    End $$
Delimiter ;

/*S e r v i c i o s*/

/*-------- Agregar Servicios---------*/
Delimiter $$
	create procedure sp_AgregarServicio(
		in _fechaServicio date,
        in _tipoServicio varchar(100),
        in _horaServicio time,
        in _lugarServicio varchar(100),
        in _telefonoContacto varchar(100),
        in _codigoEmpresa int
    )
    Begin
		insert into Servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
			values(_fechaServicio, _tipoServicio, _horaServicio,_lugarServicio, _telefonoContacto, _codigoEmpresa);
    End $$
Delimiter ;

call sp_AgregarServicio("20200212","Menu De Comida Tipica","10:49:12","Zona 6 de Mixco","23357743",1);
call sp_AgregarServicio("20200623","Menu De comida Americana","11:00:00","Zona 5 Ciudad De Guatemala ","25435465",2);
call sp_AgregarServicio("20200408","Menu de Comida Mexicana","14:00:00","Zona 4 Ciudad De Guatemala","54931023",3);
call sp_AgregarServicio("20200912","Menu de Comida Hondureña","13:23:00","Zona 18 Ciudad De Guatemala","45342356",4);
call sp_AgregarServicio("20200413","Menu de Comida Peruana","15:00:00","ZOna 11 Ciudad De Guatemala","43567645",5);

/*------- Listar Servicios-----------*/
Delimiter $$
	create procedure sp_ListarServicio()
		Begin
			select 
				Servicios.codigoServicio,
                Servicios.fechaServicio,
                Servicios.tipoServicio,
                Servicios.horaServicio,
                Servicios.lugarServicio,
                Servicios.telefonoContacto,
                Servicios.codigoEmpresa
					from Servicios;
        End $$
Delimiter ;
/*--------Actualizar Servicios ---------*/
Delimiter $$
	create procedure sp_ActualizarServicio(
		in _codigoServicio int,
		in _fechaServicio date,
        in _tipoServicio varchar(100),
        in _horaServicio time,
        in _lugarServicio varchar(100),
        in _telefonoContacto varchar(100),
        in _codigoEmpresa int
    )
    Begin
		Update Servicios set
			fechaServicio = _fechaServicio,
            tipoServicio = _tipoServicio,
            horaServicio = _horaServicio,
            lugarServicio = _lugarServicio,
            telefonoContacto = _telefonoContacto,
            codigoEmpresa = _codigoEmpresa
				where codigoServicio = _codigoServicio;
    End $$
Delimiter ;
/*------- Buscar Servicios ---------*/
Delimiter $$
	create procedure sp_BuscarServicio(
		in _codigoServicio int
    )
    Begin
		select 
			Servicios.codigoServicio,
            Servicios.fechaServicio,
            Servicios.tipoServicio,
            Servicios.horaServicio,
            Servicios.lugarServicio,
            Servicios.telefonoContacto,
            Servicios.codigoEmpresa
				from Servicios where codigoServicio = _codigoServicio;
    End $$
Delimiter ;
/*-------- Eliminar Servicios ---------*/
Delimiter $$
	create procedure sp_EliminarServicio(
		in _codigoServicio int
    )
    Begin
		Delete from Servicios
			where codigoServicio = _codigoServicio;
    End $$
Delimiter ;

/*Tipo de-- E m p l e a d o s*/
/*-------Agregar Tipos de Empleados ------------*/
Delimiter $$
	create procedure sp_AgregarTipoEmpleado(
		in _descripcion varchar(100)
    )
    Begin
		insert into TipoEmpleado(descripcion)
			values(_descripcion);
    End $$
Delimiter ;
call sp_AgregarTipoEmpleado("Cocinero De Mariscos");
call sp_AgregarTipoEmpleado("Cocinero De Comida Exotica");
call sp_AgregarTipoEmpleado("Cocinero De Comida Mexicana");
call sp_AgregarTipoEmpleado("Cocinero De Comida Peruana");
call sp_AgregarTipoEmpleado("Cocinero De Comida Hondureña");

/*-------- Listar Tipo de Empleados -----------*/
Delimiter $$
	create procedure sp_ListarTipoEmpleado()
    Begin
		select
			TipoEmpleado.codigoTipoEmpleado,
			TipoEmpleado.descripcion
				from TipoEmpleado;
    End $$
Delimiter ;
/*-------- Actualizar Tipo de Empleados -----------*/
Delimiter $$
	create procedure sp_ActualizarTipoEmpleado(
		in _codigoTipoEmpleado int,
        in _descripcion varchar(100)
    )
    Begin
		Update TipoEmpleado set
			descripcion = _descripcion
				where codigoTipoEmpleado = _codigoTipoEmpleado;
    End $$
Delimiter ;
/*-------- Eliminar Tipo de Empleados -----------*/
Delimiter $$
	create procedure sp_EliminarTipoEmpleado(
		in _codigoTipoEmpleado int
    )
    Begin
		Delete from TipoEmpleado
			where codigoTipoEmpleado = _codigoTipoEmpleado;
    End $$
Delimiter ;
/*--------- Buscar Tipo de Empleados ------------*/
Delimiter $$
	create procedure sp_BuscarTipoEmpleado(
		in _codigoTipoEmpleado int
    )
    Begin
		select 
			TipoEmpleado.codigoTipoEmpleado,
            TipoEmpleado.descripcion
				from TipoEmpleado where codigoTipoEmpleado = _codigoTipoEmpleado;
    End $$
Delimiter ;

/*E M P L E A D O S*/
/*------------Agregar Empleados -----------*/
Delimiter $$
	create procedure sp_AgregarEmpleado(
		in _numeroEmpleado int,
        in _apellidosEmpleado varchar(150),
        in _nombresEmpleado varchar(150),
        in _direccionEmpleado varchar(150),
        in _telefonoContacto varchar(10),
        in _gradoCocinero varchar(50),
        in _codigoTipoEmpleado int
    )
    Begin
		insert into Empleados(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado)
			values(_numeroEmpleado, _apellidosEmpleado, _nombresEmpleado, _direccionEmpleado, _telefonoContacto, _gradoCocinero, _codigoTipoEmpleado);
    End $$
Delimiter ;
call sp_AgregarEmpleado("2016277","Valladares Salguero","Kenneth Luis Ernestio","24 calle 11 avenida zona 5","35774118","Gefe De Area De Comida Mexicana",1);
call sp_AgregarEmpleado("2016520","Tumax Baquiax","Fernando Jose","12 calle 14 avenida Zona 3","543467","Gerente De Area de Comida Peruana",2);
call sp_AgregarEmpleado("2016023","Valdez Arevalo","Rui Luis","23 calle 18 avenida Zona 11","86545323","Gefe De Area De Comida Hondureña",3);
call sp_AgregarEmpleado("2011232","Rios Cifuentes","Roger Josue","19 calle 14 avenida Villanueva","43546545","Gefe De Area de Exotica",4);
call sp_AgregarEmpleado("2020233","Zamora Camarena","Mario Cesar","12 calle 15 avenida Zona 8 De Mixco","43546567","Gefe de Area De Area De Mariscos",5);

/*----------- Listar Empleados -----------*/
Delimiter $$
   	create procedure sp_ListarEmpleado()
    Begin
		select 
			Empleados.codigoEmpleado,
            Empleados.numeroEmpleado,
            Empleados.apellidosEmpleado,
            Empleados.nombresEmpleado,
            Empleados.direccionEmpleado,
            Empleados.telefonoContacto,
            Empleados.gradoCocinero,
            Empleados.codigoTipoEmpleado
				from Empleados;
    End $$
Delimiter ;
/*------------Actualizar Empleados ------------*/
Delimiter $$
	create procedure sp_ActualizarEmpleado(
		in _codigoEmpleado int,
        in _numeroEmpleado int,
        in _apellidosEmpleado varchar(150),
        in _nombresEmpleado varchar(150),
        in _direccionEmpleado varchar(150),
        in _telefonoContacto varchar(10),
        in _gradoCocinero varchar(50),
        in _codigoTipoEmpleado int
    )
    Begin
		Update Empleados set
			numeroEmpleado = _numeroEmpleado,
            apellidosEmpleado = _apellidosEmpleado,
            nombresEmpleado = _nombresEmpleado,
            direccionEmpleado = _direccionEmpleado,
            telefonoContacto = _telefonoContacto,
            gradoCocinero = _gradoCocinero,
            codigoTipoEmpleado = _codigoTipoEmpleado
				where codigoEmpleado = _codigoEmpleado;
    End $$
Delimiter ;
/*-----------Eliminar Empleados ---------------*/
Delimiter $$
	create procedure sp_EliminarEmpleado(
		in _codigoEmpleado int
    )
    Begin
		Delete from Empleados
			where codigoEmpleado = _codigoEmpleado;
    End $$
Delimiter ;
/*---------- Buscar Empleados ----------------*/
Delimiter $$
	create procedure sp_BuscarEmpleado(
		in _codigoEmpleado int
    )
    Begin
		select 
			Empleados.codigoEmpleado, 
            Empleados.numeroEmpleado,
            Empleados.apellidosEmpleado,
            Empleados.nombresEmpleado,
            Empleados.direccionEmpleado,
            Empleados.telefonoContacto,
            Empleados.gradoCocinero,
            Empleados.codigoTipoEmpleado
				from Empleados where codigoEmpleado = _codigoEmpleado;
    End $$
Delimiter ;

/*tipo de P L A T O S*/
/*------------Agregar Tipos de Platos -------------*/
Delimiter $$
	create procedure sp_AgregarTipoPlato(
		in _descripcionTipo varchar(100)
    )
    Begin
		insert into TipoPlato(descripcionTipo) 
			values(_descripcionTipo); 
    End $$
Delimiter ;
call sp_AgregarTipoPlato("Platos llanos");
call sp_AgregarTipoPlato("Platos hondos");
call sp_AgregarTipoPlato("Platos de postre");
call sp_AgregarTipoPlato("Platos de café");
call sp_AgregarTipoPlato("Platos de diseño");


/*-------------Listar Tipos de Platos ---------------*/
Delimiter $$
	create procedure sp_ListarTipoPlato()
    Begin
		select 
			TipoPlato.codigoTipoPlato,
            TipoPlato.descripcionTipo
				from TipoPlato;
    End $$
Delimiter ;
/*-------Actualizar Tipos de Platos----------------*/
Delimiter $$
	create procedure sp_ActualizarTipoPlato(
		in _codigoTipoPlato int,
        in _descripcionTipo varchar(100)
    )
    Begin
		Update TipoPlato set
			descripcionTipo = _descripcionTipo
				where codigoTipoPlato = _codigoTipoPlato;
    End $$
Delimiter ;
/*-------------- Eliminar Tipo de Platos---------------*/
Delimiter $$
	create procedure sp_EliminarTipoPlato(
		in _codigoTipoPlato int
    )
    Begin
		delete from TipoPlato
			where codigoTipoPlato = _codigoTipoPlato;
    End $$
Delimiter ;
/*---------------Buscar Tipo de Platos---------------*/
Delimiter $$
	create procedure sp_BuscarTipoPlato(
		in _codigoTipoPlato int 
    )
    Begin
		select 
			TipoPlato.codigoTipoPlato,
            TipoPlato.descripcionTipo
				from TipoPlato where codigoTipoPlato = _codigoTipoPlato;
    End $$
Delimiter ;

/*P L A T O S*/
/*/*------------Agregar Platos-----------------*/
Delimiter $$
	create procedure sp_AgregarPlato(
		in _cantidad int,
        in _nombrePlato varchar(50),
        in _descripcionPlato varchar(150),
        in _precioPlato decimal(10,2),
        in _codigoTipoPlato int
    )
    Begin
		insert into Platos(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
			values (_cantidad, _nombrePlato, _descripcionPlato, _precioPlato, _codigoTipoPlato);
    End $$
Delimiter ;
call sp_AgregarPlato("43","Viridiana","Plato a base de mariscos frescos","120.00",1);
call sp_AgregarPlato("23","Fabada Asturiana","Plato que explota el sabor del mango","80.00",2);
call sp_AgregarPlato("12","Gazpacho y Salmorejo","Plato en base a comida española","200.00",3);
call sp_AgregarPlato("54","Tortilla de patatas","Plato que incluye 4 tortillas de Papa con Queso","45.50",4);
call sp_AgregarPlato("23","Pulpo a la gallega","Plato con pulpo bañado en salsa picante","350.00",5);

/*-------------- Listar Platos ---------------*/
Delimiter $$
	create procedure sp_ListarPlato()
    Begin
		select 
			Platos.codigoPlato,
            Platos.cantidad,
            Platos.nombrePlato,
            Platos.descripcionPlato,
            Platos.precioPlato,
            Platos.codigoTipoPlato
				from Platos;
    End $$
Delimiter ;
/*----------- Actualizar Platos ---------------*/
Delimiter $$
	create procedure sp_ActualizarPlato(
		in _codigoPlato int,
        in _cantidad int,
        in _nombrePlato varchar(50),
        in _descripcionPlato varchar(150),
        in _precioPlato decimal(10,2),
        in _codigoTipoPlato int
    )
    Begin
		Update Platos set
			cantidad = _cantidad,
            nombrePlato = _nombrePlato,
            descripcionPlato = _descripcionPlato,
            precioPlato = _precioPlato,
            codigoTipoPlato = _codigoTipoPlato
				where codigoPlato = _codigoPlato;
    End $$
Delimiter ;
/*------------ Elimiar Platos --------------*/
Delimiter $$
	create procedure sp_ElimiarPlato(
		in _codigoPlato int
    )
    Begin
		Delete from Platos
			where codigoPlato = _codigoPlato;
    End $$
Delimiter ;
/*------------ Buscar Platos -----------------*/
Delimiter $$
	create procedure sp_BuscarPlato(
		in _codigoPlato int
    )
    Begin
		select 
			Platos.codigoPlato,
            Platos.cantidad,
            Platos.nombrePlato,
            Platos.descripcionPlato,
            Platos.precioPlato,
            Platos.codigoTipoPlato
				from Platos where codigoPlato = _codigoPlato;
    End $$
Delimiter ;*/


/*---------------AGREGAR PRODUCTOS------------------------------------*/
Delimiter $$ 
	create procedure sp_AgregarProducto(
    in _nombreProducto varchar(256),
    in _cantidad int
)
	begin 
		insert into Productos(nombreProducto,cantidad)
			values (_nombreProducto,_cantidad);
	end $$
Delimiter ;
call sp_AgregarProducto("Salsa Mexicana",34);
call sp_AgregarProducto("Pescado ",300);
call sp_AgregarProducto("Harina",20000);
call sp_AgregarProducto("Pimienta Negra",10000);
call sp_AgregarProducto("Pulpo",50);
/*---------------lISTAR PRODUCTOS------------------------------------*/
Delimiter $$
	Create procedure sp_ListarProductos()
    begin 
		select 
			Productos.codigoProducto,
            Productos.nombreProducto,
            Productos.cantidad
				from Productos;
	end $$
Delimiter ;
/*---------------ACTUALIZAR  PRODUCTOS------------------------------------*/
Delimiter $$
	create procedure so_ActualizarProductos(
    in _codigoProducto int,
    in _nombreProducto varchar(256),
    in _cantidad int
)
	begin 
		update Productos set
			nombreProducto = _nombreProducto,
            cantidad = _cantidad
				where codigoProducto = _codigoProducto;
	end $$
Delimiter ;
/*---------------ELIMINAR  PRODUCTOS------------------------------------*/
Delimiter $$
		create procedure sp_ElimnarProductos(
			in _codigoProducto int
)	
	begin 
		delete from Productos 
			where codigoProducto = _codigoProducto;
	END $$
Delimiter ;
/*---------------BUSCAR  PRODUCTOS------------------------------------*/
Delimiter $$
	create procedure sp_BuscarProducto(
		in _codigoProducto int
    ) 
		begin 
        select
			Productos.codigoProducto,
            Productos.nombreProducto,
            Productos.cantidad
				from Productos where codigoProducto = _codigoProducto;
	end $$
Delimiter ;
/* SERVICIO Y EMPLEADOS */
/*---------------- Agregar Servicios y Empleados ----------------*/

Delimiter $$
	create procedure sp_AgregarServiciosEmpleados(
		in _Servicios_codigoServicio int,
        in _Empleados_codigoEmpleado int,
        in _fechaEvento date,
        in _horaEvento time,
        in _lugarEvento varchar(150)
    )
    Begin
		insert into Servicios_has_Empleados(Servicios_codigoServicio, Empleados_codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
			values(_Servicios_codigoServicio, _Empleados_codigoEmpleado, _fechaEvento, _horaEvento, _lugarEvento);
    End $$
Delimiter ;
call sp_AgregarServiciosEmpleados(1,1,"20200412","10:00:00","Zona 5 Ciudad De Guatemala");
call sp_AgregarServiciosEmpleados(2,2,"20200923","12:00:00","Zona 4 Ciudad De Guatemala");
call sp_AgregarServiciosEmpleados(3,3,"20200513","14:00:00","Zona 3 Ciudad De Guatemala");
call sp_AgregarServiciosEmpleados(4,4,"20201023","08:00:00","Zona 8 Ciudad De Guatemala");
call sp_AgregarServiciosEmpleados(5,5,"20201221","12:00:00","Zona 12 Ciudad De Guatemala");


/*-------------- Listar Servicios y Empleados --------------------*/
Delimiter $$
	create procedure sp_ListarServiciosEmpleados()
    Begin
		select 
			Servicios_has_Empleados.Servicios_codigoServicio,
            Servicios_has_Empleados.Empleados_codigoEmpleado,
            Servicios_has_Empleados.fechaEvento,
            Servicios_has_Empleados.horaEvento,
            Servicios_has_Empleados.lugarEvento
				from Servicios_has_Empleados;
    End $$
Delimiter ;
/*-------------Actualizar Servicios y Empleados --------------------*/
Delimiter $$
	create procedure sp_ActualizarServiciosEmpleados(
		in _Servicios_codigoServicio int,
        in _Empleado_codigoEmpleado int,
        in _fechaEvento date,
        in _horaEvento time,
        in _lugarEvento varchar(150)
    )
    Begin
		Update Servicios_has_Empleados set 
			Servicios_codigoServicio = _Servicios_codigoServicio,
            Empleados_codigoEmpleado = _Empleado_codigoEmpleado,
            fechaEvento = _fechaEvento,
            horaEvento = _horaEvento,
            lugarEvento = _lugarEvento
				where Servicios_codigoServicio = _Servicios_codigoServicio;
    End $$
Delimiter ;

/*call sp_ActualizarServiciosEmpleados():
/*---------------Eliminar Servicios y Empleados -------------------*/
Delimiter $$
	create procedure sp_EliminarServiciosEmpleados(
		in _Servicios_codigoServicios int
    )
    Begin
		Delete from Servicios_has_Empleado
			where Servicios_codigoServicios = _Servicios_codigoServicios;
    End $$
Delimiter ;

/*------------- Buscar Servicios y Empleados ------------------------*/
Delimiter $$
	create procedure sp_BuscarServiciosEmpleados(
		in_Servicios_codigoServicios int
    )
    Begin
		select 
			Servicios_has_Empleados.Servicios_codigoServicio,
            Servicios_has_Empleados.Empleados_codigoEmpleado,
            Servicios_has_Empleados.fechaEvento,
            Servicios_has_Empleados.horaEvento,
            Servicios_has_Empleados.lugarEvento
				where Servicios_codigoServicios = Servicios_codigoServicios;
    End $$
Delimiter ;

/*servicios y platos */
/*-------------- Agregar Servicios y Platos -------------------------*/
Delimiter $$
	create procedure sp_AgregarServiciosPlatos(
		in _Servicios_codigoServicio int,
        in _Platos_codigoPlato int
    )
    Begin
		insert into Servicios_has_Platos(Servicios_codigoServicio, Platos_codigoPlato)
			values(_Servicios_codigoServicio, _Platos_codigoPlato);
    End $$
Delimiter ;
call sp_AgregarServiciosPlatos(1,1);
call sp_AgregarServiciosPlatos(2,2);
call sp_AgregarServiciosPlatos(3,3);
call sp_AgregarServiciosPlatos(4,4);
call sp_AgregarServiciosPlatos(5,5);

/*------------- Listar Servicios y Platos --------------------------*/
Delimiter $$
	create procedure sp_ListarServiciosPlatos()
    Begin
		select 
			Servicios_has_Platos.Servicios_codigoServicio,
            Servicios_has_Platos.Platos_codigoPlato
				from Servicios_has_Platos;
    End $$
Delimiter ;
/*--------------Actualizar Servicios y Platos --------------------*/
Delimiter $$
	create procedure sp_ActualizarServiciosPlatos(
		in _Servicios_codigoServicios int,
        in _Platos_codigoPlato int
    )
    Begin
		Update Servicios_has_Platos set
			Servicios_codigoServicio = _Servicios_codigoServicio,
            Platos_codigoPlato = _Platos_codigoPlato
				where Servicios_codigoServicio = _Servicios_codigoServicio;
    End $$
Delimiter ;
/*------------- Eliminar Servicios y Platos ----------------------*/
Delimiter $$
	create procedure sp_EliminarServiciosPlatos(
		in _Servicios_codigoServicios int
    )
    Begin
		Delete from Servicios_has_Platos
			where Servicios_codigoServicios = _Servicios_codigoServicios;
    End $$
Delimiter ;
/*------------ Buscar Servicios y Platos -----------------------*/
Delimiter $$
	create procedure sp_BuscarServiciosPlatos(
		in _Servicios_codigoServicios int
    )
    Begin
		select 
			Servicios_has_Platos.Servicios_codigoServicio,
            Servicios_has_Platos.Platos_codigoPlato
				from Servicios_has_Platos where Servicios_codigoServicio = _Servicios_codigoServicio;
    End $$
Delimiter ;
/*-------------- PRODUCTOS Y PLATOS 
/*-------------- Agregar Productos y Platos -------------------------*/
Delimiter $$
	create procedure sp_AgregarProductosPlatos(
		in _Productos_codigoProducto int,
        in _Platos_codigoPlato int
    )
    Begin
		insert into Productos_has_Platos(Productos_codigoProducto, Platos_codigoPlato)
			values(_Productos_codigoProducto, _Platos_codigoPlato);
    End $$
Delimiter ;
call sp_AgregarProductosPlatos(1,1);
call sp_AgregarProductosPlatos(2,2);
call sp_AgregarProductosPlatos(3,3);
call sp_AgregarProductosPlatos(4,4);
call sp_AgregarProductosPlatos(5,5);

/*------------- Listar Productos y Platos --------------------------*/
Delimiter $$
	create procedure sp_ListarProductosPlatos()
    Begin
		select 
			Productos_has_Platos.Productos_codigoProducto,
            Productos_has_Platos.Platos_codigoPlato
				from Productos_has_Platos;
    End $$
Delimiter ;
/*--------------Actualizar Productos y Platos --------------------*/
Delimiter $$
	create procedure sp_ActualizarProductosPlatos(
		in _Productos_codigoProductos int,
        in _Platos_codigoPlato int
    )
    Begin
		Update Productos_has_Platos set
			Productos_codigoProducto = _Productos_codigoProducto,
            Platos_codigoPlato = _Platos_codigoPlato
				where Productos_codigoProducto = _Productos_codigoProducto;
    End $$
Delimiter ;

/*------------- Eliminar Servicios y Platos ----------------------*/
Delimiter $$
	create procedure sp_EliminarProductosPlatos(
		in _Productos_codigoProductos int
    )
    Begin
		Delete from Productos_has_Platos
			where Productos_codigoProductos = _Productos_codigoProductos;
    End $$
Delimiter ;
/*------------ Buscar Servicios y Platos -----------------------*/
Delimiter $$
	create procedure sp_BuscarProductosPlatos(
		in _Productos_codigoProductos int
    )
    Begin
		select 
			Productos_has_Platos.Productos_codigoProducto,
            Productos_has_Platos.Platos_codigoPlato
				from Productos_has_Platos where Productos_codigoProducto = _Productos_codigoProducto;
    End $$
Delimiter ;
/*INNER JOIN PRESUPUESTO*/
DELIMITER $$
create procedure sp_ListarEmpresasReporte(in codigoEmpresa int)
begin 
	select * from Empresas E
    inner join Presupuesto P on E.codigoEmpresa = P.codigoEmpresa
    inner join Servicios S on E.codigoEmpresa = S.codigoEmpresa
    where E.codigoEmpresa = codigoEmpresa order By P.cantidadPresupuesto;
 END $$
DELIMITER ;
/*INNER JOIN SERVICIO*/
DELIMITER $$
CREATE PROCEDURE sp_ReporteServicio (in codigoServicio int)
BEGIN 
	select * from Servicios S 
	inner join Empresas E on S.codigoServicio = E.codigoEmpresa
	inner join Platos P on S.codigoServicio = P.codigoPlato
	inner join TipoPlato T on S.codigoServicio  = T.codigoTipoPlato
	inner join Productos D on S.codigoServicio  = D.codigoProducto 
	where S.codigoServicio  = codigoServicio Order By S.tipoServicio;
END $$
DELIMITER ;
call sp_ReporteServicio(1);