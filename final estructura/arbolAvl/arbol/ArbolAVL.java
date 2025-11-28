package arbol;

import lineales.*;


public class ArbolAVL {

    private NodoAVL raiz;

    public ArbolAVL() {
        this.raiz = null;

    }

    public NodoAVL getRaiz() {
        return raiz;
    }

    // ========================
    // PERTENECE / BUSCAR
    // ========================
    public boolean pertenece(Comparable codigoPostal) {
        return perteneceRecursivo(this.raiz, codigoPostal);
    }

    private boolean perteneceRecursivo(NodoAVL n, Comparable codigo) {
        boolean resultado = false;
        if (n != null) {

            int cmp = (n.getDato()).compareTo(codigo);
            if (cmp == 0) {
                resultado = true;
            } else {

                if (cmp > 0) {
                    resultado = perteneceRecursivo(n.getIzquierdo(), codigo);
                } else {
                    resultado = perteneceRecursivo(n.getDerecho(), codigo);

                }
            }
        }
        return resultado;
    }

    public Object buscar(String codigoPostal) {
        return buscarRecursivo(this.raiz, codigoPostal);
    }

    private Object buscarRecursivo(NodoAVL n, String codigo) {
        Object resultado = null;
        if (n != null) {
            
            int cmp = (n.getClave()).compareTo(codigo);

            if (cmp == 0) {
                resultado = n.getDato();
            } else {
                resultado = (cmp > 0)
                        ? buscarRecursivo(n.getIzquierdo(), codigo)
                        : buscarRecursivo(n.getDerecho(), codigo);
            }
        }
        return resultado;
    }

    // ========================
    // INSERTAR
    // ========================

    public boolean insertar(Comparable valor) {
        if (pertenece((valor)))
            return false;
        raiz = insertarAux(raiz, valor);
        return true;
    }

    private NodoAVL insertarAux(NodoAVL nodo, Comparable valor) {
        if (nodo == null) {
            return new NodoAVL(valor, null, null);
        }

        int cmp= (nodo.getDato()).compareTo(valor);

        if (cmp < 0) {
            nodo.setIzquierdo(insertarAux(nodo.getIzquierdo(), valor));
        } else if (cmp > 0) {
            nodo.setDerecho(insertarAux(nodo.getDerecho(), valor));
        }
        nodo.recalcularAltura();
        return balancear(nodo);
    }

    // ========================
    // ELIMINAR
    // ========================

    public boolean eliminar(Comparable elem) {
        boolean exito;
        exito = eliminarAux(this.raiz, null, elem);
        return exito;
    }

    private boolean eliminarAux(NodoAVL nodo, NodoAVL padre, Comparable elem) {
        boolean exito = false;

        if (nodo != null) {

            if (elem.compareTo(nodo.getDato()) == 0) {
                // Caso eliminar raiz padre null
                // caso de un nodo interno
                if (nodo.getDerecho() == null && nodo.getIzquierdo() == null) {
                    // es una hoja
                    casoHoja(nodo, padre);
                } else if (nodo.getIzquierdo() == null) {
                    // tengo un hijo derecho del nodo a eliminar
                    casoHijoDerecho(nodo, padre);
                } else if (nodo.getDerecho() == null) {
                    // tengo un hijo izquierdo del nodo a eliminar
                    casoHijoIzquierdo(nodo, padre);
                } else {  
                    // Se tiene dos hijos
                    // Se busca un remplazo para el nodo a eliminar que sea el elemento minimo mas
                    // grande para poner en el nodo y luego se elimina este
                    NodoAVL t = maximoMinimo(nodo.getIzquierdo());
                    nodo.setDato(t.getDato());
                    eliminarAux(nodo.getIzquierdo(), nodo, t.getDato());

                }
                
                exito = true;

            } else {
                // se realiza la buscada en el subArbol dependiendo si es mayor o menor al nodo
                // actual

                if ((nodo.getDato().compareTo(elem))< 0) {
                    exito = eliminarAux(nodo.getIzquierdo(), nodo, elem);
                } else {
                    exito = eliminarAux(nodo.getDerecho(), nodo, elem);
                }
            }

            if (exito) {
                nodo.recalcularAltura();
                balancear(nodo);
            }
        }

        return exito;
    }

    private void casoHoja(NodoAVL nodo, NodoAVL padre) {
        if (padre == null) {
            // el arbol solo tiene un elemento
            this.raiz = null;
        } else {
            if (nodo.getDato().compareTo(padre.getDato()) < 0) {
                padre.setIzquierdo(null);
            } else {
                padre.setDerecho(null);
            }
        }
    }

    private void casoHijoDerecho(NodoAVL nodo, NodoAVL padre) {

        if (padre == null) {
            this.raiz = nodo.getDerecho();
        } else {

            if (nodo.compareTo(padre) < 0) {
                // comparo para saber si el nodo a eliminar es el hijo izquierdo o derecho de
                // padre
                // si el nodo es el hijo izquierdo, y como el tiene un hijo derecho (hijjo
                // izquierdo es nulo) entonces debo setear el padre HI con el hijo derecho del
                // nodo
                padre.setIzquierdo(nodo.getDerecho());
            } else {
                // si el nodo es el hijo derecho, y como tiene un HD debo setear a padre HD con
                // el hijo derecho de nodo hijo de padre
                padre.setDerecho(nodo.getDerecho());
            }
        }
    }

    private void casoHijoIzquierdo(NodoAVL nodo, NodoAVL padre) {

        if (padre == null) {
            this.raiz = nodo.getIzquierdo();
        } else {
            if (nodo.compareTo(padre) < 0) {
                // comparo para saber donde va el nodo en padre (si es HI O HD)
                // Si el nodo es HI
                padre.setIzquierdo(nodo.getIzquierdo());
            } else {
                // si el nodo es HD
                padre.setDerecho(nodo.getIzquierdo());
            }
        }
    }

    // ========================
    // BALANCEO
    // ========================
    private NodoAVL balancear(NodoAVL nodo) {
        NodoAVL nodoBalanceado = nodo;

        if (nodo != null) {

            int balance = obtenerBalance(nodo);

            if (balance > 1) { // desbalance izquierdo 2

                if (obtenerBalance(nodo.getIzquierdo()) >= 0) {
                    nodoBalanceado = rotacionDerecha(nodo);

                } else {
                    // rotacion doble izquierda - derecha
                    nodo.setIzquierdo(rotacionIzquierda(nodo.getIzquierdo()));

                    nodoBalanceado = rotacionDerecha(nodo);

                }
            } else if (balance < -1) { // demasiado pesado a la derecha -2
                if (obtenerBalance(nodo.getDerecho()) <= 0) {
                    nodoBalanceado = rotacionIzquierda(nodo);

                } else {
                    nodo.setDerecho(rotacionDerecha(nodo.getDerecho()));

                    nodoBalanceado = rotacionIzquierda(nodo);

                }
            }
        }

        return nodoBalanceado;
    }

    private int obtenerBalance(NodoAVL nodo) {
        int balance = 0;
        if (nodo != null) {
            int alturaIzq = (nodo.getIzquierdo() != null) ? nodo.getIzquierdo().getAltura() : 0;
            int alturaDer = (nodo.getDerecho() != null) ? nodo.getDerecho().getAltura() : 0;
            balance = alturaIzq - alturaDer;
        }
        return balance;
    }

    

    private NodoAVL rotacionIzquierda(NodoAVL nodo) {
        NodoAVL nuevoPadre = nodo;
        if (nodo != null) {
            NodoAVL hijoDer = nodo.getDerecho();
            if (hijoDer != null) {
                NodoAVL temp = hijoDer.getIzquierdo();
                hijoDer.setIzquierdo(nodo);
                nodo.setDerecho(temp);
                nodo.recalcularAltura();
                hijoDer.recalcularAltura();
                nuevoPadre = hijoDer;
            }
        }
        return nuevoPadre;
    }

    private NodoAVL rotacionDerecha(NodoAVL nodo) {
        NodoAVL nuevoPadre = nodo;
        if (nodo != null) {
            NodoAVL hijoIzq = nodo.getIzquierdo();
            if (hijoIzq != null) {
                NodoAVL temp = hijoIzq.getDerecho();
                hijoIzq.setDerecho(nodo);
                nodo.setIzquierdo(temp);
                nodo.recalcularAltura();
                hijoIzq.recalcularAltura();
                nuevoPadre = hijoIzq;
            }
        }
        return nuevoPadre;
    }

    // ========================
    // LISTAR
    // ========================
    public Lista listarCiudades() {
        Lista lista = new Lista();
        listarAux(raiz, lista);
        return lista;
    }


    ///Metodo auxiliar para consultas
    public Lista ciudadesPorPrefijo(String prefijo) {
        Lista resultados = new Lista();
        ciudadesPorPrefijoRec(raiz, prefijo, resultados);
        return resultados;
    }

    private void ciudadesPorPrefijoRec(NodoAVL n, String prefijo, Lista ls) {
        if (n != null) {
            Ciudad aux = (Ciudad) n.getCiudad();
            String cp = aux.getCodigoPostal();

            if (cp.startsWith(prefijo)) {
                ls.insertar(n.getCiudad(), ls.longitud() + 1);
            }
            ciudadesPorPrefijoRec(n.getIzquierdo(), prefijo, ls);
            ciudadesPorPrefijoRec(n.getDerecho(), prefijo, ls);
        }
    }



    private void listarAux(NodoAVL n, Lista ls) {
        if (n != null) {
            listarAux(n.getIzquierdo(), ls);
            ls.insertar(n.getCiudad(), ls.longitud() + 1);
            listarAux(n.getDerecho(), ls);
        }
    }

    // ========================
    // UTILIDAD
    // ========================
    public boolean esVacia() {
        return raiz == null;
    }

 

    private NodoAVL maximoMinimo(NodoAVL nodo) {
        NodoAVL actual = nodo;
        if (nodo != null) {
            while (actual.getDerecho() != null) {
                actual = actual.getDerecho();
            }
        }
        return actual;
    }

    public void mostrarArbolBalanceado() {
        if (raiz == null) {
            System.out.println("Árbol vacío");
            return;
        }
        System.out.println("\nESTRUCTURA DEL ÁRBOL AVL:");
        System.out.println("(Raíz: " + ((Ciudad) raiz.getCiudad()).getCodigoPostal() + ")");
        mostrarArbolRec(raiz, 0);
    }

    private void mostrarArbolRec(NodoAVL nodo, int nivel) {
        if (nodo != null) {
            mostrarArbolRec(nodo.getDerecho(), nivel + 1);

            // Imprimir nodo con indentación según nivel
            System.out.println("  ".repeat(nivel * 4) + "|-- " +
                    ((Ciudad) nodo.getCiudad()).getCodigoPostal() +
                    " (" + nodo.getAltura() + ")");

            mostrarArbolRec(nodo.getIzquierdo(), nivel + 1);
        }
    }
}