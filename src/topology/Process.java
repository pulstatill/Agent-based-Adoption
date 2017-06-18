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
public class Process implements java.io.Serializable, interfaces.ProcessInterface
{
    private String name;
    private Hashtable properties;

    public Process(String name, Hashtable properties)
    {
        this.name = name;
        this.properties = properties;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Hashtable getProperties()
    {
        return properties;
    }

    public void setProperties(Hashtable properties)
    {
        this.properties = properties;
    }
    
}
