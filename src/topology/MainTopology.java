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

    private LinkedList<ProcessInterface> pList;

    public MainTopology(LinkedList<ProcessInterface> pList)
    {
        this.pList = pList;
    }

    @Override
    public LinkedList<ProcessInterface> getprocessList()
    {
        return this.pList;
    }

    @Override
    public void setprocessList(LinkedList<ProcessInterface> pList)
    {
        this.pList = pList;
    }

}
