package arbol;


public class NodoAVL {
    private Comparable dato; // comparable
    private String clave;
    private int altura;
    private NodoAVL derecho;
    private NodoAVL izquierdo;

    public NodoAVL(Comparable valor, NodoAVL nodoDerecho, NodoAVL nodoIzquierdo) {
        this.altura = 1;
        this.dato = valor;
        this.derecho = nodoDerecho;
        this.izquierdo = nodoIzquierdo;
    }

    public NodoAVL getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoAVL nodo) {
        this.derecho = nodo;
    }

    public NodoAVL getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoAVL nodo) {
        this.izquierdo = nodo;
    }

    public Comparable getDato() {
        return dato;
    }

    public String getClave(){
        return clave;
    }

    public void setDato(Comparable elem) {
        this.dato = elem;
    }

    public int getAltura() {
        return altura;
    }

    public void recalcularAltura() {
        int alturaIzq = (izquierdo != null) ? izquierdo.getAltura() : 0;
        int alturaDer = (derecho != null) ? derecho.getAltura() : 0;
        altura = Math.max(alturaIzq, alturaDer) + 1;
    }
    
}
