package grafo;
import clasesComplemento.*;
import lineales.*;
import java.util.HashMap;
import java.util.HashSet;


//debe funcionar para object utilizando equals y compareTo
public class GrafoCiudades {
    protected NodoVert inicio;

    public GrafoCiudades() {
        inicio = null;
    }
    public NodoVert inicio() {
        return inicio;
    }
    // ======================
    // INSERTAR CIUDAD (vértice) PREGUNTAR!!
    // ======================

    public boolean insertarCiudad(Ciudad c) {
        boolean insertado = false; // Variable de estado

        if (buscarVertice(c.getCodigoPostal()) == null) {
            inicio = new NodoVert(c, inicio); // Insertar al principio
            insertado = true; // Marcar como insertado
        }

        return insertado;
    }

    public boolean esVacio() {
        return (inicio == null);
    }

    // ======================
    // CAMINOS MAS LARGOS O POR MAS CIUDADES
    // ======================

    public Lista caminoMenosCiudades(Comparable origen, Comparable destino)  {
        // encuentra el camino que tenga menos nodos y lo devuelve
        // utiliza una busqueda por anchura para encontrar el primer camino que llegue
        // al destino

        Lista camino = new Lista();
        NodoVert ini = this.buscarVertice(origen);
        NodoVert fin = this.buscarVertice(destino);

        HashMap<Comparable, NodoVert> visitados = new HashMap<>();

        boolean encontrado = false;

        Cola cola = new Cola();

        if (ini != null && fin != null) {
            cola.poner(ini);
            visitados.put(ini.getCodigo(), null);

            while (!cola.esVacia() && !encontrado) {

                NodoVert t = (NodoVert) cola.obtenerFrente();
                cola.sacar();

                if (t.getCodigo().equals(destino)) {
                    encontrado = true;
                } else {

                    NodoAdy ad = t.getPrimerAdy();

                    while (ad != null) {
                        NodoVert v = ad.getVerticeDestino();
                        if (!visitados.containsKey(v.getCodigo())) { // no revisa si el vecino es un nodo en el mismo
                                                                     // nivel o que ya fue visitado
                            cola.poner(v);
                            visitados.put(v.getCodigo(), t); // mijagas de camino
                        }
                        ad = ad.getSigAdy(); // visito los demas adyacentes del primer nodo
                    }
                }

            }

            if (encontrado) {
                String cam = (String) destino;
                while (cam != null) {
                    camino.insertar(visitados.get(cam).getDato(), 1);
                    cam = (String) visitados.get(cam).getCodigo();

                }
            }

        }

        return camino;
    }

    // ======================
    // PEDIDOS DE A HACIA B
    // ======================
    // METODO PARA VERIFICAR LA EXISTENCIA DE UNA RUTA DESDE A --> B y devolver el valor del camino minimo

    public Double caminoValido(Comparable or, Comparable des) {
        Double valor = 0.0;

        Lista ls = caminoMasCortoKm(or, des);
        int i = 1;
        if(!ls.esVacia()){
        while (ls.longitud() > i) {
            Comparable c = (Comparable) ls.recuperar(i);
            Comparable b = (Comparable) ls.recuperar(i + 1);
            valor += existeRutaValor(c, b);
            i = i + 1;
        }
        }

        return valor;
    }
    //auxiliar de camino valido
    private Double existeRutaValor(Comparable origen, Comparable destino) {

        boolean encontrado = false; // Variable bandera
        Double valor = 0.0;
        NodoVert vOrigen = buscarVertice(origen);
        if (vOrigen != null) {
            NodoAdy ady = vOrigen.getPrimerAdy();
            while (ady != null && !encontrado) { // Corta cuando se encuentra
                if (ady.getVerticeDestino().compareTo(destino)==0) {
                    valor += ady.getPeso();
                    encontrado = true; // Marcar como encontrado
                }
                ady = ady.getSigAdy();
            }
        }

        return valor;
    }



    
    public boolean insertarRuta(Comparable origen, Comparable destino, Double peso) {
        boolean exito = false;

        NodoVert vOrigen = buscarVertice(origen);
        NodoVert vDestino = buscarVertice(destino);

        if (vOrigen != null && vDestino != null) {

            agregarAdyacencia(vOrigen, vDestino, peso);
            agregarAdyacencia(vDestino, vOrigen, peso);
            exito = true;
        }

        return exito;
    }

      //Caminos 

    // implementacion del algoritmo Dijkstra
    public Lista caminoMasCortoKm(Comparable origen, Comparable destino) {

        Lista camino = new Lista();

        NodoVert vOrigen = buscarVertice(origen);
        NodoVert vDestino = buscarVertice(destino);

        if (vOrigen != null && vDestino != null) {

            HashMap<Comparable, Double> distancia = new HashMap<>(); // Para ir guardando el camino y distancias de cada
            // nodo
            HashMap<Comparable, Comparable> padre = new HashMap<>(); // De donde proviene cada nodo
            HashSet<Comparable> visitados = new HashSet<>(); // Para saber los nodos visitados

            NodoVert aux = inicio;
            // Se marcan todos los nodos con una distancia de infinito menos el origen que
            // empieza en cero
            while (aux != null) {

                distancia.put((String) aux.getCodigo(), Double.POSITIVE_INFINITY);

                aux = aux.getSigVert();
            }

            distancia.put(origen, 0.0); // El origen queda marcado con 0

            // OBTENGO EL NODO MINIMO NO VISITADO

            Comparable actual = obtenerMinimoNoVisitado(distancia, visitados);

            while (actual != null) {

                visitados.add(actual); // Se marca el nodo como visitado

                NodoVert vertActual = buscarVertice(actual); // busco los vecinos del nodo actual
                NodoAdy ady = vertActual.getPrimerAdy();

                while (ady != null) { // Se ven los nodos adyacentes al visitado y se actualiza su distancia, si el
                                      // camino se topa con un nodo que ya
                                      // se marco la distancia, si la nueva distancia es menor se debe actualizar el
                                      // padre y la distancia ya que se encontro un mejor camino

                    NodoVert vecino = ady.getVerticeDestino();
                    Comparable codVecino = vecino.getCodigo();
                    double nuevaDist = distancia.get(actual) + ady.getPeso();

                    if (nuevaDist < distancia.get(codVecino)) {
                        // la primera pasada marca las distancias

                        distancia.put(codVecino, nuevaDist);
                        padre.put(codVecino, actual); // Se actualiza en caso de tener una distancia menor para guardar
                                                      // el camino
                    }
                    ady = ady.getSigAdy(); // se revisan todos los vecinos del nodo actual
                }

                actual = obtenerMinimoNoVisitado(distancia, visitados); // actual pasa a ser el vecino con menos
                                                                        // distancia registrada
            }

            if (distancia.get(destino) != Double.POSITIVE_INFINITY) { // devuelve el camino encontrado
                Comparable nodo = destino;
                while (nodo != null) {
                    camino.insertar(nodo, 1);
                    nodo = padre.get(nodo);
                }
            }
        }
        return camino;
        // se marcan todos los nodos //calcula distancia de todos los nodos
        // se revisa su distancia
        // se repite desde el nodo visitado de menor distancia
        // repetir
    }
    //AUXILIAR DE DIJKSTRA
    private Comparable obtenerMinimoNoVisitado(HashMap<Comparable, Double> dist, HashSet<Comparable> visitados) { // Metodo
                                                                                                                  // que
        // actualiza las
        // distancias de
        // los vecinos
        // Si encuentra una mejor distancia a un vecino por ejemplo B y C van visitando
        // a D pero primero fue por B y dio 10 ahora cuando vea el camino de C da 5
        // entonces devuelve C
        Comparable minNodo = null;
        double minValor = Double.POSITIVE_INFINITY;

        for (Comparable codigo : dist.keySet()) {
            if (!visitados.contains(codigo)) {
                double valor = dist.get(codigo);
                if (valor < minValor) {
                    minValor = valor;
                    minNodo = codigo;
                }
            }
        }
        return minNodo;
    }




    // METODO QUE DEVUELVE TODOS LOS CAMINOS QUE PASAN DE A --> B CON C INTERMEDIO -
    // SIN REPETIR CIUDADES

    public Lista caminosQuePasanPorC(Object a, Object b, Object c) {
        Lista caminosEncontrados = new Lista();

        NodoVert vA = buscarVertice(a);
        NodoVert vB = buscarVertice(b);
        NodoVert vC = buscarVertice(c);

        if (vA != null && vB != null && vC != null) {
            Lista caminoActual = new Lista();
            buscarCaminosDFS(vA, vB, vC, caminoActual, caminosEncontrados);
        }

        return caminosEncontrados;
    }

    private void buscarCaminosDFS(NodoVert actual, Comparable destino, Comparable intermedia,
        Lista caminoActual, Lista caminosEncontrados) {

        Comparable codActual = actual.getCodigo();

        // Evitar ciclos: si ya está en el camino, no seguir
        if (caminoActual.localizar(codActual) < 0) {

        //camino actual
        caminoActual.insertar(codActual, caminoActual.longitud() + 1);

        // Si llegamos al destino
        if (codActual.equals(destino)) {
            // Verificar si el camino contiene la ciudad intermedia
            if (caminoActual.localizar(intermedia) > 0) {
                caminosEncontrados.insertar(caminoActual.clone(), caminosEncontrados.longitud() + 1);
            }
        } else {
            // Seguir explorando adyacencias
            NodoAdy ady = actual.getPrimerAdy();
            while (ady != null) {
                buscarCaminosDFS(  ady.getVerticeDestino(),destino,intermedia,caminoActual,caminosEncontrados);
                ady = ady.getSigAdy();
            }
        }
        // Backtracking: eliminar el nodo actual antes de volver
        int pos = caminoActual.localizar(codActual);
        if (pos > 0) {
            caminoActual.eliminar(pos);
        }
    }
    }



    // METODO PARA VERIFICAR LA DISTANCIA DE UN VIAJE DEPENDIENDO  KM DADOS ES
    // POSIBLE // hecho en profundidad
    public boolean verificarDistanciaMaxima(Comparable origen, Comparable destino, double maxKm) { 
        
        NodoVert nodo = buscarVertice(origen);
        boolean exito =false;

        if(nodo!=null){
             Lista visitados = new Lista();
             Comparable aux = nodo.getCodigo();
             visitados.insertar(aux,1);
              exito = this.verificarDistanciaMaximaDFS(nodo, destino, maxKm,0,visitados);

        }
       
        return exito;
    }

    //Correccion ahora utiliza DFS

    public boolean verificarDistanciaMaximaDFS(NodoVert n,Comparable d,double maxKm,double recorrido,Lista visitados){
        System.out.println("lista visitados");
        System.out.println(visitados.toString());
       
        Object aux = n.getDato();
        NodoAdy nodo = n.getPrimerAdy();
        
        boolean exito = false;

        if(aux.equals(d)){
        if(recorrido <= maxKm){
             exito=true;
            }
        }else{

            while(nodo!=null){

            Double peso = nodo.getPeso();

            Double distancia = peso + recorrido;
        
            NodoVert actual = nodo.getVerticeDestino();
            Comparable clave =actual.getCodigo();

            

            visitados.insertar(clave,visitados.longitud()+1);


            if(recorrido <=maxKm){
            exito = verificarDistanciaMaximaDFS(actual, d, maxKm, distancia, visitados);
            }

            nodo = nodo.getSigAdy();
        }
        visitados.eliminar(visitados.longitud());
       
        }

    return exito;}

   
    

    public boolean eliminarRuta(Comparable origen, Comparable destino) {
        boolean exito = false;

        NodoVert vOrigen = buscarVertice(origen);
        NodoVert vDestino = buscarVertice(destino);

        if (vOrigen != null && vDestino != null) {

            eliminarAdyacencia(vOrigen, vDestino);
            eliminarAdyacencia(vDestino, vOrigen);

            exito = true; // existen y se puede borrar la ruta
        }

        return exito;
    }

    private boolean eliminarAdyacencia(NodoVert origen, NodoVert destino) {
        NodoAdy actual = origen.getPrimerAdy();
        NodoAdy anterior = null;
        boolean eliminado = false;

        while (actual != null && !eliminado) {
            if (actual.getVerticeDestino() == destino) {
                if (anterior == null) {
                    origen.setPrimerAdy(actual.getSigAdy());
                } else {
                    anterior.setSigAdy(actual.getSigAdy());
                }
                eliminado = true;
            } else {
                anterior = actual;
            }
            actual = actual.getSigAdy();
        }

        return eliminado;
    }

    private void agregarAdyacencia(NodoVert origen, NodoVert destino, Double peso) {

        boolean encontrada = false;
        NodoAdy adyActual = origen.getPrimerAdy();

        while (adyActual != null && !encontrada) {
            if (adyActual.getVerticeDestino() == destino) {
                adyActual.setPeso(peso); // ya existía, actualizamos peso
                encontrada = true;
            }
            adyActual = adyActual.getSigAdy();
        }

        if (!encontrada) {
            NodoAdy nueva = new NodoAdy(destino, origen.getPrimerAdy(), peso);
            origen.setPrimerAdy(nueva);
        }
    }

    private NodoVert buscarVertice(Comparable codigo) {
        NodoVert aux = inicio;
        NodoVert encontrado = null;

        while (aux != null && encontrado == null) {
            if (aux.compareTo(codigo) == 0) {
                encontrado = aux; // Almacenamos el nodo encontrado
            }
            aux = aux.getSigVert();
        }
        return encontrado;
    }

   

// LISTA TODOS LOS VERTICES 

    public Lista listarCiudades() {
        Lista lista = new Lista();
        NodoVert aux = inicio;
        while (aux != null) {
            lista.insertar(aux.getDato(), lista.longitud() + 1);
            aux = aux.getSigVert();
        }
        return lista;
    }

    // ======================
    // LISTA TODAS LAS RUTAS DESDE UNA CIUDAD
    // ======================

    public Lista listarRutasDesde(Comparable codigoOrigen) {
        Lista lista = new Lista();
        NodoVert v = buscarVertice(codigoOrigen);
        if (v != null) {
            NodoAdy ady = v.getPrimerAdy();
            while (ady != null) {
                String ruta = v.getCodigo() + " -> " + ady.getVerticeDestino().getCodigo()
                        + " (peso=" + ady.getPeso() + ")";
                lista.insertar(ruta, lista.longitud() + 1);
                ady = ady.getSigAdy();
            }
        }
        return lista;
    }

    // ======================
    // VERIFICAR EXISTENCIA DE RUTA
    // ======================

    public boolean existeRuta(Comparable origen, Comparable destino) {

        boolean encontrado = false; // Variable bandera
        
        NodoVert vOrigen = buscarVertice(origen);

        if (vOrigen != null) {
            
            NodoAdy ady = vOrigen.getPrimerAdy();

            while (ady != null && !encontrado) { // Corta cuando se encuentra
                if (ady.getVerticeDestino().compareTo(destino)==0) {
                    encontrado = true; // Marcar como encontrado
                }
                ady = ady.getSigAdy();
            }
        }

        return encontrado;
    }

    //log 
    public String volcarEstadoCompleto() {
        StringBuilder sb = new StringBuilder();
        NodoVert actual = inicio;

        while (actual != null) {
            sb.append("Ciudad: ").append(((Ciudad) actual.getDato()).getCodigoPostal())
                    .append(" - ").append(((Ciudad) actual.getDato()).getNombre()).append("\n");

            NodoAdy ady = actual.getPrimerAdy();
            while (ady != null) {
                sb.append("  -> ")
                        .append(((Ciudad) ady.getVerticeDestino().getDato()).getCodigoPostal())
                        .append(" (")
                        .append(ady.getPeso())
                        .append(" km)\n");
                ady = ady.getSigAdy();
            }

            actual = actual.getSigVert();
            sb.append("\n");
        }
        return sb.toString();
    }


    public void mostrarGrafoCompleto() {
        if (inicio != null) { 
        System.out.println("\nESTRUCTURA DEL GRAFO:");
        NodoVert actual = inicio;

        while (actual != null) {

            System.out.print(((Ciudad) actual.getDato()).getCodigoPostal() + " ["
                    + ((Ciudad) actual.getDato()).getNombre() + "]: ");

            NodoAdy ady = actual.getPrimerAdy();
            while (ady != null) {
                System.out.print("->" + ((Ciudad) (ady.getVerticeDestino().getDato())).getCodigoPostal() +
                        "(" + ady.getPeso() + "km)");
                ady = ady.getSigAdy();
            }

            System.out.println();
            actual = actual.getSigVert();
        }
    }else{
             System.out.println("Grafo vacío");
    }
}

}
