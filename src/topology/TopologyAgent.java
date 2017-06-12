/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topology;

import jade.core.Agent;
import java.util.LinkedList;

/**
 *
 * @author Philipp
 */
public class TopologyAgent extends Agent
{

    @Override
    protected void setup()
    {
        System.out.println("Helllo! Toplogy-agent " + getAID().getName() + " is ready.");
        Object[] args = getArguments();
        if (args != null && args.length > 0)
        {
            LinkedList<Process> pList = (LinkedList<Process>) args[0];
        } else
        {
            System.out.println("No Toplogy found");
            doDelete();
        }
        

    }

    @Override
    protected void takeDown()
    {
        
    }
}
