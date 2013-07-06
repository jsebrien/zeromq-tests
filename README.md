zeromq-tests
============
This repo contains code allowing to perform multithreaded request-reply between a php webpage, and a java application, using ZeroMQ.

Installation prerequisites
-------

To run these tests, you need:
- ZeroMQ 3.2.3
- JDK >=7 64 bits + jzmq (java extension for zmq)
- Apache webserver + php extension + pzmq (zeromq extension for php)
- Git
- Maven
- Ubuntu 13.04 64 bits

The `installAndStartServer.sh` script file takes care of installing every software needed to make the test case work.

The procedure steps are described in greater detail on hashmade.fr blog (use the ZeroMQ tag to find the corresponding post), or follow this 
<a href="http://hashmade.fr/zeromq-communicate-from-php-to-java-with-multithreaded-request-reply" target="_blank" >link</a>

Download the project
-------

git clone https://github.com/jsebrien/zeromq-tests.git

Compile the project
-------

mvn package

Run test-case
-------

mvn exec:java -Dexec.mainClass="blog.hashmade.zeromq.main.RequestReplyServer" 

Use cases
-------

###  Simple form submission

In this test, some form fields are posted and used to create a TransportLayerMessage bean, 
which is jsonifed, and sent to a Java backend application, using ZeroMQ messaging library.








-----
