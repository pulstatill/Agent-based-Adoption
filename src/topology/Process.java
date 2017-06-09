/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topology;

import java.util.Hashtable;

/**
 *
 * @author Philipp
 */
public class Process
{
    private String name;
    private Hashtable properties;

    public Process(String name, Hashtable properties)
    {
        this.name = name;
        this.properties = properties;
    }
    
}
