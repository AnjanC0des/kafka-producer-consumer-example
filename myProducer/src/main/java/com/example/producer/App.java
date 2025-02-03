package com.example.producer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "APP INIT." );
	MyProducer p=new MyProducer();
	p.publish();
	System.out.println("FINISH");
    }
}
