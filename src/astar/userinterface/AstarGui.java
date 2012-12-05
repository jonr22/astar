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
import java.awt.event.MouseMotionListener;
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

/**
 * Display GUI with Grid where user can select start and end nodes, and
 * place walls/blocks to find the shortest path between. The Astar algorithm
 * can be used with various heuristics, selected by the user, to find the
 * shortest path.
 *
 * @author Jonathan Reimels
 *
 */
public class AstarGui {
    private JFrame _frame;
    private GridBoard _gridBoard;
    private Grid _grid;
    private Astar _implementation = AstarFactory.getDefault();
    private JLabel _infoLabel;

    private static final String INFO_STRING = "Time: %d ms  |  Steps: %d  |  Path: %d units";
    private static final String DEFAULT_INFO_STRING = "Time: 0 ms  |  Steps: 0  |  Path: 0 units";

    /**
     * Create the GUI and instantiate the grid displayed to the user
     */
    public void buildGui() {

        // instantiate a new Grid
        _grid = new Grid();

        // create the frame with borders
        _frame = new JFrame("A*");
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // create the top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        // create buttons for the top panel
        JButton runBtn = new JButton("Run");
        JButton clearBtn = new JButton("Clear");

        // create and populate combobox with implemented algorithms
        JComboBox implementationList = new JComboBox(instantiateAlgorithms());

        // set actions for top panel buttons
        clearBtn.addActionListener(new ClearGridListener());
        runBtn.addActionListener(new  RunGridListener());
        implementationList.addActionListener(new SelectImplementationListener());

        // add buttons/comboboxes to top panel
        topPanel.add(runBtn);
        topPanel.add(clearBtn);
        topPanel.add(implementationList);

        // add panel to main window
        background.add(topPanel, BorderLayout.NORTH);

        // create bottom panel to display statistics
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        // add default text to statistics bar
        _infoLabel = new JLabel();
        _infoLabel.setText(DEFAULT_INFO_STRING);

        bottomPanel.add(_infoLabel);

        background.add(bottomPanel, BorderLayout.SOUTH);

        // create GridBoard (the display of the Grid) and add listeners
        _gridBoard = new GridBoard();
        _gridBoard.addMouseListener(new UserInputListener());
        _gridBoard.addMouseMotionListener(new UserInputListener());
        background.add(_gridBoard, BorderLayout.CENTER);

        // display frame
        _frame.getContentPane().add(background);
        _frame.setSize(500, 500);
        _frame.setVisible(true);
    }

    /**
     * JPanel display of Grid
     */
    private class GridBoard extends JPanel {

        /**
         * Generated Serial Version ID
         */
        private static final long serialVersionUID = 1711881728225317214L;

        @Override
        public void paintComponent(Graphics g) {
            try {
                super.paintComponent(g);

                // set colors for various items
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

                        // get appropriate color for NodeType
                        nodeValue = _grid.getValue(new Coordinate(row, col));
                        switch (nodeValue) {
                            case EMPTY:
                                continue;
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

                        // fill in grid block with selected color
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

    /**
     * Clear the entire Grid
     */
    private class ClearGridListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent a) {
            _infoLabel.setText(DEFAULT_INFO_STRING);
            _grid.clear();
            _gridBoard.repaint();
        }
    }

    /**
     * Run the selected algorithm
     */
    private class RunGridListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent a) {
            try {
                // check that start and end nodes have been set
                if (_grid.getStart() == null || _grid.getEnd() == null) {
                    JOptionPane.showMessageDialog(null, "Start and End nodes must be set!");
                    return;
                }

                // clear the previous path (this is safe for the first run)
                _grid.clearPath();
                _gridBoard.repaint();

                // set the grid for Astar to use
                _implementation.setGrid(_grid);

                // call the algorithm
                ArrayList<Coordinate> path = _implementation.findPath();

                // check if a path couldn't be found
                if (path == null) {
                    JOptionPane.showMessageDialog(null, "Unable to find a path!");
                    return;
                }

                // iterate through the returned path, and set the path locations on the grid
                Iterator<Coordinate> pathIter = path.iterator();
                while (pathIter.hasNext()) {
                    _grid.setValue(pathIter.next(), Grid.NodeType.PATH);
                }

                // update the statistics info
                _infoLabel.setText(String.format(INFO_STRING, _implementation.getRuntime(),_implementation.getStepCount(), _implementation.getDistance()));

                // update the GUI
                _gridBoard.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Set which algorithm to use
     */
    private class SelectImplementationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent a) {
            try {
                // get currently selected Astar Implementation in the combobox
                JComboBox cb = (JComboBox)a.getSource();
                _implementation = (Astar)cb.getSelectedItem();

                // reset statistics and clear the current path
                _infoLabel.setText(DEFAULT_INFO_STRING);
                _grid.clearPath();

                // update the GUI
                _gridBoard.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Handle mouse clicks (to set walls/blocks and start/end nodes)
     */
    private class UserInputListener implements MouseListener, MouseMotionListener {
        private Coordinate _coord = new Coordinate(); // hold the currently clicked on coordinate

        /**
         * Handle all mouse clicks
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            // find and set the coordinates of the current click
            _coord.setRow((e.getY() * Grid.SIZE) /_gridBoard.getHeight());
            _coord.setCol((e.getX() * Grid.SIZE) /_gridBoard.getWidth());

            // if right mouse was clicked display a context
            //   menu, otherwise add/remove a block to the Grid
            if (e.getButton() != MouseEvent.BUTTON1) {
                PopUpMenu menu = new PopUpMenu();
                menu.show(e.getComponent(), e.getX(), e.getY());
            } else {
                addBlock(); // add/remove a block
            }

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Coordinate coord = new Coordinate(
                    (e.getY() * Grid.SIZE) /_gridBoard.getHeight(),
                    (e.getX() * Grid.SIZE) /_gridBoard.getWidth());

            if (!_coord.isEqual(coord)) {
                _coord = coord.clone();
                addBlock();
            }
        }

        // Ignore all other mouse events
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
        @Override
        public void mouseMoved(MouseEvent e) {}

        /**
         * Add/Remove a block/wall to the grid at the current Coordinate
         */
        private void addBlock() {
            // set defaults
            Grid.NodeType fillValue = Grid.NodeType.BLOCK;
            Grid.NodeType currentValue = Grid.NodeType.EMPTY;

            try {
                // get value of currently selected coordinate
                currentValue = _grid.getValue(_coord);

                // set the fill value for the coordinate based on the current value
                //   ie if it's currently empty or part of a path, it becomes a block,
                //   otherwise its set to empty
                switch (currentValue) {
                    case EMPTY:
                    case PATH:
                        fillValue = Grid.NodeType.BLOCK;
                        break;
                    case BLOCK:
                    case START:
                    case END:
                        fillValue = Grid.NodeType.EMPTY;
                        break;
                }

                // set new value for the coordinate and update the GUI
                _grid.setValue(_coord, fillValue);
                _gridBoard.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        /**
         * Context menu for right-clicks
         */
        private class PopUpMenu extends JPopupMenu {
            /**
             * Generated Serial Version ID
             */
            private static final long serialVersionUID = 783571762215800686L;

            public PopUpMenu(){
                // create context menu items
                JMenuItem startItem = new JMenuItem("Start");
                JMenuItem endItem = new JMenuItem("End");

                // add click actions
                startItem.addActionListener(new StartNodeListener());
                endItem.addActionListener(new EndNodeListener());

                // add the items to the context menu
                add(startItem);
                add(endItem);
            }
        }

        /**
         * Add start node to grid
         */
        private class StartNodeListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent a) {
                try {
                    // set start node to currently selected coordinate
                    _grid.setValue(_coord, Grid.NodeType.START);
                    _gridBoard.repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        /**
         * Add end node to grid
         */
        private class EndNodeListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent a) {
                try {
                    // set end node to currently selected coordinate
                    _grid.setValue(_coord, Grid.NodeType.END);
                    _gridBoard.repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Get and instantiate all implemented Astar algorithms
     * @return array of implemented algorithms
     */
    private Astar[] instantiateAlgorithms() {
        // get list of implemented algorithms from the Factory and create an array to hold them
        AstarFactory.Implementation implementationValues[] = AstarFactory.Implementation.values();
        Astar implementations[] = new Astar[implementationValues.length];

        // loop through all implementations, instantiate them and add them to the array
        for (int i = 0; i < implementationValues.length; i++) {
            implementations[i] = AstarFactory.getAstar(implementationValues[i]);
        }

        // return instantiated algorithms
        return implementations;
    }

    /**
     * Run GUI
     * @param args
     */
    public static void main(String[] args) {
        AstarGui gui = new AstarGui();
        gui.buildGui();
    }
}
