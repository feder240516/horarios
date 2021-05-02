package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import Clases.Horario;
import Clases.Materia;
import Control.Main;

@SuppressWarnings("serial")
public class VerHorario extends JFrame {
    Horario horario;
    int num_horario;
    public VerHorario(int i,Rectangle rect){
        horario = Main.getHorario(i);
        num_horario = i;
        initComponents(rect);
    }

    private void initComponents(Rectangle rect){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        jPanel_content = new JPanel(new BorderLayout());
        getContentPane().add(jPanel_content);

        //################################################
        //NORTH
        //################################################

        jPanel_north = new JPanel(new BorderLayout());
        jButton_izq = new JButton("<");
        jButton_izq.addActionListener((ae) -> pressButtonIzq());
        if(num_horario == 0) jButton_izq.setEnabled(false);
        jTextField_num_horario = new JTextField(String.valueOf(num_horario+1), 4);
        jLabel_north = new JLabel(String.format("of %d",Main.getNumHorarios()));
        jButton_go = new JButton("Go");
        jButton_go.addActionListener((ae) -> pressButtonGo());
        jButton_der = new JButton(">");
        jButton_der.addActionListener((ae) -> pressButtonDer());
        if (num_horario == Main.getNumHorarios()-1) jButton_der.setEnabled(false);
        jPanel_north_center = new JPanel(new FlowLayout());
        jPanel_north_center.add(jTextField_num_horario);
        jPanel_north_center.add(jLabel_north);
        jPanel_north_center.add(jButton_go);
        jPanel_north.add(jButton_izq,BorderLayout.WEST);
        jPanel_north.add(jButton_der,BorderLayout.EAST);
        jPanel_north.add(jPanel_north_center,BorderLayout.CENTER);
        jPanel_content.add(jPanel_north,BorderLayout.NORTH);

        //################################################
        //CENTER
        //################################################

        jPanel_center = new JPanel();
        modelo = new AbstractTableModel() {
            int ROWS = 14;
            int COLUMNS = 8;

            @Override
            public int getRowCount() {return ROWS;}

            @Override
            public int getColumnCount() {return COLUMNS;}

            @Override
            public Object getValueAt(int i, int i1) {
                if(i1==0){
                    return "" + (i+6) + ":00";
                }else{
                    return horario.getHora((i1-1)*ROWS+i);
                }
            }

            @Override
            public String getColumnName(int i) {
                switch(i){
                case 0:
                    return "Hora";
                case 1:
                    return "Lunes";
                case 2:
                    return "Martes";
                case 3:
                    return "Miércoles";
                case 4:
                    return "Jueves";
                case 5:
                    return "Viernes";
                case 6:
                    return "Sábado";
                case 7:
                    return "Domingo";
                default:
                    throw new IndexOutOfBoundsException("Columna inexistente");
                }
            }

        };
        jTable = new JTable(modelo);
        jScrollPane = new JScrollPane(jTable);
        jPanel_content.add(jScrollPane,BorderLayout.CENTER);
        //jPanel_content.add(jPanel_center,BorderLayout.CENTER);

        //################################################
        //SOUTH
        //################################################

        jPanel_south = new JPanel();
        jButton_recalculate = new JButton("Recalcular");
        jButton_exportar = new JButton("Exportar");
        jButton_nueva_materia = new JButton("Nueva Materia");
        jButton_recalculate.addActionListener((ActionEvent ae) -> pressButtonRecalculate());
        jButton_exportar.addActionListener((ActionEvent ae) -> pressButtonExportar());
        jButton_nueva_materia.addActionListener((ActionEvent ae) -> pressButtonNuevaMateria());
        jPanel_south.add(jButton_recalculate);
        jPanel_south.add(jButton_nueva_materia);
        jPanel_south.add(jButton_exportar);
        jPanel_content.add(jPanel_south,BorderLayout.SOUTH);

        //################################################
        //PACK
        //################################################
        if(rect==null){
            pack();
            setLocation(100, 100);
        }
        else setBounds(rect);

        //jScrollPane.setSize(jTable.getSize());
        //setResizable(false);


    }

    private void pressButtonIzq(){
        /*VerHorario verhorario = new VerHorario(num_horario-1,getBounds());
        verhorario.setVisible(true);
        dispose();*/
        horario = Main.getHorario(--num_horario);
        modelo.fireTableDataChanged();
        jTextField_num_horario.setText(num_horario+1+"");
        if(num_horario==0)jButton_izq.setEnabled(false);
        jButton_der.setEnabled(true);
    }

    private void pressButtonDer(){
        /*VerHorario verhorario = new VerHorario(num_horario+1,getBounds());
        verhorario.setVisible(true);
        dispose();*/
        horario = Main.getHorario(++num_horario);
        modelo.fireTableDataChanged();
        jTextField_num_horario.setText(num_horario+1+"");
        if(num_horario==Main.getNumHorarios()-1)jButton_der.setEnabled(false);
        jButton_izq.setEnabled(true);
    }

    private void pressButtonGo(){
        /*VerHorario verhorario = new VerHorario(
                Integer.parseInt(jTextField_num_horario.getText())-1,
                getBounds());
        verhorario.setVisible(true);
        dispose();*/
        num_horario = Integer.parseInt(jTextField_num_horario.getText())-1;
        horario = Main.getHorario(num_horario);
        modelo.fireTableDataChanged();
        jButton_izq.setEnabled(num_horario!=0);
        jButton_der.setEnabled(num_horario!=Main.getNumHorarios()-1);
    }

    private void pressButtonRecalculate() {
        this.dispose();
        Main.clearHorarios();
        Espere espere = new Espere();
        espere.setVisible(true);
    }

    private void pressButtonExportar() {
        JFileChooser file_chooser = new JFileChooser();
        int result = file_chooser.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            exportar(file_chooser.getSelectedFile());
        }
    }

    private void pressButtonNuevaMateria() {
        NuevaMateria nuevaMateria = new NuevaMateria(Main.getNumMateriasCreadas());
        nuevaMateria.setVisible(true);
        Main.forgetFrame(this);
        dispose();
    }

    private void exportar(File myfile){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(myfile));
            for(Materia i:Main.getMaterias()){
                bw.write(i.toRecord());
                bw.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(VerHorario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(VerHorario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private JPanel jPanel_content;
    private JPanel jPanel_north;
    private JPanel jPanel_north_center;
    private JPanel jPanel_center;
    private JPanel jPanel_south;
    private JTable jTable;
    private JScrollPane jScrollPane;
    private JButton jButton_izq;
    private JButton jButton_der;
    private JButton jButton_recalculate;
    private JButton jButton_exportar;
    private JButton jButton_nueva_materia;
    private AbstractTableModel modelo;
    private JTextField jTextField_num_horario;
    private JLabel jLabel_north;
    private JButton jButton_go;
}
