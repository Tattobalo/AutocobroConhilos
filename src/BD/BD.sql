DROP DATABASE autocobro;
CREATE DATABASE IF NOT EXISTS autocobro;
USE autocobro;

-- 2. Crear la tabla principal de productos
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- 3. Crear la tabla de presentaciones y precios
CREATE TABLE precios_presentaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

-- 4. Insertar datos de prueba para que veas algo en tu JPanel
INSERT INTO productos (nombre) VALUES ('Refresco'), ('Papas');

INSERT INTO precios_presentaciones (producto_id, descripcion, precio) VALUES 
(1, 'Lata 355ml', 15.50),
(1, 'Botella 600ml', 20.00),
(2, 'Bolsa Familiar 200g', 45.00);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    ruta_foto_perfilusuarios VARCHAR(255) -- Aquí se guardará la dirección de la imagen opcional
);
