package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModuleInterface;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;
  private final InventoryModuleInterface inventoryModule;

  public record ProductSearchResult(long id, String name) {
  }

  // I have to search for products by name, descr, attribu...
  // CR: you should not return products out of stock
  // (stock is NOT in the catalog module)

  // or: "search/sort documents by instrument description"
  // (intrument descr is NOT in the documentation module)

//  private final victor.training.modulith.inventory.impl.StockRepo doesntCompile;
  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam String name,
      @RequestParam PageRequest pageRequest) {
    // ❌ Map<Long, Integer> allStock = inventoryModule.findAllStocks();// OUT OF MEMORY+ 5 sec QUERY RUN TIME
    // ❌ StockRepo
    // 🤔 Map<Long, String> instrumentDescription = load all from THEM
    //     instrumentModule.findAllDescriptions(allInstrumentIdsForWhichIHaveDocuments);
    //    ok if map < 1 MB, for filtering, not for SORTING
    // ORDER BY  WHEN DOC.INSTR_ID CASE 1 THEN 2 CASE 2 THEN 3 ELSE 0... 💩 DONT

    // you need to display documents filtered/sorted by
    // doc attributes + instrument description
    // 🤔 List<Document> all = docRepo.findAll();
    // + List<InstrumentDto> allInstrument = inventoryModule.findAll();
    // stream.sorted().skip(20).limit(20)
    // OR: pushed all data to Frontend and JOIN filter/sort/paginate 😈

    // What if document has username -> User.fullName

    // what if the datasets are too big? Sample-Patient > 10.000--------

    // you have to WHERE > ORDER BY > LIMIT+OFFSET in SQL
    // a) JOIN THEIR DATA with my table CATALOG.PRODUCT JOIN INVENTORY.STOCK
    // BAD: they can't anymore freely ALTER TABLE
    // BAD: my tests will have to populate THEIR data. => I must understand THEIR DATA (won't fix)
    // BAD: misinterpret their data 😈
    // SELECT *
    // FROM PRODUCT JOIN STOCK
    // WHERE STOCK.ITEMS - (SELECT SUM(..) FROM STOCK_RESERVATION..)>0
    // also: in the ORDER.IS_CONFIRMED=0,1,2,3
    // Partial Solution: JOIN THEIR VIEW (created with love❤️ for you)
    // CREATE OR REPLACE VIEW PUB_STOCK AS SELECT STOCK.ITEMS - (SELECT SUM(..) FROM STOCK_RESERVATION..)>0 value from ....
    // avoid if you plan to microservice <1y

    // b) Replicate in CATALOG.Product.stock data from inventory. how?
    // 1) On change in inventory, fire an event @Observed by Catalog
    // GOOD: inventory decoupled of observers
    // BAD: difficult to follow flow
    // BAD: publisher has to WAIT for all observers
    // BAD: observers run in arbitrary order, one exception stops the next observers
    // ± synchronous => perfect in sync (transactional)
    // Use-Case when publisher doesn't want their observer to be in THEIR transaction
    //    not-critical side-effect (email patient) caused by a critical flow (store results)
    //     ==> @Asynchronous
    // the observers were made asynchronous,
    // and because they were so fast they got back to the publisher
    // module before that got to commit

    // 2) inventory PUSH data: calls catalog api to 'tell them the new stock'
    // GOOD: always in sync (ACID transacted)
    // BAD: inventory must know of you (and 3 others like you)

    // 3) catalog PULL data from inventory every 1-5-30 seconds or every time I need it (too often)
    //     update product.stock based on inventoryApi.getAllStock() or inventoryApi.getAllStockUpdatedSince(lastRunTime)
    // BAD: useless calls; waste of DB effort

    // 4) ❌INSERT INTO CATALOG.?? FROM (SELECT FROM INVENTORY.STOCK_VIEW) - back to a, no benefit
    // 5) ❌TRIGGER BEFORE COMMIT DONT!



    // (inconsistencies?!?!?!? OMG WTF)




    return productRepo.search(name, pageRequest)
        .stream()
//       ❌ .filter(p-> inventoryModule.getStockByProduct(p.getId())) // N+1  queries problem => performance disaster
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
    // IMAGINE  SELECT * FROM CATALOG.PRODUCT
    // JOIN INVENTORY.STOCK_OUT_VIEW {3 columns} carefully
    // designed with love💖 by the INVETORY TEAM

    // Create or replace view INVENTORY.STOCK_OUT_VIEW as
    // SELECT PRODUCT_ID, ITEMS
    // FROM INVENTORY.STOCK
  }


}
