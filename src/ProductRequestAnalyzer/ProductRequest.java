/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductRequestAnalyzer;

import java.util.LinkedList;

/**
 *
 * @author Philipp
 */
public class ProductRequest implements interfaces.ProductRequestInterface
{
    private LinkedList pList;

    public ProductRequest(LinkedList pList)
    {
        this.pList = pList;
    }

    @Override
    public LinkedList getpList()
    {
        return pList;
    }

    @Override
    public void setpList(LinkedList pList)
    {
        this.pList = pList;
    }
    
    
}
