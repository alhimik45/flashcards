package com.alex.flashcard;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.alex.flashcard.Card;

@StaticMetamodel(Card.class)
public class Card_ {
	public static volatile SingularAttribute<Card,Long> id;
	public static volatile SingularAttribute<Card,String> ask;
	public static volatile SingularAttribute<Card,String> answer;
	public static volatile SingularAttribute<Card,Byte> complete;
	public static volatile SetAttribute<Card,String> tags;
}
