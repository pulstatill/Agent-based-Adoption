/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import ProductRequestAnalyzer.PRABehaviour;
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
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import main.Debugger;
import main.TestMain;
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

                        } catch (Exception e)
                        {
                            properties.put(propname, value);
                        }
                        plist.add(new ProductReference(panels.get(i).getOp().getTextField().getText(), properties));
                    }

                    area = panels.get(i + 1).getOp().getTextArea().getText();
                    props = area.split("\n");
                    properties = new Hashtable();
                    for (String prop : props)
                    {
                        System.out.println(".action()    " + prop);
                        String propname = prop.split(":")[0].trim();
                        String value = prop.split(":")[1].trim();
                        
                        try
                        {
                            int intvalue = new Integer(value);
                            properties.put(propname, intvalue);

                        } catch (Exception e)
                        {
                            properties.put(propname, value);
                        }
                        plist.add(new ProcessRefercence(panels.get(i + 1).getOp().getTextField().getText(), properties));
                    }
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
                    plist.add(new ProductReference(panels.get(panels.size() - 1).getOp().getTextField().getText(), properties));
                }
                ProductRequest prq = new ProductRequest(plist);

                sd.setType("ProductRequestAnalyzer");
                sd.setName("Product-Request-Analyzer");
                template.addServices(sd);
                MessageTemplate mt;
                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                AID prqagent;
                try
                {
                    DFAgentDescription[] searchtop = DFService.search(myAgent, template);
                    prqagent = searchtop[0].getName();
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
