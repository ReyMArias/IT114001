package Core;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

import Server.ClientPlayer;

public class Projectile extends GameObject {
	private Dimension screenBounds;
	private Point direction = new Point(0, 0);
	private int radius = 2;
	private int team = 0;

	private void getCollidingPlayers(List<ClientPlayer> clientPlayers) {

	}

	protected void destroy() {

	}

	public void update() {

	}
}