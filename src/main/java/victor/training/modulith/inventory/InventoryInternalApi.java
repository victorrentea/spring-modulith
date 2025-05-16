package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.model.StockReservation;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.inventory.service.StockService;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryInternalApi {
  private final StockService stockService;
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  public void reserveStock(long orderId, List<LineItem> items) {
    stockService.reserveStock(orderId, items);
  }

  public void confirmReservation(long orderId) {
    stockService.confirmReservation(orderId);
  }

  public StockKnob getStockDetails(long productId) {
    int reserved = stockReservationRepo.findAllByProductId(productId).stream()
        .mapToInt(StockReservation::items)
        .sum();
    int available = stockRepo.findById(productId).map(Stock::items).orElse(0);
    return new StockKnob(available, reserved);
  }

  //a) it's my module -> change it
  //module managed by another team->
  //b) raise a ticket and block your dev until those son on the beaches do it - waste
  //c) open-source model: I change their code and submit them a dedicated PR that they have to 4-eyes review.⭐️!important
      // via CODEOWNERS file
  public int getStock(long productId) {
    return stockRepo.findById(productId).map(Stock::items).orElse(0);
  }
//  public ❌(domain) Stock getStock(long productId) {
//  public StockInternalDto getStock(long productId) {
//  public StockKnob getStock(long productId) {
//  public .. getStock(StockSearchCriteriaKnob productId) {
  // knob(internal dto) are used by all clients of inventory.
  //    => might lead to every client module wanting more stuff in your Knob
  //    PERFECT!
  //    It means inventory useful.
  //    together for a meeting, we're gonna have a wonderful discussion in which you ask and I refuse .
  //   Let's see how we fulfill your need, my dear customer.
  //   Healthies way to evolve APIs.
}


//A) REST❄️ API DTO vs DOMAIN OBJECT vs Mongo or JPA @ENTITY ~ DB TABLE
// let there be one more layer of mappers ,because we are 🤢 or 😰 of JPA
// "I would not like to have deps ..."

//B) REST❄️ API DTO vs DOMAIN OBJECT === Mongo or JPA @ENTITY ~ DB TABLE

//both A+B modules will expose to other modules a flavor DTOs❄️
// DON'T expose internal DOMAIN MODEL to other modules (TEAMS🩸),
// the same way you don't leak it through your REST API
// to allow yourself to evolve your internal structure.
// it's about control - MATRIX => team autonomy => speed in dev.