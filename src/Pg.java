import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Pg {
    public static final String CLASS = "org.postgresql.Driver";
    private static final String cncSDefault =
            "jdbc:postgresql://localhost:5432/postgres";// datadb
    private static final String userDefault = "postgres";// neurotec
    private static final String pwDefault = "s911user";// 2xTbAHj89Pmm
    private static Shell shell;
    
    private String msg;
    private Connection cnc;
    Statement sttm;
    private String cncS;
    private String user;
    private String pw;
    
    public Pg(String cncS, String user, String pw) {
        this.cncS = cncS;
        this.user = user;
        this.pw = pw;
        msg = "";
    }
    
    public Pg() {
        this(Conf.getConf().getCncUrl(), Conf.getConf().getUserName(),
                Conf.getConf().getPassword());
    }
    
    public String getMsg() {
        return msg;
    }
    
    void setMsg(String msg) {
        this.msg = msg;
    }
    
    public boolean isOkClass() {
        boolean oc = true;
        try {
            Class.forName(CLASS);
        } catch (ClassNotFoundException ecnf) {
            oc = false;
            msg = "Class not found: \"" + CLASS +
                    "\". Maybe not included in classpath.";
            shell.prn(msg);
            shell.prn(ecnf);
        }
        return oc;
    }
    
    public boolean isOkConnect() {
        boolean oc = true;
        try {
            cnc = DriverManager.getConnection(cncS, user, pw);
        } catch (SQLException esql) {
            oc = false;
            msg = "Could not connect.\r\nConnection string: \"" +
                    cncS + "\";\r\nuser: " + user + ";\r\npassword: " +
                    (
                    (pw == null) ? ("not ") :
                    (
                    (pw.equals("")) ? ("not ") : ("")
                    )
                    ) + "provided.";
            shell.prn(msg);
            shell.prn(esql);
        }
        
        if (oc) {
            try {
                sttm = cnc.createStatement();
            } catch (SQLException esql) {
                msg = "Could not create SQL statement.";
            }
        }
        
        return oc;
    }
    
    public boolean open() {
        boolean goOn = isOkClass();
        
        if (goOn) {
            goOn = isOkConnect();
        }
        
        return goOn;
    }
    
    public void close() {
        if (cnc == null) {
            msg += "\r\nNo need to close connection as there was none.";
        } else {
            try {
                cnc.close();
                msg += "\r\nConnection closed successfully.";
            } catch (SQLException esql) {
                msg += "\r\nCould not close connection.";
            }
        }
    }
    
    public static void setShell(Shell shell) {
        Pg.shell = shell;
    }
    
    public static ResultSet select(String query) {
        ResultSet oc = null;
        Pg pg = new Pg();
        
        if (pg.open()) {
            try {
                oc = pg.sttm.executeQuery(query);
            } catch (SQLException esql) {
                pg.setMsg("Failed to run query: " + query);
                shell.prn(esql);
            }
        }
        
        pg.close();
        shell.prn(pg.getMsg());
        pg = null;
        
        return oc;
    }
    
    public static int update(String query) {
        int oc = 0;
        Pg pg = new Pg();
        
        if (pg.open()) {
            try {
                oc = pg.sttm.executeUpdate(query);
            } catch (SQLException esql) {
                pg.setMsg("Failed to run query: " + query);
                shell.prn(esql);
            }
        }
        
        pg.close();
        shell.prn(pg.getMsg());
        pg = null;
        
        return oc;
    }
    
    public static void main(/* без аргументів */) {
        main(new String[0]);
    }
    
    public static void main(String[] args) {
        /*Pg pg = new Pg();
        pg.open();
        
        pg.close();
        
        System.out.println(pg.getMsg());*/
        ResultSet rs = select("select id, displayname from sources");
        if (rs != null) {
            try {
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + " " +
                            rs.getString("displayname"));
                }
            } catch (SQLException esql) {
                System.out.println("Error in query.");
                esql.printStackTrace();
            }
        }
    }
}

