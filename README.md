# MVC - Aplicación de Gestión de Usuarios y Población

Esta es una aplicación Java basada en el patrón de diseño MVC (Modelo-Vista-Controlador). La aplicación permite gestionar usuarios, autenticar usuarios y realizar consultas sobre una base de datos de población, además de importar datos desde un archivo CSV y generar archivos XML.

## Descripción

La aplicación tiene las siguientes funcionalidades clave:

- **Gestión de Usuarios**: Permite registrar, autenticar y verificar usuarios con contraseñas hasheadas en MD5.
- **Base de Datos**: Utiliza una base de datos MySQL para almacenar y consultar información sobre usuarios y datos de población.
- **Importación de Datos**: Permite importar datos desde un archivo CSV y almacenarlos en una tabla MySQL.
- **Generación de Archivos XML**: Transforma los datos de población en archivos XML, facilitando su uso para otras aplicaciones.
- **Interfaz Gráfica**: Ofrece una interfaz gráfica donde el usuario puede interactuar con la aplicación.

## Requisitos

- Java 8 o superior
- MySQL (para la base de datos)
- Librerías JDBC para conectar con la base de datos
- IDE (IntelliJ IDEA, Eclipse, etc.) o un compilador de Java
- Biblioteca para manejar archivos CSV (se usa `BufferedReader` de Java)

## Instalación

1. **Clonar el repositorio:**

   ```bash
   git clone https://github.com/usuario/repo.git
   cd repo
