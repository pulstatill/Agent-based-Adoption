/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.LinkedList;

/**
 *
 * @author philipp
 */
public interface NewTopologyInterface
{
    public String getName();
    public String getFullName();
    public LinkedList<NewTopologyInterface> getChilds();
    public LinkedList<NewTopologyInterface> getParents();
}
