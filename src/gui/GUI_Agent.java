/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import ProductRequestAnalyzer.ProductRequest;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Debugger;
import references.ProcessRefercence;
import references.ProductReference;

/**
 *
 * @author philipp
 */
public class GUI_Agent extends Agent
{

    private DFAgentDescription template = new DFAgentDescription();
    private ServiceDescription sd = new ServiceDescription();

    @Override
    protected void takeDown()
    {
        super.takeDown(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void setup()
    {
        super.setup();
        Debugger.log("Hello! GUI_Agent " + getAID().getName() + " is ready");

        new MainFrame(this);

    }

    public void sendoffer(LinkedList<SetPanel> panels)
    {
        addBehaviour(new OneShotBehaviour()
        {
            @Override
            public void action()
            {
                LinkedList<Object> plist = new LinkedList<>();
                for (int i = 0; i < panels.size() - 1; i += 2)
                {
                    String area = panels.get(i).getOp().getTextArea().getText();
                    String[] props = area.split("\n");
                    Hashtable properties = new Hashtable();
                    for (String prop : props)
                    {
                        String propname = prop.split(":")[0].trim();
                        String value = prop.split(":")[1].trim();
                        try
                        {
                            int intvalue = new Integer(value);
                            properties.put(propname, intvalue);

                        } catch (NumberFormatException e)
                        {
                            properties.put(propname, value);
                        }
                    }
                    plist.add(new ProductReference(panels.get(i).getOp().getTextField().getText(), properties));
                    
                    area = panels.get(i + 1).getOp().getTextArea().getText();
                    props = area.split("\n");
                    properties = new Hashtable();
                    for (String prop : props)
                    {
                        String propname = prop.split(":")[0].trim();
                        String value = prop.split(":")[1].trim();

                        try
                        {
                            int intvalue = new Integer(value);
                            properties.put(propname, intvalue);

                        } catch (NumberFormatException e)
                        {
                            properties.put(propname, value);
                        } 
                    }
                    plist.add(new ProcessRefercence(panels.get(i + 1).getOp().getTextField().getText(), properties));
                }

                String area = panels.get(panels.size() - 1).getOp().getTextArea().getText();
                String[] props = area.split("\n");
                Hashtable properties = new Hashtable();
                for (String prop : props)
                {
                    String propname = prop.split(":")[0].trim();
                    String value = prop.split(":")[1].trim();
                    try
                    {
                        int intvalue = new Integer(value);
                        properties.put(propname, intvalue);

                    } catch (NumberFormatException e)
                    {
                        properties.put(propname, value);
                    }
                }
                plist.add(new ProductReference(panels.get(panels.size() - 1).getOp().getTextField().getText(), properties));
                ProductRequest prq = new ProductRequest(plist);

                try
                {
                    sd.setType("Process");
                    template.addServices(sd);
                    DFAgentDescription[] searchfe = DFService.search(myAgent, template);
                    for (DFAgentDescription fe : searchfe)
                    {
                        ACLMessage informfe = new ACLMessage(ACLMessage.INFORM);
                        informfe.addReceiver(fe.getName());
                        informfe.setConversationId("Reset");
                        informfe.setReplyWith("Reset done" + System.currentTimeMillis());
                        myAgent.send(informfe);
                        Debugger.log("Reset Behaviour of " + fe.getName());
                    }
                    template.removeServices(sd);
                    sd.setType("Topology");                   
                    template.addServices(sd);
                    DFAgentDescription[] searchtop = DFService.search(myAgent, template);
                    for(DFAgentDescription top : searchtop)
                    {
                        ACLMessage informtop = new ACLMessage(ACLMessage.INFORM);
                        informtop.addReceiver(top.getName());
                        informtop.setConversationId("Reset");
                        informtop.setReplyWith("Reset done" + System.currentTimeMillis());
                        myAgent.send(informtop);
                        Debugger.log("Reset Behaviour of " + top.getName());
                    }
                    
                } catch (FIPAException ex)
                {
                    Logger.getLogger(GUI_Agent.class.getName()).log(Level.SEVERE, null, ex);
                }

                template.removeServices(sd);
                sd.setType("ProductRequestAnalyzer");
                sd.setName("Product-Request-Analyzer");
                template.addServices(sd);
                MessageTemplate mt;
                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                AID prqagent;
                try
                {
                    DFAgentDescription[] searchprq = DFService.search(myAgent, template);
                    prqagent = searchprq[0].getName();
                    request.addReceiver(prqagent);
                    request.setConversationId("Product Request");
                    request.setReplyWith("Request-Topology" + System.currentTimeMillis());
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId(request.getConversationId()), MessageTemplate.MatchInReplyTo(request.getReplyWith()));
                    request.setContentObject((Serializable) ((Object) prq));
                    myAgent.send(request);
                    Debugger.log("Message send");
                } catch (IOException | FIPAException ex)
                {
                    Logger.getLogger(GUI_Agent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
