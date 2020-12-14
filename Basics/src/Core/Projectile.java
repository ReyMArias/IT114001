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
	private int x, y, speed;
	private boolean visible;

	public Projectile(int startX, int startY) {
		x = startX;
		y = startY;
		speed = 10;
		visible = true;
	}

	private void getCollidingPlayers(List<ClientPlayer> clientPlayers) {

	}

	protected void destroy() {

	}

	public void update() {
		x += speed;
	}
}