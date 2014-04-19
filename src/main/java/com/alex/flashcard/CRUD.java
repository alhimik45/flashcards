package com.alex.flashcard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CRUD {
	private final String PERSISTENCE_UNIT_NAME = "Cards";
	private final EntityManagerFactory factory = Persistence
			.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	private final CriteriaBuilder cb = factory.getCriteriaBuilder();


	public CRUD() {

	}

	private EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

	private void closeEntityManager(EntityManager em) {
		em.close();
	}

	public void Create(Card c) {
		EntityManager em = getEntityManager();
		
		em.getTransaction().begin();
		em.persist(c);
		em.getTransaction().commit();
		closeEntityManager(em);
	}

	public void Update(Card c) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		em.merge(c);
		em.getTransaction().commit();
		closeEntityManager(em);
	}

	public void Delete(Long cardId) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		em.remove(em.find(Card.class, cardId));
		em.getTransaction().commit();
		closeEntityManager(em);
	}

	public List<Card> ReadById(Long id) {

		CriteriaQuery<Card> q = cb.createQuery(Card.class);
		Path<Long> CardId = q.from(Card.class).get(Card_.id);
		q.where(cb.equal(CardId, id));
		EntityManager em = getEntityManager();
		return em.createQuery(q).getResultList();
	}

	public List<Card> ReadByTagsByAnd(Set<String> tags) {

		CriteriaQuery<Card> q = cb.createQuery(Card.class);

		List<Predicate> Predicates = new ArrayList<>();

		Root<Card> from = q.from(Card.class);

		Expression<Set<String>> cardTags = from.get(Card_.tags);

		for (String tag : tags) {

			Expression<String> param = cb.literal(tag);
			Predicate predicate = cb.isMember(param, cardTags);
			Predicates.add(predicate);

		}

		q.where(cb.and(Predicates.toArray(new Predicate[0])));
		EntityManager em = getEntityManager();
		return em.createQuery(q).getResultList();
	}

	public List<Card> ReadByTagsByOr(Set<String> tags) {
		CriteriaQuery<Card> q = cb.createQuery(Card.class);
		List<Predicate> Predicates = new ArrayList<>();
		Root<Card> from = q.from(Card.class);
		Expression<Set<String>> cardTags = from.get(Card_.tags);

		for (String tag : tags) {
			Expression<String> param = cb.literal(tag);
			Predicate predicate = cb.isMember(param, cardTags);
			Predicates.add(predicate);
		}
		q.where(cb.or(Predicates.toArray(new Predicate[0])));
		EntityManager em = getEntityManager();
		return em.createQuery(q).getResultList();
	}

	public List<Card> ReadAll() {
		CriteriaQuery<Card> q = cb.createQuery(Card.class);
		q.from(Card.class);
		EntityManager em = getEntityManager();
		return em.createQuery(q).getResultList();
	}

	public void changeComplete(Long id, boolean ok) {
		EntityManager em = getEntityManager();
		Card c = em.find(Card.class, id);
		em.getTransaction().begin();
		c.doComplete(ok);
		em.getTransaction().commit();
		closeEntityManager(em);

	}

}
