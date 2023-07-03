import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Object> values = new HashMap<>();

    boolean idExist(String ID){
        return values.containsKey(ID);
    }

    Object Get(String ID) {
        if (values.containsKey(ID)) {
            return values.get(ID);
        }
        throw new RuntimeException("Variable no definida '" + ID + "'.");
    }

    void assign(String ID, Object value){
        values.put(ID, value);
    }
}
