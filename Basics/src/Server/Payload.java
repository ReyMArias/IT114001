package Server;

import java.awt.Point;
import java.io.Serializable;

public class Payload implements Serializable {

	/**
	 * baeldung.com/java-serial-version-uid
	 */

	public class PlayerInfo implements Serializable {

		private static final long serialVersionUID = -6687715510484845706L;
		private int teamId = 0;
		private int playerId;

		public void setTeamId(int id) {
			teamId = id;
		}

		public void setPlayerId(int id) {
			playerId = id;
		}

		public int getTeamId() {
			return teamId;
		}

		public int getPlayerId() {
			return playerId;
		}
	}

	private PlayerInfo playerInfo = new PlayerInfo();

	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(int teamID, int playerID) {
		playerInfo.teamId = teamID;
		playerInfo.playerId = playerID;
	}

	private static final long serialVersionUID = -6687715510484845706L;

	private String clientName;// ~2 bytes per character

	public void setClientName(String s) {
		this.clientName = s;
	}

	public String getClientName() {
		return clientName;
	}

	private String message;// ~2 bytes per character

	public void setMessage(String s) {
		this.message = s;
	}

	public String getMessage() {
		return this.message;
	}

	private PayloadType payloadType;// 4 bytes

	public void setPayloadType(PayloadType pt) {
		this.payloadType = pt;
	}

	public PayloadType getPayloadType() {
		return this.payloadType;
	}

	private int number;// 4 bytes

	public void setNumber(int n) {
		this.number = n;
	}

	public int getNumber() {
		return this.number;
	}

	int x = 0;// 4 bytes
	int y = 0;// 4 bytes

	public void setPoint(Point p) {
		x = p.x;
		y = p.y;
	}

	public Point getPoint() {
		return new Point(x, y);
	}

	// added so two sets of x,y could be sent
	int x2 = 0;// 4 bytes
	int y2 = 0;// 4 bytes

	public void setPoint2(Point p) {
		x2 = p.x;
		y2 = p.y;
	}

	boolean flag = false;// 1 bit

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean getFlag() {
		return flag;
	}

	public Point getPoint2() {
		return new Point(x2, y2);
	}

	@Override
	public String toString() {
		return String.format("Type[%s], Number[%s], Message[%s]", getPayloadType().toString(), getNumber(),
				getMessage());
	}

	private GameState gameState;

	public void setState(GameState state) {
		gameState = state;
	}

	public GameState getState() {
		return gameState;
	}

	private long time;

	public void setTime(long t) {
		time = t;
	}

	public long getTime() {
		return time;
	}
}