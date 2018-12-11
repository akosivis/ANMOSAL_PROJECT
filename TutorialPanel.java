import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TutorialPanel extends JFrame implements ActionListener{

//	private Container c = getContentPane();
	private JButton prev, next;
	private JLabel[] headerLabel = new JLabel[7];
	private JLabel[] imageLabel = new JLabel[7];
	private JPanel main, content, nav;
	private JPanel[] page = new JPanel[7];
	private CardLayout cardLayout = new CardLayout();
	private	int i;

	// images
	private ImageIcon image1 = new ImageIcon("img/tut1.png");
	private ImageIcon image2 = new ImageIcon("img/tut2.png");
	private ImageIcon image3 = new ImageIcon("img/tut3.png");
	private ImageIcon image4 = new ImageIcon("img/tut4.png");
	private ImageIcon image5 = new ImageIcon("img/tut5.png");
	private ImageIcon image6 = new ImageIcon("img/tut6.png");
	private ImageIcon image7 = new ImageIcon("img/tut7.png");
	
	public void initializePages() {
		main = new JPanel();
		for(i=0; i<7; i++) {
			//initialize pages
			page[i] = new JPanel();
			page[i].setBackground(Color.WHITE);
			page[i].setLayout(new FlowLayout());
			page[i].setPreferredSize(new Dimension(300, 100));
			// initialize headers
			headerLabel[i] = new JLabel();
			headerLabel[i].setBackground(Color.WHITE);
			headerLabel[i].setPreferredSize(new Dimension(600, 60));
			// initialize image labels
			imageLabel[i] = new JLabel();
			imageLabel[i].setBackground(Color.GRAY);
			imageLabel[i].setPreferredSize(new Dimension(300, 200));
		}
		/*****   CONTENT OF PAGES   *****/
		imageLabel[0].setIcon(image1);
		imageLabel[1].setIcon(image2);
		imageLabel[2].setIcon(image3);
		imageLabel[3].setIcon(image4);
		imageLabel[4].setIcon(image5);
		imageLabel[5].setIcon(image6);
		imageLabel[6].setIcon(image7);
		
		// navigation buttons
		prev = new JButton("<");
		prev.setBackground(Color.WHITE);
		prev.addActionListener(this);
		next = new JButton(">");
		next.setBackground(Color.WHITE);
		next.addActionListener(this);
		// content panel
		content = new JPanel();
		content.setLayout(cardLayout);
		content.setBackground(Color.WHITE);
		content.setPreferredSize(new Dimension(600, 300));
		// navigation panel
		nav = new JPanel();
		nav.setBackground(Color.WHITE);
		nav.setPreferredSize(new Dimension(600, 50));
		nav.add(prev);
		nav.add(next);
		// add labels to pages and add pages to content panel
		for(i=0; i<7; i++) {
			page[i].add(headerLabel[i]);
			page[i].add(imageLabel[i]);
			String title = "Page " + (i+1);
			content.add(page[i], title);
		}
		main.add(content);
		main.add(nav);
	}
	
	public TutorialPanel() {
		super("Conquer User Guide");
		initializePages();
		this.setPreferredSize(new Dimension(300, 400));
		this.setBackground(Color.WHITE);
		this.add(main);
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == next){
			cardLayout.next(content);
		}
		
		if(ae.getSource() == prev){
			cardLayout.previous(content);
		}
	}

}
