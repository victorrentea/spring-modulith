-- copy data from PRODUCT to REVIEWED_PRODUCT
INSERT INTO CATALOG.REVIEWED_PRODUCT (ID, PRODUCT_ID, STARS) SELECT next value for reviewed_product_seq, ID, STARS FROM CATALOG.PRODUCT

-- link existing PRODUCT_REVIEW to the new REVIEWED_PRODUCT table
UPDATE CATALOG.PRODUCT_REVIEW SET REVIEWED_PRODUCT_ID = (SELECT ID FROM CATALOG.REVIEWED_PRODUCT WHERE PRODUCT_ID = PRODUCT_REVIEW.PRODUCT_ID)