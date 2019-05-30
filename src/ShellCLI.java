public class ShellCLI implements Shell {
    @Override
    public void prn(String s) {
        System.out.println(s);
    }
    
    @Override
    public void prn(Exception e) {
        e.printStackTrace();
    }
}
