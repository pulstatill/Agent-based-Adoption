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
public class Process extends PPRElement
{

    /**
     * Reprsents an Process
     *
     * @param name Name of the Process parameter
     */
    public Process(String name)
    {
        super(name);
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();
        
        b.append("Prozess ").append(name).append(" hat zu volgenden Typen Verbindugen: \n");
        
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
