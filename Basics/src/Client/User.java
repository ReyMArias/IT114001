package Client;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class User extends JPanel {
	private String name;
	private JTextField nameField;
	private String teamText;

	public User(String name) {
		this.name = name;
		nameField = new JTextField(name);
		nameField.setEditable(false);
		this.setLayout(new BorderLayout());
		this.add(nameField);
	}

	public String getName() {
		return name;
	}

	public void setUserColor(int teamId) {
		if (teamId == 1) {
			teamText = "red";
		} else {
			teamText = "blue";
		}

		nameField.setText("\u001B31;1m" + name);
		repaint();
	}
}