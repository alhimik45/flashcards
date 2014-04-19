package com.alex.flashcard;

import static spark.Spark.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import spark.*;

public class App {
	final static Random rand = new Random();

	public static void main(String[] args) {
		final CRUD DB = new CRUD();
		final String noResponse="";
		staticFileLocation("public");
		get(new Route("/") {

			@Override
			public Object handle(Request request, Response response) {

                response.redirect("/index.html");
				return "<script src=\"http://code.jquery.com/jquery-latest.min.js\" type=\"text/javascript\"></script>";
			}

		});

		post(new Route("/ajax/get", "application/json") {
			@Override
			public Object handle(Request request, Response response) {
				try {
					final String tags = request.queryParams("tags");
					final List<Card> cards;
					final Set<String> tagSet = tags != null ? splitTags(tags)
							: null;
					final String filterType = request.queryParams("FilterType");
					if (filterType != null) {
						switch (filterType) {
						case "and":
							cards = DB.ReadByTagsByAnd(tagSet);
							break;
						case "or":
							cards = DB.ReadByTagsByOr(tagSet);
							break;
						default:
							cards = DB.ReadAll();
						}
					} else {
						cards = DB.ReadAll();
					}
					if (cards.size() > 0) {

                        return "{\"card\":" + getOneCard(cards) + "}";
					} else {
						return noResponse;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return noResponse;
			}
		});

		post(new Route("/ajax/add", "application/json") {
			@Override
			public Object handle(Request request, Response response) {

				Card card = new Card();
				card.setAsk(request.queryParams("ask"));
				card.setAnswer(request.queryParams("answer"));
				card.setTags(splitTags(request.queryParams("tags")));
				DB.Create(card);
				return noResponse;
			}
		});

		post(new Route("/ajax/update", "application/json") {
			@Override
			public Object handle(Request request, Response response) {
				try {
					Card card = new Card();
					card.setAsk(request.queryParams("ask"));
					card.setAnswer(request.queryParams("answer"));
					card.setTags(splitTags(request.queryParams("tags")));
					card.setId(Long.parseLong(request.queryParams("id")));
					DB.Update(card);
				} catch (Exception e) {

					e.printStackTrace();
				}
				return noResponse;
			}
		});
		post(new Route("/ajax/delete", "application/json") {
			@Override
			public Object handle(Request request, Response response) {
				try {
					DB.Delete(Long.parseLong(request.queryParams("id")));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return noResponse;
			}
		});
		post(new Route("/ajax/remember") {
			@Override
			public Object handle(Request request, Response response) {
				try {
					DB.changeComplete(
							Long.parseLong(request.queryParams("id")), true);
				} catch (Exception e) {
					e.printStackTrace();

				}
				return noResponse;
			}
		});
		post(new Route("/ajax/notremember") {
			@Override
			public Object handle(Request request, Response response) {
				try {
					DB.changeComplete(
							Long.parseLong(request.queryParams("id")), false);
				} catch (Exception e) {
					e.printStackTrace();

				}
				return noResponse;
			}
		});

	}

	private static Set<String> splitTags(final String tags) {
		if (tags!=null) {
			return new HashSet<String>(Arrays.asList(tags.replaceAll("\\s", "").split(",")));
		}
		return new HashSet<String>();
	}

	private static Card getOneCard(final List<Card> cards) {
		int total = 1;
		for (Card card : cards) {
			total += card.getComplete();
		}
		int rnd = rand.nextInt(total);
		for (Card card : cards) {
			total -= card.getComplete();
			if (total < rnd) {
				return card;

			}
		}
		return cards.get(0);
	}

}