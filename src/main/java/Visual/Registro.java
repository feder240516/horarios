package Visual;
import Control.Main;
import java.awt.*;
import javax.swing.*;

import Clases.Materia;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("serial")
public class Registro extends JFrame{
    public Registro(){
        super();
        initComponents();
    }

    private void initComponents(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jPanel_content = new JPanel(new BorderLayout());
        getContentPane().add(jPanel_content);
        //################################################
        //NORTH
        //################################################
        jPanel_north = new JPanel();
        jTextArea_welcome = new JTextPane();
        jTextArea_welcome.setContentType("text/html");
        jTextArea_welcome.setBackground(new Color(0,0,0,0));
        jTextArea_welcome.setText("<div style='width:300px;'>Bienvenido! Este programa le permitirá"
                + " generar una lista de horarios disponibles de acuerdo"
                + " a las clases que vaya a ver. Presione en Go para crear"
                + " horarios o en Importar si tiene un archivo de horario ya"
                + " creado</div>");
        //jTextArea_welcome.setLineWrap(true);
        //jTextArea_welcome.setWrapStyleWord(true);
        //jTextArea_welcome.setOpaque(false);
        jTextArea_welcome.setEditable(false);

        //jTextfield_num_materias = new JTextField(4);

        jPanel_north.add(jTextArea_welcome);
        //jPanel_north.add(jTextfield_num_materias);

        //################################################
        //SOUTH
        //################################################
        jPanel_south = new JPanel();
        jButton_go = new JButton("Go");
        jButton_go.addActionListener((ActionEvent ae)->pressButtonGo());
        jButton_importar = new JButton("Importar");
        jButton_importar.addActionListener((ActionEvent ae) -> pressButtonImportar());
        jButton_salir = new JButton("Salir");
        jButton_salir.addActionListener((ActionEvent ae) -> {
            dispose();
        });
        jPanel_south.add(jButton_go);
        jPanel_south.add(jButton_importar);
        jPanel_south.add(jButton_salir);
        //################################################
        //PACK
        //################################################
        jPanel_content.add(jPanel_north,BorderLayout.NORTH);
        jPanel_content.add(jPanel_south,BorderLayout.SOUTH);
        pack();
        setResizable(true);
        setLocation(100,100);
    }

    private void pressButtonGo(){
        /*int num_materias = Integer.parseInt(jTextfield_num_materias.getText());
        if (num_materias<1) {
            JOptionPane.showMessageDialog(this, "Cantidad positiva");
            return;
        }*/
        //Main.setNumMaterias(num_materias);
        NuevaMateria nuevamateria = new NuevaMateria(0);
        nuevamateria.setVisible(true);
        //Main.rememberFrame(nuevamateria);
        Rectangle rect = nuevamateria.getBounds();
        Creados creados = Main.buildCreados(rect);
        creados.setVisible(true);
        //Main.addObservableListListener(creados.tree_materias);
        //Main.rememberFrame(creados);
        dispose();
    }

    private void pressButtonImportar() {
        try {
            JFileChooser file_chooser = new JFileChooser();
            //file_chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = file_chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File myfile = file_chooser.getSelectedFile();

                if (myfile.exists()) {
                    Creados creados = Main.buildCreados(new Rectangle(600,100,0,0));
                    creados.setVisible(true);
                    BufferedReader br = new BufferedReader(new FileReader(myfile));
                    String input;
                    try{
                        while(!(input = br.readLine()).equals("")){
                            Main.addMateria(Materia.fromFile(input));
                            //Main.setNumMaterias(Main.getNumMaterias()+1);
                        }
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }
                    br.close();
                    Espere espere = new Espere();
                    espere.setVisible(true);
                    this.dispose();
                    //espere.main();
                } else {
                    JOptionPane.showMessageDialog(this, "El archivo no existe");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JPanel jPanel_content;
    private JPanel jPanel_north;
    //private JLabel jLabel_north1;
    private JTextPane jTextArea_welcome;
    //private JTextField jTextfield_num_materias;
    private JTextField jTextfield_importar;
    private JButton jButton_go;
    private JPanel jPanel_south;
    private JButton jButton_importar;
    private JButton jButton_salir;

}
