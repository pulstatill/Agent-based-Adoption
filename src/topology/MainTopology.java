/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topology;

import interfaces.ProcessInterface;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Philipp
 */
public class MainTopology implements Serializable, interfaces.TopologyInterface
{
    private LinkedList<Process> pList;
    
    public MainTopology (LinkedList<Process> pList)
    {
        this.pList = pList;
    }

    @Override
    public LinkedList<ProcessInterface> getprocessList()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setprocessList(LinkedList<ProcessInterface> pList)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    
}
