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
public class Constraint extends Interpendency
{

    /**
     *
     * @param constraint the String value of the Constraint
     */
    public Constraint(String constraint)
    {
        super(constraint);
    }

    @Override
    public String toString()
    {
        return " mit der Einschränkung: " + getName();
    }
}
