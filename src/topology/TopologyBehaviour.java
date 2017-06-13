/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topology;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
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
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null)
                {
                    ACLMessage reply = msg.createReply();
                    try
                    {
                        ProductRequestAnalyzer.ProductRequest prq = (ProductRequestAnalyzer.ProductRequest) msg.getContentObject();
                        mainTopology = ((TopologyAgent) myAgent).getMainTopology();
                        boolean outoforder = false;
                        for (int i = 0; i < (prq.getpList().size() - 1) / 2; i++)
                        {
                            if (mainTopology.getprocessList().get(i) != null)
                            {
                                if (!((topology.Process) prq.getpList().get(i * 2 + 1)).getName().equalsIgnoreCase(mainTopology.getprocessList().get(i).getName()))
                                {
                                    outoforder = true;
                                }
                            }
                        }
                        if (outoforder)
                        {
                            boolean noprocessmissing = true;
                            for (int i = 0; i < (prq.getpList().size() - 1) / 2; i++)
                            {
                                boolean processmissing = true;
                                for (int j = 0; j < mainTopology.getprocessList().size(); j++)
                                {

                                    if (!((topology.Process) prq.getpList().get(i * 2 + 1)).getName().equalsIgnoreCase(mainTopology.getprocessList().get(j).getName()))
                                    {
                                        processmissing = false;
                                        j = mainTopology.getprocessList().size();
                                    }
                                }
                                if (processmissing)
                                {
                                    reply.setPerformative(ACLMessage.PROPOSE);
                                    reply.setContent("processes are not in order");
                                    i = (prq.getpList().size() - 1) / 2;
                                }
                            }
                        } else
                        {
                            reply.setPerformative(ACLMessage.PROPOSE);
                            reply.setContent("everythings fit");
                        }

                    } catch (UnreadableException ex)
                    {
                        Logger.getLogger(TopologyBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
        }
    }

    @Override
    public boolean done()
    {
        return false;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
