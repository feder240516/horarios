package Control;

import Clases.*;
import Libs.ObservableArrayList;
import Libs.ObservableArrayListListener;
import Visual.*;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.UIManager;


public class Main {
    public static Main main_manager;
    static int num_materias;
    static ObservableArrayList<Materia> materias;
    static ArrayList<Horario> horarios;
    //static HashSet<ActionListener> listeners = new HashSet<>();
    static ArrayList<JFrame> frames_activos = new ArrayList<>();
    static FocusListener focus_listener = new FocusListener() {

        @Override
        public void focusGained(FocusEvent e) {
            System.out.println(frames_activos.size());
            for(JFrame jframe: frames_activos) {
                jframe.toFront();
            }
        }
        @Override public void focusLost(FocusEvent e) {}

    };
    static Creados creados;

    public static int getNumMaterias(){
        return num_materias;
    }
    public static int getNumMateriasCreadas() {
        return materias.size();
    }
    public static void setNumMaterias(int num){
        num_materias = num;
    }
    public static void addMateria(Materia materia){
        materias.add(materia);
        materia.setChangeListener(materias);
    }
    public static void printMaterias(){
        materias.forEach(System.out::println);
    }
    public static ObservableArrayList<Materia> getMaterias() {
        return materias;
    }
    public static Materia getMateria(int i){
        return materias.get(i);
    }
    public static int getIndexMateria(Materia materia) {
        return materias.indexOf(materia);
    }
    public static void addHorario(Horario horario){
        horarios.add(horario);
    }
    public static Horario getHorario(int i){
        return horarios.get(i);
    }
    public static int getNumHorarios(){
        return horarios.size();
    }
    public static void closeCreados() {
        creados.dispose();
    }
    public static Creados buildCreados(Rectangle rect) {
        creados = new Creados(rect);
        Main.addObservableListListener(creados.tree_materias);
        return creados;
    }
    public static Creados getCreados() {
        return creados;
    }
    public static void rememberFrame(JFrame jframe) {
        frames_activos.add(jframe);
        jframe.addFocusListener(focus_listener);
    }
    public static void forgetFrame(JFrame jframe) {
        frames_activos.remove(jframe);
        jframe.removeFocusListener(focus_listener);
    }
    public static void addObservableListListener(ObservableArrayListListener<Materia> al) {
        materias.addObservableArrayListListener(al);
    }
    public static void removeObservableListListener(ObservableArrayListListener<Materia> al) {
        materias.removeObservableArrayListListener(al);
    }
    public static void clearHorarios() {
        horarios.clear();
    }

    public static void main(String[] args) {
        main_manager = new Main();
        materias = new ObservableArrayList<>();
        horarios = new ArrayList<>();
        try{
            for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
                System.out.println(info.getName());
                if (info.getName().equals("Nimbus"))UIManager.setLookAndFeel(info.getClassName());
            }
        }catch(Exception e){}
        Registro ventana = new Registro();
        ventana.setVisible(true);
    }
}
