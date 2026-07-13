package grafo;
public class NodoAdy {
    private NodoVert verticeDestino; // nodo al cual se hace referencia
    private NodoAdy sigAdy; // Siguiente adyacente
    private Double peso; // distancia de la rutas

    public NodoAdy(NodoVert destino, NodoAdy siguiente, Double peso) {
        this.verticeDestino = destino;
        this.sigAdy = siguiente;
        this.peso = peso;
    }

    public NodoVert getVerticeDestino() {
         return verticeDestino; }
    
     public NodoAdy getSigAdy() { 
        return sigAdy; 
    }
    
    public void setSigAdy(NodoAdy sig) {
         this.sigAdy = sig; }

    public Double getPeso() {
         return peso; }
    
    public void setPeso(Double p) {
         this.peso = p; }
}
