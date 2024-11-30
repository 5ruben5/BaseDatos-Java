package MVC;

import java.io.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Modelo: Contiene la lógica para interactuar con la base de datos, gestionar usuarios,
 * registrar usuarios, realizar consultas SQL y manejar la importación/exportación de datos.
 */

public class Modelo {

    private static final String URL = "jdbc:mysql://localhost:3306/population";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = ""; 

    /**
     * Obtiene una conexión a la base de datos.
     * 
     * @return Una conexión a la base de datos.
     * @throws SQLException Si hay un error al obtener la conexión.
     */
    
    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    
    /**
     * Obtiene el tipo de usuario y la contraseña desde la base de datos para un usuario dado.
     * 
     * @param usuario El nombre de usuario para buscar.
     * @return Un arreglo con el tipo de usuario y la contraseña, o null si no se encuentra el usuario.
     */

    private String[] obtenerTipoYContrasena(String usuario) {
        String query = "SELECT type, password FROM users WHERE login = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("type"), rs.getString("password")};
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener tipo y contraseña: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene el tipo de usuario para un nombre de usuario y contraseña proporcionados.
     * 
     * @param usuario El nombre de usuario para verificar.
     * @param contrasenaHasheada La contraseña hasheada proporcionada.
     * @return El tipo de usuario (por ejemplo, "admin", "client") o null si no coincide la contraseña.
     */
    
    public String obtenerTipoUsuario(String usuario, String contrasenaHasheada) {
        String[] datosUsuario = obtenerTipoYContrasena(usuario);
        if (datosUsuario != null && contrasenaHasheada.equals(datosUsuario[1])) {
            return datosUsuario[0];
        }
        return null;
    }

    /**
     * Verifica si un usuario tiene permisos de administrador.
     * 
     * @param usuario El nombre de usuario a verificar.
     * @param contrasena La contraseña proporcionada para el usuario.
     * @return true si el usuario es administrador, false de lo contrario.
     */
    public boolean esUsuarioAdmin(String usuario, String contrasena) {
        String[] datosUsuario = obtenerTipoYContrasena(usuario);
        if (datosUsuario != null && "admin".equalsIgnoreCase(datosUsuario[0]) && contrasena.equals(datosUsuario[1])) {
            return true;
        }
        return false;
    }

    /**
     * Genera el hash MD5 de una contraseña.
     * 
     * @param contraseña La contraseña que se va a hashear.
     * @return La cadena con el valor hash MD5 de la contraseña.
     */
    
    public String generarMD5(String contraseña) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(contraseña.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Registra un nuevo usuario en la base de datos.
     * 
     * @param usuario El nombre del usuario a registrar.
     * @param contraseña La contraseña del usuario.
     * @return true si el registro fue exitoso, false si el usuario ya existe o ocurrió un error.
     */

    public boolean registrarUsuario(String usuario, String contraseña) {
        if (existeUsuario(usuario)) {
            return false;
        }

        String contrasenaHasheada = generarMD5(contraseña);
        String query = "INSERT INTO users (login, password, type) VALUES (?, ?, 'client')";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(query)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasenaHasheada);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Verifica si un usuario ya existe en la base de datos.
     * 
     * @param usuario El nombre de usuario a verificar.
     * @return true si el usuario existe, false de lo contrario.
     */

    private boolean existeUsuario(String usuario) {
        String query = "SELECT COUNT(*) FROM users WHERE login = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Importa datos desde un archivo CSV a la base de datos.
     * 
     * @param rutaCSV La ruta al archivo CSV que contiene los datos.
     * @return true si la importación fue exitosa, false en caso de error.
     */

    public boolean importarDatosDesdeCSV(String rutaCSV) {
        String dropTableQuery = "DROP TABLE IF EXISTS population";
        String createTableQuery = """
            CREATE TABLE population (
                country VARCHAR(30),
                population VARCHAR(30),
                density VARCHAR(30),
                area VARCHAR(30),
                fertility VARCHAR(30),
                age VARCHAR(30),
                urban VARCHAR(30),
                share VARCHAR(30)
            )
        """;
        try (Connection conexion = obtenerConexion()) {
            
            try (PreparedStatement stmt = conexion.prepareStatement(dropTableQuery)) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conexion.prepareStatement(createTableQuery)) {
                stmt.executeUpdate();
            }

            
            try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
                String linea;
                linea = br.readLine();
                String insertQuery = "INSERT INTO population (country, population, density, area, fertility, age, urban, share) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conexion.prepareStatement(insertQuery)) {
                    while ((linea = br.readLine()) != null) {
                        String[] datos = linea.split(";");
                        for (int i = 0; i < datos.length; i++) {
                            if ("N.A.".equals(datos[i])) {
                                datos[i] = null;
                            }
                        }
                        for (int i = 0; i < datos.length; i++) {
                            stmt.setString(i + 1, datos[i]);
                        }
                        stmt.executeUpdate();
                    }
                }
            }
            return true;
        } catch (SQLException | IOException e) {
            System.out.println("Error durante la importación: " + e.getMessage());
        }
        return false;
    }

    
    /**
     * Genera archivos XML a partir de los datos de un archivo CSV.
     * 
     * @param rutaCSV La ruta al archivo CSV que contiene los datos.
     * @param carpetaXML La carpeta donde se guardarán los archivos XML generados.
     * @return true si los archivos XML fueron generados correctamente, false si ocurrió un error.
     */
    public boolean generarArchivosXMLDesdeCSV(String rutaCSV, String carpetaXML) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
            String linea;
            br.readLine(); // Saltar cabecera
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                String xmlContenido = generarXML(datos);
                escribirArchivoXML(carpetaXML, datos[0], xmlContenido);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Genera una cadena de texto en formato XML a partir de un arreglo de datos.
     * 
     * @param datos Un arreglo de datos para representar en formato XML.
     * @return La cadena XML generada.
     */
    private String generarXML(String[] datos) {
        return "<country>\n" +
               "    <name>" + datos[0] + "</name>\n" +
               "    <population>" + datos[1] + "</population>\n" +
               "    <density>" + datos[2] + "</density>\n" +
               "    <area>" + datos[3] + "</area>\n" +
               "    <fertility>" + datos[4] + "</fertility>\n" +
               "    <age>" + datos[5] + "</age>\n" +
               "    <urban>" + datos[6] + "</urban>\n" +
               "    <share>" + datos[7] + "</share>\n" +
               "</country>\n" +
               "---------------------------------------------------------";
    }

    /**
     * Escribe un archivo XML con el contenido proporcionado en una carpeta especificada.
     * 
     * @param carpetaXML La carpeta donde se guardará el archivo XML.
     * @param country El nombre del país que será el nombre del archivo XML.
     * @param xmlContenido El contenido XML que se escribirá en el archivo.
     */
    private void escribirArchivoXML(String carpetaXML, String country, String xmlContenido) {
        try {
            File dir = new File(carpetaXML);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String rutaArchivo = carpetaXML + "/" + country + ".xml";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write(xmlContenido);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ejecuta una consulta SQL personalizada en la base de datos.
     * 
     * @param usuario El nombre del usuario que está realizando la consulta.
     * @param contrasenaHasheada La contraseña hasheada del usuario.
     * @param consulta La consulta SQL a ejecutar.
     * @return Una lista de arreglos de cadenas con los resultados de la consulta, o null si no tiene permisos.
     */
    
    public List<String[]> ejecutarConsultaLibre(String usuario, String contrasenaHasheada, String consulta) {
        List<String[]> resultados = new ArrayList<>();
        String tipoUsuario = obtenerTipoUsuario(usuario, contrasenaHasheada);
        if (tipoUsuario == null || (!"admin".equalsIgnoreCase(tipoUsuario) && consulta.toLowerCase().contains("users"))) {
            return null; // Denegar permisos
        }
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(consulta);
             ResultSet rs = stmt.executeQuery()) {

            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                String[] fila = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    fila[i - 1] = rs.getString(i);
                }
                resultados.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error en consulta libre: " + e.getMessage());
        }
        return resultados;
    }
    
    /**
     * Ejecuta una consulta SQL personalizada y devuelve los resultados como una cadena de texto.
     * 
     * @param consultaSQL La consulta SQL a ejecutar.
     * @return Una cadena con los resultados de la consulta.
     * @throws Exception Si ocurre un error en la consulta SQL.
     */

    public String ejecutarConsultaSQL(String consultaSQL) throws Exception {
        StringBuilder resultado = new StringBuilder();

        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", "root", "");
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(consultaSQL)) {

            int columnas = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= columnas; i++) {
                resultado.append(rs.getMetaData().getColumnName(i)).append("\t");
            }
            resultado.append("\n");

            while (rs.next()) {
                for (int i = 1; i <= columnas; i++) {
                    resultado.append(rs.getString(i)).append("\t");
                }
                resultado.append("\n");
            }
        } catch (SQLException e) {
            throw new Exception("SQL Error: " + e.getMessage(), e);
        }

        return resultado.toString();
    }
}
