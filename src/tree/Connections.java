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
public class Connections
{

    private PPRElement element;
    private Interpendency in;

    /**
     *
     * @param element the PPR Element to the interpedency
     * @param interdependency the interpendency to the PPR Element
     */
    public Connections(PPRElement element, String interdependency)
    {

        this.element = element;

        /* Leerzeichen entfernen */
        while (interdependency.startsWith(" "))
        {
            interdependency = interdependency.substring(1, interdependency.length());
        }
        while (interdependency.endsWith(" "))
        {
            interdependency = interdependency.substring(0, interdependency.length() - 1);
        }

        /* Unterscheiden zwischen Korrelation und Einschränkung */
        if (interdependency.startsWith("+") || interdependency.startsWith("-") || interdependency.startsWith("0"))
        {
            in = new Constraint(interdependency);
        } else
        {
            in = new Correlation(interdependency);
        }

    }

    /**
     *
     * @return an object of the Class Correlation or Constraint
     */
    public Interpendency getIn()
    {
        return in;
    }

    /**
     *
     * @param in 
     */
    public void setIn(Interpendency in)
    {
        this.in = in;
    }

    /**
     *
     * @param element
     */
    public void setElement(PPRElement element)
    {
        this.element = element;
    }

    /**
     *
     * @return
     */
    public PPRElement getElement()
    {
        return element;
    }

}
