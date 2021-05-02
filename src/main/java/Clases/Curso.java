package Clases;

import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.nodes.Node;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Curso {
    private ArrayList<Integer> horas;
    private String nombre;
    private Materia materia;
    private boolean enabled;
    public Curso(boolean[] seleccionados,String nombre, Materia materia){
        horas = new ArrayList<>();
        for (int i = 0; i < seleccionados.length; i++) {
            if(seleccionados[i]){
                horas.add(i);
            }
        }
        this.nombre = nombre;
        this.materia = materia;
        this.enabled = true;
    }

    public static Curso fromJson(JsonElement json, Materia materia) {
        String nombre = json.getAsJsonObject().get("Curso").getAsString();
        JsonArray horas = json.getAsJsonObject().get("horas").getAsJsonArray();
        boolean enabled = json.getAsJsonObject().get("enabled").getAsInt()==1?true:false;
        boolean[] total = new boolean[Materia.NUM_DIAS * Materia.NUM_HORAS];
        for (int i = 0; i < total.length; i++) {
            total[i] = false;
        }
        for(JsonElement js: horas) {
            total[js.getAsInt()] = true;
        }
        Curso curso = new Curso(total,nombre,materia);
        curso.setEnabled(enabled);
        return curso;

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        materia.checkEnabled();
    }

    public static Curso fromHTMLNode(Node node, Node name_node, Materia materia) {
        System.out.println(node.childNodeSize() + "dsfafdsafdsa");

        Node subtable = node
                .childNode(3)
                .childNode(1)
                .childNode(0)
                .childNode(1); //children of this: 3,5,7... cada hora

        String profesor = subtable
                .childNode(2)
                .childNode(9)
                .childNode(1)
                .childNode(0)
                .childNode(0)
                .toString();
        boolean[] seleccionados = new boolean[Materia.NUM_DIAS * Materia.NUM_HORAS];
        for(int j = 0; j < seleccionados.length; j++) {
            seleccionados[j] = false;
        }
        for(int i = 2; i < subtable.childNodeSize(); i+=2) {
            String dia = subtable
                    .childNode(i)
                    .childNode(1)
                    .childNode(1)
                    .childNode(0)
                    .childNode(0)
                    .toString();
            String hora_inicio = subtable
                    .childNode(i)
                    .childNode(3)
                    .childNode(1)
                    .childNode(0)
                    .childNode(0)
                    .toString();
            seleccionados[horaToIndex(dia, hora_inicio)] = true;
        }

        String nombre = name_node
                .childNode(3)
                .childNode(1)
                .childNode(0)
                .childNode(1)
                .childNode(2)
                .childNode(1)
                .childNode(1)
                .childNode(0)
                .childNode(0)
                .childNode(0)
                .toString();
        String newnombre = profesor.substring(0, 5) + " " + nombre;
        return new Curso(seleccionados, newnombre, materia);
    }

    public static int horaToIndex(String dia, String hora) {
        String dialow = dia.toLowerCase();
        int numdia = -1;
        switch(dialow.charAt(0)) {
        case 'l':
            numdia = 0;
            break;
        case 'm':
            switch(dialow.charAt(1)) {
            case 'a':
                numdia = 1;
                break;
            case 'i':
                numdia = 2;
                break;
            default:
                throw new IllegalArgumentException("Día no encontrado; argumento " + dia);
            }
            break;
        case 'j':
            numdia = 3;
            break;
        case 'v':
            numdia = 4;
            break;
        case 's':
            numdia = 5;
            break;
        case 'd':
            numdia = 6;
            break;
        default:
            throw new IllegalArgumentException("Día no encontrado; argumento " + dia);
        }
        int numhora = Integer.valueOf(hora.split(":")[0]) - 6;
        return numdia * Materia.NUM_HORAS + numhora;
    }

    public ArrayList<Integer> getHoras() {
        return horas;
    }

    public String getNombre(){
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String toHorario() {
        return String.format("%s - %s", materia.getNombre(), nombre);
    }

    public String toRecord() {
        return String.format("{Curso: \"%s\", horas: %s, enabled: %d}", nombre, horas, enabled?1:0);
    }

}
