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
public class ProductReference implements java.io.Serializable, interfaces.ProductInterface
{

    private String name;
    private Hashtable properties;

    public ProductReference(String name, Hashtable properties)
    {
        this.name = name;
        this.properties = properties;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
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

}
