import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class RestRequestSend {
    public static void main(String[] args) {
        System.out.println("Використання:");
        System.out.println("java RestRequestSend [протокол://веб-адреса[ метод_запиту[ тип_вмісту файл_із_тілом_запиту]]]");
        System.out.println("Приклад:");
        System.out.println("java RestRequestSend http://site085239.domain35287.com POST application/json RestRequestBody.json");
        boolean goOn = true;
        
        URL hp = null;
        String urlS = "http://www.google.com";
        try {
            if (args != null) {
                if (args.length > 0) {
                    urlS = args[0];
                }
            }
            hp = new URL(urlS);
            System.out.println("Порт: " + hp.getPort());
        } catch (MalformedURLException emu) {
            goOn = false;
            System.err.println("E: Неправильний URL.");
            emu.printStackTrace();
        }
        
        HttpURLConnection cnc = null;
        if (goOn) {
            try {
                cnc = (HttpURLConnection) hp.openConnection();
            } catch (IOException eio) {
                goOn = false;
                System.err.println("E: Неможливо відкрити з'єднання.");
                eio.printStackTrace();
            }
        }
        
        if (goOn) {
            if (args != null) {
                if (args.length > 1) {
                    try {
                        cnc.setRequestMethod(args[1]);
                    } catch (ProtocolException ep) {
                        goOn = false;
                        System.err.println("E: Неправильний метод запиту: \"" + args[1] + "\".");
                        ep.printStackTrace();
                    }
                }
            }
        }
        
        if (goOn) {
            if (args != null) {
                if (args.length > 2) {
                    cnc.setRequestProperty("Content-Type", args[2]);
                }
                
                if (args.length > 3) {
                    InputStream fStream = null;
                    BufferedReader br = null;
                    File file = new File(args[3]);
                    cnc.setRequestProperty(Serverochok.CONTENT_LENGTH, "" + file.length());
                    try {
                        fStream = new FileInputStream(args[3]);
                    } catch (FileNotFoundException efnf) {
                        goOn = false;
                        System.err.println("E: Файл не знайдений: \"" + args[3] + "\".");
                        efnf.printStackTrace();
                    }
                    
                    /*String s = "";
                    String request = "";
                    if (goOn) {
                        br = new BufferedReader(new InputStreamReader(fStream));
                        while ((s != null) && goOn) {
                            request += s;
                            try {
                                s = br.readLine();
                            } catch (IOException eio) {
                                goOn = false;
                                System.err.println("Не вдалося прочитати файл \"" + args[3] + "\".");
                                eio.printStackTrace();
                            }
                        }
                        if (goOn) {
                            try {
                                br.close();
                            } catch (IOException eio) {
                            }
                        }
                    }
                    if (goOn) {
                        cnc.setDoOutput(true);
                        cnc.setAllowUserInteraction(false);
                        PrintStream ps = null;
                        try {
                            ps = new PrintStream(cnc.getOutputStream());
                        } catch (IOException eio) {
                            goOn = false;
                            System.err.println("Не вдалося отримати потік виведення на сервер.");
                            eio.printStackTrace();
                        }
                        ps.print(request);
                        ps.close();
                    }*/
                    
                    byte[] bb = null;
                    if (goOn) {
                        try {
                            bb = Files.readAllBytes(file.toPath());
                        } catch (IOException eio) {
                            goOn = false;
                            System.err.println("Не вдалося прочитати файл \"" + args[3] + "\".");
                            eio.printStackTrace();
                        }
                    }
                    OutputStream outputStream = null;
                    if (goOn) {
                        cnc.setDoOutput(true);
                        cnc.setAllowUserInteraction(false);
                        try {
                            outputStream = cnc.getOutputStream();
                        } catch (IOException eio) {
                            goOn = false;
                            System.err.println("Не вдалося отримати потік виведення на сервер.");
                            eio.printStackTrace();
                        }
                    }
                    if (goOn) {
                        try {
                            outputStream.write(bb);
                        } catch (IOException eio) {
                            goOn = false;
                            System.err.println("Не вдалося передати запит на сервер.");
                            eio.printStackTrace();
                        }
                    }
                    if (goOn) {
                        try {
                            outputStream.flush();
                        } catch (IOException eio) {
                            goOn = false;
                            System.err.println("Не вдалося закрити потік виведення на сервер.");
                            eio.printStackTrace();
                        }
                    }
                }
            }
        }
        
        if (goOn) {
            System.out.println("Метод запиту: " + cnc.getRequestMethod());
            try {
                System.out.println("Код відповіді: " + cnc.getResponseCode());
                System.out.println("Відповідь: " + cnc.getResponseMessage());
            } catch (IOException eio) {
                goOn = false;
                System.err.println("E: Відповідь не отримана.");
                eio.printStackTrace();
            }
        }
        
        InputStream input = null;
        if (goOn) {
            Map<String, List<String>> hdMap = cnc.getHeaderFields();
            Set<String> hdFields = hdMap.keySet();
            System.out.println("\nЗаголовок (як ключі і значення):");
            for (String key: hdFields) {
                System.out.println("ключ " + key + ", значення: " + hdMap.get(key));
            }
            
            try {
                input = cnc.getInputStream();
            } catch (IOException eio) {
                goOn = false;
                System.err.println("E: Вхідний потік не отриманий.");
                eio.printStackTrace();
            }
        }
        
        if (goOn) {
            System.out.println("\nВміст:");
            int c = 0;
            try {
                c = input.read();
                System.out.print((char) c);
            } catch (IOException eio) {
                goOn = false;
                System.err.println("E: Помилка читання вхідного потоку.");
                eio.printStackTrace();
            }
            while ((c != -1) && goOn) {
                try {
                    c = input.read();
                    System.out.print((char) c);
                } catch (IOException eio) {
                    goOn = false;
                    System.err.println("E: Помилка читання вхідного потоку.");
                    eio.printStackTrace();
                }
            }
        }
        
        if (goOn) {
            System.out.println();
            try {
                input.close();
            } catch (IOException eio) {
                goOn = false;
                System.err.println("E: Неможливо закрити вхідний потік.");
                eio.printStackTrace();
            }
        }
    }
}

