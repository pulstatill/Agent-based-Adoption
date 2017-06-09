/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tree;

/**
 *
 * @author billi
 */
public abstract class Interpendency
{

    /**
     * Represents the Name of the Interpendency
     */
    protected String name;

    /**
     *
     * @param name String value of the interpendency
     */
    public Interpendency(String name)
    {
        this.name = name;
    }

    /**
     *
     * @return String value of the interpendency
     */
    public String getName()
    {
        return name;
    }

    /**
     *
     * @param name String value of the interpendency
     */
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public abstract String toString();
}
