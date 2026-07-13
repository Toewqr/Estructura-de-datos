package clasesComplemento;
import java.util.Objects;

public class ClaveCliente implements Comparable <ClaveCliente> {
    private String tipoDoc;
    private String numDoc;

    public ClaveCliente(String tipoDoc, String numDoc) {
        this.tipoDoc = tipoDoc;
        this.numDoc = numDoc;
    }

    // Getters
    public String getTipoDoc() { return tipoDoc; }
    public String getNumDoc() { return numDoc; }

    // Métodos para estructuras hash
    @Override
    public boolean equals(Object o) {
        ClaveCliente that = (ClaveCliente) o;
        return Objects.equals(tipoDoc, that.tipoDoc) && 
               Objects.equals(numDoc, that.numDoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoDoc, numDoc);
        
    } //investigar : Sobreescribir como buscan los metdos de hash, en este caso para que busque tanto por tipo y numDoc; Tambien sucede que al sobreescribir equals, este es usado en
    // para metodos hash por lo que debo tambien soobreescribir hascode que indica a que bucket va un objeto y como lo encuentra


    // Método para Comparable
    @Override
    public int compareTo(ClaveCliente otro) {
        // Primero comparar por tipoDoc
        int cmpTipo = this.tipoDoc.compareTo(otro.tipoDoc);
        if (cmpTipo != 0) {
            return cmpTipo;
        }
        // Si tipoDoc es igual, comparar por numDoc
        return this.numDoc.compareTo(otro.numDoc);
    }

    @Override
    public String toString() {
        return tipoDoc + ":" + numDoc;
    }

    public String getNumeroDocumento() {
     return numDoc;
    }
}