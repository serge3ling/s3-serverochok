import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class ShellGUI extends JFrame implements Shell, Runnable {
    
    // # WndMain inner classes
    
    class ListenWork implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent eve) {
            fireToggleWork();
        }
    }
    
    class CloseWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent eve) {
            serverochok.setQuit(true);
        }
    }
    
    // # WndMain variables
    
    public static final String WORK_BT_IDLE = "Run";
    public static final String WORK_BT_WORK = "Stop";
    
    private Serverochok serverochok;
    
    private JPanel panel;
    private GroupLayout layout;
    private JButton workBt; // toggle
    private JTextArea sessionLog;
    private JScrollPane logScroll;
    
    private ListenWork listenWork;
    
    // # WndMain methods
    
    // constructor
    public ShellGUI(Serverochok serverochok) {
        this.serverochok = serverochok;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(Serverochok.SERVER_NAME);
        panel = new JPanel();
        workBt = new JButton(WORK_BT_WORK);
        sessionLog = new JTextArea();
        logScroll = new JScrollPane(sessionLog);
        
        listenWork = new ListenWork();
        workBt.addActionListener(listenWork);
        addWindowListener(new CloseWindowAdapter());
        
        layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(workBt)
                .addComponent(logScroll)
                );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(workBt)
                .addComponent(logScroll)
                );
        
        setContentPane(panel);
        pack();
        setSize(620, 440);
        setLocationByPlatform(true);
    }
    
    void fireToggleWork() {
        workBt.setText(serverochok.toggleWork() ? (WORK_BT_WORK) :
                (WORK_BT_IDLE));
    }
    
    @Override
    public void run() {
        boolean goOn = (serverochok != null);
        
        if (goOn) {
            setVisible(true);
        }
    }
    
    public synchronized void appendToLog(String s) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                sessionLog.append(s + "\n");
                if (sessionLog.getLineCount() > 511) {
                    try {
                        sessionLog.replaceRange("", 0, sessionLog.getLineEndOffset(8));
                    } catch (javax.swing.text.BadLocationException ebl) {
                        System.err.println("Bad location exception in sessionLog.");
                    }
                }
                sessionLog.setCaretPosition(sessionLog.getDocument().getLength());
            }
        });
    }
    
    @Override
    public void prn(String s) {
        //System.out.println(s);
        appendToLog(s);
    }
    
    @Override
    public void prn(Exception e) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString();
        appendToLog(sStackTrace);
    }
}
