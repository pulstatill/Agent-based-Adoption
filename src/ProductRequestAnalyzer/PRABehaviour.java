/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductRequestAnalyzer;

import interfaces.ProductRequestInterface;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
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

    /* Attribute */
    private int step = 0; // Aktueller Schritt
    private MessageTemplate mt; // Message Template zum Empfangen der Topology 
    private int msgtofe; // Anzahl der Narichten an FE Agenten 
    private LinkedList<MessageTemplate> mts = new LinkedList<>(); // Alle Message Templates zum Empfangen der Narichten
    private DFAgentDescription template = new DFAgentDescription();
    private ServiceDescription sd = new ServiceDescription();

    @Override
    public void action()
    {

        switch (step)
        {
            case 0:
                /* Sende Naricht an Topology Agent und erfrage Topology */
                step0();
                break;
            case 1:
                /* Empfange Naricht von Topology Agent und überprüfe auf Erfüllbarkeit */
                step1();
                break;
            case 2:
                /* Frage von allen FE Agenten die Prozess Attribute an */
                step2();
                break;
            case 3:
                /* Empfange Antworten der FE Agenten */
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
        AID top;
        Debugger.log("PRABehaviour Step 0");
        sd.setType("Topology");
        sd.setName("Factory-Topology");
        template.addServices(sd);
        try
        {
            /* Suche nach Topology Agenten. Momentan wird davon ausgegangen, dass nur einer existiert*/
            DFAgentDescription[] searchtop = DFService.search(myAgent, template);
            top = searchtop[0].getName();
            request.addReceiver(top);
            request.setConversationId("Request-Topology");
            /* System.currenTime Millis für eine eindeutige indentifizierbarkeit */
            request.setReplyWith("Request-Topology" + System.currentTimeMillis());
            /* Sichere Template zum späteren Empfangen */
            mt = MessageTemplate.and(MessageTemplate.MatchConversationId(request.getConversationId()), MessageTemplate.MatchInReplyTo(request.getReplyWith()));
            /* Sende Naricht an Topology Agent */
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
        /* Empfange Antwort von Toplogy Agent */
        ACLMessage reply = myAgent.receive(mt);
        if (reply != null)
        {
            if (reply.getPerformative() == ACLMessage.INFORM)
            {
                try
                {
                    /* Produkt Anfrage von PRA Agent laden */
                    ProductRequestInterface prq = ((PRA_Agent) myAgent).getPrq();
                    /* Toplogy aus Antwort Naricht von Toplogy Agent laden */
                    topology.MainTopology mainTopology = (MainTopology) reply.getContentObject();
                    /* Prüfen ob alle Prozesse vorhande und an der richtigen Stelle sind */
                    boolean outoforder = false;
                    /* Nur jedes zweite Element, in der Produktanfrage ist ein Prozess */
                    for (int i = 0; i < (prq.getpList().size() - 1) / 2; i++)
                    {
                        if (mainTopology.getprocessList().get(i) != null)
                        {
                            /* Prüfe ob Prozessname übereinstimmen */
                            if (!((topology.Process) prq.getpList().get(i * 2 + 1)).getName().equalsIgnoreCase(mainTopology.getprocessList().get(i).getName()))
                            {
                                /* Prozesse nicht in der richtigen Reihenfolge oder, ein oder mehrere Prozesse, nicht vorhanden */
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
                        /* Prüfen ob Prozesse nicht vorhanden oder nicht in der korrekten Reihenfolge */
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
            /* Falls keine Naricht empfangen wurden, Behaviour blockieren, bis neue Narichten empfagen wurden */
            block();
        }
    }

    private void step2()
    {
        Debugger.log("PRABehaviour Step 2");
        LinkedList plist = ((PRA_Agent) myAgent).getPrq().getpList();
        AID processname;
        /* Nur jedes zweite Element in der Liste ist ein Prozess */
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
                    /* Frage alle FE-Agenten an, die den Prozess ausfürhen können */
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
                        /* Erhöhe Counter zum Zählen der Anfragen um eins */
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
