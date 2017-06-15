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
import main.Debugger;

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
        Debugger.log("Helllo! Toplogy-agent " + getAID().getName() + " is ready.");
        Object[] args = getArguments();
        if (args != null && args.length > 0)
        {
            mainTopology = (MainTopology) args[0];
            Debugger.log("Topolgy found");
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
                        Debugger.log("FE-Agent for Process " + processList.getName() + " initialized");
                    } catch (StaleProxyException ex)
                    {
                        Logger.getLogger(TopologyAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }

        } else
        {
            Debugger.log("No Toplogy found");
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
            Debugger.log("DFService: " + sd.getName() + " registered");
        } catch (FIPAException fe)
        {
            Logger.getLogger(TopologyAgent.class.getName()).log(Level.SEVERE, null, fe);
        }
        addBehaviour(new TopologyBehaviour());
    }

    @Override
    protected void takeDown()
    {
        Debugger.log("TopologyAgent " + getAID().getName() +" terminating");
    }

    public MainTopology getMainTopology()
    {
        return mainTopology;
    }
}
