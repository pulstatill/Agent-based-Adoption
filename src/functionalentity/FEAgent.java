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

    private interfaces.ProcessInterface[] process;

    @Override
    protected void setup()
    {
        Debugger.log("Hello! FEAgent " + getAID().getName() + " is ready");
        Object[] args = getArguments();
        if (args != null && args.length > 0)
        {
            process = new interfaces.ProcessInterface[args.length];
            for (int i = 0; i < args.length; i++)
            {
                Object arg = args[i];
                process[i] = (interfaces.ProcessInterface) arg;
                Debugger.log("Process found: " + process[i].getName() + " added to FEAgent: " + getAID().getName());
                try
                {
                    DFAgentDescription dfd = new DFAgentDescription();
                    dfd.setName(getAID());
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("Process");
                    sd.setName(process[i].getName());
                    dfd.addServices(sd);
                    DFService.register(this, dfd);
                    Debugger.log("DFService: " + sd.getName() + " registered");
                } catch (FIPAException fe)
                {
                    Logger.getLogger(TopologyAgent.class.getName()).log(Level.SEVERE, null, fe);
                }
                addBehaviour(new FEBehaviour());
            }
        } else
        {
            Debugger.log("No Processes was found: " + getAID().getName());
            doDelete();
        }

    }

    @Override
    protected void takeDown()
    {
        Debugger.log("FEAgent: " + getAID().getName() + " terminating");
    }

    public interfaces.ProcessInterface[] getProcesses()
    {
        return process;
    }
}
