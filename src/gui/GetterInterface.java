/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author philipp
 */
public interface GetterInterface
{
    public JTextField getTextField();
    public void setTextField(String textField);
    public JTextArea getTextArea();
    public void setTextArea(String textArea);
}
