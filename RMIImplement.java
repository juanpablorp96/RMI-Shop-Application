/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan Pablo
 */
// Java program to implement the Search interface 
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.*; 
import java.rmi.server.*; 
import java.util.List;
public class RMIImplement extends UnicastRemoteObject implements RMIInterface 
{ 
	// Default constructor to throw RemoteException 
	// from its parent constructor 
	RMIImplement() throws RemoteException 
	{ 
		super(); 
	}
        private static String[] leerArchivo(String nombreArchivo) {
        Path ruta = Paths.get(nombreArchivo);
        try {
            List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
            String[] lines = lineas.toArray(new String[lineas.size()]);
            return lines;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
        }
        String[] items = leerArchivo("items.txt");
        String[] prices = leerArchivo("prices.txt");
        String[] quantity = leerArchivo("quantity.txt");

	// Implementation of the query interface 
        @Override
	public String get_items(String search) 
					throws RemoteException 
	{ 
		String result; 
		if (search.equals("1")) 
			result = items[0]; 
		else
			result = "Not Found"; 

		return result; 
	} 
        @Override
        public String get_prices(String search) 
					throws RemoteException 
	{ 
		String result; 
		if (search.equals("1")) 
			result = prices[0]; 
		else
			result = "Not Found"; 

		return result; 
	}
        @Override
        public String get_quantity(String search) 
					throws RemoteException 
	{ 
		String result; 
		if (search.equals("1")) 
			result = quantity[0]; 
		else
			result = "Not Found"; 

		return result; 
	}
} 

