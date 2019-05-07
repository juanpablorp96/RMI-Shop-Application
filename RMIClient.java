/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan Pablo
 */
//program for client application 
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.*; 
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
public class RMIClient 
{
        private static String bytesToHex(byte[] hash) {
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
            }
            return hexString.toString();
        }
        
        private static void init(RMIInterface access) throws RemoteException{
            Map<String, String> cards;
            cards = access.get_cards();
            while(true){
                String value; 
                Scanner input = new Scanner(System.in);
                
                System.out.println("1. Registrarse : ");
                System.out.println("2. Iniciar sesión : ");

                //while(true){
                
                value = input.next();
		try
		{ 
                    if(value.equals("1")){
                        Boolean answer;
                        String username, password, hash;
                        System.out.println("Ingrese un nombre de usuario : ");
                        username = input.next();
                        System.out.println("Ingrese una contraseña : ");
                        password = input.next();

                        
                        // hasheo de la contraseña
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                        hash = bytesToHex(encodedhash);
                        
                        answer = access.register(username, hash);
                        if(answer == true){
                            System.out.println("Registrado exitosamente");
                            shopping(access);
                        }
                        else{
                            System.out.println("Error al registrarse");
                        }
                        
                    }
                    
                    if(value.equals("2")){
                        Boolean answer;
                        String username, password, card, hash;
                        System.out.println("Ingrese nombre de usuario : ");
                        username = input.next();
                        System.out.println("Ingrese contraseña : ");
                        password = input.next();
                        System.out.println("Ingrese numero de tarjeta : ");
                        card = input.next();
                        
                        // hasheo de la contraseña
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                        hash = bytesToHex(encodedhash);
                        
                        answer = access.login(username, hash, card);
                        if(answer == true){
                            System.out.println("Bienvenido");
                            shopping(access);
                        }
                        if(answer == false){
                            System.out.println("Error algunos datos no son correctos");
                        }
                        
                    }
                    
                    
                    
		} 
		catch(Exception ae) 
		{ 
			System.out.println(ae); 
		} 
                //}
            }
        }
        private static void shopping(RMIInterface access) throws RemoteException{
            String[] items, prices, quantity;
            Map<Integer, String> carrito = new HashMap<Integer, String>();
            items = access.get_items(); 
            prices = access.get_prices();
            quantity = access.get_quantity();
            Scanner input = new Scanner(System.in);
            String value;
            System.out.println("1. Ver catalogo : ");

                //while(true){
                
                value = input.next();
		
                    if(value.equals("1")){
            System.out.println("Numero     Item        Precio        Disponibles");
                        int i = items.length;
                        for(int k = 0;k<i;k++)
                        {
                            System.out.println(k+1 +"\t  " + items[k] +"\t  " + prices[k] +"\t  " + "\t  " + quantity[k]);
                        }
                        Boolean nextItem = true;
                        while(nextItem){
                            System.out.println("Escoja un item para agregar al carrito de compras...");
                            int item = input.nextInt();
                            Boolean addItem = true;
                            while (addItem){
                                System.out.println("¿Cuantos desea?..."); //valida si hay suficientes disponibles
                                String num = input.next();
                                if(Integer.parseInt(num) <= Integer.parseInt(quantity[item - 1])){
                                    carrito.put(item, num);
                                    addItem = false;
                                    System.out.println("Item agregado exitosamente");
                                    System.out.println("¿Desea agregar mas? 1. Si  2. No");
                                    String next = input.next();
                                    if(next.equals("2")){
                                        nextItem = false;
                                    }
                                }
                                else{
                                    System.out.println("No hay suficientes items disponibles, ingrese una cantidad inferior...");
                                } 
                            }
                        }
                        // Imprimir map
                        System.out.println("Su carrito de compras es:");
                        Iterator it = carrito.keySet().iterator();
                        System.out.println("Numero     Item        Precio        Cantidad");
                        while(it.hasNext()){
                          Integer key = (Integer) it.next();
                          System.out.println(key +"\t  " + items[key - 1] +"\t  " + prices[key - 1] +"\t  " + carrito.get(key));
                        }
                        
                        System.out.println("¿Desea eliminar un item del carrito? 1. Si, eliminar  2. No, finalizar compra");
                        String delete = input.next();
                        
                        if(delete.equals("1")){
                            Boolean delete_next = true;
                            while(delete_next){
                                System.out.println("Ingrese el numero");
                                Integer item_del = input.nextInt();
                                carrito.remove(item_del);
                                System.out.println("Elemento eliminado del carrito exitosamente");
                                System.out.println("Desea eliminar otro item? 1. Si  2. No, finalizar compra");
                                String continue_del = input.next();
                                if(continue_del.equals("2")){
                                    delete_next = false;
                                    delete = "2";
                                }
                            }
                            System.out.println("Su carrito de compras es:");
                            Iterator it2 = carrito.keySet().iterator();
                            System.out.println("Numero     Item        Precio        Cantidad");
                            while(it2.hasNext()){
                              Integer key = (Integer) it2.next();
                              System.out.println(key +"\t  " + items[key - 1] +"\t  " + prices[key - 1] +"\t  " + carrito.get(key));
                            }
                        }
                        Boolean check;
                        if(delete.equals("2")){
                            check = access.check_out(carrito);
                            if(check == true){
                                System.out.println("Compra realizada exitosamente!");
                                System.out.println("Ha sido regresado al menu...");
                                shopping(access);
                            }
                            else{
                                System.out.println("Error, ya no hay unidades suficientes");
                                System.out.println("Ha sido regresado al menu...");
                                shopping(access);
                            }
                        }
                    }
            
        }
	public static void main(String args[]) throws NotBoundException, MalformedURLException, RemoteException 
	{
            RMIInterface access = (RMIInterface)Naming.lookup("rmi://localhost:1900" + "/distribuidos");
            init(access);
	} 
} 

