package server;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Admin{

    private static Socket socket;
    private int port;
    private static String host;
    /**
     * Default constructor
     */
    public Admin(){
    }

    /**
     * Constructor with port as parameter
     * @param port to connect
     */
    public Admin(int port){
        this.socket = null;
        this.port = port;
    }

    /**
     * getter for socket
     * @return the socket that is being used
     */
    public static Socket getSocket() {
        return socket;
    }

    /**
     * Setter for socket
     * @param socket to use
     */
    public static void setSocket(Socket socket) {
        Admin.socket = socket;
    }

    /**
     * Getter for port
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Setter for port
     * @param port to use
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Getter for the host address
     * @return the host address of the server
     */
    public static String getHost() {
        return host;
    }

    /**
     * Setter for host
     * @param host to use
     */
    public static void setHost(String host) {
        Admin.host = host;
    }

    /**
     * This method connects with the sever using the random generated
     * password for the server and the host address of the server
     * @param password to login
     * @param host to login
     */
    public void login(String password, String host){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //initialize connection
        //TODO connect to the server with the given password and host address
    }

    /**
     * Requests all the events in the server
     * @return List<Event> with all events in the server
     */
    public List<Event> getEvents(){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //initialize connection
        //TODO call to server
        List<Event> list = new ArrayList<Event>();
        return list;
    }

    /**
     * Requests all the events in the server, ordered by date
     * @return List<Event> with all events in the server ordered by date
     */
    public List<Event> orderByCDate(){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //initialize connection
        List<Event> list = new ArrayList<Event>();
        //TODO call to server
        return list;
    }

    /**
     * Orders the events by last activity
     * @return the sorted list with events
     */
    public List<Event> orderByLastActivity(){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //initialize connection
        List<Event> list = new ArrayList<Event>();
        //TODO call to server
        return list;
    }

    /**
     * Orders events by title
     * @return the ordered list
     */
    public List<Event> orderByTitle(){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //initialize connection
        List<Event> list = new ArrayList<Event>();
        //TODO call to server
        return list;
    }

    /**
     * Gets an event and deletes it from the database of the server
     * @param event to delete
     */
    public void deleteEvent(Event event){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //initialize connection
        //TODO call to server with event id
        //should delete all related expenses and debts!!!
    }

    /**
     * Retreives all related information of an event and stores it
     * into a JSON file in the specified filepath
     * @param filepath to store the JSON
     * @param event to retrieve
     */
    public void exportEvents(String filepath, Event event){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //initialize connection
        //TODO call to server to get all events
        List<Event> list = null; //TODO receive all events
        //TODO translate to JSON -- probably call anothe method
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filepath));
            writer.write(String.valueOf(list));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a filepath to a JSON file from which it reads
     * events, expenses and debts and adds them to the database of the server
     * @param filepath of the JSON file specified by the admin
     */
    public void importEvents(String filepath) throws IOException {
        socket = new Socket(host, port);
        //initialize connection
        List<Event> events = new ArrayList<Event>();
        List<Expense>  expenses = new ArrayList<Expense>();
        List<Debt> debts = new ArrayList<Debt>();
        BufferedReader reader = null;
        String output = "";
        try {
            reader = new BufferedReader(new FileReader(filepath));
            output = String.valueOf(reader.read());
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //TODO send JSON to server to add the events into database.
    }
}
