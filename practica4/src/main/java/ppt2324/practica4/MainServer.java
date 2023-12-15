package ppt2324.practica4;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Protocolos de Transporte
 * Grado en Ingenier�a Telem�tica
 * Dpto. Ingen�er�a de Telecomunicaci�n
 * Univerisdad de Ja�n
 *
 *******************************************************
 * Pr�ctica 4.
 * Fichero: MainServer.java
 * Versi�n: 1.0
 * Curso: 2023/2024
 * Descripci�n:
 * 	Servidor sencillo multi-hebra Socket TCP para atenci�n al protocolo HTTP/1.1
 * Autor: Juan Carlos Cuevas Mart�nez
 *
 ******************************************************
 * Alumno 1:
 * Alumno 2:
 *
 ******************************************************/
public class MainServer {
    
    static ServerSocket server=null;
    static final short TCP_PORT = 80;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            InetAddress serveraddr = InetAddress.getLocalHost();
            server = new ServerSocket(TCP_PORT,5,serveraddr);
            System.out.println("Simple HTTP/1.1. Server waiting in "+serveraddr+" port "+TCP_PORT);
            while(true){
                Socket s = server.accept();
                HttpConnection conn = new HttpConnection(s);
                new Thread(conn).start();
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    
}
