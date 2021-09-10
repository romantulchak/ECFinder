CREATE TABLE IF NOT EXISTS entity_test
(
    id   bigint primary key,
    name varchar(50)

);

CREATE TABLE IF NOT EXISTS entity_test_collections
(
    entity_test_id bigint references entity_test,
    name           varchar(50),
    dateTime       timestamp,
    price          numeric check ( price > 0)
);


INSERT INTO entity_test
SELECT *
FROM (
         SELECT 1, 'Test 1'
         union
         SELECT 2, 'Test 2'
         union
         SELECT 3, 'Test 3'
     ) x
WHERE NOT EXISTS(SELECT * FROM entity_test);


INSERT INTO entity_test_collections
SELECT *
FROM (
         SELECT 1, 'Test_elm-col1', now(), 100
         union
         SELECT 1, 'Test_elm-col2', now(), 150
         union
         SELECT 1, 'Test_elm-col3', now(), 300
     ) x
WHERE NOT EXISTS(SELECT * FROM entity_test_collections);
