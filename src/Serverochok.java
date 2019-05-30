import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.SwingUtilities;

// yar's @ habrahabr.ru (2009-09-09) served as pattern.

public class Serverochok extends Thread {
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String SERVER_NAME = "S3-Serverochok";
    public static final int RELAX_WAIT = 128;
    
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean quit;
    private boolean work;
    private Shell shell;
    private Acceptor acceptor;
    
    public synchronized void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    public synchronized Socket getSocket() {
        return socket;
    }
    
    public synchronized void setQuit(boolean yes) {
        quit = yes;
    }
    
    public synchronized boolean getQuit() {
        return quit;
    }
    
    public synchronized boolean toggleWork() {
        boolean work = false;
        if (acceptor == null) {
            acceptor = new Acceptor(serverSocket, this, shell);
        } else {
            work = !acceptor.getWork();
            acceptor.setWork(work);
            if (work) {
                makeServerSocketWrap();
                acceptor.setServerSocket(serverSocket);
            } else {
                try {
                    serverSocket.close();
                } catch (IOException | NullPointerException e) {
                }
            }
        }
        return work;
    }
    
    public synchronized boolean makeServerSocket() {
        boolean oc = true;
        int port = Conf.getConf().getPort();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException eio) {
            oc = false;
            shell.prn("New serverSocket fail, port " + port + ".");
        }
        return oc;
    }
    
    public synchronized void makeServerSocketWrap() {
        if (makeServerSocket()) {
            shell.prn("Server started.");
            shell.prn("Listening to port " + Conf.getConf().getPort() + ".");
        } else {
            setQuit(true);
        }
    }
    
    @Override
    public void run() {
        shell = Conf.getConf().getGui() ? (new ShellGUI(this)) :
                (new ShellCLI());
        if (Conf.getConf().getGui()) {
            SwingUtilities.invokeLater((ShellGUI) shell);
        }
        makeServerSocketWrap();
        if (!getQuit()) {
            toggleWork();
        }
        Socket sc = null;
        
        while (!getQuit()) {
            try {
                sleep(RELAX_WAIT);
            } catch (InterruptedException ei) {
            }
            
            sc = getSocket();
            if (sc != null) {
                try {
                    new Thread(new SocketHandler(socket, shell)).start();
                } catch (Throwable th) {
                }
                setSocket(null);
            }
        }
        
        if (acceptor != null) {
            acceptor.setQuit(true);
        }
        try {
            serverSocket.close();
        } catch (IOException|NullPointerException e) {
        }
    }
    
    public static void main(String[] args) throws Throwable {
        Serverochok srv = new Serverochok();
        srv.start();
        /*ServerSocket ssc = new ServerSocket(Conf.getConf().getPort());
        System.out.println("Server started.");
        System.out.println("Listening to port " + Conf.getConf().getPort() +
                ".");
        while (true) {
            Socket sc = ssc.accept();
            System.out.println("Client accepted.");
            new Thread(new SocketHandler(sc)).start();
        }*/
    }

    private static class SocketHandler implements Runnable {
        private Socket sc;
        private InputStream is;
        private OutputStream os;
        private Jack jack;
        private Shell shell;
        private String responseText = "<i>Hi from " + SERVER_NAME + ".</i>";

        private SocketHandler(Socket sc, Shell shell) throws Throwable {
            this.sc = sc;
            this.is = sc.getInputStream();
            this.os = sc.getOutputStream();
            jack = new Jack("(empty)", shell);
            this.shell = shell;
        }

        @Override
        public void run() {
            try {
                readInput();
                writeResponse(responseText/*"<i>Hi from the little server.</i>"*/);
            } catch (Throwable t) {
            } finally {
                try {
                    sc.close();
                } catch (Throwable t) {
                }
            }
            shell.prn("Client processing done.");//System.out.println("Client processing done.");
        }

        private void readInput() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            shell.prn("Input:");//System.out.println("Input:");
            boolean goOn = true;
            
            int contentLength = -1;
            boolean contentLengthNotFound = true;
            String contentLengthColon = CONTENT_LENGTH + ":";
            int contentLengthColonLength = contentLengthColon.length();
            while (goOn) {
                String s = br.readLine();
                if (s == null) {
                    s = "(null)";
                    goOn = false;
                } else {
                    if (s.trim().length() == 0) {
                        s = "(empty line)";
                        goOn = false;
                    }
                }
                if (contentLengthNotFound) {
                    s = s.trim();
                    if (s.indexOf(contentLengthColon) >= 0) {shell.prn("(headers)");//System.out.print("(h) ");
                        contentLengthNotFound = false;
                        contentLength = Integer.parseInt(
                                s.substring(contentLengthColonLength).trim());
                    }
                }
                shell.prn(s);//System.out.println(s);
                /*if(s == null || s.trim().length() == 0) {
                    break;
                }*/
            }
            
            char[] cc = new char[0];
            if (contentLength > 0) {
                cc = new char[contentLength];
                try {
                    br.read(cc, 0, contentLength);
                    String s = new String(cc);
                    shell.prn("query: " + s);//System.out.println("query: " + s);
                    responseText = jack.handle(s);
                    shell.prn(responseText);//System.out.println(responseText);
                } catch (IOException eio) {
                    goOn = false;
                    shell.prn("E: Could not read request body.");//System.err.println("E: Could not read request body.");
                    eio.printStackTrace();
                }
            }
        }
        
        private void writeResponse(String s) throws Throwable {
            byte[] sBytes = s.getBytes("UTF-8");
            String response = "HTTP/1.1 200 OK\r\n" +//"HTTP/1.1 200 OK\r\n" + //"HTTP/1.1 201 Created\r\n" +
                    "Server: " + SERVER_NAME + "\r\n" +
                    "Access-Control-Allow-Origin: *\r\n" +
                    "Access-Control-Allow-Headers: Content-Type, Content-Length\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + sBytes.length + "\r\n" +
                    "Connection: close\r\n\r\n";
            os.write(response.getBytes("UTF-8"));
            os.write(sBytes);
            os.flush();
            shell.prn("Result sent.");//System.out.println("Result sent.");
        }
    }
}

