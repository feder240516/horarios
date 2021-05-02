package Clases;

import java.io.File;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import Control.MainOpt1;

public class Materia {

    public static int NUM_DIAS = 7;
    public static int NUM_HORAS = 14;
    public static int FIRST_HORA = 6;
    private static int id_count = 0;



    private String nombre;
    private int id;
    private boolean enabled;
    private ChangeListener lista;
    private ArrayList<Curso> cursos;

    public Materia(String name){
        cursos = new ArrayList<>();
        nombre = name;
        id = id_count++;
        lista = null;
        enabled = true;
    }

    public static Materia fromHTML(String name, String html, String keyword) {
        Document doc = Jsoup.parse(html);
        Node table_content;
        ArrayList<Integer> path = MainOpt1.explore(doc,keyword);
        Node sub_node = doc;
        for(int i = 0; path.get(i) != 10; i++){
            sub_node = sub_node.childNode(path.get(i));
        }
        Node try_a = tryA(doc);
        Node try_b = tryB(sub_node);
        if(try_a != null) table_content = try_a;
        else if(try_b != null) table_content = try_b;
        else return null;
        Materia materia = new Materia(name);
        System.out.println(table_content.childNodeSize());
        try {
            for(int i = 6; i < table_content.childNodeSize(); i+=10) {
                materia.addCurso(Curso.fromHTMLNode(table_content.childNode(i),table_content.childNode(i-4),materia));
            }}catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        return materia;
    }

    public static Node tryA(Node head) {
        try {
            return head.childNode(0)
                    .childNode(1)
                    .childNode(0)
                    .childNode(1)
                    .childNode(0)
                    .childNode(0)
                    .childNode(1)
                    .childNode(1)
                    .childNode(10)
                    .childNode(3)
                    .childNode(1)
                    .childNode(0)
                    .childNode(1)
                    .childNode(0)
                    .childNode(0)
                    .childNode(1)
                    .childNode(1);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Node tryB(Node head) {
        try {
            return head.childNode(10)
                    .childNode(3)
                    .childNode(1)
                    .childNode(0)
                    .childNode(1)
                    .childNode(0)
                    .childNode(0)
                    .childNode(1)
                    .childNode(1);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Materia fromFile(String line) {
        JsonElement json = new JsonParser().parse(line);
        String name = json.getAsJsonObject().get("Materia").getAsString();
        Materia materia = new Materia(name);
        System.out.println(name);
        System.out.println(json.getAsJsonObject().get("Cursos").getAsJsonArray().size());
        for (JsonElement j:json.getAsJsonObject().get("Cursos").getAsJsonArray()) {
            materia.addCurso(Curso.fromJson(j,materia));
        }
        System.out.println(materia.toRecord());
        materia.checkEnabled();
        return materia;

        /*String[] sp = line.split(":");
        String nombre = sp[0];
        Materia materia = new Materia(nombre);
        String[] cur = sp[1].split("[{}]");
        for(String i:cur){
            if (!i.equals("")){
                materia.addCurso(Curso.fromFile(i));
            }
        }*/

    }

    public void setChangeListener(ChangeListener ls) {
        lista = ls;
    }

    public void changeState() {
        if(lista != null) {
            lista.stateChanged(new ChangeEvent(this));
        }
    }

    public void addCurso(Curso curso){
        cursos.add(curso);
        changeState();
    }

    public void removeCurso(Curso curso) {
        cursos.remove(curso);
        changeState();
    }

    public boolean hasInitialized(){
        return nombre != null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public int getNumCursos(){
        return cursos.size();
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public Curso getCurso(int i){
        return cursos.get(i);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void checkEnabled() {
        for(Curso c:cursos) {
            if (c.isEnabled()) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        changeState();
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (obj == null) return (this==null);
        if (!(obj instanceof Materia)) return false;
        return ((Materia) obj).id == (this.id);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String toRecord(){
        StringBuilder string = new StringBuilder("{Materia: \""+nombre+"\",Cursos:[");
        for(int i = 0; i < cursos.size(); i++) {
            string.append(cursos.get(i).toRecord());
            if (i < cursos.size()-1) {
                string.append(",");
            }
        }
        return string.append("]}").toString();
    }
}
