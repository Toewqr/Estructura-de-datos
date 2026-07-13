import java.util.HashMap;
import java.util.Scanner;
import grafo.*;
import arbol.*;
import lineales.*;
import clasesComplemento.*;

public class ConsultaViajes {
    private GrafoCiudades grafo;
    private ArbolAVL arbol;
    private Logger logger = Logger.getInstancia();

    private Scanner sc;

    public ConsultaViajes(GrafoCiudades grafo, ArbolAVL arbol) {
        this.grafo = grafo;
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
                case 1 -> altaRuta();
                case 2 -> bajaRuta();
                case 3 -> listarRutas();
                case 4 -> consultarConexion();
                case 5 -> caminoMenosCiudades();
                case 6 -> caminoMenorDistancia();
                case 7 -> caminosQuePasanPorC();
                case 8 -> verificarDistanciaMaxima();
                case 9 -> consultarCaminoPerfecto();
                case 10 -> verificarViajeCapacidad();
                case 11 -> mostrarGrafoCompleto();
                case 0 -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    // posibles pedidos y camino perfecto 
    private void verificarViajeCapacidad() {
        System.out.println("=== Verificar viaje con capacidad ===");
        System.out.print("Ciudad origen (CP): ");
        Comparable origen = sc.nextLine();
        System.out.print("Ciudad destino (CP): ");
        Comparable destino = sc.nextLine();
        System.out.print("Capacidad del camión (m³): ");
        double capacidad = Double.parseDouble(sc.nextLine());

        // Obtener pedidos posibles
        Lista pedidosPosibles = this.posiblesPedidos(origen, destino, capacidad);
        double espacioUtilizado = 0.0;

        if (pedidosPosibles.esVacia()) {
            System.out.println("No se encontraron pedidos adicionales para transportar.");
        } else {
            System.out.println("\nPedidos que pueden ser transportados:");
            for (int i = 1; i <= pedidosPosibles.longitud(); i++) {
                Pedido p = (Pedido) pedidosPosibles.recuperar(i);
                System.out.println(" - " + p.getCliente().getNombres() +
                        " | Volumen: " + p.getMetrosCubicos() + " m³" +
                        " | De: " + p.getOrigen().getCodigoPostal() +
                        " a " + p.getDestino().getCodigoPostal());
                espacioUtilizado += p.getMetrosCubicos();
            }
            System.out.printf("\nEspacio utilizado: %.2f m³ (%.2f m³ disponibles)\n",
                    espacioUtilizado, capacidad - espacioUtilizado);
        }
    }

    public void consultarCaminoPerfecto() {
        System.out.println("=== Verificar Camino Perfecto ===");
        System.out.print("Ingrese lista de ciudades (CP) separadas por comas: ");
        String linea = sc.nextLine().trim();
        System.out.print("Ingrese capacidad del camión (m³): ");
        double capacidad = Double.parseDouble(sc.nextLine().trim());

        // Parseo de entrada a Lista
        Lista ruta = new Lista();
        String[] tokens = linea.split(",");
        for (int k = 0; k < tokens.length; k++) {
            
            Comparable cp = tokens[k].trim();

            if (cp!=null) {
                ruta.insertar(cp, ruta.longitud() + 1);
            }
        }
        // Lógica de verificación
        boolean esPerfecto = false;
        if (!ruta.esVacia() && ruta.longitud() > 1) {
            // Suponemos que Grafo tiene el método esCaminoPerfecto:
            esPerfecto = this.caminoPerfecto(ruta, capacidad);
        }

        // Salida al usuario
        if (esPerfecto) {
            System.out.println("\nEl camino indicado SÍ es un “camino perfecto”.");
        } else {
            System.out.println("\nEl camino indicado NO cumple como camino perfecto.");
        }

        // ÚNICO return al final
        return;
    }

//implementaciones de grafo 
//fuera de grafo : dado un camino de ciudades se busca si es un camino perfecto 

    public boolean caminoPerfecto(Lista ciudades, double camionPeso) {
       
        int n = ciudades.longitud(); 
        double espacioDisponible = camionPeso; // espacio disponible para entregas
        double[] entregas = new double[n]; // simula el camion 
        boolean caminoPerfecto = true; //corte
        HashMap<Comparable, Integer> map = new HashMap<>(); // mpa de las ciudades con su posicion en el arreglo 
        boolean seguir = true;

        // Crear mapa de posiciones
        for (int i = 1; i <= n; i++) {
            Comparable cp = (Comparable) ciudades.recuperar(i);
            map.put(cp, i - 1); // empieza en 0 
        }

        // Verificar existencia de rutas
        for (int j = 1; j < n && seguir; j++) {

            Comparable ciudadActual = (Comparable) ciudades.recuperar(j);
            Comparable proxCiudad = (Comparable) ciudades.recuperar(j + 1);
            if (!grafo.existeRuta(ciudadActual, proxCiudad)) {
                seguir = false;
                caminoPerfecto = false;
            }
        }

        if (seguir) {

            for (int q = 1; q < n && caminoPerfecto; q++) { //itera hasta terminar el camino o por corte
                Comparable cp = (Comparable) ciudades.recuperar(q);
                int index = map.get(cp);
                // la primera posicion nunca tiene nada 
                //descarga y pierde espacio disponible // Si el indice tiene un valor y se llega a este desocupa y suma espacio 
                espacioDisponible += entregas[index];
                entregas[index] = 0;

                Ciudad ciudad = arbol.buscar(cp);
                if (ciudad == null)
                    continue;

                HashMap<Comparable, Lista> pedidosPorDestino = ciudad.getPedidosPorDestino();

                //Busca el pedido mas chico en terminos de espacio destinado a alguna de las ciudades 
                Pedido pedido = buscarPedidoValido(pedidosPorDestino, cp, ciudades, map);

                if (pedido != null && espacioDisponible >= pedido.getMetrosCubicos()) {
                    double espacioOcupar = pedido.getMetrosCubicos();
                    espacioDisponible -= espacioOcupar; // se toma elpedido y se pierde espacio hasta que se desocupe 
                    String destino = pedido.getDestino().getCodigoPostal();
                    int indexDestino = map.get(destino);
                    entregas[indexDestino] += espacioOcupar; // se suma al espacio del camion en la posicion del destino
                } else {
                    caminoPerfecto = false;
                }
            }
        }
        return caminoPerfecto;
    }
//////////// busca el pedido mas chico en temrinos de metros cubicos 
    private Pedido buscarPedidoValido(HashMap<Comparable, Lista> pedidos, Comparable ciudadActual,
        Lista ciudadesCamino, HashMap<Comparable, Integer> map) {
        int n = ciudadesCamino.longitud();
        double menorValor = Double.POSITIVE_INFINITY;
        Pedido menorPedido = null;
        int c = map.get(ciudadActual);

        for (int k = c + 1; k < n; k++) {
            String cp = (String) ciudadesCamino.recuperar(k + 1);
            if (pedidos.containsKey(cp)) {
                Lista ls = pedidos.get(cp);
                for (int i = 1; i <= ls.longitud(); i++) {
                    Pedido p = (Pedido) ls.recuperar(i);
                    if (p != null && !p.getTerminado() && p.getMetrosCubicos() < menorValor) {
                        menorValor = p.getMetrosCubicos();
                        menorPedido = p;
                    }
                }
            }
        }
        return menorPedido;
    }
///////////Metodo auxliar: Recorrer el camino más corto desde origen hasta destino y recolectar pedidos que el camión pueda cargar según su capacidad.
    public Lista posiblesPedidos(Comparable origen, Comparable destino, Double capacidadCamion) {

        Lista camino = grafo.caminoMasCortoKm(origen, destino);
        Lista pedidosPosibles = new Lista();
        double espacioRestante = capacidadCamion;
        int i = 1;
        boolean continuar = (!camino.esVacia());

        while (i <= camino.longitud() && espacioRestante > 0.0 && continuar) {

            Comparable cpActual = (Comparable) camino.recuperar(i);
            Ciudad ciudadActual = arbol.buscar(cpActual);

            if (ciudadActual != null) {
                HashMap<Comparable, Lista> pedidosCiudad = ciudadActual.getPedidosPorDestino(); //obtiene los pedidos de la ciudad actual 
                espacioRestante = procesarPedidosCiudad(pedidosCiudad, camino, i, espacioRestante, pedidosPosibles); //procesa todos los pedidos y los agrega al espacio si van a alguna 
                                                                                                            ////////// de las ciudades de la lista
            }
            i++;
        }
        return pedidosPosibles;
    }

 ///ultra fuerza bruta 

    private double procesarPedidosCiudad(HashMap<Comparable, Lista> pedidosPorDestino, Lista camino, int posActual,
            double espacioRestante, Lista pedidosSeleccionados) {

        double nuevoEspacio = espacioRestante;
        boolean camionLleno = (nuevoEspacio <= 0.0);

        for (Comparable cpDestino : pedidosPorDestino.keySet()) { // for por cada codigo postal de destino // itera en el has para buscar cada lista
            if (camionLleno) {
                continue;
            }

            int posDestino = camino.localizar(cpDestino);
            boolean destinoValido = (posDestino > posActual);

            if (destinoValido) {
                Lista pedidos = pedidosPorDestino.get(cpDestino);
                int j = 1;
                boolean seguirBuscando = true;

                //por cada lista se debe iterar toda y sumar los pesos mientras pueda
              // Itera en toda la lista 
                while (j <= pedidos.longitud() && !camionLleno && seguirBuscando) { //Se busca en toda la lista de destino n si se puede llevar sus pedidos
                    
                    Pedido p = (Pedido) pedidos.recuperar(j);
                    boolean pedidoValido = (p != null && !p.getTerminado() && p.getMetrosCubicos() <= nuevoEspacio);

                    if (pedidoValido) {
                        pedidosSeleccionados.insertar(p, pedidosSeleccionados.longitud() + 1);
                        nuevoEspacio -= p.getMetrosCubicos();
                        camionLleno = (nuevoEspacio <= 0.00001);
                    }
                    j++;
                    seguirBuscando = (j <= pedidos.longitud());
                }
            }
        }
        return nuevoEspacio;
    }



    private void caminoMenosCiudades() {

        System.out.println("Ingrese el origen del destino ");
        Comparable origen = sc.nextLine();
        System.out.println("Ingrese el destino ");
        Comparable destino = sc.nextLine();

        Lista ls = grafo.caminoMenosCiudades(origen, destino);

        if (ls.esVacia()) {
            System.out.println("No hay camino entre " +(String) origen + " y " + (String)destino);
        } else {
            System.out.println("Camino con menos ciudades:");
            int pos = 1;
            while (pos <= ls.longitud()) {
                Ciudad c = (Ciudad) ls.recuperar(pos);
                System.out.println(c.toString());
                pos++;
            }
        }
    }

    private void caminoMenorDistancia() {
        System.out.println("Ingrese los valores siguiente ->");
        System.out.println();
        System.out.print("Ciudad origen (CP): ");
        Comparable origen = sc.nextLine();
        System.out.print("Ciudad destino (CP): ");
        Comparable destino = sc.nextLine();
        Lista ls = new Lista();
        ls = grafo.caminoMasCortoKm(origen, destino);

        if (!ls.esVacia()) {
            System.out.println("El camino mas corto necesita pasar por... ");
            String camino = "";
            for (int i = 1; ls.longitud() + 1 > i; i++) {
                camino = camino + ("--->" + (String) ls.recuperar(i));
            }
            camino = camino + " " + "camino de menor km ";
        }

    }

    private void caminosQuePasanPorC() {
        System.out.println("Ingrese los siguientes valores:");
        System.out.print("Ciudad origen (CP): ");
        Comparable a = sc.nextLine();
        System.out.print("Ciudad destino (CP): ");
        Comparable b = sc.nextLine();
        System.out.print("Ciudad intermedia (CP): ");
        Comparable c = sc.nextLine();

        Lista caminos = grafo.caminosQuePasanPorC(a, b, c);

        if (caminos.esVacia()) {
            System.out.println("\nNo se encontraron caminos que pasen por la ciudad intermedia indicada.");
        } else {
            System.out.println("\nCaminos encontrados que pasan por " + (String)c + ":");

            for (int i = 1; i <= caminos.longitud(); i++) {
                Lista camino = (Lista) caminos.recuperar(i);
                StringBuilder sb = new StringBuilder();

                for (int j = 1; j <= camino.longitud(); j++) {
                    sb.append(camino.recuperar(j));
                    if (j < camino.longitud()) {
                        sb.append(" -> ");
                    }
                }
                System.out.println(sb.toString());
            }
        }
    }

    private void verificarDistanciaMaxima() {
        System.out.println("Verificación de distancia máxima permitida entre dos ciudades.");

        System.out.print("Ingrese ciudad origen (CP): ");
        Comparable origen = sc.nextLine();

        System.out.print("Ingrese ciudad destino (CP): ");
        Comparable destino = sc.nextLine();

        System.out.print("Ingrese la distancia máxima permitida (km): ");
        double maxKm = Double.parseDouble(sc.nextLine());

        boolean dentroDelLimite = grafo.verificarDistanciaMaxima(origen, destino, maxKm);

        if (dentroDelLimite) {
            System.out.println(
                    " Existe un camino entre " + (String)origen + " y " + (String)destino + " que no supera los " + maxKm + " km.");
        } else {
            System.out.println(" No existe un camino entre " + (String)origen + " y " +(String) destino
                    + " que cumpla con la distancia máxima especificada.");
        }
    }

    private void mostrarMenu() {
        System.out.println("\n--- Gestión de Rutas ---");
        System.out.println("1. Alta ruta");//
        System.out.println("2. Baja ruta");//
        System.out.println("3. Listar rutas desde ciudad");//
        System.out.println("4. Consultar conexión");//
        System.out.println("5. Camino con menos ciudades");//
        System.out.println("6. Camino de menor distancia");//
        System.out.println("7. Caminos que pasan por ciudad intermedia");//
        System.out.println("8. Verificar distancia máxima");//

        System.out.println("9. Verificar camino perfecto");//
        System.out.println("10. VerificarViajeCapacidad");
        System.out.println("11. Mostrar estructura completa del grafo");
        System.out.println("0. Volver");
    }

    private void mostrarGrafoCompleto() {
        grafo.mostrarGrafoCompleto();
    }

    private void altaRuta() {
        System.out.print("Ciudad origen (CP): ");
        Comparable origen = sc.nextLine();
        System.out.print("Ciudad destino (CP): ");
        Comparable destino = sc.nextLine();
        System.out.print("Peso (distancia/pedidos): ");
        double peso = sc.nextDouble();
        sc.nextLine();

        boolean exito = grafo.insertarRuta(origen, destino, peso);
        System.out.println(exito ? "Ruta agregada/actualizada." : "Error: ciudad no encontrada.");
        if (exito) {
            logger.log("Se creó ruta: " + origen + " -> " + destino + " (" + peso + " km)");
        }

    }

    private void bajaRuta() {
        System.out.print("Ciudad origen (CP): ");
        Comparable origen = sc.nextLine();
        System.out.print("Ciudad destino (CP): ");
        Comparable destino = sc.nextLine();

        boolean exito = grafo.eliminarRuta(origen, destino);
        System.out.println(exito ? "Ruta eliminada." : "No existe esa ruta.");
        if (exito) {
            logger.log("Se eliminó ruta: " + origen + " -> " + destino);
        }
    }

    private void listarRutas() {
        System.out.print("Ciudad origen (CP): ");
        Comparable origen = sc.nextLine();
        Lista rutas = grafo.listarRutasDesde(origen);
        System.out.println("Rutas: " + rutas);
    }

    private void consultarConexion() {
        System.out.print("Ciudad origen (CP): ");
        String origen = sc.nextLine();
        System.out.print("Ciudad destino (CP): ");
        String destino = sc.nextLine();

        boolean existe = grafo.existeRuta(origen, destino);
        System.out.println(existe ? "Hay conexión." : "No existe conexión directa.");
    }
}
