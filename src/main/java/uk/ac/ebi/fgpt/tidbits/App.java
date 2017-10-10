package uk.ac.ebi.fgpt.tidbits;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        System.out.println("This is a test for stupid developers");

        ArrayList<SimpleDataAttribute> myList = new ArrayList<SimpleDataAttribute>();

        SimpleDataAttribute attribute = new SimpleDataAttribute(0);

        attribute.addVariable("?foo");

        myList.add(attribute);


        attribute.addVariable("?bar");

        attribute.addString("disease");


        System.out.println(myList.get(0).getIndex());
        System.out.println(myList.get(0).getOpplVariables().get(0));
        System.out.println(myList.get(0).getOpplVariables().get(1));
        System.out.println(myList.get(0).getPermissibleStrings().get(0));

    }
}
