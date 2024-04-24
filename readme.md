# Modular Monolith Coding Kata
using Spring Modulith https://spring.io/projects/spring-modulith/

## Domain Overview

### Catalog

- Search product (REST)
- Get product details (REST)

### Order Management

- Place order (REST): returns a redirect url to a payment page
- calls shipping provider with customer.address to get a shipping number

### Payment

- Calls Payment Gateway to get payment redirect URL
- Is called back by Payment Gateway to report result

### Shipping

### Inventory

- Add stock (REST)
- API get stock bulk [pid,pid...]
- API reserve stock (orderid, items:[{},{},{}])
- publish OutOfStockEvent/BackInStockEvent
- listen OrderConfirmedEvent(orderid) to effect the stock reservation

### Customer

- API get customer address (needed at shipping)

## Spring Modulith Intro
A module can only reference by default the top-level package of other modules. Any class in a subpackage (eg. `impl`) is considered implementation detail and it's unaccessible from outside that module.

Modules cannot form cycles.

A diagram overviewing the module interactions is generated at each test run. See `index.adoc`

## Exercises
Taking small baby-steps, try to implement the following changes in code:
1Return the number of items in stock in product page (see `GetProductApi`)
    - Respect the encapsulation of `inventory` module (`ArchitectureTest` should pass)
   - <details><summary>Hint</summary>Retrieve the stock item number via a call to a new method in `InventoryModule`</details>
2. Pull payment logic out of `order` module into a separate `payment` module.
    - Check there are no illegal internal calls or cycles with `ArchitectureTest`
    - <details><summary>Hint</summary>Have an event thrown from payment back into order</details>
    - <details><summary>Hint2</summary>That event can be named PaymentCompletedEvent and should be placed in the payment module</details>
    - Encapsulate the new 'payment' module: hide its internals exposing the least amount of public stuff
    - <details><summary>Hint</summary>Use an 'impl' package for simplicity</details>
3. Search should only return products in stock (see `SearchProductApi`)
    - What options can you identify. Tradeoffs of each?
    1. <details><summary>Option</summary>Find all products and join in-memory with all stock. Or vice-versa.</details>
    1. <details><summary>Option</summary>Force a JOIN via SQL/JPQL</details>
    1. <details><summary>Option</summary>Replicate stock item number at every change via events from `inventory`</details>
    1. <details><summary>Option</summary>Publish `OutOfStockEvent` and `BackInStockEvent` from `inventory`, keep a `Product.inStock` boolean; </details>
    1. <details><summary>Option</summary>A separate ElasticSearch engine to which you publish product{id,name} and stock{items} (just imagine)</details>
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

