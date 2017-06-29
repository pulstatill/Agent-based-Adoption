/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductRequestAnalyzer;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Philipp
 */
public class ProductRequest implements interfaces.ProductRequestInterface, Serializable
{

    private LinkedList<Object> pList;

    public ProductRequest(LinkedList<Object> pList)
    {
        this.pList = pList;
    }

    @Override
    public LinkedList getpList()
    {
        return pList;
    }

    @Override
    public void setpList(LinkedList<Object> pList)
    {
        this.pList = pList;
    }

}
