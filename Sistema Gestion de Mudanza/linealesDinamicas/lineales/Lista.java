package lineales;

public class Lista {
    private Nodo cabecera;

    public Lista() {
        cabecera = null;
    }

    public boolean insertar(Object unObjeto, int posicion) {
        if (posicion < 1 || posicion > this.longitud() + 1)
            return false;

        if (posicion == 1) {
            cabecera = new Nodo(unObjeto, cabecera);
        } else {
            Nodo aux = cabecera;
            int i = 1;
            while (i < posicion - 1) {
                aux = aux.getEnlace();
                i++;
            }
            Nodo nuevo = new Nodo(unObjeto, aux.getEnlace());
            aux.setEnlace(nuevo);
        }
        return true;
    }

    public int longitud() {
        int longitud = 0;
        Nodo aux = cabecera;
        while (aux != null) {
            aux = aux.getEnlace();
            longitud++;
        }
        return longitud;
    }

    public boolean eliminar(int pos) {
        if (pos < 1 || pos > this.longitud())
            return false;

        if (pos == 1) {
            cabecera = cabecera.getEnlace();
        } else {
            Nodo aux = cabecera;
            int i = 1;
            while (i < pos - 1) {
                aux = aux.getEnlace();
                i++;
            }
            aux.setEnlace(aux.getEnlace().getEnlace());
        }
        return true;
    }

    public Object recuperar(int pos) {
        if (pos < 1 || pos > this.longitud())
            return null;
        Nodo aux = cabecera;
        int i = 1;
        while (i < pos) {
            aux = aux.getEnlace();
            i++;
        }
        return aux.getElem();
    }

    public int localizar(Object unObjeto) {
        Nodo aux = cabecera;
        int i = 1;
        while (aux != null) {
            if (unObjeto.equals(aux.getElem())){
                return i;
            }
                
            aux = aux.getEnlace();
            i++;
        }
        return -1;
    }

    public boolean esVacia() {
        return cabecera == null;
    }

    public void vaciar() {
        cabecera = null;
    }

    public Lista clone() {
        Lista nueva = new Lista();
        if (!esVacia())
            nueva.cabecera = auxClone(cabecera);
        return nueva;
    }

    private Nodo auxClone(Nodo n) {
        if (n == null)
            return null;
        return new Nodo(n.getElem(), auxClone(n.getEnlace()));
    }

    public String toString() {
        Nodo aux = cabecera;
        StringBuilder sb = new StringBuilder("[");
        while (aux != null) {
            sb.append(aux.getElem() != null ? aux.getElem().toString() : " ");
            if (aux.getEnlace() != null)
                sb.append(", ");
            aux = aux.getEnlace();
        }
        sb.append("]");
        return sb.toString();
    }
    

}
