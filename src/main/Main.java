/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import topology.MainTopology;

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
        // TODO code application logic here
        jade.core.Runtime rt = jade.core.Runtime.instance();
        System.out.println("Runtime created");
        
        Profile profile = new ProfileImpl(null, 1200, null);
        System.out.println("profile created");
        AgentContainer mainContainer = rt.createMainContainer(profile);
        
        ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
        System.out.println("Launching the agent container ..."+pContainer);
        AgentContainer cont = rt.createAgentContainer(pContainer);
        System.out.println("Launching the agent container after ..." +pContainer);
        System.out.println("containers created");
        System.out.println("Launching the rma agent on the main container ...");
        try
        {
            AgentController rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            rma.start();
            Object[] b = {new MainTopology(initTop())};
            AgentController topa = cont.createNewAgent("TopologyAgent", "topology.TopologyAgent", b);
            topa.start();
            
            
        }
        catch(StaleProxyException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static LinkedList<topology.Process> initTop()
    {
        LinkedList<topology.Process> pList = new LinkedList<topology.Process>();
        Hashtable properties = new Hashtable();
        properties.put("Durchmesser_Max", new Integer(10));
        properties.put("Durchmesser_Min", new Integer(2));
        properties.put("Höhe_Max", new Integer(5));
        properties.put("Gewicht_Max", new Integer(5));
        properties.put("Geschwindigkeit", new Integer(2));
        pList.add(new topology.Process("verteilen", properties));
        properties = new Hashtable();
        properties.put("Durchmesser_Max", new Integer(10));
        properties.put("Höhe_Min", new Integer(2));
        properties.put("Höhe_Max", new Integer(5));
        properties.put("Gewicht_Max", new Integer(5));
        properties.put("Geschwindigkeit", new Integer(2));
        pList.add(new topology.Process("prüfe und transport", properties));
        properties = new Hashtable();
        properties.put("Durchmesser_Max", new Integer(10));
        properties.put("Höhe_Max", new Integer(5));
        properties.put("Bohrdurchmesser", new Integer(1));
        properties.put("Geschwindigeit", new Integer(2));
        properties.put("Bohrtiefe", new Integer(2));
        pList.add(new topology.Process("bearbeiten", properties));
        properties = new Hashtable();
        properties.put("Durchmesser_Max", new Integer(10));
        properties.put("Durchmesser_Min", new Integer(2));
        properties.put("Höhe_Max", new Integer(5));
        properties.put("Gewicht_Max", new Integer(5));
        properties.put("Geschwindigkeit", new Integer(2));
        pList.add(new topology.Process("lagern", properties));
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
        prqList.add(new topology.Product("ZwischenProdukt", properties));
        properties = new Hashtable();
        properties.put("Geschwindigkeit", new Integer(2));
        prqList.add(new topology.Product("verteilen", properties));
        properties = new Hashtable();
        properties.put("Durchmesser", new Integer(8));
        properties.put("Höhe", new Integer(3));
        properties.put("Farbe", "Rot");
        properties.put("Gewicht", new Integer(1));
        properties.put("Material", "Kunstoff");
        prqList.add(new topology.Product("ZwischenProdukt", properties));
        properties = new Hashtable();
        properties.put("Geschwindigkeit", new Integer(2));
        prqList.add(new topology.Product("prüfe und transport", properties));
        properties = new Hashtable();
        properties.put("Durchmesser", new Integer(8));
        properties.put("Höhe", new Integer(3));
        properties.put("Farbe", "Rot");
        properties.put("Gewicht", new Integer(1));
        properties.put("Material", "Kunstoff");
        prqList.add(new topology.Product("ZwischenProdukt", properties));
        properties = new Hashtable();
        properties.put("Geschwindigkeit", new Integer(1));
        properties.put("Bohrdurchmesser", new Integer(1));
        properties.put("Bohrtiefe", new Integer(2));
        prqList.add(new topology.Product("bearbeiten", properties));
        properties = new Hashtable();
        properties.put("Durchmesser", new Integer(8));
        properties.put("Höhe", new Integer(3));
        properties.put("Farbe", "Rot");
        properties.put("Gewicht", new Integer(1));
        properties.put("Material", "Kunstoff");
        prqList.add(new topology.Product("ZwischenProdukt", properties));
        properties = new Hashtable();
        properties.put("Geschwindigkeit", new Integer(1));
        prqList.add(new topology.Product("lagern", properties));
        properties = new Hashtable();
        properties.put("Durchmesser", new Integer(8));
        properties.put("Höhe", new Integer(3));
        properties.put("Farbe", "Rot");
        properties.put("Gewicht", new Integer(1));
        properties.put("Material", "Kunstoff");
        prqList.add(new topology.Product("ZwischenProdukt", properties));
        return prqList;
    }
}
