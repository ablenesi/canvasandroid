package edu.ubbcluj.canvasAndroid.model;

import java.util.ArrayList;
import java.util.List;

public class Conversation {

	private int id;
	private List<Person> participants;
	String date;
	String clock;

	public String getClock() {
		return clock;
	}

	public void setClock(String clock) {
		this.clock = clock;
	}

	String lastMessage;

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String last_message) {
		this.lastMessage = last_message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Conversation() {
		participants = new ArrayList<Person>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Person> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Person> participants) {
		this.participants = participants;
	}

	public void addParticipant(int id, String name) {
		participants.add(new Person(id, name));
	}

}
