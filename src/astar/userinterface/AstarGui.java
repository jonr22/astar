package astar.userinterface;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import astar.info.Coordinate;
import astar.info.Grid;
import astar.pathfinder.Astar;
import astar.pathfinder.AstarFactory;

public class AstarGui {
	private JFrame _frame;
	private GridBoard _gridBoard;
	private Grid _grid;
	private Astar _implementation = AstarFactory.getDefault();
	private JLabel _infoLabel;
	
	private static final String DEFAULT_INFO = "Time: 0 ms\tPath: 0 units";
	
	public void buildGui() {
		
		_grid = new Grid();
		
		_frame = new JFrame("A*");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JButton runBtn = new JButton("Run");
        JButton clearBtn = new JButton("Clear");
        JComboBox implementationList = new JComboBox(instantiateAlgorithms());
        
        clearBtn.addActionListener(new ClearGridListener());
        runBtn.addActionListener(new  RunGridListener());
        implementationList.addActionListener(new SelectImplementationListener());
        
        topPanel.add(runBtn);
        topPanel.add(clearBtn);
        topPanel.add(implementationList);
        
        background.add(topPanel, BorderLayout.NORTH);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        
        _infoLabel = new JLabel();
        _infoLabel.setText("Time: 0 ms");
        
        bottomPanel.add(_infoLabel);
        
        background.add(bottomPanel, BorderLayout.SOUTH);
        
        _gridBoard = new GridBoard();
        _gridBoard.addMouseListener(new UserInputListener());
        background.add(_gridBoard, BorderLayout.CENTER);
        
        _frame.getContentPane().add(background);
        _frame.setSize(500, 500);
        _frame.setVisible(true);
        
	}
    
	private class GridBoard extends JPanel {
		public void paintComponent(Graphics g) {
			try {
				super.paintComponent(g);
				
				Color blockColor = Color.DARK_GRAY;
				Color backgroundColor = Color.WHITE;
				Color gridColor = Color.BLACK;
				Color startColor = Color.GREEN;
				Color endColor = Color.RED;
				Color pathColor = Color.BLUE;
				Color nodeColor;
				
				Grid.NodeType nodeValue;
				
				int lineThickness = 2;
				int width = getWidth();
				int height = getHeight();
				Graphics2D g2d = (Graphics2D) g;
				
				// draw background
				g2d.setPaint(backgroundColor);
				g2d.fill(new Rectangle2D.Double(0, 0, width, height));
				 
				// draw nodes (do this before grid lines, so that the lines appear on top of blocks)
				for (int row = 0; row < Grid.SIZE; row++) {
					for (int col = 0; col < Grid.SIZE; col++) {
						nodeValue = _grid.getValue(new Coordinate(row, col));
						switch (nodeValue) {
							case EMPTY:
								continue; //TODO: check this
							case BLOCK:
								nodeColor = blockColor;
								break;
							case START:
								nodeColor = startColor;
								break;
							case END:
								nodeColor = endColor;
								break;
							case PATH:
								nodeColor = pathColor;
								break;
							default:
								nodeColor = backgroundColor;
								break;
						}
						
						g2d.setPaint(nodeColor);
						g2d.fill(new Rectangle2D.Double(
							(width * col) / Grid.SIZE, 
							(height * row) / Grid.SIZE, 
							width / Grid.SIZE, 
							height / Grid.SIZE));
					
					}
				}
				
				// draw grid
				g2d.setPaint(gridColor);
				g2d.setStroke(new BasicStroke(lineThickness));
				for (int lineNum = 0; lineNum <= Grid.SIZE; lineNum++) {
					g2d.draw(new Line2D.Double(0, (height * lineNum) / Grid.SIZE, width, (height * lineNum) / Grid.SIZE));
					g2d.draw(new Line2D.Double((width * lineNum) / Grid.SIZE, 0, (width * lineNum) / Grid.SIZE, height));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
    private class ClearGridListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
        	_grid.clear();
        	_gridBoard.repaint();
        }
    }
    
    private class RunGridListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
        	try {
        		if (_grid.getStart() == null || _grid.getEnd() == null) {
        			System.out.println("Run clicked without a start and end node set.");
        			JOptionPane.showMessageDialog(null, "Start and End nodes must be set!");
        			return;
        		}
        		
        		_grid.clearPath();
        		_gridBoard.repaint();
        		
	        	_implementation.setGrid(_grid);
	        	ArrayList<Coordinate> path = _implementation.findPath();
	        	Iterator<Coordinate> pathIter = path.iterator();
	        	while (pathIter.hasNext()) {
	        		_grid.setValue(pathIter.next(), Grid.NodeType.PATH);
	        	}
	        	
	        	_infoLabel.setText(String.format("Time: %d ms\tPath: 0 units", _implementation.getRuntime()));
	        	
	        	_gridBoard.repaint();
        	} catch (Exception ex) {
        		ex.printStackTrace();
        	}
        }
    }
    
    private class SelectImplementationListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
        	try {
        		JComboBox cb = (JComboBox)a.getSource();
        		_implementation = (Astar)cb.getSelectedItem();
        		
        		_infoLabel.setText(DEFAULT_INFO);
        		_grid.clearPath();
        		
	        	_gridBoard.repaint();
        	} catch (Exception ex) {
        		ex.printStackTrace();
        	}
        }
    }
    
	private class UserInputListener implements MouseListener {
		private Coordinate _coord = new Coordinate();
		
		public void mouseClicked(MouseEvent e) {
			_coord.setRow((e.getY() * Grid.SIZE) /_gridBoard.getHeight());
			_coord.setCol((e.getX() * Grid.SIZE) /_gridBoard.getWidth());
			
			//if (e.isPopupTrigger()) {
			if (e.getButton() != MouseEvent.BUTTON1) {
				PopUpMenu menu = new PopUpMenu();
				menu.show(e.getComponent(), e.getX(), e.getY());
			} else {
				addBlock();
			}

		}
		
		// Ignore other mouse events
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		
		private void addBlock() {
			Grid.NodeType fillValue = Grid.NodeType.BLOCK;
			Grid.NodeType currentValue = Grid.NodeType.EMPTY;
			
			System.out.println("Click: row = " + _coord.getRow() + ", col = " + _coord.getCol());
			try {
				currentValue = _grid.getValue(_coord);
				
				switch (currentValue) {
					case EMPTY:
						fillValue = Grid.NodeType.BLOCK;
						break;
					case BLOCK:
					case START:
					case END:
						fillValue = Grid.NodeType.EMPTY;
						break;
				}
				
				_grid.setValue(_coord, fillValue);
				_gridBoard.repaint();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		private class PopUpMenu extends JPopupMenu {
		    public PopUpMenu(){
			    JMenuItem startItem = new JMenuItem("Start");
			    JMenuItem endItem = new JMenuItem("End");
			    
			    startItem.addActionListener(new StartNodeListener());
			    endItem.addActionListener(new EndNodeListener());
			    
		        add(startItem);
		        add(endItem);
		    }
		}
		
	    private class StartNodeListener implements ActionListener {
	        public void actionPerformed(ActionEvent a) {
	        	try {
	        		_grid.setValue(_coord, Grid.NodeType.START);
	        		_gridBoard.repaint();
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        	}
	        }
	    }
	    
	    private class EndNodeListener implements ActionListener {
	        public void actionPerformed(ActionEvent a) {
	        	try {
	        		_grid.setValue(_coord, Grid.NodeType.END);
	        		_gridBoard.repaint();
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        	}
	        }
	    }
    }
	
	private Astar[] instantiateAlgorithms() {
		AstarFactory.Implementation implementationValues[] = AstarFactory.Implementation.values();
		Astar implementations[] = new Astar[implementationValues.length];
		for (int i = 0; i < implementationValues.length; i++) {
			implementations[i] = AstarFactory.getAstar(implementationValues[i]);
		}
		return implementations;
	}
	
	public static void main(String[] args) {
		AstarGui gui = new AstarGui();
		gui.buildGui();
	}
}
