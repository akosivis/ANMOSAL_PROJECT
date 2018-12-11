// Chat client for game

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

public class ChatUI extends JPanel{
	
	JPanel panelChats, panelButton, allPanels;
	JFrame frame;
	JScrollPane fieldChats;
	JTextField fieldInsertChats;
	JButton sendButton, disconnectButton;
	JTextArea textToAdd, nameOfUser;
	ChatClient client;

	//public ChatUI(String name){
	public ChatUI(){
		//this.client = new ChatClient(name);
		textToAdd = new JTextArea();
		nameOfUser = new JTextArea();
		//------1st panel 
		fieldChats = new JScrollPane(textToAdd);
		fieldChats.setPreferredSize(new Dimension(600, 60));
		fieldChats.setViewportView(textToAdd);
		panelChats = new JPanel();
		panelChats.add(fieldChats);

		//------2nd panel 
		fieldInsertChats = new JTextField("");
		fieldInsertChats.setPreferredSize(new Dimension(400, 35));	
		sendButton = new JButton("Send");

		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				client.sendChat(fieldInsertChats.getText());

				// UI
				// textToAdd.setText(textToAdd.getText() + "\n" + name + ": " + fieldInsertChats.getText());
				textToAdd.setText(textToAdd.getText() + "\n" + ": " + fieldInsertChats.getText());
				fieldInsertChats.setText("");
			}
		});

		sendButton.setPreferredSize(new Dimension(100, 35));
		disconnectButton = new JButton("Disconnect");	
		disconnectButton.setPreferredSize(new Dimension(100, 35));
		
		panelButton = new JPanel();
		panelButton.add(fieldInsertChats);
		panelButton.add(sendButton);
		panelButton.add(disconnectButton);	
		panelButton.setLayout(new FlowLayout());

		add(panelChats);
		add(panelButton);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	 	//createAndShowGUI(name);
	}

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ChatUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
} 
