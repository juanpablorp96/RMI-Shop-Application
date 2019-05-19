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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

            access.cards_map();
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
                            init(access);
                        }
                        else{
                            System.out.println("Error al registrarse");
                            init(access);
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
                            shopping(access, card);
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
        private static void shopping(RMIInterface access, String card) throws RemoteException{
            String[] items, prices, quantity;
            Map<String, String> card_value = new HashMap<String, String>();
            int indexTX;
            Map<Integer, String> carrito = new HashMap<Integer, String>();
            List<String> TX_i = new ArrayList<String>();
            String operation;
            Scanner input = new Scanner(System.in);
            String value;
            System.out.println("1. Ver catalogo : ");
            System.out.println("2. Recargar tarjeta : ");

                //while(true){
                
                value = input.next();
		
                    if(value.equals("1")){
                        indexTX = access.get_indexTX();
                        access.set_indexTX();
                        access.create_transaction(indexTX, TX_i);
                        items = access.get_items();
                        prices = access.get_prices();
                        quantity = access.get_quantity();
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
                            String s_item = Integer.toString(item);
                            operation = "W," + s_item;
                            access.add_operation(indexTX, operation);
                            System.out.println("index->" + indexTX + "operation->" + operation);
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
                        
                        
                        while(true){
                            // Imprimir map
                            System.out.println("Su carrito de compras es:");
                            Iterator it = carrito.keySet().iterator();
                            System.out.println("Numero     Item        Precio        Cantidad");
                            while(it.hasNext()){
                              Integer key = (Integer) it.next();
                              System.out.println(key +"\t  " + items[key - 1] +"\t  " + prices[key - 1] +"\t  " + "\t  " + carrito.get(key));
                            }


                            System.out.println("¿Desea modificar un item del carrito? 1. Si, eliminar  2. Si, cambiar cantidad 3. No, finalizar compra");
                            String fix = input.next();

                            if(fix.equals("1")){

                                System.out.println("Ingrese el numero del item");
                                Integer item_del = input.nextInt();
                                carrito.remove(item_del);
                                System.out.println("Elemento eliminado del carrito exitosamente");
                            }

                            if(fix.equals("2")){

                                System.out.println("Ingrese el numero del item");
                                Integer item_set = input.nextInt();
                                System.out.println("La cantidad actual es -> " + carrito.get(item_set));
                                
                                
                                Boolean fixQuantity = true;
                                while (fixQuantity){
                                    System.out.println("Ingrese la nueva cantidad");
                                    String new_quantity = input.next();
                                    if(Integer.parseInt(new_quantity) <= Integer.parseInt(quantity[item_set - 1])){
                                        if(carrito.replace(item_set, carrito.get(item_set), new_quantity) == true){
                                            System.out.println("Cantidad modificada exitosamente");
                                            fixQuantity = false;
                                        }
                                        else{
                                            System.out.println("Error, no se pudo hacer el cambio");
                                        }
                                    }
                                    else{
                                        System.out.println("No hay suficientes items disponibles, ingrese una cantidad inferior...");
                                    } 
                                }
                                

                            }

                            Boolean check = false;
                            if(fix.equals("3")){
                                if(access.rollbackValidation(indexTX)){
                                    access.commitTX(indexTX);
                                    check = access.check_out(carrito, card);
                                    System.out.println("Validación hacia atras -> EXITOSA");
                                    if(check == true){
                                    System.out.println("Compra realizada exitosamente!");
                                    System.out.println("Ha sido regresado al menu...");
                                    shopping(access, card);
                                    }
                                    else{
                                        System.out.println("No tiene dinero suficiente en la tarjeta...");
                                        System.out.println("Ha sido regresado al menu...");
                                        shopping(access, card);
                                    }
                                }
                                else{
                                    access.commitTX(indexTX);
                                    System.out.println("Validación hacia atras -> ABORTADA");
                                    System.out.println("Ha sido regresado al menu...");
                                    shopping(access, card);
                                }
                            }
                        }
                    }
                    
                    if(value.equals("2")){
                        card_value = access.get_cards();
                        System.out.println("Cantidad actual -> " + card_value.get(card));
                        System.out.println("Ingrese la cantidad que desea recargar...");
                        int money = input.nextInt();
                        access.deposit(money, card);
                        System.out.println("Recarga exitosa!");
                        card_value = access.get_cards();
                        System.out.println("Cantidad actual -> " + card_value.get(card));
                        shopping(access, card);
                    }
            
        }
	public static void main(String args[]) throws NotBoundException, MalformedURLException, RemoteException 
	{
            RMIInterface access = (RMIInterface)Naming.lookup("rmi://localhost:1900" + "/distribuidos");
            init(access);
	} 
} 

