package ppt2324.practica4;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
 * Alumno 1: 
 * Alumno 2:
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

			String line = bis.readLine();
			while (!(line=bis.readLine()).equals("") && line!=null) {
				System.out.println("Le�do["+line.length()+"]: "+line);
				dos.write(("ECO " + line + "\r\n").getBytes());
				dos.flush();
			}
		} catch (IOException ex) {
			Logger.getLogger(HttpConnection.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				dos.close();
				socket.close();
			} catch (IOException ex) {
				Logger.getLogger(HttpConnection.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

}
