/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topology;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Philipp
 */
public class MainTopology implements Serializable
{
    private LinkedList<Process> pList;
    
    public MainTopology (LinkedList<Process> pList)
    {
        this.pList = pList;
    }

    public LinkedList<Process> getprocessList()
    {
        return pList;
    }

    public void setprocessList(LinkedList<Process> pList)
    {
        this.pList = pList;
    }
    
}
