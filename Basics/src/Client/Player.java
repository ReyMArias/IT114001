package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

import Core.GameObject;

public class Player extends GameObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6088251166673414031L;
	private static final int GUN = 2;
	private Color color = Color.WHITE;
	private Point nameOffset = new Point(0, -5);
	private boolean isReady = false;
	private Point barrel = new Point(position.x + (size.width / 2), position.y + (size.height / 2));
	public int HP = 3;
	public int shotX = (position.x + (size.width / 2));
	public int shotY = (position.y + (size.height / 2));

	public void setDirectionLine(Point dir) {
		barrel.x = dir.x + 90;
		barrel.y = dir.y + 90;
	}

	public void setReady(boolean r) {
		isReady = r;
	}

	public boolean isReady() {
		return isReady;
	}

	/**
	 * Gets called by the game engine to draw the current location/size
	 */
	@Override
	public boolean draw(Graphics g) {
		// using a boolean here so we can block drawing if isActive is false via call to
		// super
		if (super.draw(g)) {
			g.setColor(color);
			g.fillOval(position.x, position.y, size.width, size.height);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Monospaced", Font.PLAIN, 12));
			g.drawString("Name: " + name, position.x + nameOffset.x, position.y + nameOffset.y);
			if (barrel.x != position.x || barrel.y != position.y) {
				g.drawLine(position.x + (size.width / 2), position.y + (size.height / 2),
						position.x + (size.width / 2) + (barrel.x * GUN),
						position.y + (size.height / 2) + (barrel.y * GUN));
			}

		}
		if (HP <= 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String toString() {
		return String.format("Player ID: %d, Name: %s, p: (%d,%d), s: (%d, %d), d: (%d, %d), isActive: %s", id, name,
				position.x, position.y, speed.x, speed.y, direction.x, direction.y, isActive);
	}

	@Override
	public void setTeam(int teamNumber) {
		switch (teamNumber) {
		case 1:
			color = Color.RED;
			break;
		case 2:
			color = Color.BLUE;
			break;
		default:
			break;
		}

		team = teamNumber;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color teamColor) {
		color = teamColor;
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int health) {
		HP = health;
	}

}