package lineales;
public class Cola {
     private Nodo frente;
    private Nodo fin;
    
    public Cola (){
        this.frente= null;
        this.fin= null;
    }
    
    public boolean poner(Object unObjeto){
        Nodo aux = new Nodo(unObjeto,null);
        
        if (this.esVacia()){
            this.frente= aux;
        }else{
            this.fin.setEnlace(aux);
        }
        
        this.fin=(aux);
        
        return true;
    }
    
    public boolean sacar(){
        boolean exito;
        
        if(this.esVacia()){
            exito = false;
        }else{
            this.frente = this.frente.getEnlace();
            if (this.frente == null){
                this.fin = null;
            }
            exito = true;
        }
        return exito;
    }
    
    public Object obtenerFrente (){
        Object mandar = null;
        if (!this.esVacia()){
            mandar = this.frente.getElem();
        }
        return mandar;
    }
    
    public boolean esVacia (){
        return (this.frente== null);
    }
    
    public void vaciar (){
        this.frente=(null);
        this.fin=(null);
    }
    
public Cola clone() {
    
    Cola clon = new Cola();
    
    if (!this.esVacia()) {
        Nodo aux1 = this.frente;
        Nodo aux2;
        
        clon.frente=new Nodo(aux1.getElem(),null);
        aux1=aux1.getEnlace();
        aux2=clon.frente;
        
        while (aux1 !=null){
            aux2.setEnlace(new Nodo(aux1.getElem(),null));
            aux2=aux2.getEnlace();
            aux1=aux1.getEnlace(); 
        }
         clon.fin=aux2;
        
    }
    
    return clon;
}
    
    public String toString() {
    String resultado = "";
    Nodo aux = this.frente;
    
    while(aux != null){
        resultado += aux.getElem().toString() + " ";
        aux = aux.getEnlace();
    }
    
    return resultado;
}   
    
}
