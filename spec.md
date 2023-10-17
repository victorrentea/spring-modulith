# Ecommerce 
## Catalog
- Search products in stock (REST)
- Get product details (REST+API) 
- Listen OutOfStockEvent, BackInStockEvent
- Impl:
  - Product{name,boolean inStock}

## Order Management
- Place Order (REST)
  - Calls payment gateway to get redirect url to send user browser to pay
  - Is called back by PaymentGateway with the payment status 
  - Calls shipping provider with customer.address: get a tracking number to save on order
  - Is called back by ShippingProvider with the shipping status (SHIPPED OK|FAILED)
- Impl:
  - Publish OrderStatusChangedEvent
    Order{id,status,items:[{productId, number}], shippingNumber, customerId}

## Inventory (aka stock)
- Add Stock REST
- Get Stock API bulk [productId,...]
- Reserve Stock until order is paid (orderid, items:[{},{},{}])
- Confirm Stock when order is paid (via OrderConfirmedEvent)
- Impl:
  - Publish OutOfStockEvent/BackInStockEvent

## Payment
    statless
- API get payment redirect URL + orderId
- REST payment accepted + orderId
- publish PaymentAcceptedEvent

## Customer
    Customer{id,fullName, address}
- CRUD

Q=Rabbit