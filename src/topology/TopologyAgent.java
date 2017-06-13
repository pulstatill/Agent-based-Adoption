/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topology;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philipp
 */
public class TopologyAgent extends Agent
{

    private MainTopology mainTopology;

    @Override
    protected void setup()
    {
        System.out.println("Helllo! Toplogy-agent " + getAID().getName() + " is ready.");
        Object[] args = getArguments();
        if (args != null && args.length > 0)
        {
            mainTopology = (MainTopology) args[0];
            if (mainTopology.getprocessList() != null)
            {
                mainTopology.getprocessList().forEach((processList) ->
                {
                    try
                    {
                        Object[] arg =
                        {
                            processList
                        };
                        AgentController feAgent = getContainerController().createNewAgent("FE-Agent " + processList.getName(), "functionalentity.FEAgent", arg);
                        feAgent.start();
                    } catch (StaleProxyException ex)
                    {
                        Logger.getLogger(TopologyAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }

        } else
        {
            System.out.println("No Toplogy found");
            doDelete();
        }

        try
        {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType("Topology");
            sd.setName("Factory-Topology");
            dfd.addServices(sd);
            DFService.register(this, dfd);
        } catch (FIPAException fe)
        {
            Logger.getLogger(TopologyAgent.class.getName()).log(Level.SEVERE, null, fe);
        }

    }

    @Override
    protected void takeDown()
    {

    }

    public MainTopology getMainTopology()
    {
        return mainTopology;
    }
}
