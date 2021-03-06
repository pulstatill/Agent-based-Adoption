/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionalentity;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Debugger;

/**
 *
 * @author philipp
 */
public class FEBehaviour extends Behaviour
{

    private int step;

    @Override
    public void action()
    {
        switch (step)
        {
            case 0:
                Debugger.log("FEBehaviour Step 0 " + ((FEAgent) myAgent).getName());
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null)
                {
                    for (int i = 0; i < ((FEAgent) myAgent).getProcesses().length; i++)
                    {
                        if (msg.getContent().equalsIgnoreCase(((FEAgent) myAgent).getProcesses()[i].getName()))
                        {
                            try
                            {
                                ACLMessage reply = msg.createReply();
                                reply.setPerformative(ACLMessage.INFORM);
                                Object a = ((FEAgent) myAgent).getProcesses()[i];
                                reply.setContentObject((Serializable) a);
                                Debugger.log("Message sent to PRA " + ((FEAgent) myAgent).getName());;
                                myAgent.send(reply);
                            } catch (IOException ex)
                            {
                                Logger.getLogger(FEBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
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

    public int getStep()
    {
        return step;
    }

    public void setStep(int step)
    {
        this.step = step;
    }

}
