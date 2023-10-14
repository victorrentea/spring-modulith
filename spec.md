
## Medic Schedule
- se pot seta orele de functionare ale unei clinici
- un customer isi poate seta disponibilitatea in avans pt o anumita clinica, in intervalul la care clinica functioneaza
- un customer poate seta cat timp maxim accepta sa stea fara programare (intre sloturi)
- ofera sloturi de ore pt pacient pe o anumita sapt
- poate cancela un slot -> ridica eventuri pt reschedule 
- NEXT: un customer poate seta reguli: eg luni intre 16-19

## Pricing
- HR isi seteaza un hourly fee pt customer si un patient fee
- clinica isi seteaza un rent fee / cabinet / ora
- pt un anumit slot intr-o clinica la un customer, se calculeaza un fee { patientPrice, clinicFee, medicFee } 

## Appointment
- pacientul poate vedea sloturile libere + price dintr-o luna, filtrand dupa clinica, customer, sau specialitatea lui
- pacientul poate crea o programare
- sistemul ii scrie pacientului sa confirme o programare cu 24 de ore inainte
- pacientul poate confirma o programare
- pacientul poate anula o programare

## Visit
- 
- dupa consultatie se creaza o vizita ce trebuie platita, asociata unui appointment.
- pe consultatie se pot adauga observatii
- ea este legata de un appointment



==========
# catalog
    Product{name,id,inStock}
- REST search product
- REST+API get product details (name, price)
- listen OutOfStockEvent, BackInStockEvent
# order management
    Order{id,status,items:[{productId, number}], shippingNumber, customerId}
- REST place order
- listen PaymentAcceptedEvent, publish OrderConfirmedEvent
- call shipping provider with customer.address: get a shipping number
# payment
    statless
- API get payment redirect URL + orderId
- REST payment accepted + orderId
- publish PaymentAcceptedEvent
# inventory 
- REST add stock (pid, count)
- API get stock bulk [pid,pid...]
- API reserve stock (orderid, items:[{},{},{}])
- publish OutOfStockEvent/BackInStockEvent
- listen OrderConfirmedEvent(orderid)
# customer
    Customer{id,fullName, address}
- API get customer address (needed on shipping)
- listen to orderConfirmedEvent


* shipping is external (just an adapter)