/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topology;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philipp
 */
public class TopologyBehaviour extends Behaviour
{

    private int step = 0;
    private MainTopology mainTopology;

    @Override
    public void action()
    {
        switch (step)
        {
            case 0:
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage msg = myAgent.receive(mt);

                if (msg != null)
                {
                    try
                    {
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        Object a = ((TopologyAgent)myAgent).getMainTopology();
                        reply.setContentObject((Serializable) a);
                        //reply.setContent("Topology");
                        myAgent.send(reply);
                        step++;
                    } catch (IOException ex)
                    {
                        Logger.getLogger(TopologyBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else
                {
                    block();
                }
                break;
        }
    }

    @Override
    public boolean done()
    {
        return false;
    }

}
