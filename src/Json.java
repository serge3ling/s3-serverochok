import java.util.TreeMap;

public class Json {
    String s;
    TreeMap<String, String> map;
    
    public Json(String s) {
        setSourceString(s);
    }
    
    public void setSourceString(String s) {
        this.s = s;
        makeMap();
    }
    
    public void makeMap() {
        map = new TreeMap<String, String>();
        int ln = s.length();
        boolean goOn = (ln > 1);
        if (goOn) {
            goOn = s.substring(0, 1).equals("{") &&
                s.substring(ln - 1, ln).equals("}");
        }
    }
}

