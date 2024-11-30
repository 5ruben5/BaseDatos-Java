package MVC;

/**
 * Clase Principal: Punto de entrada de la aplicación. Inicializa el modelo, la vista y el controlador,
 * y muestra la interfaz gráfica de usuario.
 */
public class Principal {

	/**
     * Método principal que arranca la aplicación.
     * Crea las instancias de Modelo, Vista y Controlador,
     * y hace visible la vista de la interfaz de usuario.
     * 
     * @param args Los argumentos de la línea de comandos (no se usan en este caso).
     */
	
    public static void main(String[] args) {

        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo, vista);
        
        vista.setVisible(true);
    }
}
