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
import java.util.StringTokenizer;
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
        String[] cards = leerArchivo("cards.txt");
        List users = leerArchivo_list("users.txt");
        Map<String, String> card_value = new HashMap<String, String>();
        

	// Implementation of the query interface 
        @Override
	public String[] get_items() 
					throws RemoteException 
	{
		//String result[] = items; 
                
		return items; 
	} 
        @Override
        public String[] get_prices() 
					throws RemoteException 
	{
		//String result[] = prices;

		return prices; 
	}
        @Override
        public String[] get_quantity() 
					throws RemoteException 
	{
		return quantity; 
	}
        
        @Override
        public Map<String, String> get_cards() 
					throws RemoteException 
	{
                String cadena = cards[0] + ",";
                
                for(int i=1; i<cards.length; i++){ 
                    if(i != cards.length - 1){
                        cadena = cadena + cards[i] + ",";
                    }
                    else{
                        cadena = cadena + cards[i];
                    }
                }
                String[] values = cadena.split(",");
                for(int i=0; i<(cards.length * 2); i+=2){
                    card_value.put(values[i], values[i+1]);
                }

		return card_value; 
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
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter("quantity.txt");
                    } catch (IOException ex) {
                        Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //String to_write = username + "," + hash;
                    try {
                        for(String q : quantity){
                            fw.write(q);
                            fw.write("\r\n");
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        fw.close();
                    } catch (IOException ex) {
                        Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    return false;
                }
            }
            
            return true;            
        }
        
        @Override
        public Boolean register(String username, String hash){

            FileWriter fw = null;
            try {
                fw = new FileWriter("users.txt", true);
            } catch (IOException ex) {
                Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
            }
            String to_write = username + "," + hash;
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
        
        @Override
        public Boolean login(String username, String hash, String card){
            System.out.println("entradas-->" + "u->" + username + "h->" + hash + "c->" + card);
            Boolean user_flag = false;
            Boolean card_flag = false;
            Map<String, String> users_map = new HashMap<String, String>();
            
            String cadena = users.get(0) + ",";
                
            for(int i=1; i<users.size(); i++){ 
                if(i != users.size() - 1){
                    cadena = cadena + users.get(i) + ",";
                }
                else{
                    cadena = cadena + users.get(i);
                }
                }
            String[] values = cadena.split(",");
            for(int i=0; i<(users.size() * 2); i+=2){
                users_map.put(values[i], values[i+1]);
            }
            Iterator it3 = users_map.keySet().iterator();
                        System.out.println("user     hash");
                        while(it3.hasNext()){
                          String key = (String) it3.next();
                          System.out.println(key +"\t  " + users_map.get(key));
                        }
                
            Iterator it = users_map.keySet().iterator();
            while(it.hasNext()){
                String key = (String) it.next();
                if(key.equals(username) && users_map.get(key).equals(hash)){
                    System.out.println("IGUALES TRUE user!");
                    user_flag = true;
                }
            }
            Iterator it2 = card_value.keySet().iterator();
            while(it2.hasNext()){
                String key = (String) it2.next();
                if(key.equals(card)){
                    System.out.println("IGUALES TRUE card!");
                    card_flag = true;
                }
            }
            
            if(user_flag == true && card_flag == true){
                return true;
            }
            else{
                return false;
            }
        }

} 

