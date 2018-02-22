# postgres-jsonb-support

This project has the objective to supply an easiest way to map plain Java Objects to Postgres Jsonb type. 

## DockerFile

Go to resource directory and run:

`docker-compose up`

## Getting Started:

1. Create a table with following columns:

`id serial primary key, document jsonb, created_at date, updated_at date`

2. Get database connection:

`Connection connection = new ConnectionFactory().getConnection("jdbc:postgresql://localhost:5433/docker", "docker", "docker");`

3. Use operations below.

## Supported database operations:

1. Insert: `entityDAO.save(Object);`
2. Find All: `entityDAO.findAll();`
3. Find: `entityDAO.find( filters... );`
4. Update `entityDAO.update(Object, filters... )`
5. Delete `entityDAO.delete( filters... )`

### Filters supported:
* eq : =
* lt : <
* gt : >
* ge : >=
* le : <=
* and : and
* or : or

#### Example using filter on select query:

Add one of the filters above in find() method to apply it. Example:

`List<Entity> entities = entityDAO.find(eq("fruit", "orange""));`

The native query for this operation will be:

`select * from entity where document ->> 'fruit' = 'orange';`

##### To query nested fields on json use "." syntax:
Example:

`List<Entity> entities = entityDAO.find(eq("fruits.breakfast", "orange""));`

Native query:

`select * from entity where document -> 'fruit' ->> 'breakfast' = 'orange';`

#### To query an Array:

Use metho asList in filters operation. Example:

`List<Entity> entities = entityDAO.find(eq("sports", asList( "soccer")));`

Native query:

`select * from entity where document -> 'sports' -> '{"swim", "soccer"}';`

### Code Examples
For more examples, go to json-support-example project and run the Application class.
  
## Working and Future Progress:
1. ~~Implement CRUD operations with mocked list~~
2. ~~Add support to Postgres~~
3. ~~New project with examples~~
4. ~~Docker compose with postgres~~
5. ~~Support to jsonb type~~
...
