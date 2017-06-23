/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.Hashtable;

/**
 *
 * @author Philipp
 */
public interface ProcessInterface
{
    public String getName();
    public void setName(String name);
    public Hashtable getProperties();
    public void setProperties(Hashtable properties);
    public String getFullName();
            
}
