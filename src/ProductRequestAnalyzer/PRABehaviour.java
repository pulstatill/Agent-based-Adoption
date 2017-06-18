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
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Debugger;
import topology.MainTopology;

/**
 *
 * @author Philipp
 */
public class PRABehaviour extends Behaviour
{

    private int step = 0;
    private AID top;
    private MessageTemplate mt;
    private int msgtofe;
    private LinkedList<MessageTemplate> mts = new LinkedList<>();
    private DFAgentDescription template = new DFAgentDescription();
    private ServiceDescription sd = new ServiceDescription();

    @Override
    public void action()
    {

        switch (step)
        {
            case 0:
                step0();
                break;
            case 1:
                step1();
                break;
            case 2:
                step2();
                break;
            case 3:
                step3();
                break;
            case 4:
                Debugger.log("PRABehaviour Step 4");
                step++;
                break;
        }
    }

    @Override
    public boolean done()
    {
        return false;
    }

    private void step0()
    {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);

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
            request.setConversationId("Request-Topology");
            request.setReplyWith("Request-Topology" + System.currentTimeMillis());
            mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Request-Topology"), MessageTemplate.MatchInReplyTo(request.getReplyWith()));
            myAgent.send(request);
            Debugger.log("Message sent to request topology");
        } catch (FIPAException ex)
        {
            Logger.getLogger(PRABehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        step++;
    }

    private void step1()
    {
        Debugger.log("PRABehaviour Step 1");
        ACLMessage reply = myAgent.receive(mt);
        if (reply != null)
        {
            if (reply.getPerformative() == ACLMessage.INFORM)
            {
                try
                {
                    ProductRequest prq = ((PRA_Agent) myAgent).getPrq();
                    topology.MainTopology mainTopology = (MainTopology) reply.getContentObject();
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
                    if (!outoforder)
                    {
                        Debugger.log("everythings fit");
                        step++;
                    } else
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
                                Debugger.log("Processes are out of order");
                                i = (prq.getpList().size() - 1) / 2;
                                step++;
                            } else
                            {
                                Debugger.log("Some Processes are missing");
                                step++;
                            }
                        }
                    }
                } catch (UnreadableException ex)
                {
                    Logger.getLogger(PRABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else
        {
            block();
        }
    }

    private void step2()
    {
        Debugger.log("PRABehaviour Step 2");
        LinkedList plist = ((PRA_Agent) myAgent).getPrq().getpList();
        AID processname;

        for (int i = 0; i < plist.size() - 1; i += 2)
        {
            template.removeServices(sd);
            sd.setType("Process");
            sd.setName(((topology.Process) plist.get(i + 1)).getName());
            template.addServices(sd);
            try
            {
                DFAgentDescription[] searchfe = DFService.search(myAgent, template);
                if (searchfe != null)
                {
                    for (DFAgentDescription searchfe1 : searchfe)
                    {
                        processname = searchfe1.getName();
                        ACLMessage requestfe = new ACLMessage(ACLMessage.REQUEST);
                        requestfe.addReceiver(processname);
                        requestfe.setConversationId(processname.toString());
                        requestfe.setReplyWith("Property of Process" + System.currentTimeMillis());
                        mts.add(MessageTemplate.and(MessageTemplate.MatchConversationId(requestfe.getConversationId()), MessageTemplate.MatchInReplyTo(requestfe.getReplyWith())));
                        myAgent.send(requestfe);
                        Debugger.log("Message sent to request Process properties of:" + processname.getName());
                        msgtofe++;
                    }
                }
            } catch (FIPAException ex)
            {
                Logger.getLogger(PRABehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        step++;
    }

    private void step3()
    {
        Debugger.log("PRABehaviour Step 3");
        if (msgtofe != 0)
        {
            topology.Process topprocess;
            LinkedList list = ((PRA_Agent) myAgent).getPrq().getpList();
            int j;
            for (MessageTemplate mt1 : mts)
            {
                ACLMessage replys = myAgent.receive();
                if (replys != null)
                {

                    for (int i = 0; i < (list.size() - 1) / 2; i++)
                    {

                        try
                        {
                            topprocess = (topology.Process) replys.getContentObject();
                            if (((topology.Process) list.get(i * 2 + 1)).getName().equalsIgnoreCase(topprocess.getName()))
                            {
                                j = i * 2 + 1;
                                i = list.size();

                                Hashtable tablefe = topprocess.getProperties();
                                Hashtable tableprod = ((topology.Product) (((PRA_Agent) myAgent).getPrq().getpList()).get(j - 1)).getProperties();
                                Enumeration keyspro = tableprod.keys();
                                Debugger.log("Check Product Parameter");
                                Debugger.log("from " + topprocess.getName());
                                while (keyspro.hasMoreElements())
                                {
                                    String key = (String) keyspro.nextElement();

                                    if (tablefe.get(key) != null)
                                    {
                                        Debugger.log("Check key:" + key);
                                        if (!tableprod.get(key).toString().equalsIgnoreCase(tablefe.get(key).toString()))
                                        {
                                            Debugger.log("Parameter " + key + " does not fit");

                                        } else
                                        {
                                            Debugger.log("Parameter " + key + " fits");
                                        }
                                    }
                                    if (tablefe.get(key + "_Max") != null)
                                    {
                                        Debugger.log("Check key: " + key + " to max Value");
                                        if (!((Integer) tableprod.get(key) <= (Integer) tablefe.get(key + "_Max")))
                                        {
                                            Debugger.log("Parameter " + key + " does not fit");

                                        } else
                                        {
                                            Debugger.log("Parameter " + key + " fits");
                                        }
                                    }
                                    if (tablefe.get(key + "_Min") != null)
                                    {
                                        Debugger.log("Check key: " + key + " to min Value");
                                        if (!((Integer) tableprod.get(key) >= (Integer) tablefe.get(key + "_Min")))
                                        {
                                            Debugger.log("Parameter " + key + " does not fit");

                                        } else
                                        {
                                            Debugger.log("Parameter " + key + " fits");
                                        }
                                    }
                                }
                                Hashtable tableproc = ((topology.Process) (((PRA_Agent) myAgent).getPrq().getpList()).get(j)).getProperties();
                                keyspro = tableproc.keys();
                                Debugger.log("Prüfe Process Parameter");
                                while (keyspro.hasMoreElements())
                                {
                                    String key = (String) keyspro.nextElement();
                                    Debugger.log("Check key:" + key);
                                    if (tablefe.get(key) != null)
                                    {
                                        if (!tableproc.get(key).toString().equalsIgnoreCase(tablefe.get(key).toString()))
                                        {
                                            Debugger.log("Parameter " + key + " does not fit");

                                        } else
                                        {
                                            Debugger.log("Parameter " + key + " fits");
                                        }
                                    }
                                    if (tablefe.get(key + "_Max") != null)
                                    {
                                        Debugger.log("Check key: " + key + " to max Value");
                                        if (!((Integer) tableproc.get(key) <= (Integer) tablefe.get(key + "_Max")))
                                        {
                                            Debugger.log("Parameter " + key + " does not fit");

                                        } else
                                        {
                                            Debugger.log("Parameter " + key + " fits");
                                        }
                                    }
                                    if (tablefe.get(key + "_Min") != null)
                                    {
                                        Debugger.log("Check key: " + key + " to min Value");
                                        if (!((Integer) tableproc.get(key) >= (Integer) tablefe.get(key + "_Min")))
                                        {
                                            Debugger.log("Parameter " + key + " does not fit");

                                        } else
                                        {
                                            Debugger.log("Parameter " + key + " fits");
                                        }
                                    }
                                }
                            }

                        } catch (UnreadableException ex)
                        {
                            Logger.getLogger(PRABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    msgtofe--;
                }
                //mts.remove(mt1);
            }
        } else
        {
            step++;
        }
    }
}
