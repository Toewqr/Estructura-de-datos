import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import grafo.*;
import arbol.*;
import clasesComplemento.*;
import java.util.Scanner;

public class SistemaMudanza {
    
    private ArbolAVL arbolCiudades;
    private GrafoCiudades grafo;
    private Map<ClaveCliente, Cliente> clientes; // HashMap
    private Scanner sc;

        private Logger logger;


    public SistemaMudanza() {
        arbolCiudades = new ArbolAVL();
        grafo = new GrafoCiudades();
        clientes = new HashMap<>();
        sc = new Scanner(System.in);
        logger = Logger.getInstancia();
    }

    public void iniciar() {
        boolean salir = false;
        logger.log("INICIO DEL SISTEMA");
    

        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1 -> cargarDatosIniciales();
                case 2 -> new ConsultaCiudad(arbolCiudades).menu();
                case 3 -> new ConsultaViajes(grafo, arbolCiudades).menu();
                case 4 -> new ConsultaClientes(clientes).menu();
                case 0 -> salir = true;
                default -> System.out.println("Opción inválida.");
            }
        }

        System.out.println("Sistema cerrado.");
      
    logger.log("FIN DEL SISTEMA");
    logger.cerrar();  
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- SISTEMA MUDANZAS ---");
        System.out.println("1. Carga inicial");
        System.out.println("2. ABM/Consultas Ciudades");
        System.out.println("3. ABM/Consultas Viajes");
        System.out.println("4. ABM/Consultas Clientes");
        System.out.println("0. Salir");
        System.out.print("Seleccione opción: ");
    }

private void cargarDatosIniciales() {
   
    System.out.println("Cargando datos iniciales...");
    
    cargarArchivo("sistema/"+"ciudades.txt", linea -> {
        if (linea.startsWith("C;")) {
            String[] datos = linea.split(";");
            Ciudad ciudad = new Ciudad(datos[1], datos[2], datos[3]);
            arbolCiudades.insertar(ciudad);
            grafo.insertarCiudad(ciudad);
        }
    });

        System.out.println("Carga inicial completada!");
        logger.log("CARGA INICIAL COMPLETADA");
        volcarEstadoCompleto();
    
   
    cargarArchivo("sistema/"+"clientes.txt", linea -> {
        if (linea.startsWith("P;")) {
            String[] datos = linea.split(";");
            ClaveCliente clave = new ClaveCliente(datos[1], datos[2]);
            Cliente cliente = new Cliente(
                clave, datos[4], datos[3], datos[5], "");
            clientes.put(clave, cliente);
        }
    });
    
    
    cargarArchivo("sistema/"+"rutas.txt", linea -> {
        if (linea.startsWith("R;")) {
            String[] datos = linea.split(";");
            double distancia = Double.parseDouble(datos[3]);
            grafo.insertarRuta(datos[1], datos[2], distancia);
        }
    });
    
   
    cargarArchivo("sistema/"+"solicitudes.txt", linea -> {
        if (linea.startsWith("S;")) {
            String[] datos = linea.split(";");
            ClaveCliente clave = new ClaveCliente(datos[4], datos[5]);
            Cliente cliente = clientes.get(clave);
            
            if (cliente != null) {
                Ciudad origen = arbolCiudades.buscar(datos[1]);
                Ciudad destino = arbolCiudades.buscar(datos[2]);
                
                if (origen != null && destino != null) {
                    Pedido pedido = new Pedido(
                        origen, destino, datos[3], cliente,
                        Double.parseDouble(datos[6]),
                        Integer.parseInt(datos[7]),
                        datos[8], datos[9],
                        datos[10].equals("T")
                    );
                    origen.agregarPedido(destino.getCodigoPostal(), pedido);
                }
            }
        }
    });
    
    System.out.println("Carga inicial completada!");

            logger.log("CARGA INICIAL COMPLETADA");
        volcarEstadoCompleto();
}

private void cargarArchivo(String nombreArchivo, java.util.function.Consumer<String> procesador) {
    try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            procesador.accept(linea);
        }
    } catch (IOException e) {
        System.err.println("Error al cargar " + nombreArchivo + ": " + e.getMessage());
    }
}
//carga correcta de clientes ciudades arbol y grafo rutas
private void volcarEstadoCompleto() {

        logger.volcarEstado("ARBOL DE CIUDADES", arbolCiudades.listarCiudades().toString());
        logger.volcarEstado("GRAFO DE RUTAS", grafo.volcarEstadoCompleto()); 
        logger.volcarEstado("CLIENTES", volcarClientes());
    }
//clientes cargados con clave nombre apellido y telefono
private String volcarClientes() {
    StringBuilder sb = new StringBuilder();
    for (Cliente cliente : clientes.values()) {
        sb.append("Clave: ").append(cliente.getClave())
          .append(" | Nombre: ").append(cliente.getNombres()).append(" ").append(cliente.getApellidos())
          .append(" | Tel: ").append(cliente.getTelefono())
          .append("\n");
    }
    return sb.toString();
}
//iniciar sistema4
public static void main(String[] args) {
    SistemaMudanza sistema = new SistemaMudanza();
    
    
    String[] archivos = {"ciudades.txt", "clientes.txt", "rutas.txt", "solicitudes.txt"};
    for (String archivo : archivos) {
        if (!new java.io.File("sistema/"+archivo).exists()) {
            System.err.println("ERROR: No se encuentra " + archivo + " en el directorio actual");
            System.err.println("Directorio actual: " + System.getProperty("user.dir"));
            return;
        }
    }
    
    sistema.iniciar();
}


}
