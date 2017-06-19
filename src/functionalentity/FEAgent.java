/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionalentity;

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
public class FEAgent extends Agent
{

    private interfaces.ProcessInterface process;

    @Override
    protected void setup()
    {
        Debugger.log("Hello! FEAgent " + getAID().getName() + " is ready");
        Object[] args = getArguments();
        if (args != null && args.length > 0)
        {
            process = (interfaces.ProcessInterface) args[0];
            Debugger.log("Process found: " + process.getName() + "  " + getAID().getName());
        } else
        {
            Debugger.log("No Process was found: " + getAID().getName());
            doDelete();
        }
        try
        {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType("Process");
            sd.setName(process.getName());
            dfd.addServices(sd);
            DFService.register(this, dfd);
            Debugger.log("DFService: " + sd.getName() + " registered");
        } catch (FIPAException fe)
        {
            Logger.getLogger(TopologyAgent.class.getName()).log(Level.SEVERE, null, fe);
        }
        addBehaviour(new FEBehaviour());
    }

    @Override
    protected void takeDown()
    {
        Debugger.log("FEAgent: " + getAID().getName() + " terminating");
    }

    public interfaces.ProcessInterface getProcess()
    {
        return process;
    }
}
