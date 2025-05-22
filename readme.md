# Modular Monolith Exercise

Uses Spring Modulith https://spring.io/projects/spring-modulith/

## Module Overview

- `catalog`: product details management & search
- `order`: order workflow
- `inventory`: stock and reservations
- `customer` data
- `shipping`

## Spring Modulith Intro
A module can only reference by default the classes in the top-level package of other modules. A class in any subpackage (eg. `impl`) is considered implementation detail, and it should NOT be accessed from outside that module. Extra packages can be exposed via @NamedInterface

Modules cannot form dependency cycles.

`index.adoc` contains a diagram of the module interactions generated at each test run.

## Exercises

Expand the hints if needed, and keep running `ArchSpringModulithTest`:
1. In `GetProductApi` return the stock in the response.
- `catalog` should not access any internal class of `inventory` module (run tests).
- Enable and Pass `GetProductE2ETest`.
- <details><summary>Hint</summary>Retrieve the stock item number via a call to a new method in `InventoryInternalApi`</details>

2. Extract payment-related classes out of `order` module into a new `payment` module.
- Fix the encapsulation violation and the dependency cycle introduced.
  - <details><summary>Hint to fix 'non-exposed..':</summary>Code having to do with the `order` internals should stay in `order`.</details>
  - <details><summary>Hint to fix cycle:Solution#1</summary>Have a `PaymentCompletedEvent` thrown from payment back into order</details>
  - <details><summary>Hint to fix cycle:Solution#2</summary>Introduce an interface in one of the modules implemented in the other (aka Dependency Inversion). Which module should hold the interface?</details>
- Encapsulate the new `payment` module: hide as many classes exposing as few classes to other modules
  - <details><summary>Hint</summary>Move classes in a subpackage, like 'impl'</details>
3. `SearchProductApi` should only return products in stock
    - Test `SearchProductsApiTest` should pass.

    1. <details><summary>Option</summary>Search for matching products and join in-memory with all stock. Or vice-versa.</details>
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
    - tests should pass

