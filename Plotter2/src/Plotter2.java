import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rodrigo
 */
public class Plotter2 extends JPanel {
	
	Trinomal currentTrinomal = new Trinomal(0.0, 0.0, 0.0, -10.0, 10.0);

	private static final int FONT_SIZE = 12;
	private int width = 800;
	private int heigth = 400;

	static JTextField aCoefficient = new JTextField(5);
	static JTextField bCoefficient = new JTextField(5);
	static JTextField cCoefficient = new JTextField(5);
	static JTextField leftLimit = new JTextField(5);
	static JTextField rightLimit = new JTextField(5);
	static JButton button = new JButton("Plot!");
	
	static final int LEFT_GRAPH_PADDING = 30;
	static final int TOP_GRAPH_PADDING = 30;
	static final int GRAPH_WIDTH = 750;
	static final int GRAPH_HEIGHT = 500;
	static final int BAR_SIZE = 10;
	
	double roundUpTo2Decimal(double input) {
		return Math.round(input * 100.0) / 100.0;
	}
	
	double lmap(double val, double inMin, double inMax, double outMin, double outMax)
	{
	    return outMin + (outMax - outMin) * ((val - inMin) / (inMax - inMin));
	}
	
	private double trinomal(double a, double b, double c, double arg) {
		return a*arg*arg + b*arg + c;
	}
	
	private ArrayList<Point2D.Double> calculateTrinomalPoints(double a, double b, double c, double leftLimit, double rightLimit, double step) {
		ArrayList<Point2D.Double> toReturn = new ArrayList<Point2D.Double>();
		double iterator = leftLimit;
		while(iterator < rightLimit) {
			toReturn.add(new Point2D.Double(iterator, trinomal(a,b,c,iterator)));
			iterator += step;
		}
		return toReturn;
	}

	public Plotter2() {
		button.addActionListener(new PlotBTNListener());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//plot(g2);
		
		//drawPoint(g2, 100, 100);
		plot(g2, currentTrinomal.getL(), currentTrinomal.getR(), currentTrinomal.getA(), currentTrinomal.getB(), currentTrinomal.getC());
	}

	private void plot(Graphics2D g2, double l, double r, double a, double b, double c) {
		// draw white background
		drawWhiteBackground(g2);

		// create hatch marks and grid lines for y axis.
		//drawYGridAndMarks(g2);

		// and for x axis
		//drawXGridAndMarks(g2);

		// create x and y axes
		createXAndYAxis(g2);
		
		ArrayList<Point2D.Double> points = calculateTrinomalPoints(a, b, c, l, r, 0.01);
		drawPoints(g2, points, l, r);
		
		drawYMarks(g2, getMinPoint(points), getMaxPoint(points), 10);
		drawXMarks(g2, l, r, 10);
	}
	
	//public void plot() {
		// TODO Auto-generated method stub

	//}

	private void createXAndYAxis(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		//Y AXIS
		g2.draw(new Line2D.Double(LEFT_GRAPH_PADDING, TOP_GRAPH_PADDING, LEFT_GRAPH_PADDING, TOP_GRAPH_PADDING + GRAPH_HEIGHT));
		//X AXIS
		g2.draw(new Line2D.Double(LEFT_GRAPH_PADDING, TOP_GRAPH_PADDING + GRAPH_HEIGHT, LEFT_GRAPH_PADDING + GRAPH_WIDTH, TOP_GRAPH_PADDING + GRAPH_HEIGHT));
	}

	private void drawXMarks(Graphics2D g2, double leftLimit, double rightLimit, int noOfDivisions) {
		int graphLeftPixelBound = LEFT_GRAPH_PADDING;
		int graphRightPixelBound = LEFT_GRAPH_PADDING + GRAPH_WIDTH;
		
		int graphTopPixelBound = TOP_GRAPH_PADDING;
		int graphBottomPixelBound = TOP_GRAPH_PADDING + GRAPH_HEIGHT;
		
		LinSpace l = new LinSpace(graphLeftPixelBound, graphRightPixelBound, noOfDivisions);
		
		g2.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
		
		//-1-th iteration
		g2.drawLine(graphLeftPixelBound, graphBottomPixelBound, graphLeftPixelBound, graphBottomPixelBound - BAR_SIZE);
		g2.drawString(roundUpTo2Decimal(lmap(graphLeftPixelBound, graphLeftPixelBound, graphRightPixelBound, leftLimit, rightLimit)) + "", graphLeftPixelBound, graphBottomPixelBound + BAR_SIZE);
		//---------------
		
		while(l.hasNext()) {
			float nextFloat = l.getNextFloat();
			g2.drawLine((int)nextFloat, graphBottomPixelBound, (int)nextFloat, graphBottomPixelBound - BAR_SIZE);
			g2.drawString(roundUpTo2Decimal(lmap(nextFloat, graphLeftPixelBound, graphRightPixelBound, leftLimit, rightLimit)) + "", nextFloat, graphBottomPixelBound + BAR_SIZE);
		}
	}

	private void drawYMarks(Graphics2D g2, double bottomLimit, double topLimit, int noOfDivisions) {
		int graphLeftPixelBound = LEFT_GRAPH_PADDING;
		int graphRightPixelBound = LEFT_GRAPH_PADDING + GRAPH_WIDTH;
		
		int graphTopPixelBound = TOP_GRAPH_PADDING;
		int graphBottomPixelBound = TOP_GRAPH_PADDING + GRAPH_HEIGHT;
		
		LinSpace l = new LinSpace(graphTopPixelBound, graphBottomPixelBound, noOfDivisions);
		
		g2.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
		
		//-1-th iteration
		g2.drawLine(graphLeftPixelBound, graphTopPixelBound, graphLeftPixelBound + BAR_SIZE, graphTopPixelBound);
		g2.drawString(roundUpTo2Decimal(lmap(graphTopPixelBound, graphBottomPixelBound, graphTopPixelBound, bottomLimit, topLimit)) + "", graphLeftPixelBound - 3*BAR_SIZE, graphTopPixelBound);
		//---------------
		
		while(l.hasNext()) {
			float nextFloat = l.getNextFloat();
			if(!l.hasNext())
				break;
			//g2.drawLine((int)nextFloat, graphBottomPixelBound, (int)nextFloat, graphBottomPixelBound - BAR_SIZE);
			g2.drawLine(graphLeftPixelBound, (int)nextFloat, graphLeftPixelBound + BAR_SIZE, (int)nextFloat);
			g2.drawString(roundUpTo2Decimal(lmap(nextFloat, graphBottomPixelBound, graphTopPixelBound, bottomLimit, topLimit)) + "", graphLeftPixelBound - 3*BAR_SIZE, nextFloat);
		}
	}
	
	private void drawPoint(Graphics2D g2, int x, int y) {
		// TODO Auto-generated method stub
		g2.drawLine(x, y, x, y);
	}
	
	private void drawPoints(Graphics2D g2, ArrayList<Point2D.Double> points, double leftLimit, double rightLimit) {
		int graphLeftPixelBound = LEFT_GRAPH_PADDING;
		int graphRightPixelBound = LEFT_GRAPH_PADDING + GRAPH_WIDTH;
		
		int graphTopPixelBound = TOP_GRAPH_PADDING;
		int graphBottomPixelBound = TOP_GRAPH_PADDING + GRAPH_HEIGHT;
		
		
		
		for(Point2D.Double p : points) {
			double x = lmap(p.getX(), leftLimit, rightLimit, graphLeftPixelBound, graphRightPixelBound);
			double y = lmap(p.getY(), getMinPoint(points), getMaxPoint(points), graphBottomPixelBound, graphTopPixelBound);
			drawPoint(g2, (int)x, (int)y);
		}
	}
	
	double getMaxPoint(ArrayList<Point2D.Double> points) {
		double maxY = points.get(0).getY();
		for(Point2D.Double p : points) {
			if(p.getY() > maxY)
				maxY = p.getY();
		}
		return maxY;
	}
	
	double getMinPoint(ArrayList<Point2D.Double> points) {
		double minY = points.get(0).getY();
		for(Point2D.Double p : points) {
			if(p.getY() < minY)
				minY = p.getY();
		}
		return minY;
	}

	private void drawWhiteBackground(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.fillRect(LEFT_GRAPH_PADDING, TOP_GRAPH_PADDING, GRAPH_WIDTH, GRAPH_HEIGHT);
		g2.setColor(Color.BLACK);
	}

	private static void createAndShowGui() {
		Plotter2 mainPanel = new Plotter2();
		mainPanel.setPreferredSize(new Dimension(800, 600));
		JFrame frame = new JFrame("DrawGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(mainPanel, BorderLayout.NORTH);

		JPanel panel = new JPanel();

		panel.add(new JLabel("A coefficient"));
		panel.add(aCoefficient);
		panel.add(new JLabel("B coefficient"));
		panel.add(bCoefficient);
		panel.add(new JLabel("C coefficient"));
		panel.add(cCoefficient);
		panel.add(new JLabel("Left limit"));
		panel.add(leftLimit);
		panel.add(new JLabel("Right limit"));
		panel.add(rightLimit);

		panel.add(button);

		frame.getContentPane().add(panel, BorderLayout.SOUTH);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}
	
	class PlotBTNListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	currentTrinomal = new Trinomal(getA(), getB(), getC(), getL(), getR());
	    	/*Double a = Double.parseDouble(aCoefficient.getText());
	    	Double b = Double.parseDouble(bCoefficient.getText());
	    	Double c = Double.parseDouble(cCoefficient.getText());
	    	Double l = Double.parseDouble(leftLimit.getText());
	    	Double r = Double.parseDouble(rightLimit.getText());*/
	    	repaint();
	        //myTextArea.append(e.getActionCommand() + newline);
	    }
	}
	
	double getA() {
		Double a = 0.0;
		try {
			a = Double.parseDouble(aCoefficient.getText());
		} catch(Exception e) {
		}
		return a;
	}
	
	double getB() {
		Double b = 0.0;
		try {
			b = Double.parseDouble(bCoefficient.getText());
		} catch(Exception e) {
		}
		return b;
	}
	
	double getC() {
		Double c = 0.0;
		try {
			c = Double.parseDouble(cCoefficient.getText());
		} catch(Exception e) {
		}
		return c;
	}
	
	double getL() {
		Double l = 0.0;
		try {
			l = Double.parseDouble(leftLimit.getText());
		} catch(Exception e) {
		}
		return l;
	}
	
	double getR() {
		Double r = 0.0;
		try {
			r = Double.parseDouble(rightLimit.getText());
		} catch(Exception e) {
		}
		return r;
	}
}