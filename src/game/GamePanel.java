import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {

    public enum GameState { PlacingShips, FiringShots, GameOver }
    private StatusPanel statusPanel;
    private SelectionGrid computer;
    private SelectionGrid player;
    private BattleshipAI aiController;

    private Ship placingShip;
    private Position tempPlacingPosition;
    private int placingShipIndex;
    private GameState gameState;
    public static boolean debugModeActive;

    public GamePanel(int aiChoice) {
        computer = new SelectionGrid(0,0);
        player = new SelectionGrid(0,computer.getHeight()+50);
        setBackground(new Color(42, 136, 163));
        setPreferredSize(new Dimension(computer.getWidth(), player.getPosition().y + player.getHeight()));
        addMouseListener(this);
        addMouseMotionListener(this);
        if(aiChoice == 0) aiController = new SimpleRandomAI(player);
        else aiController = new SmarterAI(player,aiChoice == 2,aiChoice == 2);
        statusPanel = new StatusPanel(new Position(0,computer.getHeight()+1),computer.getWidth(),49);
        restart();
    }

    public void paint(Graphics g) {
        super.paint(g);
        computer.paint(g);
        player.paint(g);
        if(gameState == GameState.PlacingShips) {
            placingShip.paint(g);
        }
        statusPanel.paint(g);
    }

    public void handleInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        } else if(keyCode == KeyEvent.VK_R) {
            restart();
        } else if(gameState == GameState.PlacingShips && keyCode == KeyEvent.VK_Z) {
            placingShip.toggleSideways();
            updateShipPlacement(tempPlacingPosition);
        } else if(keyCode == KeyEvent.VK_D) {
            debugModeActive = !debugModeActive;
        }
        repaint();
    }

    public void restart() {
        computer.reset();
        player.reset();
        // El jugador puede ver sus propios barcos por defecto
        player.setShowShips(true);
        aiController.reset();
        tempPlacingPosition = new Position(0,0);
        placingShip = new Ship(new Position(0,0),
                               new Position(player.getPosition().x,player.getPosition().y),
                               SelectionGrid.BOAT_SIZES[0], true);
        placingShipIndex = 0;
        updateShipPlacement(tempPlacingPosition);
        computer.populateShips();
        debugModeActive = false;
        statusPanel.reset();
        gameState = GameState.PlacingShips;
    }

    private void tryPlaceShip(Position mousePosition) {
        Position targetPosition = player.getPositionInGrid(mousePosition.x, mousePosition.y);
        updateShipPlacement(targetPosition);
        if(player.canPlaceShipAt(targetPosition.x, targetPosition.y,
                SelectionGrid.BOAT_SIZES[placingShipIndex],placingShip.isSideways())) {
            placeShip(targetPosition);
        }
    }

    private void placeShip(Position targetPosition) {
        placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Placed);
        player.placeShip(placingShip,tempPlacingPosition.x,tempPlacingPosition.y);
        placingShipIndex++;
        // Si aun se encuentran naves para colocar
        if(placingShipIndex < SelectionGrid.BOAT_SIZES.length) {
            placingShip = new Ship(new Position(targetPosition.x, targetPosition.y),
                          new Position(player.getPosition().x + targetPosition.x * SelectionGrid.CELL_SIZE,
                       player.getPosition().y + targetPosition.y * SelectionGrid.CELL_SIZE),
                          SelectionGrid.BOAT_SIZES[placingShipIndex], true);
            updateShipPlacement(tempPlacingPosition);
        } else {
            gameState = GameState.FiringShots;
            statusPanel.setTopLine("Ataca a la maquin!");
            statusPanel.setBottomLine("Destruye al enemigo!");
        }
    }

    private void tryFireAtComputer(Position mousePosition) {
        Position targetPosition = computer.getPositionInGrid(mousePosition.x,mousePosition.y);
        // Ignora si la posicion ha sido inicializada antes
        if(!computer.isPositionMarked(targetPosition)) {
            doPlayerTurn(targetPosition);
            // Solo hace el turno de la computadora si el juego no terminó en el turno del jugador
            if(!computer.areAllShipsDestroyed()) {
                doAITurn();
            }
        }
    }

    private void doPlayerTurn(Position targetPosition) {
        boolean hit = computer.markPosition(targetPosition);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if(hit && computer.getMarkerAtPosition(targetPosition).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destruido)";
        }
        statusPanel.setTopLine("Jugador " + hitMiss + " " + targetPosition + destroyed);
        if(computer.areAllShipsDestroyed()) {
            // ganas
            gameState = GameState.GameOver;
            statusPanel.showGameOver(true);
        }
    }

    private void doAITurn() {
        Position aiMove = aiController.selectMove();
        boolean hit = player.markPosition(aiMove);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if(hit && player.getMarkerAtPosition(aiMove).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destruido)";
        }
        statusPanel.setBottomLine("Computadora " + hitMiss + " " + aiMove + destroyed);
        if(player.areAllShipsDestroyed()) {
            // La computadora gana
            gameState = GameState.GameOver;
            statusPanel.showGameOver(false);
        }
    }

    private void tryMovePlacingShip(Position mousePosition) {
        if(player.isPositionInside(mousePosition)) {
            Position targetPos = player.getPositionInGrid(mousePosition.x, mousePosition.y);
            updateShipPlacement(targetPos);
        }
    }

    private void updateShipPlacement(Position targetPos) {
        // Restringir para encajar dentro de la grilla
        if(placingShip.isSideways()) {
            targetPos.x = Math.min(targetPos.x, SelectionGrid.GRID_WIDTH - SelectionGrid.BOAT_SIZES[placingShipIndex]);
        } else {
            targetPos.y = Math.min(targetPos.y, SelectionGrid.GRID_HEIGHT - SelectionGrid.BOAT_SIZES[placingShipIndex]);
        }
        // Update drawing position to use the new target position
        placingShip.setDrawPosition(new Position(targetPos),
                                    new Position(player.getPosition().x + targetPos.x * SelectionGrid.CELL_SIZE,
                                 player.getPosition().y + targetPos.y * SelectionGrid.CELL_SIZE));
        // Actualizar la posición del dibujo para usar la nueva posición de destino
        tempPlacingPosition = targetPos;
        // Cambia el color del barco en función de si se puede colocar en la ubicación actual
        if(player.canPlaceShipAt(tempPlacingPosition.x, tempPlacingPosition.y,
                SelectionGrid.BOAT_SIZES[placingShipIndex],placingShip.isSideways())) {
            placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Valid);
        } else {
            placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Invalid);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Position mousePosition = new Position(e.getX(), e.getY());
        if(gameState == GameState.PlacingShips && player.isPositionInside(mousePosition)) {
            tryPlaceShip(mousePosition);
        } else if(gameState == GameState.FiringShots && computer.isPositionInside(mousePosition)) {
            tryFireAtComputer(mousePosition);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameState != GameState.PlacingShips) return;
        tryMovePlacingShip(new Position(e.getX(), e.getY()));
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
  
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}
}
