package MVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase Controlador: Gestiona las interacciones entre el modelo y la vista, 
 * y establece las acciones a realizar cuando el usuario interactúa con la interfaz.
 */
public class Controlador {

    private Modelo modelo;
    private Vista vista;

    /**
     * Constructor de la clase Controlador. Inicializa el controlador con el modelo y la vista,
     * y configura los eventos para los botones de la interfaz.
     * 
     * @param modelo El modelo que contiene la lógica de negocio.
     * @param vista La vista que representa la interfaz de usuario.
     */
    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        control();
    }

    /**
     * Configura los eventos de los botones en la vista y asigna las acciones correspondientes.
     * - Muestra el diálogo de login cuando se hace clic en el botón de login.
     * - Realiza el logout cuando se hace clic en el botón de logout.
     */
    private void control() {
        // Configura el evento para el botón de login
        vista.getBotonLogin().addActionListener(new ActionListener() {
            /**
             * Este método es llamado cuando el usuario hace clic en el botón de login.
             * Muestra el diálogo de login en la vista.
             * 
             * @param e El evento de acción generado por el clic en el botón de login.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.mostrarDialogoLogin();
            }
        });

        // Configura el evento para el botón de logout
        vista.getBotonLogout().addActionListener(new ActionListener() {
            /**
             * Este método es llamado cuando el usuario hace clic en el botón de logout.
             * Realiza la acción de logout en la vista, cerrando la sesión del usuario.
             * 
             * @param e El evento de acción generado por el clic en el botón de logout.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.logout();
            }
        });
    }
}
