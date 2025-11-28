import java.util.Scanner;
import clasesComplemento.*;
import lineales.*;
import arbol.*;


public class ConsultaCiudad {
    private ArbolAVL arbol;
    private Scanner sc;
    private Logger logger = Logger.getInstancia();

    public ConsultaCiudad(ArbolAVL arbol) {
        this.arbol = arbol;
        this.sc = new Scanner(System.in);
        
    }

    public void menu() {
        boolean volver = false;
        while (!volver) {
            mostrarMenu();
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> altaCiudad();
                case 2 -> bajaCiudad();
                case 3 -> buscarCiudad();
                case 4 -> listarCiudades();
                case 5 -> ciudadesPorPrefijo();
                case 6 -> mostrarArbolBalanceado();
                case 7 -> listarPedidosDeCiudad();
                case 8 -> mostrarPedidosEntreCiudades();

                case 0 -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n--- Gestión de Ciudades ---");
        System.out.println("1. Alta ciudad");
        System.out.println("2. Baja ciudad");
        System.out.println("3. Buscar ciudad por código");
        System.out.println("4. Listar todas las ciudades");
        System.out.println("5. ciudades por prefijo");
        System.out.println("6. Mostrar estructura del árbol");
        System.out.println("7. listar pedidos de una ciudad");
        System.out.println("8. Mostrar pedidos hacia una ciudad desde un origen");
        System.out.println("0. Volver");
        System.out.print("Opción: ");
    }

    private void altaCiudad() {
        System.out.print("Código postal: ");
        Comparable cp = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Provincia: ");
        String prov = sc.nextLine();

        Ciudad c = new Ciudad(cp, nombre, prov);
        boolean exito = arbol.insertar(c);
        System.out.println(exito ? "Ciudad agregada." : "Ya existe esa ciudad.");
        if (exito) {
            logger.log("Se creó la ciudad: " + cp + " - " + nombre);
        }
    }
//
    private void listarPedidosDeCiudad() {
        System.out.print("Código postal origen: ");
        Comparable cpOrigen = sc.nextLine();
        
        Lista pedidos = this.obtenerPedidosDeCiudad(cpOrigen);
        Ciudad origen = arbol.buscar(cpOrigen);
        
        
        if (origen == null) {
            System.out.println("Error: Ciudad no encontrada");
            return;
        }
        
        if (pedidos.esVacia()) {
            System.out.println("\nNo hay pedidos registrados en " + origen.getNombre());
        } else {
            System.out.println("\nPedidos de " + origen.getNombre() + ":");
            System.out.println("====================================");
            
            for (int i = 1; i <= pedidos.longitud(); i++) {
                Pedido p = (Pedido) pedidos.recuperar(i);
                System.out.println(p);
                System.out.println("Destino: " + p.getDestino().getNombre());
                System.out.println("------------------------------------");
            }
        }
    }

     public Lista obtenerPedidosDeCiudad(Comparable codigoPostalOrigen) {
        Ciudad ciudad = arbol.buscar(codigoPostalOrigen);
        if (ciudad != null) {
            return ciudad.obtenerTodosPedidosSalientes();
        }
        return new Lista(); // Lista vacía si no existe la ciudad
    }

    private void bajaCiudad() {

        System.out.print("Código postal a eliminar: ");
        String cp = sc.nextLine();
        boolean exito = arbol.eliminar(cp);
        System.out.println(exito ? "Ciudad eliminada." : "No existe esa ciudad.");
        if (exito) {
            logger.log("Se eliminó la ciudad: " + cp);
        }
    }

    /// utliliza metodo auxiliar

    private void mostrarPedidosEntreCiudades() {
        System.out.print("Código postal origen: ");
        Comparable cpOrigen = sc.nextLine();
        System.out.print("Código postal destino: ");
        Comparable cpDestino = sc.nextLine();
        
        Lista pedidos = this.obtenerPedidosDirectos(cpOrigen, cpDestino);
        Ciudad origen = arbol.buscar(cpOrigen);
        Ciudad destino = arbol.buscar(cpDestino);
        
        if (origen == null || destino == null) {
            System.out.println("Error: Una o ambas ciudades no existen");
            return;
        }
        
        if (pedidos.esVacia()) {
            System.out.println("\nNo hay pedidos de " + origen.getNombre() + 
                              " a " + destino.getNombre());
        } else {
            System.out.println("\nPedidos de " + origen.getNombre() + 
                              " a " + destino.getNombre() + ":");
            System.out.println("====================================");
            
            for (int i = 1; i <= pedidos.longitud(); i++) {
                Pedido p = (Pedido) pedidos.recuperar(i);
                System.out.println(p);
                System.out.println("------------------------------------");
            }
        }
    }
        public Lista obtenerPedidosDirectos(Comparable codigoPostalOrigen, Comparable codigoPostalDestino) {
        Ciudad ciudadOrigen = arbol.buscar(codigoPostalOrigen);
        Lista ls = new Lista();
        if (ciudadOrigen != null) {
            ls = ciudadOrigen.obtenerPedidosDirectosADestino(codigoPostalDestino);
        }
        return ls; // Lista vacía si no existe la ciudad origen
    }


    private void buscarCiudad() {
        System.out.print("Código postal: ");
        String cp = sc.nextLine();
        Ciudad c = arbol.buscar(cp);
        System.out.println(c != null ? c : "Ciudad no encontrada.");
    }

    private void listarCiudades() {
        Lista lista = arbol.listarCiudades();
        System.out.println("Ciudades: " + lista);
    }

    private void ciudadesPorPrefijo() {
    System.out.print("Prefijo postal (ej: '83'): ");
    String prefijo = sc.nextLine();
    Lista ciudades = arbol.ciudadesPorPrefijo(prefijo);
    System.out.println("Ciudades con prefijo " + prefijo + ":");
    for (int i = 1; i <= ciudades.longitud(); i++) {
        System.out.println(ciudades.recuperar(i));
    }

}

private void mostrarArbolBalanceado() {
    arbol.mostrarArbolBalanceado();
}
}
