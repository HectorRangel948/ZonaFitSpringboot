package gm.zona_fit;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.ClienteServicio;
import gm.zona_fit.servicio.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {
	@Autowired
	private IClienteServicio clienteServicio;
	private static final Logger logger =
			LoggerFactory.getLogger(ZonaFitApplication.class);

	public static void main(String[] args) {
		logger.info("Iniciando la aplicación");
		//Levantar la fabrica de Spring
		SpringApplication.run(ZonaFitApplication.class, args);
		logger.info("Aplicación Finalizada");

	}

	@Override
	public void run(String... args) throws Exception {
		menu();
	}

	private void menu(){
		boolean salir=false;
		int seleccion = 0;
		Scanner console = new Scanner(System.in);
		Cliente cliente = new Cliente();


		logger.info("***Aplicación Zona Fit (GYM)***");

		while(!salir){
			logger.info("""
			    \n1.-Listar clientes
				2.-Encontrar cliente
				3.-Crear cliente
				4.-Modificar cliente
				5.-Eliminar cliente
				6.-Salir
					""");
			try{
			seleccion=console.nextInt();
			switch(seleccion){
				case 1-> listarClientes();
				case 2-> encontrarCliente(clienteServicio, console);
				case 3-> crearCliente(cliente, clienteServicio, console);
				case 4-> modificarCliente(cliente, clienteServicio, console);
				case 5-> eliminarCliente(cliente, clienteServicio, console);
				case 6-> salir=true;
			}
			}catch(Exception e){
				System.out.println("Error en la selección\n" + e.getMessage());
			}
		}
	}

	private void listarClientes(){
		List<Cliente> clientes=clienteServicio.listarClientes();
		clientes.forEach(cliente->logger.info(cliente.toString()));
	}

	private Cliente encontrarCliente(IClienteServicio clienteServicio, Scanner console){
		Cliente cliente=null;
		Integer idCliente;
		logger.info("Ingrese el id del cliente: ");
		try{
			idCliente=console.nextInt();
			cliente=clienteServicio.buscarClientePorId(idCliente);
			logger.info(cliente.toString());
		}catch(Exception e){
			logger.info("Error al ingresar el id del cliente\n" + e.getMessage());
		}
		return cliente;
	}

	private void crearCliente(Cliente cliente, IClienteServicio clienteServicio, Scanner console){
		try{
			console.nextLine();
			logger.info("Ingrese el nombre del cliente: ");
			cliente.setNombre(console.nextLine());
			logger.info("Ingrese el apellido del cliente: ");
			cliente.setApellido(console.nextLine());
			logger.info("Ingrese la membresía del cliente: ");
			cliente.setMembresia(console.nextInt());
			clienteServicio.guardarCliente(cliente);
			logger.info("\nSe guardó con éxito al nuevo cliente");
			listarClientes();
		}catch(Exception e){
			logger.info("Falló la creación del cliente\n" + e.getMessage());
		}
	}

	//No funciona modificarCliente
	private void modificarCliente(Cliente cliente, IClienteServicio clienteServicio, Scanner console){
		Integer idCliente;

		try{
			listarClientes();
			cliente=encontrarCliente(clienteServicio, console);
			idCliente=cliente.getId();
			if(idCliente!=null){
				logger.info("Id Cliente: "+idCliente);
				console.nextLine();
				logger.info("\nIngrese el nuevo nombre del cliente: ");
				cliente.setNombre(console.nextLine());
				logger.info("Ingrese el nuevo apellido del cliente: ");
				cliente.setApellido(console.nextLine());
				logger.info("Ingrese la nueva membresía del cliente: ");
				cliente.setMembresia(console.nextInt());
				logger.info("Nueva información del cliente: " + cliente);
				clienteServicio.guardarCliente(cliente);
				logger.info("Se guardó con éxito la información del cliente\n");
			}
			else{logger.info("Cliente no encontrado\n");}
			listarClientes();
		}catch(Exception e){
			logger.info("Falló la mofidicación del cliente\n"+e.getMessage());
		}
	}

	private void eliminarCliente(Cliente cliente, IClienteServicio clienteServicio, Scanner console){
		try{
			listarClientes();
			console.nextLine();
			logger.info("Ingrese el id del cliente a eliminar: ");
			cliente.setId(console.nextInt());
			clienteServicio.elminiarCliente(cliente);
			logger.info("Se eliminó al cliente exitosamente\n");
			listarClientes();
		}catch(Exception e){
			logger.info("Falló la eliminación del cliente\n" + e.getMessage());
		}

	}


}
