import java.util.ArrayList;
import java.util.List;

public class Node {
    private final Token value;
    private List<Node> children;

    public Node(Token value){
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void insertChild(Node n){
        if(children == null){
            children = new ArrayList<>();
            children.add(n);
        }
        else{
            children.add(0, n);
        }
    }

    public void insertNextChild(Node n){
        if(children == null){
            children = new ArrayList<>();
            children.add(n);
        }
        else{
            children.add(n);
        }
    }

    public void insertChild(List<Node> childrenNode){
        if(children == null){
            children = new ArrayList<>();
        }

        for(Node n : childrenNode){
            children.add(n);
        }
    }

    public Token getValue(){
        return value;
    }

    public List<Node> getChild() {
        return children;
    }
}
