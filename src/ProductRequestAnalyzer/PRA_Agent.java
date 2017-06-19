/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductRequestAnalyzer;

import interfaces.ProductRequestInterface;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
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
        } else
        {
            Debugger.log("No ProductRequest found.");
            doDelete();
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
            Logger.getLogger(TopologyAgent.class.getName()).log(Level.SEVERE, null, fe);
        }
        addBehaviour(new PRABehaviour());
        Debugger.log("PRABehaviour added");
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
}
