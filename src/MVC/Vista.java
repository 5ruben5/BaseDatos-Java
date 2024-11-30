package MVC;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Vista extends JFrame {

    private JPanel contentPane;
    private JButton botonLogin, botonLogout, botonRegistrar, botonImportar, btnEjecutarConsulta, btnExportarCSV;
    private Modelo modelo;
    private JTextArea textArea; 
    
    /**
     * Clase Vista: Representa la interfaz gráfica de usuario (GUI) para interactuar con la base de datos.
     * Contiene la creación de la ventana principal y los eventos relacionados con los botones de inicio de sesión,
     * registro de usuario, consultas SQL, importación de datos y exportación a CSV.
     */

    public Vista() {
        modelo = new Modelo();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Base de Datos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBounds(250, 10, 300, 40);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(lblTitulo);

        botonLogin = new JButton("Inicio");
        botonLogin.setFont(new Font("Arial", Font.BOLD, 14));
        botonLogin.setBounds(50, 70, 150, 40);
        botonLogin.setBackground(new Color(30, 144, 255));
        botonLogin.setForeground(Color.WHITE);
        contentPane.add(botonLogin);

        botonLogout = new JButton("Salir");
        botonLogout.setFont(new Font("Arial", Font.BOLD, 14));
        botonLogout.setBounds(221, 70, 150, 40);
        botonLogout.setBackground(new Color(255, 69, 0));
        botonLogout.setForeground(Color.WHITE);
        botonLogout.setEnabled(false);
        contentPane.add(botonLogout);

        botonRegistrar = new JButton("Registrar Usuario");
        botonRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        botonRegistrar.setBounds(394, 70, 182, 40);
        botonRegistrar.setBackground(new Color(34, 139, 34));
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setEnabled(false);
        contentPane.add(botonRegistrar);

        botonImportar = new JButton("Importar Datos");
        botonImportar.setFont(new Font("Arial", Font.BOLD, 14));
        botonImportar.setBounds(600, 70, 150, 40);
        botonImportar.setBackground(new Color(70, 130, 180));
        botonImportar.setForeground(Color.WHITE);
        botonImportar.setEnabled(false);
        contentPane.add(botonImportar);

        contentPane.setBackground(new Color(240, 248, 255));
        
        textArea = new JTextArea();
        textArea.setBounds(50, 150, 700, 150); 
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setText("XML Data");
        textArea.setForeground(Color.GRAY);
        textArea.setCaretPosition(0);
        
    
        JTextField txtConsultaSQL = new JTextField();
        txtConsultaSQL.setBounds(50, 448, 500, 30);
        txtConsultaSQL.setFont(new Font("Arial", Font.PLAIN, 14));
        txtConsultaSQL.setToolTipText("Escribe tu consulta SQL aquí");
        contentPane.add(txtConsultaSQL);

        
        btnEjecutarConsulta = new JButton("Ejecutar Consulta");
        btnEjecutarConsulta.setBounds(560, 447, 170, 30);
        btnEjecutarConsulta.setFont(new Font("Arial", Font.BOLD, 14));
        btnEjecutarConsulta.setBackground(new Color(60, 179, 113));
        btnEjecutarConsulta.setForeground(Color.WHITE);
        btnEjecutarConsulta.setEnabled(false);
        contentPane.add(btnEjecutarConsulta);
        
        
        btnExportarCSV = new JButton("Exportar a CSV");
        btnExportarCSV.setFont(new Font("Arial", Font.BOLD, 14));
        btnExportarCSV.setBounds(302, 510, 200, 40);
        btnExportarCSV.setBackground(new Color(255, 165, 0));
        btnExportarCSV.setForeground(Color.WHITE);
        btnExportarCSV.setEnabled(false);
        contentPane.add(btnExportarCSV);
        
        
        
    
        textArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textArea.getText().equals("XML Data")) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK); 
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText("XML Data");
                    textArea.setForeground(Color.GRAY);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(50, 150, 700, 250);
        contentPane.add(scrollPane);

      
        botonRegistrar.addActionListener(e -> mostrarDialogoRegistro());

        botonLogout.addActionListener(e -> logout());
        
        btnExportarCSV.addActionListener(e -> {
            if (textArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay datos para exportar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }


            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar como");
            fileChooser.setSelectedFile(new File("resultado.csv")); 

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File archivoCSV = fileChooser.getSelectedFile();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoCSV))) {
                    writer.write(textArea.getText());
                    JOptionPane.showMessageDialog(this, "Datos exportados correctamente a " + archivoCSV.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al exportar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
        botonImportar.addActionListener(e -> {
            String rutaCSV = "AE02_population.csv";
            String carpetaXML = "xml";
            boolean resultado = modelo.generarArchivosXMLDesdeCSV(rutaCSV, carpetaXML);

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Datos importados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                mostrarContenidoXML(carpetaXML);
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un error al importar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        
        btnEjecutarConsulta.addActionListener(e -> {
            String consultaSQL = txtConsultaSQL.getText().trim();
            

            if (consultaSQL.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa una consulta SQL.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String resultado = modelo.ejecutarConsultaSQL(consultaSQL);
                textArea.setText(resultado); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al ejecutar la consulta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
    }
    
    /**
     * Devuelve el botón de inicio de sesión.
     *
     * @return El botón de inicio de sesión (botonLogin).
     */
    
    public JButton getBotonLogin() {
        return botonLogin;
    }
    
    /**
     * Devuelve el botón de cerrar sesión.
     *
     * @return El botón de cerrar sesión (botonLogout).
     */

    public JButton getBotonLogout() {
        return botonLogout;
    }
    
    /**
     * Muestra el contenido de los archivos XML generados en el área de texto.
     *
     * @param carpeta La ruta de la carpeta que contiene los archivos XML.
     */
    
    private void mostrarContenidoXML(String carpetaXML) {
        File folder = new File(carpetaXML);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".xml"));

        if (files != null) {
            StringBuilder contenido = new StringBuilder();
            for (File file : files) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        contenido.append(line).append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            textArea.setText(contenido.toString());
        }
    }
    
    /**
     * Muestra un cuadro de diálogo para registrar un nuevo usuario.
     */
    
    public void mostrarDialogoLogin() {
        JDialog dialogo = new JDialog(this, "Login", true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setLayout(null);
        

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuario.setBounds(50, 30, 100, 30);
        dialogo.getContentPane().add(lblUsuario);

        JTextField txtUsuario = new JTextField();
        txtUsuario.setBounds(150, 30, 200, 30);
        dialogo.getContentPane().add(txtUsuario);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        lblContraseña.setBounds(50, 80, 100, 30);
        dialogo.getContentPane().add(lblContraseña);

        JPasswordField txtContraseña = new JPasswordField();
        txtContraseña.setBounds(150, 80, 200, 30);
        dialogo.getContentPane().add(txtContraseña);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(100, 150, 80, 30);
        btnAceptar.setBackground(new Color(34, 139, 34));
        btnAceptar.setForeground(Color.WHITE);
        dialogo.getContentPane().add(btnAceptar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 150, 90, 30);
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        dialogo.getContentPane().add(btnCancelar);

        btnAceptar.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String contraseña = new String(txtContraseña.getPassword());

            String contrasenaHasheada = modelo.generarMD5(contraseña);

            String tipoUsuario = modelo.obtenerTipoUsuario(usuario, contrasenaHasheada);

            if (tipoUsuario != null) {
                if ("admin".equalsIgnoreCase(tipoUsuario)) {
                    JOptionPane.showMessageDialog(dialogo, "Inicio de sesión exitoso como admin.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    activarBotones(true); 
                } else if ("client".equalsIgnoreCase(tipoUsuario)) {
                    JOptionPane.showMessageDialog(dialogo, "Inicio de sesión exitoso como cliente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    activarBotones(false); 
                }
                dialogo.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogo, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.setVisible(true);
    }


    private void mostrarDialogoRegistro() {
    	JDialog dialogo = new JDialog(this, "Registrar Usuario", true);
        dialogo.setSize(400, 350); 
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuario.setBounds(50, 30, 100, 30);
        dialogo.getContentPane().add(lblUsuario);

        JTextField txtUsuario = new JTextField();
        txtUsuario.setBounds(150, 30, 200, 30);
        dialogo.getContentPane().add(txtUsuario);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        lblContraseña.setBounds(50, 80, 100, 30);
        dialogo.getContentPane().add(lblContraseña);

        JPasswordField txtContraseña = new JPasswordField();
        txtContraseña.setBounds(150, 80, 200, 30);
        dialogo.getContentPane().add(txtContraseña);

        JLabel lblConfirmarContraseña = new JLabel("Confirmar Contraseña:");
        lblConfirmarContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        lblConfirmarContraseña.setBounds(50, 130, 150, 30);
        dialogo.getContentPane().add(lblConfirmarContraseña);

        JPasswordField txtConfirmarContraseña = new JPasswordField();
        txtConfirmarContraseña.setBounds(200, 130, 150, 30);
        dialogo.getContentPane().add(txtConfirmarContraseña);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(100, 200, 80, 30);
        btnAceptar.setBackground(new Color(34, 139, 34));
        btnAceptar.setForeground(Color.WHITE);
        dialogo.getContentPane().add(btnAceptar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 200, 90, 30);
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        dialogo.getContentPane().add(btnCancelar);

        btnCancelar.addActionListener(e -> dialogo.dispose());

        btnAceptar.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String contraseña = new String(txtContraseña.getPassword());
            String confirmarContraseña = new String(txtConfirmarContraseña.getPassword());

            if (!contraseña.equals(confirmarContraseña)) {
                JOptionPane.showMessageDialog(dialogo, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }

            if (!modelo.registrarUsuario(usuario, contraseña)) {
                JOptionPane.showMessageDialog(dialogo, "El usuario ya existe. Intente con otro nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialogo, "Usuario registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
            }
        });


        dialogo.setVisible(true);
    }


    private void activarBotones(boolean esAdmin) {
        botonLogin.setEnabled(false);
        botonLogout.setEnabled(true);
        btnEjecutarConsulta.setEnabled(true);
        btnExportarCSV.setEnabled(true);
        if (esAdmin) {
            botonRegistrar.setEnabled(true);
            botonImportar.setEnabled(true);
        } else {
            botonRegistrar.setEnabled(false);
            botonImportar.setEnabled(false);
        }
    }

    /**
     * Realiza el cierre de sesión y vuelve al estado inicial.
     */
    
    public void logout() {
        botonLogin.setEnabled(true);
        botonLogout.setEnabled(false);
        botonRegistrar.setEnabled(false);
        botonImportar.setEnabled(false);
        btnEjecutarConsulta.setEnabled(false);
        btnExportarCSV.setEnabled(false);
    }

}
