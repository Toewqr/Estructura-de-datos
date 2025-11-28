package clasesComplemento;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Pedido {
    private Ciudad origen;
    private Ciudad destino;
    private LocalDate fecha;
    private Cliente cliente;
    private double metrosCubicos;
    private int cantidadBultos;
    private String domicilioRetiro;
    private String domicilioEntrega;
    private boolean pagado;
    private boolean terminado;

    public Pedido(Ciudad origen, Ciudad destino, String fechaStr, Cliente cliente,
            double metrosCubicos, int cantidadBultos,
            String domicilioRetiro, String domicilioEntrega, boolean pagado) {

        this.terminado= false;
        this.origen = origen;
        this.destino = destino;

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            this.fecha = LocalDate.parse(fechaStr, fmt);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/MM/yyyy");
        }

        this.cliente = cliente;
        this.metrosCubicos = metrosCubicos;
        this.cantidadBultos = cantidadBultos;
        this.domicilioRetiro = domicilioRetiro;
        this.domicilioEntrega = domicilioEntrega;
        this.pagado = pagado;
    }

    // Getters
    public Ciudad getOrigen() {
        return origen;
    }

    public Ciudad getDestino() {
        return destino;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getCpDestino() {
    return destino.getCodigoPostal();
}

    public double getMetrosCubicos() {
        return metrosCubicos;
    }

    public int getCantidadBultos() {
        return cantidadBultos;
    }

    public String getDomicilioRetiro() {
        return domicilioRetiro;
    }

    public String getDomicilioEntrega() {
        return domicilioEntrega;
    }

    public boolean isPagado() {
        return pagado;
    }

    // Setters
    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public void actualizarDomicilios(String retiro, String entrega) {
        this.domicilioRetiro = retiro;
        this.domicilioEntrega = entrega;
    }

    public void actualizarMedidas(double metrosCubicos, int cantidadBultos) {
        if (metrosCubicos <= 0){
            System.out.println(" m3 debe ser positivo");
        }
          
        if (cantidadBultos < 0){
            System.out.println("Bultos no puede ser negativo");
        }
        this.metrosCubicos = metrosCubicos;
        this.cantidadBultos = cantidadBultos;
    }

    @Override
    public String toString() {
        return "Pedido de " + cliente.getNombres() + " " + cliente.getApellidos() +
                "\nDe: " + origen.getNombre() + " a " + destino.getNombre() +
                "\nFecha: " + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                "\nVolumen: " + metrosCubicos + " m3, Bultos: " + cantidadBultos +
                "\nPagado: " + (pagado ? "Sí" : "No");
    }

    public boolean getTerminado() {
       return terminado;
    }
}