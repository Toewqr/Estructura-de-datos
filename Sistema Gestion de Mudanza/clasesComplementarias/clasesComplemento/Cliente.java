package clasesComplemento;

public class Cliente {
    
    private ClaveCliente clave;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;

    // Constructor
    public Cliente(ClaveCliente clave, String nombres, String apellidos, 
                   String telefono, String email) {
        this.clave = clave;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
    }

    public Cliente(String text, String text2, String text3, String text4, String text5) {
       
    }

    // Getters (no setters para clave, pues es inmutable)
    public ClaveCliente getClaveCliente(){
        return clave;
    }
    public String getClave() { return clave.toString(); }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    // Setters (excepto para clave)
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Cliente: " + nombres + " " + apellidos + 
               "\nDocumento: " + clave +
               "\nTel: " + telefono + 
               "\nEmail: " + email;
    }
}