/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductRequestAnalyzer;

import interfaces.ProductRequestInterface;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Debugger;
import topology.TopologyAgent;

/**
 *
 * @author Philipp
 */
public class PRA_Agent extends Agent
{

    private ProductRequestInterface prq;

    @Override
    protected void setup()
    {
        Debugger.log("Hello! PRA_Agent " + getAID().getName() + " is ready");

        Object[] args = getArguments();
        if (args != null && args.length > 0)
        {
            prq = (ProductRequestInterface) args[0];
            Debugger.log("Product Request found");
            addBehaviour(new PRABehaviour());
        } else
        {
            Debugger.log("No ProductRequest found.");
            addBehaviour(new SimpleBehaviour()
            {
                private boolean done = false;
                @Override
                public void action()
                {
                    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                    ACLMessage msg = myAgent.receive(mt);
                    if (msg != null)
                    {
                        try
                        {
                            ((PRA_Agent) myAgent).setPrq((ProductRequestInterface) msg.getContentObject());
                            addBehaviour(new PRABehaviour());
                            done = true;
                        } catch (UnreadableException ex)
                        {
                            Logger.getLogger(PRA_Agent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else
                    {
                        block();
                    }
                }
                @Override
                public boolean done()
                {
                    return done;
                }
            });
        }
        try
        {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType("ProductRequestAnalyzer");
            sd.setName("Product-Request-Analyzer");
            dfd.addServices(sd);
            DFService.register(this, dfd);
            Debugger.log("DFService: " + sd.getName() + " registered");
        } catch (FIPAException fe)
        {
            Logger.getLogger(PRA_Agent.class.getName()).log(Level.SEVERE, null, fe);
        }
        //addBehaviour(new PRABehaviour());
    }

    @Override
    protected void takeDown()
    {
        Debugger.log("PRA_Agent " + getAID().getName() + " terminating");
    }

    public ProductRequestInterface getPrq()
    {
        return prq;
    }

    public void setPrq(ProductRequestInterface prq)
    {
        this.prq = prq;
    }
}
