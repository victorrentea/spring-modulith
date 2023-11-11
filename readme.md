# Modular Monolith example with Spring Modulith

## High-level Requirements

### Catalog
`Product{name,inStock}`
- REST search product
- REST get product details
- listen: OutOfStockEvent, BackInStockEvent

### Order Management
`Order{status,items:[{productId, number}], shippingNumber, customerId}`
- REST place order
- listen: PaymentAcceptedEvent, publish OrderConfirmedEvent
- calls shipping provider with customer.address to get a shipping number
- calls payment gateway to get a payment url

### Payment
Statless.
- calls Payment Gateway to get payment redirect URL
- REST payment accepted (called by Payment Gateway)
- publish PaymentAcceptedEvent

### Inventory 
Stateless.
- REST add stock
- API get stock bulk [pid,pid...]
- API reserve stock (orderid, items:[{},{},{}])
- publish OutOfStockEvent/BackInStockEvent
- listen OrderConfirmedEvent(orderid) to effect the stock reservation

### Customer
`Customer{fullName, address}`
- API get customer address (needed at shipping)
