package Guiao7;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



class ContactListReader {
    private List<Contact> contacts;
    private Lock lock = new ReentrantLock();

    public ContactListReader() {
        contacts = new ArrayList<>();

        contacts.add(new Contact("John", 20, 253123321, null, new ArrayList<>(Arrays.asList("john@mail.com"))));
        contacts.add(new Contact("Alice", 30, 253987654, "CompanyInc.", new ArrayList<>(Arrays.asList("alice.personal@mail.com", "alice.business@mail.com"))));
        contacts.add(new Contact("Bob", 40, 253123456, "Comp.Ld", new ArrayList<>(Arrays.asList("bob@mail.com", "bob.work@mail.com"))));
    }

    // @TODO
    public void addContact (DataInputStream in) throws IOException {
        Contact contact = Contact.deserialize(in);
        this.lock.lock();
        try{
            this.contacts.add(contact);
        } finally {
            this.lock.unlock();
        }
    }

    // @TODO
    public void getContacts (DataOutputStream out) throws IOException {
        lock.lock();
        try{
            out.writeInt(contacts.size());
            out.flush();
            for(Contact c : contacts){
                c.serialize(out);
            }
        }finally{
            lock.unlock();
        }
    }

    public void printContacts(){
        for (Contact c : this.contacts){
            System.out.println(c.toString());
        }
    }
    
}

class WriterWorker implements Runnable{
    private Socket socket;
    private ContactListReader contactList;

    public WriterWorker (Socket socket, ContactListReader contactList) {
        this.socket = socket;
        this.contactList = contactList;
    }

    // @TODO
    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));

            boolean isOpen = true;
        
            while(isOpen){
                this.contactList.addContact(in);
                
            }
            this.contactList.printContacts();

            socket.shutdownInput();
            socket.close();

            System.out.println("Connection closed.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class ReaderWorker implements Runnable {
    private Socket socket;
    private ContactListReader contactList;

    public ReaderWorker(Socket socket, ContactListReader contactList){
        this.socket = socket;
        this.contactList = contactList;
    }

    public void run(){
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
            this.contactList.getContacts(out);

            socket.shutdownOutput();
            socket.close();
            
        } catch (IOException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}

class ServerSocketWriterWorker implements Runnable {
    private ServerSocket serverSocket;
    private ContactListReader contactList;


    public ServerSocketWriterWorker(int port, ContactListReader contactList) throws IOException{
        this.serverSocket = new ServerSocket(port);
        this.contactList = contactList;
    }

    public void run(){
        while(true){
            Socket socket;
            try{
                socket = serverSocket.accept();
                Thread worker = new Thread(new WriterWorker(socket, contactList));
                worker.start();

            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}

class ServerSocketReaderWorker implements Runnable{
    private ServerSocket serverSocket;
    private ContactListReader contactList;

    public ServerSocketReaderWorker(int port, ContactListReader contactList) throws IOException{
        this.serverSocket = new ServerSocket(port);
        this.contactList = contactList;
    }

    public void run(){
        while(true){
            Socket socket;
            try{
                socket = this.serverSocket.accept();
                Thread worker = new Thread(new ReaderWorker(socket, contactList));
                worker.start();

            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}



public class ServerReader {

    public static void main (String[] args) throws IOException {
        ContactListReader contactList = new ContactListReader();

        Thread writer_worker = new Thread(new ServerSocketWriterWorker(12345, contactList));
        Thread reader_worker = new Thread(new ServerSocketReaderWorker(34567, contactList));

        writer_worker.start();
        reader_worker.start();

    }

}