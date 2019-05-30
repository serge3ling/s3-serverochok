import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Jack {
    public static final String HOTSPOT = "HordaAngelCynnan";
    public static final String STUB_RESPONSE =
            "<i>Jack says hi.</i>";
    
    private String s;
    
    Jack(String str, Shell shell) {
        this.s = str.trim();
        Pg.setShell(shell);
    }
    
    String afterExecute(ResultSet rs) {
        String oc = STUB_RESPONSE;
        
        try {
            int rowCount = 0;
            oc = "{\r\n" +
                    "\"rowCount\": \"" + HOTSPOT + "\",\r\n\"rows\": [";
            
            boolean firstLoop = true;
            ResultSetMetaData rsmd = null;
            while (rs.next()) {
                if (firstLoop) {
                    rsmd = rs.getMetaData();
                    firstLoop = false;
                }
                
                rowCount++;
                oc += "\r\n{\r\n";
                
                int colCnt = rsmd.getColumnCount();
                for (int i = 1; i <= colCnt; i++) {
                    String colLb = rsmd.getColumnLabel(i);
                    oc = oc + "\"" + colLb + "\": \"" + rs.getString(colLb) +
                            ((i == colCnt) ? ("\"") : ("\",\r\n"));
                }
                oc += "\r\n},";
            }
            if (rowCount > 0) {
                oc = oc.substring(0, (oc.length() - 1));
            }
            oc += "\r\n]\r\n}";
            oc = oc.replaceFirst(HOTSPOT, "" + rowCount);
        } catch (SQLException esql) {
            esql.printStackTrace();
        }
        
        return oc;
    }
    
    String afterUpdate(int cnt) {
        return "{\"rowCount\": \"" + cnt + "\"}";
    }
    
    String handle(String str) {
        s = str.trim();
        String oc = STUB_RESPONSE;
        
        String[] sa = stripAsJson(str);
        String key = sa[0];
        String val = sa[1];
        
        if (!key.equals("")) {
            if (key.toLowerCase().equals("pg")) {
                if (val.toLowerCase().indexOf("select") == 0) {
                    ResultSet rs = Pg.select(val);
                    if (rs != null) {
                        oc = afterExecute(rs);
                    }
                }
                
                if ((val.toLowerCase().indexOf("update") == 0) ||
                        (val.toLowerCase().indexOf("insert") == 0) ||
                        (val.toLowerCase().indexOf("delete") == 0)) {
                    oc = afterUpdate(Pg.update(val));
                }
            }
        }
        
        return oc;
    }
    
    String handle() {
        return handle(s);
    }
    
    static String[] stripAsJson(String s) {
        String[] oc = new String[2];
        String key = "";
        oc[0] = ""; // key
        oc[1] = ""; // value
        
        s = s.trim();
        int ix = -1;
        int ln = s.length();
        int last = ln - 1;
        char chr0 = 'c';
        boolean goOn = (ln >= 3);
        if (goOn) {
            if ((s.charAt(0) == '{') && (s.charAt(last) == '}')) {
                s = s.substring(1, last).trim();
                ln = s.length();
                last = ln - 1;
            }
            
            chr0 = s.charAt(0);
            if ((chr0 == '"') || (chr0 == '\'')) {
                if (ln < 5) {
                    goOn = false;
                } else {
                    ix = s.indexOf(chr0, 2);
                    if ((ix >= 0) && (ix < (last - 1))) {
                        key = s.substring(1, ix);
                        s = key + s.substring(ix + 1);
                        ln -= 2;
                        last -= 2;
                    } else {
                        goOn = false;
                    }
                }
            }
        }
        
        if (goOn) {
            goOn = (ln >= 3);
        }
        
        if (goOn) {
            ix = s.indexOf(':', key.length());
            goOn = ((ix >= 0) && (ix < last));
        }
        
        if (goOn) {
            key = s.substring(0, ix).trim();
            goOn = (key.length() > 0);
        }
        
        if (goOn) {
            s = s.substring(ix + 1).trim();
            ln = s.length();
            last = ln - 1;
            goOn = (ln > 0);
        }
        
        if (goOn) {
            chr0 = s.charAt(0);
            if ((chr0 == '\'') || (chr0 == '"')) {
                goOn = (s.charAt(last) == chr0) && (ln > 2);
                if (goOn) {
                    s = s.substring(1, last).trim();
                    //ln -= 2;
                    //last -= 2;
                }
            }
        }
        
        if (goOn) {
            oc[0] = key;
            oc[1] = s.replaceAll("\\Q\\\"\\E", "\"").
                    replaceAll("\\Q\\'\\E", "'");
        }
        
        return oc;
    }
    
    static String stringArray2toKeyValueString(String[] sa) {
        return ("Key: \"" + sa[0] + "\", value: \"" + sa[1] + "\".");
    }
    
    public static void main(String[] args) {
        Jack jack = new Jack("{pg: \"select id, param_lang_tv, param_europe, " +
                "param_baby from inlimited_params;\"}", null);
        System.out.println("Response:\r\n" + jack.handle());
        String[] sa = Jack.stripAsJson("{\"pg\": \"describe story\"}");
        System.out.println(stringArray2toKeyValueString(sa));
    }
}

