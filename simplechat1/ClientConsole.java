// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String name, String host, int port) 
  {
  	try{
  		client = new ChatClient(name,host,port,this);
  	}catch(IOException exception){
  		System.out.println("Error: Can't setup connection! Terminating Client.");
  		System.exit(1);
  	}
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    client.handleMessageFromClientUI("#login " + client.getID());
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        if(message.charAt(0) == '#'){
        	handleCommand(message);
        }else{
        	client.handleMessageFromClientUI(message);
        }
        
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method reads and executes command messages from users 
   * @param msg cmd sent by user
   */
  public void handleCommand(String msg){
  	int end = msg.length();
  	if(msg.contains("quit")){
  		client.quit();
  	}
  	else if( msg.contains("logoff")){
  		try{
  			client.closeConnection();
  		} catch(IOException e){
  		}
  		
  	}
  	else if(msg.contains("sethost")){
  		if(client.isConnected()){
  			System.out.println("You can't change the host while connected!");
  		}else{
  			String host = "";
  			for(int i=9; i<end;i++){
  				host += msg.charAt(i);
  			}
  			client.setHost(host);
        System.out.println("Host set to: " + host);
  		}
  	}
  	else if(msg.contains("setport")){
  		if(client.isConnected()){
  			System.out.println("You can't change the host while connected!");
  		}else{
  			String input = "";
  			for(int i=9; i<end;i++){
  				input += msg.charAt(i);
  			}
  			int port = Integer.parseInt(input);
  			client.setPort(port);
        System.out.println("Port set to: " + port);
  		}
  	}
  	else if(msg.contains("login")){
      if(client.isConnected()){
        System.out.println("Hey, you're already logged in!");
      }
      else{
    		try{
    			client.openConnection();
    		}
    		catch(IOException e){} 
      } 		
  	}
  	else if(msg.contains("gethost")){
  		System.out.println(client.getHost());
  	}
  	else if(msg.contains("getport")){
  		System.out.println(client.getPort());
  	}
  	else{
  		System.out.println("Sorry, that isn't a recognizable command");
  	}

  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println(message);
  }



  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number
    String username = "";

    try{username = args[0];}
    catch(ArrayIndexOutOfBoundsException e){
      System.out.println("You need a login ID");
      System.exit(0);
    }

    try
    {
      host = args[1];
      port = Integer.parseInt(args[2]);
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
      port = DEFAULT_PORT;
    }

    ClientConsole chat= new ClientConsole(username, host, port);

    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
