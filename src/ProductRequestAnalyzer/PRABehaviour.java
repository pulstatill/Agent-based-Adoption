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
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Debugger;

/**
 *
 * @author Philipp
 */
public class PRABehaviour extends Behaviour
{

    private int step = 0;
    private AID top;
    private MessageTemplate mt;

    @Override
    public void action()
    {

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();

        switch (step)
        {
            case 0:
                Debugger.log("PRABehaviour Step 0");
                sd.setType("Topology");
                sd.setName("Factory-Topology");
                template.addServices(sd);
                try
                {
                    DFAgentDescription[] searchtop = DFService.search(myAgent, template);
                    top = searchtop[0].getName();
                    request.addReceiver(top);
                    Object b = ((PRA_Agent) myAgent).getPrq();
                    request.setContentObject((Serializable) b);
                    request.setConversationId("Request-Topology");
                    request.setReplyWith("Request-Topology" + System.currentTimeMillis());
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Request-Topology"), MessageTemplate.MatchInReplyTo(request.getReplyWith()));
                    myAgent.send(request);
                    Debugger.log("Message sent to request topology");
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                step++;
                break;

            case 1:
                Debugger.log("PRABehaviour Step 1");
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null)
                {
                    if (reply.getPerformative() == ACLMessage.INFORM)
                    {
                        switch (reply.getContent())
                        {
                            case "everythings fit":
                                step++;
                                Debugger.log("Order of Topology mesh. Next Step");
                                break;
                            case "out of order":
                                break;
                            case "process missing":
                                break;
                        }
                    }
                } else
                {
                    block();
                }
            case 2:
                LinkedList plist = ((PRA_Agent) myAgent).getPrq().getpList();
                AID processname;
                for (int i = 0; i < plist.size(); i += 2)
                {
                    template.removeServices(sd);
                    sd.setType("Process");
                    sd.setName(((topology.Process) plist.get(i + 1)).getName());
                    template.addServices(sd);
                    try
                    {
                        DFAgentDescription[] searchfe = DFService.search(myAgent, template);
                        if(searchfe != null)
                        {
                            for (int j = 0; j < searchfe.length; i++)
                            {
                                processname = searchfe[j].getName();
                                request = new ACLMessage(ACLMessage.REQUEST);
                                request.addReceiver(processname);
                                request.setConversationId(processname.getName());
                                request.setReplyWith("Property of Process" + System.currentTimeMillis());
                                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Request Process property"), MessageTemplate.MatchInReplyTo(request.getReplyWith()));
                                myAgent.send(request);
                                Debugger.log("Message sent to request Process properties of:"  +  processname.getName());
                                
                                
                            }
                        }
                    } catch (FIPAException ex)
                    {
                        Logger.getLogger(PRABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
        }
    }

    @Override
    public boolean done()
    {
        return false;
    }

}
