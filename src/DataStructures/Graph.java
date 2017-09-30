package DataStructures;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<Node> nodes;
    Node selected;

    public Graph(){
        nodes = new ArrayList<>(1);
        Node root = new Node("Root");
        nodes.add(root);
        selected = root;
    }

    // Create a new node and attach it to the selected node
    public Node appendNew(String new_node_title){
        Node new_node = new Node(new_node_title);
        nodes.add(new_node);
        selected.connectTo(new_node);
        return new_node;
    }

    // Delete selected node
    public void deleteNode(){
        List<Edge> selected_edges = selected.getEdges();

        for(Edge i : selected_edges){
            i.delete();
        }

        nodes.remove(selected);
        selected = nodes.get(nodes.size()-1);
    }
}

class Node {
    private List<Edge> edges;
    private String title;
    private ConnectedString content;

    Node(String initial_title){
        title = initial_title;
        content = new ConnectedString("");
        edges = new ArrayList<>(1);
    }

    void connectTo(Node child){
        Edge e = new Edge(this, child, 1);
        edges.add(e);
        child.edges.add(e);
    }

    void disconnectFrom(Node father){
        Edge to_delete = searchForEdge(father, this);

        if(to_delete == null)
            return;

        father.edges.remove(to_delete);
        this.edges.remove(to_delete);
    }

    private Edge searchForEdge(Node father, Node child){
        for (Edge i : father.edges) {
            if (i.getChild() == child)
                return i;
        }
        return null;
    }

    List<Edge> getEdges(){ return edges; }

    List<Edge> getChildren(){
        List<Edge> children = new ArrayList<>(0);

        for (Edge i : edges) {
            if(i.getFather() == this){
                children.add(i);
            }
        }
        return children;
    }

    List<Edge> getParents(){
        List<Edge> parents = new ArrayList<>(0);

        for (Edge i : edges) {
            if(i.getChild() == this){
                parents.add(i);
            }
        }
        return parents;
    }
}

class Edge {
    private int type;
    private Node start;
    private Node end;

    Edge(Node father, Node child, int initial_type){
        start = father;
        end = child;
        type = initial_type;
    }

    Node getFather(){
        return start;
    }

    Node getChild(){
        return end;
    }

    // Delete edge wherever it appears
    void delete(){
        start.disconnectFrom(end);
    }
}
