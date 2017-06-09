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
public class Correlation extends Interpendency
{

    /**
     *
     * @param correlation the name of the Correlation 
     */
    public Correlation(String correlation)
    {
        super(correlation);
    }

    @Override
    public String toString()
    {
        return " mit der Korrelation: " + getName();
    }
}
