/**
* Web worker: an object of this class executes in its own new thread
* to receive and respond to a single HTTP request. After the constructor
* the object executes on its "run" method, and leaves when it is done.
*
* One WebWorker object is only responsible for one client connection. 
* This code uses Java threads to parallelize the handling of clients:
* each WebWorker runs in its own thread. This means that you can essentially
* just think about what is happening on one client at a time, ignoring 
* the fact that the entirety of the webserver execution might be handling
* other clients, too. 
*
* This WebWorker class (i.e., an object of this class) is where all the
* client interaction is done. The "run()" method is the beginning -- think
* of it as the "main()" for a client interaction. It does three things in
* a row, invoking three methods in this class: it reads the incoming HTTP
* request; it writes out an HTTP header to begin its response, and then it
* writes out some HTML content for the response content. HTTP requests and
* responses are just lines of text (in a very particular format). 
*
**/

import java.net.Socket;
import java.lang.Runnable;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.TimeZone;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class WebWorker implements Runnable
{

private Socket socket;

/**
* Constructor: must have a valid open socket
**/
public WebWorker(Socket s)
{
   socket = s;
}

/**
* Worker thread starting point. Each worker handles just one HTTP 
* request and then returns, which destroys the thread. This method
* assumes that whoever created the worker created it with a valid
* open socket object.
**/
public void run()
{
   System.err.println("Handling connection...");
   try {
      InputStream  is = socket.getInputStream();
      OutputStream os = socket.getOutputStream();

      //Retrieve User Requested URL for later methods.
      String location;
      location = readHTTPRequest(is);
      System.out.println("location-------: " + location);

      writeHTTPHeader(os,"text/html", location);
      writeContent(os, location);
      os.flush();
      socket.close();
   } catch (Exception e) {
      System.err.println("Output error: "+e);
   }
   System.err.println("Done handling connection.");
   return;
}

/**
* Read the HTTP request header.
**/
private String readHTTPRequest(InputStream is) throws Exception
{
   String line, local = " ";
   BufferedReader r = new BufferedReader(new InputStreamReader(is));
   
//   File file = new File("/C:\git\SimpleWebServer\p2-master\image.gif");
   //FileInputStream ar = new FileInputStream(file);
    
   /*reference from stackoverflow.com*/
//   Path path = file.toPath();
//   byte[] data = Files.readAllBytes(path);
   /**********************************/
    
   //output byte[] to screen
   //System.out.println("Bilal output: " + new String(data));

   while (true) {
      try {
         while (!r.ready()) Thread.sleep(1);
         line = r.readLine();
         System.err.println("Request line: ("+line+")");
         
         String localSpot = line.substring(0,3);
         if(localSpot.equals("GET")){
            local = line.substring(4);
            local = local.substring(0, local.indexOf(" "));
            System.err.println("Requested file is: " +local);
         }

         if (line.length()==0) break;
      } catch (Exception e) {
         System.err.println("Request error: "+e);
         break;
      }
   }
   return local;
}

/**
* Write the HTTP header lines to the client network connection.
* @param os is the OutputStream object to write to
* @param contentType is the string MIME content type (e.g. "text/html")
**/
private void writeHTTPHeader(OutputStream os, String contentType, String location) throws Exception
{
   Date d = new Date();
   DateFormat df = DateFormat.getDateTimeInstance();
   df.setTimeZone(TimeZone.getTimeZone("GMT"));

   File x = new File(location);
   //file exists
   if(x.exists() && !x.isDirectory()){
      os.write("HTTP/1.1 200 OK\n".getBytes());
      os.write("Date: ".getBytes());
      os.write((df.format(d)).getBytes());
      os.write("\n".getBytes());
      os.write("Server: Jon's own server\n".getBytes());
      //os.write("Last-Modified: Wed, 25 September 2019 23:11:55 GMT\n".getBytes());
      //os.write("Content-Length: 438\n".getBytes()); 
      os.write("Connection: close\n".getBytes());
      os.write("Content-Type: ".getBytes());
      os.write(contentType.getBytes());
      os.write("\n\n".getBytes()); 
           
// HTTP header ends with 2 newlines
      
   }
   //file does not exist
   else{
      os.write("HTTP/1.1 404 Not Found\n".getBytes());   
      os.write("Date: ".getBytes());
      os.write((df.format(d)).getBytes());
      os.write("\n".getBytes());
      os.write("Server: Jon's very own server\n".getBytes());
      //os.write("Last-Modified: Wed, 25 Sep 2019 23:11:55 GMT\n".getBytes());
      //os.write("Content-Length: 438\n".getBytes()); 
      os.write("Connection: close\n".getBytes());
      os.write("Content-Type:".getBytes());
      os.write(contentType.getBytes());
      os.write("\n\n".getBytes());
 
// HTTP header ends with 2 newlines
   }
   return;
}

/**
* Write the data content to the client network connection. This MUST
* be done after the HTTP header has been written out.
* @param os is the OutputStream object to write to
**/
private void writeContent(OutputStream os, String location) throws Exception
{
   //Remove file directory "/" from beginning
   location = location.substring(1);
   
   File x = new File(location);
   System.out.println("Location " + location);
   System.out.println("Exists: " + x.exists());
   System.out.println("IsDirectory: " + x.isDirectory());
   if(x.exists() && !x.isDirectory()){
      FileInputStream stream = new FileInputStream(location);
      BufferedReader r = new BufferedReader(new InputStreamReader(stream));

      String filex;
      while ((filex = r.readLine()) != null){
         if(filex.equals("<cs371date>")){

String pattern = "yyyy-MM-dd";
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

String date = simpleDateFormat.format(new Date());
System.out.println(date);


                     }
         if(filex.equals("<cs371server>")){
            os.write("Bilal's Server.".getBytes()); 
         }
         //os.write(filex.getBytes());
      }
      os.write(Files.readAllBytes(x.toPath()));
       
      r.close();
   }
   //else if file does not exist, display "404 Error"
   else{
      os.write("<h3>Error: 404 not Found</h3>".getBytes());

   }
}

} // end class
