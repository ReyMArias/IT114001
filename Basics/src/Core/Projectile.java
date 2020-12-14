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
		if (x > 800 || y > 800) {
			visible = false;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}