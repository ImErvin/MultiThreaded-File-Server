# MultiThreaded File Server

This is my third year project for Object Orientated programming with Java.<br/>
This multi-threaded file server allows users to download files using a set of menu options.

<hr/>

### How it Works
This is divided into a Client Side and a Server Side.<br/>
The client is presented with a menu:<br/><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![alt tag](http://image.prntscr.com/image/93a7849f7ebb498da8fd65eee6df9cc1.png)<br/>
* Connect to Server: Allows the user to connect to the file server using a socket.
* Print File Listings: Allows the user to see the possible files they can download in the directory.
* Download File: Allows the user to enter the name of the file they would like to download.
* Exit: Allows the user to close the socket connection to the server.

<br/>

The Server side will constantly listen for new clients and create a client thread for new connections. A blocking queue exists among
client threads and each request they make is recorded and logged onto a file.
