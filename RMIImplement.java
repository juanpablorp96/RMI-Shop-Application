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
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
        List<List<String>> TX = new ArrayList<List<String>>();
        int indexTX_user = 0;
        

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
        public void cards_map() 
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
                System.out.println(cadena);   
                String[] values = cadena.split(",");
                for(String v : values){
                    System.out.println(v);  
                }
                for(int i=0; i<(cards.length * 2); i+=2){
                    card_value.put(values[i], values[i+1]);
                }
 
	}
        
        @Override
        public Map<String, String> get_cards() 
					throws RemoteException 
	{
           return card_value; 
        }
        
        @Override
        public int get_indexTX() 
					throws RemoteException 
	{                
		return indexTX_user; 
	}
        
        @Override
        public void set_indexTX() 
					throws RemoteException 
	{                
		indexTX_user++; 
	}
        
        @Override
        public Boolean check_out(Map<Integer, String> carrito, String card) throws RemoteException{
            int moneyInCard = 0, new_quantity, total_price = 0;
            
            System.out.println("flag 1");   
            
            // using for-each loop for iteration over Map.entrySet() 
            for (Map.Entry<String,String> entry : card_value.entrySet())  
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue());
            
            System.out.println("flag 2");
            
            for (Map.Entry<String,String> c : card_value.entrySet()){
                if(c.getKey().equals(card)){
                    moneyInCard = Integer.parseInt(c.getValue());
                }
            }
            
            System.out.println("flag 3");
            
            for (Map.Entry<Integer,String> c : carrito.entrySet()){
                total_price += Integer.parseInt(prices[c.getKey() - 1]) * Integer.parseInt(c.getValue());
            }
            
            System.out.println("flag 4");
            
            if(moneyInCard - total_price >= 0){
                
                Iterator it3 = carrito.keySet().iterator();
                while(it3.hasNext()){
                    Integer key3 = (Integer) it3.next();
                    new_quantity = (Integer.parseInt(quantity[key3 - 1]) - Integer.parseInt(carrito.get(key3)));
                    if(new_quantity >= 0){
                        quantity[key3 - 1] = Integer.toString(new_quantity);
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
                        
                        String new_value = Integer.toString(moneyInCard - total_price);
                        for (Map.Entry<String,String> c : card_value.entrySet()){
                            if(c.getKey().equals(card)){
                                c.setValue(new_value);
                            }
                        }
                        FileWriter fw2 = null;
                        try {
                            fw2 = new FileWriter("cards.txt");
                        } catch (IOException ex) {
                            Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            for (Map.Entry<String,String> c : card_value.entrySet()){
                                String to_write = c.getKey() + "," + c.getValue();
                                
                                fw2.write(to_write);
                                fw2.write("\r\n");
                            }

                        } catch (IOException ex) {
                            Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        try {
                            fw2.close();
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
            else{
                return false;
            }
            
        }
        
        public byte[] encrypt(String message) throws Exception {
            final MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
                    .getBytes("utf-8"));
            final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }

            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            final byte[] plainTextBytes = message.getBytes("utf-8");
            final byte[] cipherText = cipher.doFinal(plainTextBytes);
            // final String encodedCipherText = new sun.misc.BASE64Encoder()
            // .encode(cipherText);

            return cipherText;
        }
        
        public String decrypt(byte[] message) throws Exception {
            final MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
                    .getBytes("utf-8"));
            final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }

            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, key, iv);

            // final byte[] encData = new
            // sun.misc.BASE64Decoder().decodeBuffer(message);
            final byte[] plainText = decipher.doFinal(message);

            return new String(plainText, "UTF-8");
        }
        
        @Override
        public Boolean register(String username, String hash){
            byte[] codedtext = null;
            try {
                codedtext = encrypt(username);
            } catch (Exception ex) {
                Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
            }
            String username_str = Base64.getEncoder().encodeToString(codedtext);
            FileWriter fw = null;
            try {
                fw = new FileWriter("users.txt", true);
            } catch (IOException ex) {
                Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
            }
            String to_write = username_str + "," + hash;
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
                
            String decodedtext = null;
            Iterator it = users_map.keySet().iterator();
            while(it.hasNext()){
                String key = (String) it.next();

                byte[] bytes = Base64.getDecoder().decode(key);
                try {
                    decodedtext = decrypt(bytes);
                } catch (Exception ex) {
                    Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(decodedtext.equals(username) && users_map.get(key).equals(hash)){
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
        
        @Override
        public void create_transaction(int index, List<String> TX_i){
            
            TX.add(index, TX_i);
            
        }
        
        @Override
        public void add_operation(int index, String operation){
            
            TX.get(index).add(operation);
            
        }
        
        @Override
        public boolean rollbackValidation(int index){
            for(List<String> l : TX){
                for(String s : l){
                    System.out.println(s);
                }
                System.out.println("----------------------------------");
            }
            boolean valid = true;
            if(index == 0){
                return valid;
            }
            else{
                List<String> txs, current;
                current = TX.get(index);
                int count = index - 1;
                System.out.println("current->" + index);
                //for(int i=index-1; i<1; i=i-1){
                while(count >= 0){
                    System.out.println("i->" + count);
                    txs = TX.get(count);
                    for(String c : current){
                        for(String tx : txs){
                            if(tx.equals(c)){
                                valid = false;
                                return valid;
                            }
                        }
                    }
                    count = count - 1;
                }
                return valid;   
            }
        }
        
        @Override
        public void commitTX(int index){
            TX.get(index).clear();
        }
        
        @Override
        public void deposit(int money, String card){
            for (Map.Entry<String,String> c : card_value.entrySet()){
                if(c.getKey().equals(card)){
                    int new_value = Integer.parseInt(c.getValue()) + money;
                    c.setValue(Integer.toString(new_value));
                }
            }
            FileWriter fw = null;
            try {
                fw = new FileWriter("cards.txt");
            } catch (IOException ex) {
                Logger.getLogger(RMIImplement.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                for (Map.Entry<String,String> c : card_value.entrySet()){
                    String to_write = c.getKey() + "," + c.getValue();
                                
                    fw.write(to_write);
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

} 

