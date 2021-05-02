package Control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import Clases.Materia;

public class MainOpt1 {
    final static String FILE_URL = "C:\\Users\\feder\\Desktop\\html.html";
    final static String FILE_URL2 = "C:\\Users\\feder\\Desktop\\res.txt";
    public static void main(String[] args)  {

        BufferedReader br;
        BufferedWriter bw;
        try {
            br = new BufferedReader(new FileReader(FILE_URL));
            bw = new BufferedWriter(new FileWriter(FILE_URL2));
            String import_text = String.join("\n", br.lines().collect(Collectors.toList()));
            br.close();
            Document doc = Jsoup.parse(import_text);
            explore(doc, "2410");
            //System.out.println(Materia.tryB(doc));
            System.exit(0);
            /*Node sub_doc = doc.childNode(0)
                    .childNode(3)
                    .childNode(12)
                    .childNode(4)
                    .childNode(0)
                    .childNode(0)
                    .childNode(0)
                    .childNode(0)
                    .childNode(1)
                    .childNode(0)
                    .childNode(1)
                    .childNode(26)
                    /*.childNode(3)
                    .childNode(1)
                    .childNode(0)
                    .childNode(1)
                    .childNode(0)
                    .childNode(0)
                    .childNode(1)
                    .childNode(1)
                    .childNode(12)
                    .childNode(3)
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
                    .childNode(1)*/;
                    /*System.out.println(sub_doc.childNodeSize() + " hijos");
            bw.write("");
            for (int i = 0; i < sub_doc.childNodeSize(); i++) {
                System.out.println(String.format("\n\n%d:\n\n %s", i,sub_doc.childNode(i)));
                bw.append(String.format("\n\n%d:\n\n %s", i,sub_doc.childNode(i)));
                bw.newLine();
            }
            System.out.println(sub_doc.childNodeSize() + " hijos");


            bw.close();
            System.exit(0);*/
                    Node table_content = doc.childNode(0)
                            .childNode(1)
                            .childNode(0)
                            .childNode(1);
                    Node sub_node = table_content.childNode(3)
                            .childNode(3)
                            .childNode(1)
                            .childNode(1)
                            .childNode(1)
                            .childNode(3)
                            .childNode(1)
                            .childNode(1)
                            .childNode(1)
                            .childNode(1);
                    for (int i = 0; i < sub_node.childNodeSize(); i++) {
                        System.out.println(String.format("\n\n%d:\n\n %s", i,sub_node.childNode(i)));
                    }
                    for (int i = 3; i < sub_node.childNodeSize(); i+=2) {
                        System.out.println();
                    }


        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static ArrayList<Integer> explore(Node doc, String keyword) {
        Node sub_doc = doc;
        ArrayList<Integer> direccion = new ArrayList<>();
        while(sub_doc.childNodeSize() > 0) {
            for(int i = 0;i < sub_doc.childNodeSize(); i++) {
                if (sub_doc.childNode(i).toString().contains(keyword)) {
                    System.out.print(i);
                    direccion.add(i);
                    sub_doc = sub_doc.childNode(i);
                    break;
                }
            }
            if (sub_doc == doc) break;
        }
        System.out.println();
        System.out.println(direccion);
        System.out.println(sub_doc);
        return direccion;
    }

}
