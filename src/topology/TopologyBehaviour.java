/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topology;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Philipp
 */
public class TopologyBehaviour extends Behaviour
{
    private int step = 0;

    @Override
    public void action()
    {
        switch (step)
        {
            case 0:
                ACLMessage cfp = new ACLMessage(0);
        }
    }

    @Override
    public boolean done()
    {
        return false;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
