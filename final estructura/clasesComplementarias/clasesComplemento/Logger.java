package clasesComplemento;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//Solo marcar los datos que se ingresan e son importantes al problema
public class Logger {
    private static Logger instancia;
    private PrintWriter escritor;

    private Logger() {
        try {
            // Abrir archivo en modo append (agrega al final sin borrar)
            FileWriter fw = new FileWriter("sistema_mudanza.log", true);
            escritor = new PrintWriter(fw, true);
        } catch (IOException e) {
            System.err.println("Error al crear archivo de log: " + e.getMessage());
        }
    }

    public static Logger getInstancia() {
        if (instancia == null) {
            instancia = new Logger();
        }
        return instancia;
    }

    public void log(String mensaje) {
        if (escritor != null) {
            LocalDateTime ahora = LocalDateTime.now();
            String fecha = ahora.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            escritor.println("[" + fecha + "] " + mensaje);
        }
    }

    public void cerrar() {
        if (escritor != null) {
            escritor.close();
        }
    }

    public void volcarEstado(String seccion, String contenido) {
        log("\n===== " + seccion + " =====");
        log(contenido);
        log("=".repeat(50));
    }
}