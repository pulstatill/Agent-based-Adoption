/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tree;

import java.util.LinkedList;

/**
 *
 * @author billi
 */
public abstract class PPRElement
{

    /**
     * The Name of the PPR Element
     */
    protected String name;

    /**
     * An list of all connections the PPR Element has
     */
    protected LinkedList<Connections> connectionses;

    /**
     *
     * @param name Name of the PPR ELement
     */
    public PPRElement(String name)
    {
        this.name = name;
        connectionses = new LinkedList<>();
    }

    /**
     *
     * @return the Name of the PPR Element
     */
    public String getName()
    {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public LinkedList<Connections> getConnectionses()
    {
        return connectionses;
    }

    /**
     *
     * @param connectionses
     */
    public void setConnectionses(LinkedList<Connections> connectionses)
    {
        this.connectionses = connectionses;
    }

    /**
     *
     * @param element the other PPR Element
     * @param interdependency the interpedency between both PPR Elements
     */
    public void addConnection(PPRElement element, String interdependency)
    {
        connectionses.add(new Connections(element, interdependency));
    }

}
