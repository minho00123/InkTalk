package com.inkTalk.domain;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class User implements Serializable {
	private int id;
	private String nickname;
	private Color color = generateRandomColor();

	public Color generateRandomColor() {
		Random random = new Random();

		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);

		return new Color(red, green, blue);
	}

	public User(int id, String nickname) {
		this.id = id;
		this.nickname = nickname;
	}

	public int getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		User user = (User) obj;
		return Objects.equals(nickname, user.nickname); 
	}

	@Override
	public int hashCode() {
		return Objects.hash(nickname);
	}

}
