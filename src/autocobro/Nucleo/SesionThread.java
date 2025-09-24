package autocobro.Nucleo;

public class SesionThread extends Thread {
    
    private volatile boolean sesionActiva = true;

    @Override
    public void run() {
        System.out.println("HILO 2 (Sesion): Sesion iniciada y monitoreando en segundo plano.");
        
        while (sesionActiva) {
            try {
                // Simula una tarea en segundo plano (por ejemplo, mantener una conexión activa)
                Thread.sleep(10000); // Espera 10 segundos
            } catch (InterruptedException e) {
                // El hilo se interrumpió, salimos del bucle
                Thread.currentThread().interrupt();
                sesionActiva = false;
            }
        }
        
        System.out.println("HILO 2 (Sesion): Sesion terminada.");
    }
    
    // Método público para detener el hilo de forma segura
    public void detenerSesion() {
        sesionActiva = false;
        this.interrupt();
    }
}