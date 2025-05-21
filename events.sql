create table event_publication
(
    completion_date  timestamp(6) with time zone,
    publication_date timestamp(6) with time zone,
    id               uuid not null,
    event_type       varchar(255),
    listener_id      varchar(255),
    serialized_event varchar(255),
    primary key (id)
)
;

insert into event_publication (completion_date, event_type, listener_id, publication_date, serialized_event, id)
values (NULL,
        'victor.training.modulith.payment.PaymentCompletedEvent',
        'victor.training.modulith.order.impl.PaymentEventHandler.onPaymentCompleted(victor.training.modulith.payment.PaymentCompletedEvent)',
        '2025-05-21T10:49:02.234535Z',
        '{"orderId":1,"ok":true}',
        '2a311b02-5882-44f6-93da-971d9618b003')