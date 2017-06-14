/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author philipp
 */
public class Debugger
{
    private static boolean isEnabled()
    {
        return true;
    }
    
    public static void log(String out)
    {
        if(isEnabled())
            System.out.println(out);
    }
}
