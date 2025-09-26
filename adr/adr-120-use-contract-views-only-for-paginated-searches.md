# ADR-120: Use contract views only for paginated searches

Status: Accepted
Date: 2025-09-26

## Context
We have cross-module list/search use-cases (e.g., Catalog wants to surface products that are in stock, where stock belongs to the Inventory module). A naive implementation either:
- Enriches each row by calling another module for each item (N+1 problem) and then filters, which destroys page size guarantees and is slow, or
- Joins directly on another module’s internal tables, which couples persistence models across module boundaries, breaking encapsulation and slowing down independent evolution.

For non-paginated, single-entity queries (e.g., get product by id + stock), orchestrating calls through exposed APIs is recommended. But for paginated searches with filtering/sorting across module-owned attributes, we must preserve:
- Correct pagination (stable page size and ordering)
- Performance (single query per page)
- Module encapsulation (no cross-module table joins)

## Decision
Introduce read-optimized contract views (owned and published by the provider module) only for the paginated search/list use-cases that require attributes from multiple modules. Consumer modules query these contract views directly for search pages.

- The provider module exposes a view or projection that contains only the fields agreed in the contract (e.g., ProductSearchView with product id, name, searchable description, and a denormalized "inStock" or stock count coming from Inventory).
- The view is maintained by the provider via its own mechanisms (events, async updaters, or transactional outbox → projector). Consumers do not join provider internals; they select from the provider’s published view.
- Keep these views narrowly scoped to the paginated search needs (filter/sort fields). Do not expand them to become a generic read model for all use-cases.
- For detailed pages or one-off reads, use API composition/orchestration instead of contract views.

## Rationale
- Encapsulation: Avoids hard-coupling to another module’s private tables.
- UX integrity: Filters are applied before pagination, preserving page size and sort order.
- Performance: One query per page, no N+1 enrichments.
- Evolvability: Each module can evolve its internals; only the contract view needs stability.

## Consequences
- Eventual consistency: The search view may be slightly stale relative to the source of truth. Define acceptable staleness SLOs.
- Extra storage and projector complexity: Need a simple projector/updater to keep the view in sync.
- Clear ownership: The provider owns, documents, versions, and migrates the view.
- Limited scope: Restrict to paginated searches. Do not use for detailed retrieval where eventual consistency is not acceptable.

## Alternatives considered
1. N+1 enrichment via internal APIs, then filter locally.
   - Rejected: performance and pagination distortion.
2. Cross-module SQL joins on private tables.
   - Rejected: breaks encapsulation and slows evolution; risky migrations.
3. Client-side composition (frontend calls multiple endpoints).
   - Sometimes acceptable, but still suffers from pagination/filtering across domains and higher latency.
4. Orchestrate in a composite service layer for detail endpoints only.
   - Accepted for non-paginated, single-entity reads.
5. Dedicated search infrastructure (e.g., Elasticsearch) indexing multiple modules.
   - Valid at larger scale; heavier operational cost. Contract view can be a step toward that.

## When to use
- You need to list/paginate/search on fields spanning multiple modules.
- You must preserve page size and ordering accurately.
- You want to avoid cross-module joins and N+1 calls.

## When not to use
- Single-entity reads; prefer API composition/orchestration.
- Strong consistency requirements across modules for the same response.
- Analytics/reporting where a proper data warehouse is a better fit.

## Implementation sketch
- Provider module publishes a view: e.g., catalog.product_search_view(id, name, description, in_stock BOOLEAN, …).
- Projector subscribes to domain/integration events (e.g., StockChanged) and updates the view.
- Consumer module issues a single paginated query against the view, applying filters and sorts there.

Example (conceptual):
- GET /catalog/search?name=phone&inStock=true&page=0&size=20&sort=name,asc
- SELECT id, name FROM catalog.product_search_view WHERE name ILIKE '%phone%' AND in_stock = true ORDER BY name ASC LIMIT 20 OFFSET 0

## Migration notes
- Start with the minimal set of fields needed for the search screen.
- Backfill the view once; then keep it in sync via projector.
- Define retention and versioning strategy for the view contract; communicate changes.

## References
- Blue-green read models / projections in modular monoliths
- Outbox + projector patterns for eventual consistency
- Avoiding cross-module joins for evolvable modularity