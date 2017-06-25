/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package references;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Philipp
 */
public class TopologyReference implements Serializable, interfaces.TopologyInterface
{

    private LinkedList<String> pList = new LinkedList<>();
    private LinkedList<String> pListFull;

    public TopologyReference(LinkedList<String> old)
    {
        for(int i = 0; i < old.size(); i++)
        {
            this.pList.addLast(old.get(i));
        }
        this.pList = old;
    }

    @Override
    public LinkedList<String> getprocessList()
    {
        return this.pList;
    }

    @Override
    public void setprocessList(LinkedList<String> pList)
    {
        this.pList = pList;
    }

    @Override
    public LinkedList<String> getFullNames()
    {
        return pListFull;
    }



}
