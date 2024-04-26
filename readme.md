# Modular Monolith Exercise

using Spring Modulith https://spring.io/projects/spring-modulith/

## API Overview

- Catalog: Search product
- Catalog: Get product details
- Order: Place order, returning a redirect url to a payment page
- Inventory: Add stock
- Inventory: Get stock bulk
- Inventory: Reserve stock (confirmed via OrderConfirmedEvent)
- Customer: Get customer address

## Spring Modulith Intro
A module can only reference by default the classes in the top-level package of other modules. 
A class in any subpackage (eg. `impl`) is considered implementation detail,
and it should NOT be accessed from outside that module.

Modules cannot form dependency cycles.

A diagram overviewing the module interactions is auto-generated at each test run. See `index.adoc`

## Exercises

Taking small baby-steps, implement the changes below. 
Expand the hints if needed, and keep running `ArchitectureTest`:
1. Return the number of items currently in stock from `GetProductApi`
- `catalog` should not access any internal class of `inventory` module (run tests).
- `GetProductReturnsStockE2ETest` should pass.
- <details><summary>Hint</summary>Retrieve the stock item number via a call to a new method in `InventoryModule`</details>

2. Pull payment-related classes out of `order` module into a separate `payment` module.
    - Check there are no illegal internal calls or cycles with `ArchitectureTest`
    - <details><summary>Hint to fix 'non-exposed..':</summary>Code having to do with the `order` internals should stay in `order`.</details>
    - <details><summary>Hint to fix cycle:Solution#1</summary>Have a `PaymentCompletedEvent` thrown from payment back into order</details>
    - <details><summary>Hint to fix cycle:Solution#2</summary>Introduce an interface in one of the modules implemented in the other (aka Dependency Inversion). Which module should hold the interface?</details>
    - Encapsulate the new `payment` module: hide as many classes exposing the least amount of public stuff
    - <details><summary>Hint</summary>Move classes in a subpackage, like 'impl'</details>
3. `SearchProductApi` should only return products in stock
    - What options you see? Tradeoffs of each?

    1. <details><summary>Option</summary>Find all products and join in-memory with all stock. Or vice-versa.</details>
    1. <details><summary>Option</summary>JOIN Stock via SQL/JPQL😐</details>
    1. <details><summary>Option</summary>Replicate stock item number at every change via events from `inventory`</details>
    1. <details><summary>Option</summary>Publish `OutOfStockEvent` and `BackInStockEvent` from `inventory`, updating a `Product.inStock` boolean; </details>
    1. <details><summary>Option</summary>Join the Product with the StockView @Entity exposed by `inventory`</details>
4. Notifications
    - When payment is confirmed
    - <details>
       <summary>Hint - code snippet</summary>

      ``` @ApplicationModuleListener
       public void onOrderStatusChanged(OrderStatusChangedEvent event) {
         String customerEmail = customerModule.getCustomer(event.customerId()).email();
         if (event.status() == OrderStatus.PAYMENT_APPROVED) {
           sendPaymentConfirmedEmail(event, customerEmail);
         }
         if (event.status() == OrderStatus.SHIPPING_IN_PROGRESS) {
           sendOrderShippedEmail(event, customerEmail);
         }
       }
      ```
   </details>
5. Move Reviews-related stuff out of `catalog` module
    - E2E Test should still pass

