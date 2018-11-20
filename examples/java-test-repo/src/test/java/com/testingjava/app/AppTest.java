package com.testingjava.app;

import junit.framework.TestCase;

/**
 * Unit test for our simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * Making a simple test
     */
    public void testApp()
    {
        assertEquals(App.printIt("Bob"), "Hello Bob");
    }
}
