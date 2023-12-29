package ppt2324.practica4;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Protocolos de Transporte
 * Grado en Ingenier�a Telem�tica 
 * Dpto. Ingen�er�a de Telecomunicaci�n Univerisdad de Ja�n
 *
 *******************************************************
 * Pr�ctica 4. 
 * Fichero: HttpConnection.java
 * Versi�n: 1.0 
 * Curso: 2023/2024
 * Descripci�n: Clase sencilla de atenci�n al protocolo HTTP/1.1 
 * Autor: Juan Carlos Cuevas Mart�nez
 *
 ******************************************************
 * Alumno 1: David Elbal Ruiz
 * Alumno 2: Francisco Sánchez Orozco
 *
 ******************************************************/
public class HttpConnection implements Runnable {

	Socket socket = null;

public HttpConnection(Socket s) {
		socket = s;
	}

	@Override
	public void run() {
		DataOutputStream dos = null;
		try {
			System.out.println("Starting new HTTP connection with " + socket.getInetAddress().toString());
			dos = new DataOutputStream(socket.getOutputStream());
			dos.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
			dos.flush();
			BufferedReader bis = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String line = bis.readLine();//linea de peticion
                    String partes[] = line.split(" ");
                    if (partes.length == 3) {
                          System.out.println("Cabecera host:[" +partes[1]+"]: ");
                        if (partes[0].compareToIgnoreCase("get") == 0) {
                            while (!(line = bis.readLine()).equals("") && line != null) {
                                System.out.println("Leido[" + line.length() + "]: " + line);
                                //buscar cabecera host
                                //dos.write(("ECO " + line + "\r\n").getBytes());
                                //dos.flush();
                            }

                            //abrir archivo
                            byte[] data = readFile(partes[1]);
                            
                            if(data == null){
                                
                            }else{
                            dos.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                            dos.write(("Content_Type:"+getContentType(partes[1])+"\r\n").getBytes());
                            dos.write(("Content_Length:"+data.length+"\r\n\r\n").getBytes());
                            dos.flush();
                            dos.write("<html><body><h1>HOLA</h1></body></html>".getBytes());
                            dos.write(data);
                            dos.flush();
                            }
                        } else {
                            dos.write("HTTP/1.1 405 Method not allowed\r\n\r\n".getBytes());
                            dos.flush();
                        }
                    } else {
                        dos.write("HTTP/1.1 400 Bad request\r\n\r\n".getBytes());
                        dos.flush();
                    }
		}catch(FileNotFoundException ex){
                    try {
                        dos.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                        dos.flush();
                    } catch (IOException ex1) {
                        System.err.println("Error "+ex1.getMessage());
                       
                    }
                    
                }
             
                catch(IOException ex2){
                    try {
                        dos.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                        dos.flush();
                    } catch (IOException ex1) {
                        System.err.println("Error "+ex1.getMessage());
                       
                    }}finally {
			try {
				dos.close();
				socket.close();
			} catch (IOException ex) {
				Logger.getLogger(HttpConnection.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}
    protected byte[] readFile(String path) throws FileNotFoundException,IOException{
        File f=new File("."+path);//donde metes la pagina es donde esta la carpeta de www y nsq mas de la practica
        FileInputStream fis=new FileInputStream(f);
        byte []datos=new byte[(int)f.length()];
        fis.read(datos);
        return datos;
        // return ("<html><body><h1>Hola "+path+"</h1></body></html>").getBytes();
    }
    protected String getContentType(String path){
        if(path.endsWith(".html") || path.endsWith(".htm")){
            return "text/html";
        }else if(path.endsWith(".jpeg") || path.endsWith(".jpg")){
            return "image/jpeg";
           }else if(path.endsWith(".css")){
                        return "text/css";
                    }else{
                    String[] n = path.split(".");
                    if(n.length>=2){
                        return ("application/"+n[n.length-1]);
                        
                    }
                }
        return "";
                
        }

}
