package grafo;

public class NodoVert  {
    private Object dato;       // Dato principal
    private NodoVert sigVert;    // Siguiente vértice en la lista
    private NodoAdy primerAdy;   // Lista de adyacencias (rutas salientes)

    public NodoVert(Object ciudad, NodoVert sigVert) {
        this.dato = ciudad;
        this.sigVert = sigVert;
        this.primerAdy = null;
    }

    public Object getDato() { return dato; }

    public NodoVert getSigVert() { return sigVert; }
    public void setSigVert(NodoVert sig) { this.sigVert = sig; }

    public NodoAdy getPrimerAdy() { return primerAdy; }
    public void setPrimerAdy(NodoAdy ady) { this.primerAdy = ady; }
    
    
    
   
}
