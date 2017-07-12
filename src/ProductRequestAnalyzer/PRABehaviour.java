/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductRequestAnalyzer;

import interfaces.NewTopologyInterface;
import interfaces.ProcessInterface;
import interfaces.ProductRequestInterface;
import interfaces.TopologyInterface;
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
    private LinkedList<ProcessInterface> pis = new LinkedList<>();
    private interfaces.TopologyInterface mainTopology;
    private interfaces.NewTopologyInterface mainTopology2;
    private LinkedList<LinkedList> allpossiblepaths = new LinkedList<>();
    private LinkedList<NewTopologyInterface> walkedpath = new LinkedList<>();

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
                step12();
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
                Debugger.log("Message recieved from Topology-Agent");
                try
                {
                    LinkedList<ProcessInterface> missingprocesses = new LinkedList<>();
                    /* Produkt Anfrage von PRA Agent laden */
                    ProductRequestInterface prq = ((PRA_Agent) myAgent).getPrq();
                    /* Toplogy aus Antwort Naricht von Toplogy Agent laden */
                    mainTopology = (TopologyInterface) reply.getContentObject();
                    /* Prüfen ob alle Prozesse vorhande und an der richtigen Stelle sind */
                    boolean outoforderormissing = false, processfound = false;
                    /* Nur jedes zweite Element, in der Produktanfrage ist ein Prozess */
                    int k = 0;
                    for (int i = 0; i < (prq.getpList().size() - 1) / 2; i++)
                    {
                        for (int j = k; j < mainTopology.getprocessList().size(); j++)
                        {
                            if (((ProcessInterface) (prq.getpList().get(i * 2 + 1))).getName().equalsIgnoreCase(mainTopology.getprocessList().get(j)))
                            {
                                k = j + 1;
                                break;
                            } else if (j == (mainTopology.getprocessList().size() - 1))
                            {
                                outoforderormissing = true;
                                missingprocesses.add((ProcessInterface) prq.getpList().get(i * 2 + 1));
                            }
                        }
                    }
                    if (outoforderormissing != true)
                    {
                        Debugger.log("Topology fits to the Poduct Request");
                    } else
                    {
                        for (ProcessInterface missingprocesse : missingprocesses)
                        {
                            for (String processList : mainTopology.getprocessList())
                            {
                                if (missingprocesse.getName().equalsIgnoreCase(processList))
                                {
                                    processfound = true;
                                    Debugger.log("Process: " + missingprocesse.getName() + " is not in order");
                                    break;
                                }
                            }
                            if (!processfound)
                            {
                                Debugger.log("Process: " + missingprocesse.getName() + " could not be found in topology");
                            }
                        }
                    }

                } catch (UnreadableException ex)
                {
                    Logger.getLogger(PRABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }
                step++;
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
            sd.setName(((references.ProcessRefercence) plist.get(i + 1)).getName());
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
                        requestfe.setContent(((references.ProcessRefercence) plist.get(i + 1)).getName());
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

        while (!mts.isEmpty())
        {
            ACLMessage reply = myAgent.receive(mts.get(0));
            if (reply != null)
            {
                mts.remove(0);
                try
                {
                    pis.add((ProcessInterface) reply.getContentObject());
                } catch (UnreadableException ex)
                {
                    Logger.getLogger(PRABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else
            {
                block();
            }
        }
        LinkedList<ProcessInterface> processesfit = new LinkedList<>();
        LinkedList<ProcessInterface> processenotfit = new LinkedList<>();
        int k = 0;
        int h = 0;
        for (int i = 0; i < (((PRA_Agent) myAgent).getPrq().getpList().size() - 1) / 2; i++)
        {
            boolean newlopp = true;
            ProcessInterface prpro = (ProcessInterface) ((PRA_Agent) myAgent).getPrq().getpList().get(i * 2 + 1);
            for (ProcessInterface pi : pis)
            {
                if (prpro.getName().equalsIgnoreCase(pi.getName()))
                {
                    boolean fits = true;
                    Hashtable tablefe = pi.getProperties();
                    Hashtable tableprod = ((references.ProductReference) (((PRA_Agent) myAgent).getPrq().getpList()).get(i * 2)).getProperties();
                    Enumeration keyspro = tableprod.keys();
                    Debugger.log("Check Product Parameter");
                    Debugger.log("from " + pi.getFullName());
                    while (keyspro.hasMoreElements())
                    {
                        String key = (String) keyspro.nextElement();

                        if (tablefe.get(key) != null)
                        {
                            Debugger.log("Check key:" + key);
                            if (!tableprod.get(key).toString().equalsIgnoreCase(tablefe.get(key).toString()))
                            {
                                Debugger.log("Parameter " + key + " does not fit");
                                fits = false;
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
                                fits = false;

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
                                fits = false;

                            } else
                            {
                                Debugger.log("Parameter " + key + " fits");
                            }
                        }
                    }
                    Hashtable tableproc = ((references.ProcessRefercence) (((PRA_Agent) myAgent).getPrq().getpList()).get(i * 2 + 1)).getProperties();
                    keyspro = tableproc.keys();
                    Debugger.log("Check Process Parameter");
                    while (keyspro.hasMoreElements())
                    {
                        String key = (String) keyspro.nextElement();
                        Debugger.log("Check key:" + key);
                        if (tablefe.get(key) != null)
                        {
                            if (!tableproc.get(key).toString().equalsIgnoreCase(tablefe.get(key).toString()))
                            {
                                Debugger.log("Parameter " + key + " does not fit");
                                fits = false;

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
                                fits = false;

                            } else
                            {
                                Debugger.log("Parameter " + key + " fits");
                            }
                        }
                    }
                    if (fits)
                    {
                        Debugger.log("All Parameter of Process: " + pi.getFullName() + " fits to Product Request");
                        boolean foundposition = false;
                        /*for (int j = k; j < mainTopology.getprocessList().size(); j++)
                        {
                            if (mainTopology.getFullNames().get(j).equalsIgnoreCase(pi.getFullName()))
                            {
                                foundposition = true;
                                if (newlopp)
                                {
                                    h = j;
                                    newlopp = false;
                                    break;
                                } else
                                {
                                    if (h > j)
                                    {
                                        h = j;
                                        break;
                                    }
                                }
                            }
                        }
                        if (foundposition)
                        {
                            Debugger.log("Position of Process: " + pi.getFullName() + " fits");
                            processesfit.add(pi);
                        } else
                        {
                            Debugger.log("Position of Process: " + pi.getFullName() + " does not fit");
                        }*/
                    } else
                    {
                        Debugger.log("At least one Parameter of Process: " + pi.getFullName() + " does not fit");
                        processenotfit.add(pi);
                    }
                }
            }
            k = h;
        }
        step++;
    }

    private void step12()
    {
        Debugger.log("PRABehaviour Step 1");
        /* Empfange Antwort von Toplogy Agent */
        ACLMessage reply = myAgent.receive(mt);
        if (reply != null)
        {
            if (reply.getPerformative() == ACLMessage.INFORM)
            {
                Debugger.log("Message recieved from Topology-Agent");
                try
                {
                    /* Produkt Anfrage von PRA Agent laden */
                    ProductRequestInterface prq = ((PRA_Agent) myAgent).getPrq();
                    /* Toplogy aus Antwort Naricht von Toplogy Agent laden */
                    mainTopology2 = (NewTopologyInterface) reply.getContentObject();
                    /* Prüfen ob alle Prozesse vorhande und an der richtigen Stelle sind */
                    //First step
                    String firstprocess = ((ProcessInterface) prq.getpList().get(1)).getName();
                    firststep(mainTopology2, firstprocess);
                    for (int i = 1; i < (prq.getpList().size() - 1) / 2; i++)
                    {
                        int k = allpossiblepaths.size();
                        for (int j = 0; j < k; j++)
                        {
                            secondstep(j, (NewTopologyInterface) allpossiblepaths.get(j).removeLast(), ((ProcessInterface) (prq.getpList().get(i * 2 + 1))).getName());
                        }
                        for (int j = k - 1; j > -1; j--)
                        {
                            allpossiblepaths.remove(j);
                        }
                    }
                    Debugger.log(allpossiblepaths.size() + " possible pathes found");
                } catch (UnreadableException ex)
                {
                    Logger.getLogger(PRABehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }
                step++;
            }
        } else
        {
            /* Falls keine Naricht empfangen wurden, Behaviour blockieren, bis neue Narichten empfagen wurden */
            block();
        }
    }

    public void firststep(NewTopologyInterface step, String searchfor)
    {

        LinkedList<NewTopologyInterface> childs = step.getChilds();
        LinkedList<NewTopologyInterface> parents = step.getParents();
        if (step.getName().equalsIgnoreCase(searchfor))
        {
            LinkedList<NewTopologyInterface> path = (LinkedList<NewTopologyInterface>) walkedpath.clone();
            path.add(step);
            allpossiblepaths.add(path);
        }
        walkedpath.add(step);
        if (childs != null)
        {
            childs.forEach((child) ->
            {
                firststep(child, searchfor);
            });
        }
        walkedpath.remove(step);
    }

    public void secondstep(int pathnumber, NewTopologyInterface step, String searchfor)
    {
        LinkedList<NewTopologyInterface> childs = step.getChilds();
        LinkedList<NewTopologyInterface> parents = step.getParents();
        if (step.getName().equalsIgnoreCase(searchfor))
        {
            LinkedList<NewTopologyInterface> path = (LinkedList<NewTopologyInterface>) allpossiblepaths.get(pathnumber).clone();
            path.add(step);
            allpossiblepaths.addLast(path);
        }
        allpossiblepaths.get(pathnumber).add(step);
        if (childs != null)
        {
            int k = allpossiblepaths.get(pathnumber).size();
            childs.forEach((child) ->
            {
                boolean loop = false;
                for (int i = 0; i < k; i++)
                {
                    if (child.getFullName().equalsIgnoreCase(((NewTopologyInterface) allpossiblepaths.get(pathnumber).get(i)).getFullName()))
                    {
                        loop = true;

                    }
                }
                if (!loop)
                {
                    secondstep(pathnumber, child, searchfor);
                    allpossiblepaths.get(pathnumber).remove(child);
                }
            });
        }
        if (parents != null)
        {
            parents.forEach((parent) ->
            {
                secondstep(pathnumber, parent, searchfor);
            });
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
