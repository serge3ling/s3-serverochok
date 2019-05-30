import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Acceptor extends Thread {
    private ServerSocket serverSocket;
    private Serverochok caller;
    private Shell shell;
    private boolean quit;
    private boolean work;
    
    Acceptor(ServerSocket serverSocket, Serverochok caller, Shell shell) {
        this.serverSocket = serverSocket;
        this.caller = caller;
        this.shell = shell;
        quit = false;
        work = true;
        start();
    }
    
    public synchronized void setQuit(boolean yes) {
        quit = yes;
    }
    
    public synchronized boolean getQuit() {
        return quit;
    }
    
    public synchronized void setWork(boolean yes) {
        work = yes;
    }
    
    public synchronized boolean getWork() {
        return work;
    }
    
    public synchronized void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    @Override
    public void run() {
        while (!getQuit()) {
            try {
                if (getWork()) {
                    Socket socket = serverSocket.accept();
                    shell.prn("Accepted " + socket.getInetAddress() + ":" +
                            socket.getPort() + ".");
                    caller.setSocket(socket);
                }
                sleep(Serverochok.RELAX_WAIT);
            } catch (IOException eio) {
                boolean goOn = true;
                if (serverSocket == null) {
                    goOn = false;
                    shell.prn("accept() failed.");
                }
                if (goOn) {
                    if (serverSocket.isClosed()) {
                        goOn = false;
                        shell.prn("serverSocket is closed.");
                    }
                }
                if (goOn) {
                    shell.prn("accept() failed.");
                }
            } catch (InterruptedException ei) {
            }
        }
    }
}

