drop table STOCK_VIEW; -- only required because of spring.jpa.hibernate.ddl-auto=create

create or replace view STOCK_VIEW as
select s.PRODUCT_ID, s.ITEMS as STOCK
from STOCK s;