/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductRequestAnalyzer;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;

/**
 *
 * @author Philipp
 */
public class PRABehaviour extends Behaviour
{

    private int step = 0;
    private AID topology;
    private MessageTemplate mt;

    @Override
    public void action()
    {
        switch (step)
        {
            case 0:
                ACLMessage cfp = new ACLMessage(ACLMessage.REQUEST);
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setName("JADE-book-trading");
                sd.setType("book-selling");
                template.addServices(sd);
                try
                {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    topology = result[0].getName();
                    cfp.addReceiver(topology);
                    Object b = ((PRA_Agent) myAgent).getPrq();
                    cfp.setContentObject((Serializable) b);
                    cfp.setConversationId("Request-Topology");
                    cfp.setReplyWith("Request-Topology" + System.currentTimeMillis());
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Request-Topology"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                step = 1;
                break;

            case 1:
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null)
                {
                    if (reply.getPerformative() == ACLMessage.PROPAGATE)
                    {
                        
                    }
                } else
                {
                    block();
                }

        }
    }

    @Override
    public boolean done()
    {
        return false;
    }

}
