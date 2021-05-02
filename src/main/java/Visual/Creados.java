package Visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import Clases.Materia;
import Control.Main;


@SuppressWarnings("serial")
public class Creados extends JFrame implements TreeExpansionListener, ChangeListener{

    boolean has_been_packed;

    public Creados(Rectangle rect) {
        super("Clases creadas");
        has_been_packed = false;
        initComponents(rect);
        Main.rememberFrame(this);
    }


    private void initComponents(Rectangle rect) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        jPanel_content = new JPanel(new BorderLayout());
        getContentPane().add(jPanel_content);
        //############## CENTER ########################
        jPanel_center = new JPanel();
        tree_materias = new TreeMaterias();
        tree_materias.addTreeExpansionListener(this);
        tree_materias.addChangeListener(this);
        jPanel_center.add(tree_materias);
        System.out.println(tree_materias);
        //############## SOUTH #########################
        jPanel_south = new JPanel(new FlowLayout());
        //############### UNIR #########################
        jPanel_content.add(jPanel_center,BorderLayout.WEST);
        jPanel_content.add(jPanel_south,BorderLayout.SOUTH);
        jPanel_content.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tree_materias.restructure();
        pack();
        if (rect != null) {
            setBounds(rect.x+rect.width, rect.y, getWidth(), getHeight());
        }
    }

    public DefaultMutableTreeNode getMateriaNode(Materia materia) {
        return tree_materias.getNode(materia);
    }

    private JPanel jPanel_content;
    private JPanel jPanel_center;
    private JPanel jPanel_south;
    public TreeMaterias tree_materias;

    @Override public void treeExpanded(TreeExpansionEvent event) {pack();}
    @Override public void treeCollapsed(TreeExpansionEvent event) {pack();}
    @Override public void stateChanged(ChangeEvent e) {pack();}

}
