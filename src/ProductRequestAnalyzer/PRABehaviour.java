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
import main.Debugger;

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
                Debugger.log("PRABehaviour Step 0");
                ACLMessage cfp = new ACLMessage(ACLMessage.REQUEST);
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Topology");
                sd.setName("Factory-Topology");
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
                    myAgent.send(cfp);
                    Debugger.log("Message sent");
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                step ++;
                break;
            
            case 1:
                Debugger.log("PRABehaviour Step 1");
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null)
                {
                    if (reply.getPerformative() == ACLMessage.PROPAGATE)
                    {
                        switch (reply.getContent())
                        {
                            case "everythings fit":
                                step++;
                                Debugger.log("Order of Topology mesh. Next Step");
                                break;
                            case "out of order":  break;
                            case "process missing":
                                break;
                        }
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
