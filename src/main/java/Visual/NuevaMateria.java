package Visual;
import Clases.Curso;
import Clases.Materia;
import Control.Main;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

@SuppressWarnings("serial")
public class NuevaMateria extends JFrame{
    int count;
    Materia materia;
    public NuevaMateria(int count){
        super("Nueva materia");
        initComponents();
        this.count = count;
        this.materia = null;
        Main.rememberFrame(this);
    }

    private void initComponents(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        jPanel_content = new JPanel(new BorderLayout());
        jPanel_content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(jPanel_content);

        //#################################################
        //NORTH
        //#################################################

        jPanel_north = new JPanel(new FlowLayout());
        jLabel_north1 = new JLabel("Curso");
        jTextfield_cursoid = new JTextField(5);
        jLabel_north2 = new JLabel("Materia");
        jTextfield_materia = new JTextField(10);
        jButton_add_curso = new JButton("Añadir curso");
        jButton_add_curso.addActionListener((ActionEvent e) -> {
            pressButtonAdd();
        });
        jPanel_north.add(jLabel_north1);
        jPanel_north.add(jTextfield_cursoid);
        jPanel_north.add(jLabel_north2);
        jPanel_north.add(jTextfield_materia);
        jPanel_north.add(jButton_add_curso);

        //#################################################
        //CENTER
        //#################################################

        jPanel_center = new JPanel(new GridLayout(15, 7,18,1));
        jLabel_center1 = new JLabel("Lunes");
        jLabel_center2 = new JLabel("Martes");
        jLabel_center3 = new JLabel("Miercoles");
        jLabel_center4 = new JLabel("Jueves");
        jLabel_center5 = new JLabel("Viernes");
        jLabel_center6 = new JLabel("Sábado");
        jLabel_center7 = new JLabel("Domingo");
        checkboxes = new JCheckBox[98];
        jPanel_center.add(jLabel_center1);
        jPanel_center.add(jLabel_center2);
        jPanel_center.add(jLabel_center3);
        jPanel_center.add(jLabel_center4);
        jPanel_center.add(jLabel_center5);
        jPanel_center.add(jLabel_center6);
        jPanel_center.add(jLabel_center7);
        for (int i = 0; i < Materia.NUM_DIAS * Materia.NUM_HORAS; i++) {
            //checkboxes[i] = new CheckComponent(i%14+6);
            checkboxes[i] = new JCheckBox(""+((i%Materia.NUM_HORAS)+Materia.FIRST_HORA)+":00");
        }
        for (int i = 0; i < Materia.NUM_HORAS; i++) {
            for (int j = 0; j < Materia.NUM_DIAS; j++) {
                jPanel_center.add(checkboxes[j*Materia.NUM_HORAS+i]);
            }
        }

        //#################################################
        //SOUTH
        //#################################################

        jPanel_south = new JPanel(new FlowLayout());
        jTextfield_keyword = new JTextField(6);
        jTextfield_import = new JTextField(12);
        jButton_import = new JButton("Importar");
        jButton_import.addActionListener((ActionEvent e) -> {
            pressButtonImportar();
        });
        jButton_next = new JButton("Siguiente");
        jButton_next.addActionListener((ActionEvent e) -> {
            pressButtonSiguiente();
        });
        jButton_finish = new JButton("Finalizar");
        jButton_finish.addActionListener((ActionEvent e) -> {
            pressButtonFinish();
        });

        jLabel_bottom1 = new JLabel("Numero de cursos añadidos");
        jTextfield_count_cursos = new JTextField(4);
        jTextfield_count_cursos.setEditable(false);
        jPanel_south.add(jLabel_bottom1);
        jPanel_south.add(jTextfield_count_cursos);
        jPanel_south.add(jTextfield_keyword);
        jPanel_south.add(jTextfield_import);
        jPanel_south.add(jButton_import);
        jPanel_south.add(jButton_next);
        jPanel_south.add(jButton_finish);


        //#################################################
        //PACK
        //#################################################

        jPanel_content.add(jPanel_north,BorderLayout.NORTH);
        jPanel_content.add(jPanel_center,BorderLayout.CENTER);
        jPanel_content.add(jPanel_south,BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setLocation(100, 100);

    }

    private void pressButtonSiguiente(){
        if(materia == null){
            JOptionPane.showMessageDialog(this, "No se han agregado cursos a "
                    + "esta materia");
        } else{
            if(true || count + 1 < Main.getNumMateriasCreadas()){
                NuevaMateria nueva = new NuevaMateria(count+1);
                nueva.setVisible(true);
                Main.forgetFrame(this);
                this.dispose();
            } /*else{
                Espere espere = new Espere();
                espere.setVisible(true);
                Main.printMaterias();
            }*/

        }
    }

    private void pressButtonFinish() {
        if(Main.getNumMateriasCreadas() == 0) {
            JOptionPane.showMessageDialog(this, "No ha creado ninguna materia", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Espere espere = new Espere();
        espere.setVisible(true);
        Main.printMaterias();
        Main.forgetFrame(this);
        this.dispose();
    }

    private void clearWindow() {
        this.count++;
        this.materia = null;
        Arrays.asList(checkboxes).forEach((check) -> {
            check.setSelected(false);
        });
        jTextfield_cursoid.setText("");
        jTextfield_count_cursos.setText(String.valueOf(this.count));
        jTextfield_import.setText("");
        jTextfield_keyword.setText("");
        jTextfield_materia.setText("");
        jTextfield_materia.setEnabled(true);
    }

    private void pressButtonAdd(){
        if(jTextfield_materia.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Elija un nombre para la materia");
            return;
        }
        if(jTextfield_cursoid.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Elija un nombre para el curso");
            return;
        }
        boolean[] seleccionados = new boolean[checkboxes.length];
        boolean hay_true = false;
        for (int i = 0; i < checkboxes.length; i++) {
            seleccionados[i] = checkboxes[i].isSelected();
            if (seleccionados[i]) hay_true = true;
        }
        if(!hay_true){
            JOptionPane.showMessageDialog(this, "No ha seleccionado horas");
            return;
        }
        if(materia == null){
            materia = new Materia(jTextfield_materia.getText());
            Main.addMateria(materia);
            jTextfield_materia.setEnabled(false);
        }
        Curso curso = new Curso(seleccionados,jTextfield_cursoid.getText(),materia);
        materia.addCurso(curso);
        jTextfield_cursoid.setText("");
        Arrays.asList(checkboxes).forEach((check) -> {
            check.setSelected(false);
        });
        jTextfield_count_cursos.setText(String.valueOf(materia.getNumCursos()));
        jTextfield_import.setEnabled(false);
        jTextfield_keyword.setEnabled(false);
        jButton_import.setEnabled(false);
    }

    private void pressButtonImportar() {
        if(jTextfield_materia.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Elija un nombre para la materia");
            return;
        }
        if(jTextfield_import.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Ingrese el html a parsear");
            return;
        }
        String name = jTextfield_materia.getText();
        String import_text = jTextfield_import.getText();
        String keyword = jTextfield_keyword.getText();
        materia = Materia.fromHTML(name, import_text, keyword);
        if (materia == null) {
            JOptionPane.showMessageDialog(this, "html incompatible");
            return;
        }
        Main.addMateria(materia);
        pressButtonSiguiente();

    }

    private JPanel jPanel_content;
    private JPanel jPanel_north;
    private JLabel jLabel_north1;
    private JTextField jTextfield_cursoid;
    private JLabel jLabel_north2;
    private JTextField jTextfield_materia;
    private JButton jButton_add_curso;
    private JPanel jPanel_center;
    private JLabel jLabel_center1;
    private JLabel jLabel_center2;
    private JLabel jLabel_center3;
    private JLabel jLabel_center4;
    private JLabel jLabel_center5;
    private JLabel jLabel_center6;
    private JLabel jLabel_center7;
    private JCheckBox[] checkboxes;
    private JPanel jPanel_south;
    private JButton jButton_next;
    private JButton jButton_import;
    private JButton jButton_finish;
    private JTextField jTextfield_import;
    private JTextField jTextfield_keyword;
    private JLabel jLabel_bottom1;
    private JTextField jTextfield_count_cursos;
}
