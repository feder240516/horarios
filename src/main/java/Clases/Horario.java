package Clases;

import Control.Main;
import java.util.HashMap;

public class Horario {
    private HashMap<Integer, String> horas;
    public Horario(){
        horas = new HashMap<>(30);
    }

    /**
     * Intenta añadir un curso. Retorna true si es exitosa y false si es fallida
     * @param curso
     * @return
     */
    private boolean addCurso(Curso curso){
        for (int i : curso.getHoras()) {
            if (horas.containsKey(i)){
                return false;
            }
            horas.put(i, curso.toHorario());
        }
        return true;
    }

    /**
     * Recibe los indices que representan la ubicacion de los cursos para cada
     * materia. Intenta añadir todos los cursos indicados. Si es exitoso,
     * retorna el horario creado; en caso contrario, retorna null.
     * @param indices - los indices de los cursos
     * @return El horario generado, o null si no se pudo generar.
     */
    public static Horario tryCreateHorario(int[] indices){
        Horario horario = new Horario();
        for(int i = 0;i<indices.length;i++){
            if (!Main.getMateria(i).isEnabled()) continue;
            if (!Main.getMateria(i).getCurso(indices[i]).isEnabled()) return null;
            if(!horario.addCurso(Main.getMateria(i).getCurso(indices[i]))){
                return null;
            }
        }
        if (horario.isEmpty()) return null;
        return horario;
    }

    public boolean isEmpty() {
        return horas.isEmpty();
    }

    public String getHora(int i){
        return horas.getOrDefault(i, null);
    }
}
