package Client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import Core.BaseGamePanel;
import Server.GameState;

public class GamePanel extends BaseGamePanel implements Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1121202275148798015L;
	List<Player> players;
	Player myPlayer;
	String playerUsername;// caching it so we don't lose it when room is wiped
	private final static Logger log = Logger.getLogger(GamePanel.class.getName());
	Dimension gameAreaSize = new Dimension();
	private static int teamAScore = 0;
	private static int teamBScore = 0;

	private int mouse_x, mouse_y;
	private String str = "";

	private static final int TEXT_SIZE = 3;
	private static GameState gameState = GameState.LOBBY;
	private final static long ROUND_TIME = TimeUnit.MINUTES.toNanos(5);
	public final static long MINUTE = TimeUnit.MINUTES.toNanos(1);
	private static long timeLeft = ROUND_TIME;
	private static Dimension boundary;

	public void setPlayerName(String name) {
		playerUsername = name;
		if (myPlayer != null) {
			myPlayer.setName(playerUsername);
		}
	}

	@Override
	public synchronized void onClientConnect(String clientName, String message) {
		// TODO Auto-generated method stub
		System.out.println("Connected on Game Panel: " + clientName);
		boolean exists = false;
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null && p.getName().equalsIgnoreCase(clientName)) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			Player p = new Player();
			p.setName(clientName);
			players.add(p);
			// want .equals here instead of ==
			// https://www.geeksforgeeks.org/difference-equals-method-java/
			if (clientName.equals(playerUsername)) {
				System.out.println("Reset myPlayer");
				myPlayer = p;
			}
		}
	}

	@Override
	public void onClientDisconnect(String clientName, String message) {
		System.out.println("Disconnected on Game Panel: " + clientName);
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null && !p.getName().equals(playerUsername) && p.getName().equalsIgnoreCase(clientName)) {
				iter.remove();
				break;
			}
		}
	}

	@Override
	public void onMessageReceive(String clientName, String message) {
		// TODO Auto-generated method stub
		System.out.println("Message on Game Panel");

	}

	@Override
	public void onChangeRoom() {
		// don't clear, since we're using iterators to loop, remove via iterator
		// players.clear();
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
		myPlayer = null;
		System.out.println("Cleared players");
	}

	@Override
	public void awake() {
		players = new ArrayList<Player>();
		GamePanel gp = this;
		// fix the loss of focus when typing in chat
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				gp.getRootPane().grabFocus();
			}
		});
	}

	@Override
	public void start() {
		// TODO goes on server side, here for testing

	}

	@Override
	public void update() {
		applyControls();
		localMovePlayers();
	}

	/**
	 * Gets the current state of input to apply movement to our player
	 */
	public double degrees = 90.0;
	public int x = 0, y = 0;

	private void applyControls() {
		if (myPlayer != null) {
			if (KeyStates.W) {
				y = -1;
			}
			if (KeyStates.S) {
				y = 1;
			}
			if (!KeyStates.W && !KeyStates.S) {
				y = 0;
			}
			if (KeyStates.A) {
				x = -1;
			} else if (KeyStates.D) {
				x = 1;
			}
			if (!KeyStates.A && !KeyStates.D) {
				x = 0;
			}

			if (KeyStates.LEFTARROW) {
				degrees = degrees - 36;
			}

			if (KeyStates.RIGHTARROW) {
				degrees = degrees + 36;
			}
			/*
			 * if (KeyStates.LEFTARROW) { x = -1; } else if (KeyStates.RIGHTARROW) { x = 1;
			 * } if (!KeyStates.LEFTARROW && !KeyStates.RIGHTARROW) { x = 0; }
			 */
			boolean changed = myPlayer.setDirection(x, y);
			if (changed) {
				// only send data if direction changed, otherwise we're creating unnecessary
				// network traffic
				System.out.println("Direction changed");
				SocketClient.INSTANCE.syncDirection(new Point(x, y));
			}
		}
	}

	/**
	 * This is just an estimate/hint until we receive a position sync from the
	 * server
	 */
	private void localMovePlayers() {
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null) {
				p.move();
			}
		}
	}

	@Override
	public void lateUpdate() {
		// stuff that should happen at a slightly different time than stuff in normal
		// update()

	}

	@Override
	public synchronized void draw(Graphics g) {
		setBackground(Color.BLACK);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawPlayers(g);
		drawText(g);
		drawUI((Graphics2D) g);
	}

	private void drawBorder(Graphics g) {
		if (boundary != null) {
			g.setColor(Color.WHITE);
			g.drawRect(5, 5, boundary.width - 10, boundary.height - 10);
		}

	}

	private synchronized void drawPlayers(Graphics g) {
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null) {
				p.draw(g);
			}
		}
	}

	private void drawText(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.PLAIN, 12));
		if (myPlayer != null) {
			g.drawString("Debug MyPlayer: " + myPlayer.toString(), 10, 20);
		}

		if (gameState == GameState.GAME) {
			String timeLeftStr = "Time Left: " + (timeLeft / MINUTE) + "min left!";
			g.setColor(Color.WHITE);
			g.setFont(new Font("Monospaced", Font.BOLD, 24));
			g.drawString(timeLeftStr, boundary.width / 2, 50);
			g.drawString("Team A Score: " + teamAScore, boundary.width / 3, 70);
			g.drawString("Team B Score: " + teamBScore, (int) (boundary.width * 0.667), 70);

			g.fillOval(mouse_x, mouse_y, 10, 10); // gives the bullet
			g.drawString(mouse_x + "," + mouse_y, mouse_x + 10, mouse_y - 10); // displays the x and y position
			g.drawString(str, mouse_x + 10, mouse_y + 20); // displays the action performed
			g.drawString(str, boundary.width / 2, boundary.height / 2);

		} else {
			String notStartedStr = "Game has not started yet!";
			int offset = (boundary.width / 2) - (notStartedStr.length() * TEXT_SIZE);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Monospaced", Font.BOLD, 24));
			g.drawString(notStartedStr, offset, 50);
		}
	}

	private void drawUI(Graphics2D g2) {
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(0, 0, gameAreaSize.width, gameAreaSize.height);
		g2.setStroke(oldStroke);
	}

	@Override
	public void quit() {
		log.log(Level.INFO, "GamePanel quit");
		this.removeAll();
	}

	@Override
	public void attachListeners() {
		InputMap im = this.getRootPane().getInputMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "up_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "up_released");

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "down_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "down_released");

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "left_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "left_released");

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "right_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "right_released");

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left_arrow_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "left_arrow_released");

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right_arrow_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "right_arrow_released");

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "space_pressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "space_released");

		ActionMap am = this.getRootPane().getActionMap();

		am.put("up_pressed", new MoveAction(KeyEvent.VK_W, true));
		am.put("up_released", new MoveAction(KeyEvent.VK_W, false));

		am.put("down_pressed", new MoveAction(KeyEvent.VK_S, true));
		am.put("down_released", new MoveAction(KeyEvent.VK_S, false));

		am.put("left_pressed", new MoveAction(KeyEvent.VK_A, true));
		am.put("left_released", new MoveAction(KeyEvent.VK_A, false));

		am.put("right_pressed", new MoveAction(KeyEvent.VK_D, true));
		am.put("right_released", new MoveAction(KeyEvent.VK_D, false));

		am.put("left_arrow_pressed", new MoveAction(KeyEvent.VK_LEFT, true));
		am.put("left_arrow_released", new MoveAction(KeyEvent.VK_LEFT, false));

		am.put("right_arrow_pressed", new MoveAction(KeyEvent.VK_RIGHT, true));
		am.put("right_arrow_released", new MoveAction(KeyEvent.VK_RIGHT, false));

		am.put("space_pressed", new MoveAction(KeyEvent.VK_SPACE, true));
		am.put("space_released", new MoveAction(KeyEvent.VK_SPACE, false));
	}

	@Override
	public void onSyncDirection(String clientName, Point direction) {
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null && p.getName().equalsIgnoreCase(clientName)) {
				System.out.println("Syncing direction: " + clientName);
				p.setDirection(direction.x, direction.y);
				System.out.println("From: " + direction);
				System.out.println("To: " + p.getDirection());
				break;
			}
		}
	}

	@Override
	public void onSyncPosition(String clientName, Point position) {
		System.out.println("Got position for " + clientName);
		Iterator<Player> iter = players.iterator();
		while (iter.hasNext()) {
			Player p = iter.next();
			if (p != null && p.getName().equalsIgnoreCase(clientName)) {
				System.out.println(clientName + " set " + position);
				p.setPosition(position);
				break;
			}
		}
	}

	@Override
	public void onGetRoom(String roomName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResize(Point p) {
		// TODO Auto-generated method stub
		gameAreaSize = new Dimension(p.x, p.y);
		this.setPreferredSize(gameAreaSize);
		this.setMinimumSize(gameAreaSize);
		this.setMaximumSize(gameAreaSize);
		this.setSize(gameAreaSize);
		System.out.println(this.getSize());
		this.invalidate();
		this.repaint();
	}

	@Override
	public void onChangeTeam(int number) {
		myPlayer.setTeam(number);
	}

	@Override
	public void onSetPlayerColor(int teamId, String clientName) {
		for (Player player : players) {
			if (player.getName() == clientName) {
				if (teamId == 1) {
					player.setColor(Color.RED);
				} else {
					player.setColor(Color.BLUE);
				}
				break;
			}
		}

		repaint();
	}

	@Override
	public void onGameStart(Point startPos, int playerId) {
		for (Player player : players) {
			if (player.getId() == playerId) {
				player.setPosition(startPos);

				break;
			}
		}

		repaint();
	}

	@Override
	public void onSetGameState(GameState state) {
		gameState = state;
	}

	@Override
	public void onSetTimeLeft(long time) {
		timeLeft = time;
	}

	@Override
	public void onSetGameBoundary(int x, int y) {
		boundary = new Dimension(x, y);
	}

	@Override
	public void onSetPlayerGhost(boolean bool) {
		for (Player player : players) {
			player.setActive(bool);
		}

	}

	@Override
	public void onSetHP(Point health) {
		if (health.x == 0) {
			for (Player player : players) {
				player.setHP(health.y);
			}
		} else {
			for (Player player : players) {
				if (player.getId() == health.x) {
					player.setHP(health.y);
					return;
				}
			}
		}
	}

}