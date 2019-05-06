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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.*; 
import java.rmi.server.*; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        private static List<String> leerArchivo_list(String nombreArchivo) {
        Path ruta = Paths.get(nombreArchivo);
        try {
            List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
            //lineas.remove(lineas.size() - 1);
            //String[] lines = lineas.toArray(new String[lineas.size()]);
            return lineas;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
        }
        
        String[] items = leerArchivo("items.txt");
        String[] prices = leerArchivo("prices.txt");
        String[] quantity = leerArchivo("quantity.txt");      
        List users = leerArchivo_list("users.txt");
        

	// Implementation of the query interface 
        @Override
	public String[] get_items(String search) 
					throws RemoteException 
	{ 
		String[] result = null; 
		if (search.equals("1")){ 
			result = items; 
                }
                else{
			result[0] = "Not Found"; 
                }
		return result; 
	} 
        @Override
        public String[] get_prices(String search) 
					throws RemoteException 
	{ 
		String result[] = null; 
		if (search.equals("1")){ 
			result = prices;
                }
                else{
			result[0] = "Not Found"; 
                }

		return result; 
	}
        @Override
        public String[] get_quantity(String search) 
					throws RemoteException 
	{ 
		String result[] = null; 
		if (search.equals("1")) {
			result = quantity;
                }
                else{
			result[0] = "Not Found"; 
                }

		return result; 
	}
        
        @Override
        public Boolean check_out(Map<Integer, String> carrito){
            Map<Integer, String> car = new HashMap<Integer, String>();
            int new_quantity;
            car = carrito;
            
            Iterator it = carrito.keySet().iterator();
            System.out.println("Numero     Item        Precio        Cantidad");
            while(it.hasNext()){
                Integer key = (Integer) it.next();
                new_quantity = (Integer.parseInt(quantity[key - 1]) - Integer.parseInt(car.get(key)));
                if(new_quantity >= 0){
                    quantity[key - 1] = Integer.toString(new_quantity);
                }
                else{
                    return false;
                }
            }
            
            return true;            
        }
        
        @Override
        public Boolean register(String username, String hash, String card, String money){
            FileWriter fw = null;
            try {
                fw = new FileWriter("users.txt", true);
            } catch (IOException ex) {
                Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
            }
            String to_write = username + "," + hash + "," + card + "," + money;
                try {
                    fw.write(to_write);
                    fw.write("\r\n");
                    users.add(to_write);
                    for(int i = 0; i < users.size(); i++) {
                        System.out.println(users.get(i));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return true;
        }
} 

