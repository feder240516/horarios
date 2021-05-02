package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import Clases.Curso;
import Clases.Materia;
import Libs.ObservableArrayListEvent;
import Libs.ObservableArrayListListener;

class EnabledTreeNode extends DefaultMutableTreeNode{

    public EnabledTreeNode() {
        super();
    }

    public EnabledTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    public EnabledTreeNode(Object userObject) {
        super(userObject);
    }

    public boolean isEnabled() {
        Object obj = getUserObject();
        if (obj instanceof String) return true;
        else if (obj instanceof Materia) {
            return ((Materia)obj).isEnabled();
        } else if (obj instanceof Curso) {
            return ((Curso)obj).isEnabled();
        } else {
            throw new UnsupportedOperationException("Tipo no reconocido: " + obj.getClass());
        }
    }
}

class EnabledCellRenderer extends DefaultTreeCellRenderer{
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        // TODO Auto-generated method stub
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        EnabledTreeNode node = (EnabledTreeNode) value;
        if (!node.isEnabled()) setForeground(new Color(192,192,192));

        return this;
    }
}

@SuppressWarnings("serial")
public class TreeMaterias extends JPanel implements ObservableArrayListListener<Materia>, TreeSelectionListener {
    private DefaultTreeModel model;
    private EnabledTreeNode root;
    private EnabledTreeNode selected_node;
    private ArrayList<ChangeListener> change_listeners;
    private static String ROOT_NAME = "Materias";


    //private ArrayList<ActionListener> opening_folds; //Acciones cuando se abre o cierra una carpeta

    public TreeMaterias() {
        super(new BorderLayout());
        selected_node = null;
        change_listeners = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        root = new EnabledTreeNode(ROOT_NAME);
        model = new DefaultTreeModel(root);
        jtree_cursos = new JTree(model);
        jtree_cursos.setCellRenderer(new EnabledCellRenderer());
        jtree_cursos.addTreeSelectionListener(this);
        jscrollpane = new JScrollPane(jtree_cursos);
        jbutton_add = new JButton("Nuevo");
        jbutton_delete = new JButton("Eliminar");
        jbutton_enable = new JButton("Activar");
        jbutton_enable.setEnabled(false);
        jbutton_enable.addActionListener((ActionEvent ae) -> pressButtonEnable());

        jpanel_center = new JPanel();
        jpanel_center.add(jtree_cursos);
        jpanel_south = new JPanel();
        jpanel_south.add(jbutton_add);
        jpanel_south.add(jbutton_delete);
        jpanel_south.add(jbutton_enable);

        this.add(jpanel_center,BorderLayout.WEST);
        this.add(jpanel_south,BorderLayout.SOUTH);
    }

    private EnabledTreeNode addMateria(Materia materia) {
        EnabledTreeNode new_node = new EnabledTreeNode(materia);
        root.add(new_node);
        for (Curso c: materia.getCursos()) {
            new_node.add(new EnabledTreeNode(c));
        }
        model.nodeStructureChanged(root);
        changeState();
        return new_node;
    }

    private EnabledTreeNode insertMateria(Materia materia, int index) {
        EnabledTreeNode new_node = new EnabledTreeNode(materia);
        root.insert(new_node,index);
        for (Curso c: materia.getCursos()) {
            new_node.add(new EnabledTreeNode(c));
        }
        model.nodeStructureChanged(root);
        changeState();
        return new_node;
    }

    private void restructureMateria(Materia materia) {
        EnabledTreeNode node_materia = getNode(materia);
        node_materia.removeAllChildren();
        for(Curso c: materia.getCursos()) {
            node_materia.add(new EnabledTreeNode(c));
        }
        model.nodeStructureChanged(node_materia);
        changeState();
    }

    private EnabledTreeNode deleteMateria(Materia materia) {
        EnabledTreeNode deleting_node = getNode(materia);
        root.remove(deleting_node);
        model.nodeStructureChanged(root);
        changeState();
        return deleting_node;
    }

    private EnabledTreeNode addClase(Curso curso, Materia materia) {
        EnabledTreeNode nodo_materia = getNode(materia);
        EnabledTreeNode nodo_curso = new EnabledTreeNode(curso);
        nodo_materia.add(nodo_curso);
        model.nodeStructureChanged(nodo_materia);
        changeState();
        return nodo_curso;
    }

    private EnabledTreeNode addClase(Curso curso, int numMateria) {
        EnabledTreeNode materia = (EnabledTreeNode) root.getChildAt(numMateria);
        EnabledTreeNode nodo_curso = new EnabledTreeNode(curso);
        materia.add(nodo_curso);
        changeState();
        return nodo_curso;
    }

    public EnabledTreeNode getNode(Materia materia) {
        Enumeration<TreeNode> nodos = root.breadthFirstEnumeration();
        EnabledTreeNode nodo = null;
        while(nodos.hasMoreElements()) {
            nodo = (EnabledTreeNode) nodos.nextElement();

            Object obj = nodo.getUserObject();
            if(obj instanceof String && ROOT_NAME.equals(nodo.getUserObject())) {continue;}
            if(obj instanceof Materia && materia.equals(nodo.getUserObject())) {
                return nodo;
            }
            if(obj instanceof Curso) {break;}
        }
        return null;
    }

    public void restructure() {
        model.nodeStructureChanged(root);
    }

    /**
     *
     * @param new_value: if true, sets button to "disable". if false, sets button to "enable"
     */
    public void setEnableButton(boolean new_value) {
        jbutton_enable.setText(new_value?"Disable":"Enable");
    }

    private void pressButtonEnable() {
        if (selected_node == null) {
            System.out.println("selected_node is null");
            return;
        }
        Object obj_node = selected_node.getUserObject();
        if (obj_node instanceof Materia) {
            Materia materia_node = (Materia) obj_node;
            materia_node.setEnabled(!materia_node.isEnabled());
            setEnableButton(materia_node.isEnabled());
        } else if (obj_node instanceof Curso) {
            Curso curso_node = (Curso) obj_node;
            curso_node.setEnabled(!curso_node.isEnabled());
            setEnableButton(curso_node.isEnabled());
        }
    }

    public void addTreeExpansionListener(TreeExpansionListener tel) {
        jtree_cursos.addTreeExpansionListener(tel);
    }

    public void removeTreeExpansionListener(TreeExpansionListener tel) {
        jtree_cursos.removeTreeExpansionListener(tel);
    }

    public void addChangeListener(ChangeListener cl) {
        change_listeners.add(cl);
    }

    public void removeChangeListener(ChangeListener cl) {
        change_listeners.remove(cl);
    }

    public void changeState() {
        for(ChangeListener cl:change_listeners) {
            cl.stateChanged(new ChangeEvent(this));
        }
    }

    @Override
    public void elementAdded(ObservableArrayListEvent evt) {
        // TODO Auto-generated method stub
        Object[] elements = evt.getElements();
        int[] indices = evt.getIndices();
        EnabledTreeNode nodo_materia;
        for (int i = 0; i < elements.length; i++) {
            nodo_materia = insertMateria((Materia)elements[i],indices[i]);
        }

    }

    @Override
    public void elementModified(ObservableArrayListEvent evt) {
        // TODO Auto-generated method stub
        Object[] elements = evt.getElements();
        for (int i = 0; i < elements.length; i++) {
            restructureMateria((Materia)elements[i]);
        }
    }

    @Override
    public void elementRemoved(ObservableArrayListEvent evt) {
        // TODO Auto-generated method stub
        Object[] elements = evt.getElements();
        int[] indices = evt.getIndices();
        for (int i = 0; i < elements.length; i++) {
            insertMateria((Materia)elements[i],indices[i]);
        }
    }

    @Override
    public void listCleared(ObservableArrayListEvent evt) {
        // TODO Auto-generated method stub
        root.removeAllChildren();
        model.nodeStructureChanged(root);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        selected_node = (EnabledTreeNode)(e.getPath().getLastPathComponent());
        Object obj_node = selected_node.getUserObject();
        if (obj_node instanceof Curso) {
            jbutton_enable.setEnabled(true);
            Curso curso_node = (Curso) obj_node;
            setEnableButton(curso_node.isEnabled());
        } else if (obj_node instanceof Materia) {
            jbutton_enable.setEnabled(false);
            Materia materia_node = (Materia) obj_node;
            setEnableButton(materia_node.isEnabled());
        } else {
            jbutton_enable.setEnabled(false);
        }
    }

    public JTree getJTree() {return jtree_cursos;}

    private JTree jtree_cursos;
    private JScrollPane jscrollpane;
    private JButton jbutton_add;
    private JButton jbutton_delete;
    private JButton jbutton_enable;
    private JPanel jpanel_center;
    private JPanel jpanel_south;

}
