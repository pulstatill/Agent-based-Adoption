/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import interfaces.ProcessInterface;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import references.TopologyReference;

/**
 *
 * @author Philipp
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        /**
         * * Initialisiere JADE Rutime und RMA Agent **
         */
        jade.core.Runtime rt = jade.core.Runtime.instance();
        Debugger.log("Runtime created");

        Profile profile = new ProfileImpl(null, 1200, null);
        Debugger.log("profile created");
        AgentContainer mainContainer = rt.createMainContainer(profile);

        ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
        Debugger.log("Launching the agent container ..." + pContainer);
        AgentContainer cont = rt.createAgentContainer(pContainer);
        Debugger.log("container " + pContainer + " created");
        Debugger.log("Launching the rma agent on the main container ...");

        try
        {
            AgentController rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            /* Starte RMA Agent */
            rma.start();
            /* Initialisiere Toplogy */
            LinkedList<interfaces.ProcessInterface> pList = initTop();
            LinkedList<String> pListName = new LinkedList<String>();
            pList.forEach((process) ->
            {
                pListName.addLast(process.getFullName());
            });
            Object[] args1 =
            {
                pList,
                new TopologyReference(pListName)
            };
            AgentController topa = cont.createNewAgent("TopologyAgent", "topology.TopologyAgent", args1);
            /* Starte Topology Agent */
            topa.start();
            /* Thread für 100ms auf sleep setzen damit Topology Agent Zeit hat Service zu registrieren */
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            /* Initialisiere Produkt Anfrage */
            Object[] args2 =
            {
                new ProductRequestAnalyzer.ProductRequest(initPRQ())
            };
            AgentController pra = cont.createNewAgent("PRA-Agent", "ProductRequestAnalyzer.PRA_Agent", args2);
            /* Start PRA Agent */
            pra.start();
        } catch (StaleProxyException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static LinkedList<interfaces.ProcessInterface> initTop()
    {
        LinkedList<interfaces.ProcessInterface> pList = new LinkedList<interfaces.ProcessInterface>();
        Hashtable properties = new Hashtable();
        properties.put("Durchmesser_Max", new Integer(10));
        properties.put("Durchmesser_Min", new Integer(2));
        properties.put("Höhe_Max", new Integer(5));
        properties.put("Gewicht_Max", new Integer(5));
        properties.put("Geschwindigkeit", new Integer(2));
        pList.add(new references.ProcessRefercence("verteilen" + UUID.randomUUID().toString(), properties));
        properties = new Hashtable();
        properties.put("Durchmesser_Max", new Integer(10));
        properties.put("Höhe_Min", new Integer(2));
        properties.put("Höhe_Max", new Integer(5));
        properties.put("Gewicht_Max", new Integer(5));
        properties.put("Geschwindigkeit", new Integer(2));
        pList.add(new references.ProcessRefercence("prüfe und transport" + UUID.randomUUID().toString(), properties));
        properties = new Hashtable();
        properties.put("Durchmesser_Max", new Integer(10));
        properties.put("Höhe_Max", new Integer(5));
        properties.put("Bohrdurchmesser", new Integer(1));
        properties.put("Geschwindigeit", new Integer(2));
        properties.put("Bohrtiefe", new Integer(2));
        pList.add(new references.ProcessRefercence("bearbeiten" + UUID.randomUUID().toString(), properties));
        properties = new Hashtable();
        properties.put("Durchmesser_Max", new Integer(10));
        properties.put("Durchmesser_Min", new Integer(2));
        properties.put("Höhe_Max", new Integer(5));
        properties.put("Gewicht_Max", new Integer(5));
        properties.put("Geschwindigkeit", new Integer(2));
        pList.add(new references.ProcessRefercence("lagern" + UUID.randomUUID().toString(), properties));
        return pList;
    }

    public static LinkedList initPRQ()
    {
        LinkedList prqList = new LinkedList<Object>();
        Hashtable properties = new Hashtable();
        properties.put("Durchmesser", new Integer(8));
        properties.put("Höhe", new Integer(3));
        properties.put("Farbe", "Rot");
        properties.put("Gewicht", new Integer(1));
        properties.put("Material", "Kunstoff");
        prqList.add(new references.ProductReference("ZwischenProdukt", properties));
        properties = new Hashtable();
        properties.put("Geschwindigkeit", new Integer(2));
        prqList.add(new references.ProcessRefercence("verteilen", properties));
        properties = new Hashtable();
        properties.put("Durchmesser", new Integer(8));
        properties.put("Höhe", new Integer(3));
        properties.put("Farbe", "Rot");
        properties.put("Gewicht", new Integer(1));
        properties.put("Material", "Kunstoff");
        prqList.add(new references.ProductReference("ZwischenProdukt", properties));
        properties = new Hashtable();
        properties.put("Geschwindigkeit", new Integer(2));
        prqList.add(new references.ProcessRefercence("prüfe und transport", properties));
        properties = new Hashtable();
        properties.put("Durchmesser", new Integer(8));
        properties.put("Höhe", new Integer(3));
        properties.put("Farbe", "Rot");
        properties.put("Gewicht", new Integer(1));
        properties.put("Material", "Kunstoff");
        prqList.add(new references.ProductReference("ZwischenProdukt", properties));
        properties = new Hashtable();
        properties.put("Geschwindigkeit", new Integer(1));
        properties.put("Bohrdurchmesser", new Integer(1));
        properties.put("Bohrtiefe", new Integer(2));
        prqList.add(new references.ProcessRefercence("bearbeiten", properties));
        properties = new Hashtable();
        properties.put("Durchmesser", new Integer(8));
        properties.put("Höhe", new Integer(3));
        properties.put("Farbe", "Rot");
        properties.put("Gewicht", new Integer(1));
        properties.put("Material", "Kunstoff");
        prqList.add(new references.ProductReference("ZwischenProdukt", properties));
        properties = new Hashtable();
        properties.put("Geschwindigkeit", new Integer(2));
        prqList.add(new references.ProcessRefercence("lagern", properties));
        properties = new Hashtable();
        properties.put("Durchmesser", new Integer(8));
        properties.put("Höhe", new Integer(3));
        properties.put("Farbe", "Rot");
        properties.put("Gewicht", new Integer(1));
        properties.put("Material", "Kunstoff");
        prqList.add(new references.ProductReference("ZwischenProdukt", properties));
        return prqList;
    }
}
