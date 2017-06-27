/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package references;

import java.util.Hashtable;

/**
 *
 * @author Philipp
 */
public class ProcessRefercence implements java.io.Serializable, interfaces.ProcessInterface
{

    private String name;
    private Hashtable properties;
    private String shortname;

    public ProcessRefercence(String name, Hashtable properties)
    {
        this.shortname = name;
        this.properties = properties;
    }
    public ProcessRefercence(String shortname, String uuid, Hashtable properties)
    {
        this.shortname = shortname;
        this.name = shortname + uuid;
        this.properties = properties;
    }

    @Override
    public String getName()
    {
        return shortname;
    }

    @Override
    public void setName(String name)
    {
        this.shortname = name;
    }

    @Override
    public Hashtable getProperties()
    {
        return properties;
    }

    @Override
    public void setProperties(Hashtable properties)
    {
        this.properties = properties;
    }

    @Override
    public String getFullName()
    {
        return name;
    }

}
