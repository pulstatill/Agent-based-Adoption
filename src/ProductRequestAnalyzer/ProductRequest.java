/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductRequestAnalyzer;

import java.util.LinkedList;
import topology.Process;

/**
 *
 * @author Philipp
 */
public class ProductRequest implements java.io.Serializable
{
    private LinkedList pList;

    public ProductRequest(LinkedList pList)
    {
        this.pList = pList;
    }

    public LinkedList getpList()
    {
        return pList;
    }

    public void setpList(LinkedList pList)
    {
        this.pList = pList;
    }
    
    
}
