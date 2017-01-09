import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Plotter extends JFrame {

	JTextField aCoefficient;
	JTextField bCoefficient;
	JButton sumBt;
	JTextField resultTF;
	
	public Plotter() {
		setSize(600,600);
		initUI();
		//initSumatorWorker();
		//initListeners();
	}

	private void initUI() {
		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new GridLayout(3, 1));

		JPanel nbPanel = new JPanel();
		nbPanel.setLayout(new GridLayout(2, 2));

		nbPanel.add(new JLabel("a coefficient"));
		aCoefficient = new JTextField();
		aCoefficient.setPreferredSize(new Dimension(100, 22));
		// dodac tooltip informujacy co jest w tym polu tekstowym
		nbPanel.add(aCoefficient);

		nbPanel.add(new JLabel("b coefficient"));
		bCoefficient = new JTextField();
		bCoefficient.setPreferredSize(new Dimension(100, 22));
		// dodac tooltip informujacy co jest w tym polu tekstowym
		nbPanel.add(bCoefficient);
	
		mainPanel.add(nbPanel);

		sumBt = new JButton("Oblicz sumę");
		mainPanel.add(sumBt);
		
		JPanel resPanel = new JPanel();
		resPanel.setLayout(new GridLayout(1, 2));
		resPanel.add(new JLabel("Wynik"));
		resultTF = new JTextField();
		resultTF.setEditable(false);
		resultTF.setPreferredSize(new Dimension(100, 22));
		resPanel.add(resultTF);
		mainPanel.add(resPanel);
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		
		setContentPane(mainPanel);
		// ustala minimalny rozmiar okna
		// setMinimumSize(new Dimension(320, 200));
		pack();
		
		setTitle("Sumator - oblicznie sumy 2-ch liczb");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// utworzenie okna
				Plotter sumator = new Plotter();
				// pokazanie okna
				sumator.setVisible(true);
			}
		});

	}

}
