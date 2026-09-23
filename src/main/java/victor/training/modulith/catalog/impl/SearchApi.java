package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchApi {
    private final ProductRepo productRepo;
    private final InventoryInternalApi inventoryInternalApi;

    public record ProductSearchCriteria(String name, String description) {
    }

    public record ProductSearchResult(long id, String name) {
    }

    @GetMapping("catalog/search")
    public List<ProductSearchResult> search(
            @RequestParam ProductSearchCriteria criteria,
            @RequestParam(required = false) PageRequest pageRequest) {

        // List<long> productIdsInStock = [10k]❌
        // search paginat se face in DB: WHERE apoi ORDER apoi LIMIT/OFFSET
        // A) ✅ SELECT TU CU VIEWUL LOR (nu cu tabela ca sa-i dai vizibilitate doar pe 3 coloane +)
        // 👍 moduliths pe termen lung
        // FROM CATALOG.PRODUCT P
        // WHERE P.ID IN (SELECT ID FROM INVENTORY.STOCK_VIEW S WHERE S.ITEMS > 0)

        // B) ✅ RETII SI TU UN STOCK IN SCHEMA TA REPLICAT DE LA ĂILALȚI
        // sa nu mai treci gardu in schema lor
        // z) lantul triggerilor: aka oare cine a scris coloana aia?
        // a) job la 5 min sa dea GET la ei cu "ce mai e nou de acum 5 min"
        // b) ❌ei PUSH la mine cu PrOST
        // c) sa arunce event ei la care eu sa ascult




        // C) Elastic Search in care imping datele tu, ❤️ full text search

        // TODO only return items which are currently in stock
        return productRepo.search(criteria.name, criteria.description, pageRequest)
                .stream()
//                .filter(e -> inventoryInternalApi.getStock(e.id()) > 0)
                // ❌ @george ti-ai batut joc de pagina: ti-au cerut 20 i-ai intors pagina 1/7 cu 5 produse ramase dupa filter🤢💥
                .map(e -> new ProductSearchResult(e.id(), e.name()))
                .toList();
    }
}
