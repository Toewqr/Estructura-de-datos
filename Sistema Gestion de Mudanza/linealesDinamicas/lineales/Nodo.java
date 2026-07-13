package lineales;
public class Nodo {
        
    private Object element;
    private Nodo enlace;
    
    
    public Nodo (Object unElemento, Nodo enlace){
        this.element=unElemento;
        this.enlace=enlace;
    }
    public void setElement(Object unElemento){
        this.element=unElemento;
    }
    public void setEnlace(Nodo enlace){
        this.enlace=enlace;
    }
    public Object getElem(){
        return this.element;
    }
    public Nodo getEnlace(){
        return this.enlace;
    }
    
}

