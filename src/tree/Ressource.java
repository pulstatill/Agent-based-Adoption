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
public class Ressource extends PPRElement
{

    /**
     * Represents the Ressource
     *
     * @param name Name of the Ressource Parameter
     */
    public Ressource(String name)
    {
        super(name);
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();

        b.append("Ressource ").append(name).append(" hat zu vogenden Typen Verbindugen: \n");
        
        for (Connections c : connectionses)
        {
            
            b.append("\t").append(c.getElement().getName());
            
            if (c.getIn() != null)
            {
                b.append(c.getIn().toString());
            }
            b.append("\n");
            
        }
        return b.toString();
    }
}
