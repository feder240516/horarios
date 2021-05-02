package Visual;

import Clases.Horario;
import Control.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Espere extends JFrame{
    int[] indices;
    public Espere(){
        indices = new int[Main.getNumMateriasCreadas()];
        Main.clearHorarios();
        initComponents();
        for (int i = 0; i < indices.length; i++) {
            indices[i] = 0;
        }
        do{
            Horario horario = Horario.tryCreateHorario(indices);
            if(horario!=null){
                Main.addHorario(horario);
            }
        }while(!sumar());
        jLabel_north1.setText(String.format("Número de horarios generados: %d", Main.getNumHorarios()));
        jButton_ver.setVisible(true);
        if(Main.getNumHorarios() == 0)jButton_ver.setText("Recalcular");
        pack();
    }
    /**
     *
     * @return true si se han recorrido todas las posibilidades, false en caso
     * contrario
     */
    private boolean sumar(){
        for (int i = 0; i < indices.length; i++) {
            if (!Main.getMateria(i).isEnabled()) continue;
            /*if (indices[i]+1 < Main.getMateria(i).getNumCursos()){
                indices[i]++;
                return false;
            }else{
                indices[i] = 0;
            }*/
            do {
                indices[i]++;
            } while(indices[i] < Main.getMateria(i).getNumCursos() && !Main.getMateria(i).getCurso(indices[i]).isEnabled());
            if (indices[i] < Main.getMateria(i).getNumCursos()) return false;
            else indices[i] = 0;
        }
        return true;
    }

    private void initComponents(){
        jPanel_content = new JPanel(new BorderLayout());
        getContentPane().add(jPanel_content);
        jLabel_north1 = new JLabel("Espere...");
        jProgressBar = new JProgressBar();
        //#####
        jProgressBar.setVisible(false);
        //#####
        jButton_ver = new JButton("Ver");
        jButton_ver.setVisible(false);
        jButton_ver.addActionListener((ActionEvent ae)->{
            pressButtonVer();
        });
        jPanel_content.add(jLabel_north1,BorderLayout.NORTH);
        jPanel_content.add(jProgressBar,BorderLayout.CENTER);
        jPanel_content.add(jButton_ver,BorderLayout.SOUTH);
        jPanel_content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(100,100);
    }

    private boolean valido() {
        return Main.getNumHorarios() > 0;
    }

    private void pressButtonVer(){
        if (valido()) {
            VerHorario verhorario = new VerHorario(0,null);
            verhorario.setVisible(true);
            dispose();
        } else {
            Espere espere = new Espere();
            dispose();
            espere.setVisible(true);
        }
    }

    private JPanel jPanel_content;
    private JLabel jLabel_north1;
    private JProgressBar jProgressBar;
    private JButton jButton_ver;
}
