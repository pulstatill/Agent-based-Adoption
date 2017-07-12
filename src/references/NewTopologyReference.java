/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package references;

import interfaces.NewTopologyInterface;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author philipp
 */
public class NewTopologyReference implements NewTopologyInterface, Serializable
{
    
    private String name;
    private String fullName;
    private LinkedList<NewTopologyInterface> childs;
    private LinkedList<NewTopologyInterface> parents;

    public NewTopologyReference(String name, String fullName, LinkedList<NewTopologyInterface> childs, LinkedList<NewTopologyInterface> parents)
    {
        this.name = name;
        this.fullName = fullName;
        this.childs = childs;
        this.parents = parents;
    }

   
    
    
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getFullName()
    {
        return fullName;
    }

    @Override
    public LinkedList<NewTopologyInterface> getChilds()
    {
        return childs;
    }

    @Override
    public LinkedList<NewTopologyInterface> getParents()
    {
        return parents;
    }
    
}
