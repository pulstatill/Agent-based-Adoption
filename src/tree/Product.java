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
public class Product extends PPRElement
{

    /**
     * Represents the Product
     *
     * @param name Name of the Product Parameter
     */
    public Product(String name)
    {
        super(name);
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();
        
        b.append("Produkt ").append(name).append(" hat zu vogenden Typen Verbindugen: \n");
        
        for (Connections c : connectionses)
        {
            
            b.append("\t").append(c.getElement().getName());
            
            if (c.getIn() != null)
            {
                b.append(c.getIn().toString());
            }
            b.append("\n");
            
        }
        return new String();
    }
}
