import java.util.Map;
import java.util.Scanner;
import clasesComplemento.*;


public class ConsultaClientes {
    private Map<ClaveCliente, Cliente> clientes;
    private Scanner sc;
    private Logger logger = Logger.getInstancia();


    public ConsultaClientes(Map<ClaveCliente, Cliente> clientes) {
        this.clientes = clientes;
        this.sc = new Scanner(System.in);
    }

    public void menu() {
        boolean volver = false;
        while (!volver) {
            mostrarMenu();
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> altaCliente();
                case 2 -> bajaCliente();
                case 3 -> buscarCliente();
                case 4 -> listarClientes();
                case 0 -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n--- Gestión de Clientes ---");
        System.out.println("1. Alta cliente");
        System.out.println("2. Baja cliente");
        System.out.println("3. Buscar cliente");
        System.out.println("4. Listar todos");
        System.out.println("0. Volver");
        System.out.print("Opción: ");
    }

     private void altaCliente() {
        System.out.println("\n--- ALTA DE CLIENTE ---");
        
        // Solicitar datos del documento
        System.out.print("Tipo Documento: ");
        String tipoDoc = sc.nextLine();
        
        System.out.print("Número Documento: ");
        String numDoc = sc.nextLine();
        
        // Solicitar datos personales
        System.out.print("Nombres: ");
        String nombres = sc.nextLine();
        
        System.out.print("Apellidos: ");
        String apellidos = sc.nextLine();
        
        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();
        
        System.out.print("Email: ");
        String email = sc.nextLine();

        // Crear clave y cliente
        ClaveCliente clave = new ClaveCliente(tipoDoc, numDoc);
        Cliente nuevoCliente = new Cliente(clave, nombres, apellidos, telefono, email);
        clientes.put(clave, nuevoCliente);
        
        System.out.println(" Cliente agregado: " + nombres + " " + apellidos);
        logger.log("Se creó cliente: " + clave);
    }

    

    private void bajaCliente() {
        System.out.print("Tipo Documento: ");
        String tipoDoc = sc.nextLine();
        
        System.out.print("Número Documento: ");
        String numDoc = sc.nextLine();
        
        ClaveCliente clave = new ClaveCliente(tipoDoc, numDoc);
        Cliente eliminado = clientes.remove(clave);
        
        System.out.println(eliminado != null ? "Cliente eliminado." : "No existe ese cliente.");
                if (eliminado != null) {
            logger.log("Se eliminó cliente: " + clave);
        }

    }

    private void buscarCliente() {
        System.out.print("Tipo Documento: ");
        String tipoDoc = sc.nextLine();
    
        System.out.print("Número Documento: ");
        String numDoc = sc.nextLine();
        
        ClaveCliente clave = new ClaveCliente(tipoDoc, numDoc);
        
        Cliente c = clientes.get(clave);
        
        System.out.println(c != null ? c : "Cliente no encontrado.");
    }
    
    public void mostrarClienteCompleto(ClaveCliente clave) {
    Cliente cliente = clientes.get(clave);
    if (cliente != null) {
        System.out.println("Documento: " + cliente.getClave());
        System.out.println("Nombres: " + cliente.getNombres());
        System.out.println("Apellidos: " + cliente.getApellidos());
        System.out.println("Teléfono: " + cliente.getTelefono());
        System.out.println("Email: " + cliente.getEmail());
    } else {
        System.out.println("Cliente no encontrado.");
    }
}

    private void listarClientes() {
        System.out.println("Clientes registrados:");
        for (Cliente c : clientes.values()) {
            System.out.println(c);
        }
    }
}