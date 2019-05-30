import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class Conf {
    public static final String CONF_FILE = "Serverochok.conf";
    public static final String CHARS = "UTF-8";
    public static final String AVALANCHE_XML = "avalanche.xml"; // lower case
    public static final String PORT = "port"; // lower case
    public static final String GUI = "gui"; // lower case
    
    private static Conf cnf;
    
    private boolean parseSuccess = true;
    private String msg = "";
    private String avalancheXml = "avalanche.xml";
    private int port = 13999;
    private boolean gui = true;
    private String cncUrl = "jdbc:postgresql://localhost:5432/postgres";
    private String userName = "postgres";
    private String password = "pgsql9Pw";
    
    private Conf() {
        parseSuccess = parseConf();
    }
    
    boolean parseLine(String line) {
        boolean oc = true;
        String low = "";
        
        boolean goOn = (line != null);
        if (goOn) {
            line = line.trim();
            low = line.toLowerCase();
            goOn = (!line.equals(""));
        }
        
        if (goOn) {
            if (low.indexOf(AVALANCHE_XML) == 0) {
                String s2 = line.substring(AVALANCHE_XML.length()).trim();
                if (s2.indexOf("=") == 0) {
                    String s3 = s2.substring(1).trim();
                    if (s3.equals("")) {
                        oc = false;
                        msg += "Value of \"" + AVALANCHE_XML +
                                "\" parameter cannot be empty.";
                    } else {
                        avalancheXml = s3;
                    }
                }
            }
            
            if (low.indexOf(PORT) == 0) {
                String s2 = line.substring(PORT.length()).trim();
                if (s2.indexOf("=") == 0) {
                    String s3 = s2.substring(1).trim();
                    if (s3.equals("")) {
                        oc = false;
                        msg += "Value of \"" + PORT +
                                "\" parameter cannot be empty.";
                    } else {
                        try {
                            port = Integer.parseInt(s3);
                        } catch (NumberFormatException enf) {
                            oc = false;
                            msg += "Value of \"" + PORT +
                                    "\" parameter must be integer.";
                        }
                    }
                }
            }
            
            if (low.indexOf(GUI) == 0) {
                String s2 = line.substring(GUI.length()).trim();
                if (s2.indexOf("=") == 0) {
                    String s3 = s2.substring(1).trim();
                    gui = (s3.toLowerCase().equals("yes"));
                }
            }
        }
        
        return oc;
    }
    
    boolean parseConf() {
        boolean oc = true;
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(CONF_FILE));
        } catch (FileNotFoundException efnf) {
            oc = false;
            msg += "There is no file \"" + CONF_FILE + "\".";
        }
        
        if (oc) {
            String line = "";
            while (line != null) {
                try {
                    line = br.readLine();
                    oc = parseLine(line);
                } catch (IOException eio) {
                    oc = false;
                    line = null;
                    msg += "File \"" + CONF_FILE + "\" read error.";
                }
            }
            oc = (parseAvalancheXml()) ? (oc) : (false);
        }
        
        return oc;
    }
    
    boolean parseAvalancheXml() {
        boolean oc = true;
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(avalancheXml));
        } catch (FileNotFoundException efnf) {
            oc = false;
            msg += "There is no file \"" + avalancheXml + "\".";
        }
        
        if (oc) {
            String line = "";
            boolean goOn = true;
            int stat = 0;
            // 0 - found <connection-url>, 1 - <user-name>, 2 - <password>
            while ((line != null) && (stat <= 2) && goOn) {
                try {
                    line = br.readLine();
                    line = line.trim();
                    switch (stat) {
                        case 0:
                            if (line.indexOf("<connection-url>jdbc:postgresql://") == 0) {
                                parseCncUrl(line);
                                stat++;
                            }
                            break; // falls through
                        
                        case 1:
                            if (line.indexOf("<user-name>") == 0) {
                                parseUserName(line);
                                stat++;
                            }
                            break; // falls through
                        
                        case 2:
                            if (line.indexOf("<password>") == 0) {
                                parsePassword(line);
                                stat++;
                            }
                            break; // falls through
                    }
                } catch (IOException eio) {
                    oc = false;
                    goOn = false;
                    line = null;
                    msg += "File \"" + avalancheXml + "\" read error.";
                }
            }
        }
        
        return oc;
    }
    
    void parseCncUrl(String s) {
        s = s.substring("<connection-url>".length());
        int ix = s.lastIndexOf("</connection-url>");
        if (ix >= 0) {
            cncUrl = s.substring(0, ix);
        }
    }
    
    void parseUserName(String s) {
        int ix = s.lastIndexOf("}</user-name>");
        if (ix >= 0) {
            s = s.substring(0, ix);
            ix = s.lastIndexOf(":");
            if ((ix < (s.length() - 1)) && (ix >= 0)) {
                userName = s.substring(ix + 1);
            }
        }
    }
    
    void parsePassword(String s) {
        int ix = s.lastIndexOf("}</password>");
        if (ix >= 0) {
            s = s.substring(0, ix);
            ix = s.lastIndexOf(":");
            if ((ix < (s.length() - 1)) && (ix >= 0)) {
                password = s.substring(ix + 1);
            }
        }
    }
    
    public static Conf getConf() {
        if (cnf == null) {
            cnf = new Conf();
        }
        return cnf;
    }
    
    public boolean isParseSuccess() {
        return parseSuccess;
    }
    
    public String getMessage() {
        return msg;
    }
    
    public String getAvalancheXml() {
        return avalancheXml;
    }
    
    public int getPort() {
        return port;
    }
    
    public boolean getGui() {
        return gui;
    }
    
    public String getCncUrl() {
        return cncUrl;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public static void main(String[] args) {
        Conf cnf = getConf();
        System.out.println("Успішний розбір конфігурації: " + cnf.isParseSuccess());
        System.out.println("Повідомлення після розбору: " + cnf.getMessage());
        System.out.println("Значення " + AVALANCHE_XML + ": " + cnf.getAvalancheXml());
        System.out.println("Значення " + PORT + ": " + cnf.getPort());
        System.out.println("Рядок з'єднання з postgresql: " + cnf.getCncUrl());
        System.out.println("Користувач: " + cnf.getUserName());
        System.out.println("Пароль: " + cnf.getPassword());
    }
}

