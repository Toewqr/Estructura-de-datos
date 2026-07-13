package clasesComplemento;
import java.util.HashMap;
import lineales.*;

//ciudad comparabale sobreescribir metodos equals / compare to
public class Ciudad implements Comparable {

    private String codigoPostal;
    private String nombre;
    private String provincia;
    private HashMap<Comparable, Lista> pedidosSalientes;

    public Ciudad(String codigoPostal, String nombre, String provincia) {     
        this.codigoPostal = codigoPostal;
        this.nombre = nombre.trim();
        this.provincia = provincia.trim();
        this.pedidosSalientes = new HashMap<>();
    }

    public Double espacioRequeridoC (Comparable cp){

        Double espacio=0.0;
        if(!pedidosSalientes.get(cp).esVacia() || pedidosSalientes.get(cp)!=null){
            Lista ls = pedidosSalientes.get(cp);
            int pos = 0;
            while(ls.longitud()>= pos){
                if(!((Pedido) ls.recuperar(pos)).getTerminado()){
                    espacio += ((Pedido) ls.recuperar(pos)).getMetrosCubicos();
                }
                pos++;
            }
        }else{
            espacio=-1.0;
        }
        return espacio;
    }

    public void agregarPedido(String destino, Pedido pedido) {
        Lista lista = pedidosSalientes.get(destino);

        if (lista == null) {
            lista = new Lista();
            pedidosSalientes.put(destino, lista);
        }
        lista.insertar(pedido, lista.longitud() + 1);
    }

    public HashMap<Comparable, Lista> getPedidosPorDestino() {
        return pedidosSalientes;
    }

    public Lista obtenerPedidosDirectosADestino(Comparable destino) {
    Lista pedidosActivos = new Lista();
    Lista pedidosDestino = pedidosSalientes.get(destino);
    
    if (pedidosDestino != null) {
        for (int i = 1; i <= pedidosDestino.longitud(); i++) {
            Pedido p = (Pedido) pedidosDestino.recuperar(i);
            if (!p.getTerminado()) { // Solo pedidos activos
                pedidosActivos.insertar(p, pedidosActivos.longitud() + 1);
            }
        }
    }
    return pedidosActivos;
}
    
    // Getters
    public String getCodigoPostal() { return (String)codigoPostal; }
    public String getNombre() { return nombre; }
    public String getProvincia() { return provincia; }
    
    // Setters
    public void setNombre(String nombre) { 
        this.nombre = nombre.trim(); 
    }
    
    public void setProvincia(String provincia) { 
        this.provincia = provincia.trim(); 
    }
 
    @Override
    public String toString() {
        return nombre + " (" + codigoPostal + ")";
    }
     public Lista obtenerPedidosADestino(String codigoPostalDestino) {
        Lista pedidosADestino = new Lista();
        Lista pedidosDestino = pedidosSalientes.get(codigoPostalDestino);
        
        if (pedidosDestino != null && !pedidosDestino.esVacia()) {
            // Copiamos la lista de pedidos para ese destino
            for (int i = 1; i <= pedidosDestino.longitud(); i++) {
                pedidosADestino.insertar(pedidosDestino.recuperar(i), pedidosADestino.longitud() + 1);
            }
        }
        
        return pedidosADestino;
    }

      public Lista obtenerTodosPedidosSalientes() {
        Lista todosPedidos = new Lista();
        int posicion = 1;
        
        for (Lista listaDestino : pedidosSalientes.values()) {
            for (int i = 1; i <= listaDestino.longitud(); i++) {
                Pedido pedido = (Pedido) listaDestino.recuperar(i);
                
                // Insertar manteniendo orden cronológico inverso (más recientes primero)
                int j = 1;
                boolean insertado = false;
                
                while (j <= todosPedidos.longitud() && !insertado) {
                    Pedido pedidoExistente = (Pedido) todosPedidos.recuperar(j);
                    
                    // Comparar fechas: si el nuevo pedido es más reciente, insertar antes
                    if (pedido.getFecha().isAfter(pedidoExistente.getFecha())) {
                        todosPedidos.insertar(pedido, j);
                        insertado = true;
                    }
                    j++;
                }
                
                // Si no se insertó en el bucle, añadir al final
                if (!insertado) {
                    todosPedidos.insertar(pedido, todosPedidos.longitud() + 1);
                }
                posicion++;
            }
        }
        
        return todosPedidos;
    }
      @Override
      public int compareTo(Object otra) {
        Ciudad c = (Ciudad)otra;
         return this.codigoPostal.compareTo(c.codigoPostal);
      }

      public boolean equals(Object obj) {
        if (!(obj instanceof Ciudad)){
            return false;
        } 
        return this.codigoPostal.equals(((Ciudad) obj).codigoPostal);
    }
}