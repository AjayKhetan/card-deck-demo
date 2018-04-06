package com.javafxpert.carddeckdemo;

import com.javafxpert.carddeckdemo.model.Card;
import com.javafxpert.carddeckdemo.services.CardDeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@RestController
public class CardDeckController {
  private final CardDeckService cardDeckService;
  private final CardDeckDemoProperties cardDeckDemoProperties;
  private final Comparator<Card> comparator = (c1, c2) -> c1.getWorth() - c2.getWorth();

  @Autowired
  public CardDeckController(CardDeckService cardDeckService,
                            CardDeckDemoProperties cardDeckDemoProperties) {
    this.cardDeckService = cardDeckService;
    this.cardDeckDemoProperties = cardDeckDemoProperties;
  }

  @GetMapping("/carddeck")
  public Flux<Card> getCardDeck(@RequestParam(defaultValue = "false") boolean shuffled, @RequestParam(defaultValue = "10") int numcards) {

    Flux<Card> cardFlux = cardDeckService.getNewDeck()
            .take(numcards);
    return cardFlux;
  }

  @GetMapping("/carddeckbysuit")
  public Flux<Card> getCardDeckBySuit(@RequestParam(defaultValue = "SPADES") String suit, @RequestParam(defaultValue = "false") boolean shuffled, @RequestParam(defaultValue = "10") int numcards) {

    Flux<Card> cardFlux = cardDeckService.getNewDeck()
            .filter(card -> card.getSuit().equalsIgnoreCase(suit))
            .take(numcards);
    return cardFlux;
  }

  @GetMapping("/carddeckmerge")
  public Flux<Card> getCardDeckMerge() {
    Flux<Card> clubsFlux = getCardDeckBySuit("CLUBS", false, 13)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> heartsFlux = getCardDeckBySuit("HEARTS", false, 13)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> spadesFlux = getCardDeckBySuit("SPADES", false, 13)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> diamondsFlux = getCardDeckBySuit("DIAMONDS", false, 13)
            .delayElements(Duration.ofMillis(1));

    Flux<Card> cardFlux = Flux.merge(heartsFlux, clubsFlux, spadesFlux, diamondsFlux)
            .take(12);

    return cardFlux;
  }

  @GetMapping("/carddeckmergewith")
  public Flux<Card> getCardDeckMergeWith() {
    Flux<Card> clubsFlux = getCardDeckBySuit("CLUBS", false, 6)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> heartsFlux = getCardDeckBySuit("HEARTS", false, 6)
            .delayElements(Duration.ofMillis(1));

    Flux<Card> cardFlux = heartsFlux.mergeWith(clubsFlux).take(12);

    return cardFlux;
  }
  @GetMapping("/carddeckmergeordered")
  public Flux<Card> getCardDeckMergeOrdered() {
    Flux<Card> clubsFlux = getCardDeckBySuit("CLUBS", true, 3)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> heartsFlux = getCardDeckBySuit("HEARTS", true, 3)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> spadesFlux = getCardDeckBySuit("SPADES", true, 3)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> diamondsFlux = getCardDeckBySuit("DIAMONDS", true, 3)
            .delayElements(Duration.ofMillis(1));

    Flux<Card> cardFlux = Flux.mergeOrdered(comparator, heartsFlux, clubsFlux, spadesFlux, diamondsFlux).take(12);

    return cardFlux;
  }

  @GetMapping("/carddeckmergesort")
  public Flux<Card> getCardDeckMergeSort() {
    Flux<Card> clubsFlux = getCardDeckBySuit("CLUBS", true, 3)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> heartsFlux = getCardDeckBySuit("HEARTS", true, 3)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> spadesFlux = getCardDeckBySuit("SPADES", true, 3)
            .delayElements(Duration.ofMillis(1));
    Flux<Card> diamondsFlux = getCardDeckBySuit("DIAMONDS", true, 3)
            .delayElements(Duration.ofMillis(1));

    Flux<Card> cardFlux = Flux.merge(heartsFlux, clubsFlux, spadesFlux, diamondsFlux)
            .sort(comparator)
            .take(12);

    return cardFlux;
  }

  @GetMapping("/carddecktakelast")
  public Flux<Card> getCardDeckTakeLast() {
    Flux<Card> clubsFlux = getCardDeckBySuit("CLUBS", false, 13).sort(comparator).takeLast(3);
    Flux<Card> heartsFlux = getCardDeckBySuit("HEARTS", false, 13).sort(comparator).takeLast(3);
    Flux<Card> spadesFlux = getCardDeckBySuit("SPADES", false, 13).sort(comparator).takeLast(3);
    Flux<Card> diamondsFlux = getCardDeckBySuit("DIAMONDS", false, 13).sort(comparator).takeLast(3);

    Flux<Card> cardFlux = Flux.merge(heartsFlux, clubsFlux, spadesFlux, diamondsFlux)
            .take(12);

    return cardFlux;
  }

  @GetMapping("/carddeckriffleshuffle")
  public Flux<Card> getCardDeckRiffleShuffle() {
//    Flux<Card> clubsFlux = getCardDeckBySuit("CLUBS", false, 13);
//    Flux<Card> heartsFlux = getCardDeckBySuit("HEARTS", false, 13);
//    Flux<Card> spadesFlux = getCardDeckBySuit("SPADES", false, 13);
//    Flux<Card> diamondsFlux = getCardDeckBySuit("DIAMONDS", false, 13);
//
    Flux<Card> cardFlux = cardDeckService.getNewDeck().take(26);
            //.doOnEach(System.out::println);

//    Flux<Card> cutCards1 = cardFlux.takeLast(6);
//    Flux<Card> cutCards2 = cardFlux.take(6);
//    Flux<Tuple2<Card, Card>> tuples = Flux.zip(cutCards1, cutCards2);
//
//    Flux<Card> flatMapped = tuples.flatMap(tuple2 -> Flux.just(tuple2.getT1(), tuple2.getT2()));

    return riffleShuffle(cardFlux)
            .take(26);//.sort(comparator);
  }

  @GetMapping("/carddeckcut")
  public Flux<Card> getCardDeckCut() {
    int numCards = 26;
    return cardDeckService.cutCards(cardDeckService.getNewDeck().take(numCards));
//    int numCards = 26;
//    Flux<Card> cardFlux = cardDeckService.getNewDeck().take(numCards);
//    int cardsToCut = (int)(Math.random() * numCards);
//    Flux<Card> cutCards = Flux.concat(cardFlux.takeLast(numCards - cardsToCut), cardFlux.take(cardsToCut));
//    return cutCards;
  }

  @GetMapping("/carddeckoverhandshuffle")
  public Flux<Card> getCardDeckOverhandShuffle() {
//    Flux<Card> clubsFlux = getCardDeckBySuit("CLUBS", false, 13);
//    Flux<Card> heartsFlux = getCardDeckBySuit("HEARTS", false, 13);
//    Flux<Card> spadesFlux = getCardDeckBySuit("SPADES", false, 13);
//    Flux<Card> diamondsFlux = getCardDeckBySuit("DIAMONDS", false, 13);
//
    Flux<Card> cardFlux = cardDeckService.getNewDeck().take(26);
    //.doOnEach(System.out::println);

//    Flux<Card> cutCards1 = cardFlux.takeLast(6);
//    Flux<Card> cutCards2 = cardFlux.take(6);
//    Flux<Tuple2<Card, Card>> tuples = Flux.zip(cutCards1, cutCards2);
//
//    Flux<Card> flatMapped = tuples.flatMap(tuple2 -> Flux.just(tuple2.getT1(), tuple2.getT2()));

    return overhandShuffle(cardFlux);

//    return riffleShuffle(cardFlux, 1)
//            .take(26).sort(comparator);
  }

  @GetMapping("/carddeckshufflewell")
  public Flux<Card> getCardDeckShuffleWell() {
    Flux<Card> cardFlux = cardDeckService.getNewDeck().take(26);


    //return riffleShuffle(overhandShuffle(riffleShuffle(overhandShuffle(cardFlux)))).take(26);
    //return overhandShuffle(cardFlux).take(26);
    return riffleShuffle(cardFlux).take(26);
  }

  public Flux<Card> overhandShuffle(Flux<Card> cardFlux) {
    int totalCards = 26;
    int maxChunk = 5;
    int numCardsLeft = totalCards;
    Flux<Card> overhandShuffledCardFlux = Flux.empty();
    //int numCardsToTransfer = 0;
    while (numCardsLeft > 0) {
      //Flux<Card> tempCardFlux = cardFlux.take(totalCards - numCardsToTransfer);
      Flux<Card> tempCardFlux = cardFlux.take(numCardsLeft);
      int numCardsToTransfer = Math.min((int)(Math.random() * maxChunk + 1), numCardsLeft);
      //overhandShuffledCardFlux.concatWith(tempCardFlux.takeLast(numCardsToTransfer));
      overhandShuffledCardFlux = Flux.concat(overhandShuffledCardFlux, tempCardFlux.takeLast(numCardsToTransfer));

      numCardsLeft -= numCardsToTransfer;
    }
    return overhandShuffledCardFlux;
  }

  public Flux<Card> riffleShuffle(Flux<Card> cardFlux, int numTimes) {
    Flux<Card> riffleShuffledCardFlux = cardFlux;
    for (int i = 0; i < numTimes; i++) {
      riffleShuffledCardFlux = riffleShuffle(riffleShuffledCardFlux);
    }
    return riffleShuffledCardFlux;
  }

  public Flux<Card> riffleShuffle(Flux<Card> cardFlux) {
    int numCards = 26;

    //int cardsToCut = (int)(Math.random() * numCards);
    //int cardsToCut = numCards / 2;

    //Flux<Card> cutCards = cardFlux.takeLast(numCards - cardsToCut).concatWith(cardFlux.take(cardsToCut));
    //Flux<Card> cutCards = Flux.concat(cardFlux.takeLast(numCards - cardsToCut), cardFlux.take(cardsToCut));
    Flux<Card> cutCardsA = cardFlux.take(numCards / 2);
    Flux<Card> cutCardsB = cardFlux.takeLast(numCards / 2);
    Flux<Tuple2<Card, Card>> tuples = Flux.zip(cutCardsA, cutCardsB);

    return tuples.flatMap(tuple2 -> Flux.just(tuple2.getT1(), tuple2.getT2()));
  }


  /*
  @GetMapping("/carddeckwebclient")
  public Flux<Card> getCardDeck(boolean shuffled, int numcards) {
    String cardDeckServiceUri = cardDeckDemoProperties.getCardimageshost() + ":" + cardDeckDemoProperties.getCardimagesport() + "/carddeck";
    WebClient cardDeckWebClient = WebClient.create(cardDeckServiceUri);
    Flux<Card> cardFlux = cardDeckWebClient.get()
            .uri(builder -> builder.queryParam("shuffled", shuffled).queryParam("request", numcards).build())
            .
    return cardDeckService.getNewDeck(shuffled);


    CardDeckSubscriber<Card> cardDeckSubscriber = new CardDeckSubscriber<>();
    cardFlux.subscribe(cardDeckSubscriber);
    System.out.println("Requesting 3 more");
    cardDeckSubscriber.request(3);

  }
  */

}
