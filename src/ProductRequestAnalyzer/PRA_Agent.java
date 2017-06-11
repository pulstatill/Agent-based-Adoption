/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductRequestAnalyzer;

import jade.core.Agent;

/**
 *
 * @author Philipp
 */
public class PRA_Agent extends Agent
{
    @Override
    protected void setup()
    {
        System.out.println("Hello! PRA_Agent " + getAID().getName() + " is ready");
        
        Object[] args = getArguments();
        if(args!= null && args.length > 0)
        {
            ProductRequest prq = (ProductRequest) args[0];
        }
        
        else
        {
            System.out.println("No ProductRequest found.");
            doDelete();
        }
    }
    
    @Override
    protected void takeDown()
    {
        System.out.println("PRA_Agent " + getAID().getName() + " terminating");
    }
}
