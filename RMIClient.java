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
import java.rmi.*; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
public class RMIClient 
{ 
	public static void main(String args[]) 
	{ 
		String answer_items, answer_prices, answer_quantity, value; 
                Scanner input = new Scanner(System.in);
                Map<Integer, String> carrito = new HashMap<Integer, String>();
                String[] items;
                String[] prices;
                String[] quantity;
                System.out.println("1. ver catalogo : ");

                while(true){
                
                value = input.next();
		try
		{ 
			// lookup method to find reference of remote object 
                    if(value.equals("1")){
			RMIInterface access = 
				(RMIInterface)Naming.lookup("rmi://localhost:1900" + "/geeksforgeeks"); 
			answer_items = access.get_items(value); 
                        answer_prices = access.get_prices(value);
                        answer_quantity = access.get_quantity(value);
                        System.out.println("Numero     Item        Precios        Disponibles");
                        items = answer_items.split(",");
                        prices = answer_prices.split(",");
                        quantity = answer_quantity.split(",");
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
                        System.out.println("Numero     Item        Cantidad");
                        while(it.hasNext()){
                          Integer key = (Integer) it.next();
                          System.out.println(key +"\t  " + items[key - 1] +"\t  " + carrito.get(key));
                        }
                        
                        System.out.println("¿Desea eliminar un item del carrito? 1. Si  2. No");
                        String delete = input.next();
                        
                        if(delete.equals("1")){
                            Boolean delete_next = true;
                            while(delete_next){
                                System.out.println("Ingrese el numero");
                                Integer item_del = input.nextInt();
                                carrito.remove(item_del);
                                System.out.println("Elemento eliminado del carrito exitosamente");
                                System.out.println("Desea eliminar otro item? 1. Si  2. No");
                                String continue_del = input.next();
                                if(continue_del.equals("2")){
                                    delete_next = false;
                                }
                            }
                            System.out.println("Su carrito de compras es:");
                            Iterator it2 = carrito.keySet().iterator();
                            System.out.println("Numero     Item        Cantidad");
                            while(it2.hasNext()){
                              Integer key = (Integer) it2.next();
                              System.out.println(key +"\t  " + items[key - 1] +"\t  " + carrito.get(key));
                            }
                        }
                        
                        
                    }
                    
                    
		} 
		catch(Exception ae) 
		{ 
			System.out.println(ae); 
		} 
                }
	} 
} 

