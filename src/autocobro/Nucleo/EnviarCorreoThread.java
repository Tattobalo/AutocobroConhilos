package autocobro.Nucleo;

import autocobro.Modelos.ProductoSeleccionado;
import java.util.List;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EnviarCorreoThread extends Thread {

    private final String correoDestino;
    private final String nombreCliente;
    private final String codigoFactura;
    private final List<ProductoSeleccionado> productos;
    private final double total;

    public EnviarCorreoThread(String correoDestino, String nombreCliente, String codigoFactura, List<ProductoSeleccionado> productos, double total) {
        this.correoDestino = correoDestino;
        this.nombreCliente = nombreCliente;
        this.codigoFactura = codigoFactura;
        this.productos = productos;
        this.total = total;
    }

    @Override
    public void run() {
        System.out.println("[HILO MAIL] Configurando protocolo SMTP en segundo plano...");

        // 1. Configuración de credenciales del servidor emisor (Kiosco LiPaDaS)
        final String usuarioEmisor = "tattobalo10@gmail.com"; 
        final String contrasenaEmisor = "ozaramrjywrcyfpx"; // Generada desde tu cuenta Google

        // 2. Establecer propiedades del servidor de correo
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true"); // Conexión segura
        propiedades.put("mail.smtp.host", "smtp.gmail.com");  // Servidor Gmail
        propiedades.put("mail.smtp.port", "587");             // Puerto TLS estándar

        // 3. Crear la sesión de autenticación
        Session sesion = Session.getInstance(propiedades, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuarioEmisor, contrasenaEmisor);
            }
        });

        try {
            System.out.println("[HILO MAIL] Estructurando cuerpo del mensaje HTML...");
            
            // 4. Crear el objeto del mensaje
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(usuarioEmisor));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            mensaje.setSubject("Comprobante Fiscal Digital - Kiosco AutoCobro LiPaDaS");

            // 5. Diseñar el cuerpo en HTML estructurado
            StringBuilder cuerpoHTML = new StringBuilder();
            cuerpoHTML.append("<h2>¡Gracias por tu compra, ").append(nombreCliente).append("!</h2>");
            cuerpoHTML.append("<p>Tu transacción ha sido procesada de manera exitosa en nuestro kiosco digital.</p>");
            cuerpoHTML.append("<div style='border: 1px solid #ccc; padding: 15px; background-color: #f9f9f9; max-width: 450px;'>");
            cuerpoHTML.append("<h4>=== DETALLE DE COMPRA ===</h4>");
            
            for (ProductoSeleccionado p : productos) {
                cuerpoHTML.append("<p>").append(p.getNombre()).append(" x").append(p.getCantidad())
                          .append(" - $").append(String.format("%.2f", p.getPrecio() * p.getCantidad())).append("</p>");
            }
            
            cuerpoHTML.append("<hr>");
            cuerpoHTML.append("<h3>Total Pagado: $").append(String.format("%.2f", total)).append("</h3>");
            cuerpoHTML.append("<h4>Código Único de Facturación: <span style='color: #9933FF;'>").append(codigoFactura).append("</span></h4>");
            cuerpoHTML.append("<p style='font-size: 11px; color: #555;'>Ingresa este código en nuestro portal web para timbrar tu factura XML ante el SAT.</p>");
            cuerpoHTML.append("</div>");

            mensaje.setContent(cuerpoHTML.toString(), "text/html; charset=utf-8");

            // 6. Enviar el correo electrónico mediante un Socket de red asíncrono
            System.out.println("[HILO MAIL] Conectando al servidor SMTP y despachando paquete...");
            Transport.send(mensaje);
            
            System.out.println("[HILO MAIL] ¡Correo enviado con éxito a la bandeja de: " + correoDestino + "!");

        } catch (MessagingException e) {
            System.out.println("[HILO MAIL] ERROR CRÍTICO: No se pudo despachar el correo de red.");
            System.out.println("[HILO MAIL] Detalles del fallo SMTP: " + e.getMessage());
        }
    }
}