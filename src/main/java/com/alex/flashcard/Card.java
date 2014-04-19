package com.alex.flashcard;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "Cards")
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String ask = "";
	@Column(nullable = false)
	private String answer = "";

	private Byte complete = 100;

	@ElementCollection
	@CollectionTable(name = "Tags", joinColumns = @JoinColumn(name = "Card_Id"))
	private Set<String> tags;

	public Card() {

	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	// public Card(String ask, String answer) {
	// this.ask = ask;
	// this.answer = answer;
	// }

	@Override
	public String toString() {
		String tagsStr = tags.toString();
		return "{\"ask\":\"" + ask.replace("\"", "\\\"").replace("\n", "\\n")
				+ "\", \"answer\":\""
				+ answer.replace("\"", "\\\"").replace("\n", "\\n")
				+ "\", \"complete\":" + complete + ", \"tags\":\""
				+ tagsStr.substring(1, tagsStr.length() - 1) + "\",\"id\":"
				+ id + "}";
	}

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public byte getComplete() {
		return complete;
	}

	public void setComplete(byte complete) {
		this.complete = complete;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void doComplete(boolean ok) {
		byte complete = this.complete;
		if (ok) {
			complete -= 5;
			if (complete < 1) {
				complete = 1;
			}
		} else {
			complete += 10;
			if (complete > 100) {
				complete = 100;
			}
		}
		this.complete = complete;

	}

}
