Drop table if exists shop cascade;
Drop table if exists product cascade;
Drop table if exists time cascade;
Drop table if exists sales cascade;

CREATE TABLE shop (
    shopid serial primary key,
    shopname varchar(255) not Null,
    city varchar(255) not Null,
    region varchar(255) not Null,
    country varchar(255) not Null
);

CREATE TABLE product (
    productid serial primary key,
    article varchar(255) not Null,
    productgroup varchar(255) not Null,
    productfamily varchar(255) not Null,
    productcategory varchar(255) not Null,
    price double precision not Null
);


CREATE TABLE time (
    dateid serial primary key,
    date varchar(255) not null,
    day varchar(255) not Null,
    month varchar(255) not Null,
    quarter varchar(255) not Null,
    year varchar(255) not Null
);


CREATE TABLE sales (
    salesid SERIAL primary key,
    shopid int,
    sold double precision,
    revenue double precision,
    productid int,
    dateid int,
    FOREIGN KEY (shopid) REFERENCES shop(shopid) on delete cascade,
    FOREIGN KEY (productid) REFERENCES product(productid) on delete cascade,
    FOREIGN KEY (dateid) REFERENCES time(dateid) on delete cascade
);