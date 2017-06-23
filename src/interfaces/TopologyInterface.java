/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.LinkedList;

/**
 *
 * @author Philipp
 */
public interface TopologyInterface
{
    public LinkedList<String> getprocessList();
    public void setprocessList(LinkedList<String> pList);
    public LinkedList<String> getFullNames();
    
}
