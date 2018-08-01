# postgres-jsonb-support

This project has the objective to supply a way to let postgres to work as a document database. So in this project we map plain Java Objects to Postgres Jsonb type. 

## DockerFile

For development purpose, use an postgres docker image. Go to root directory and run:

`docker-compose up`

For running the tests docker needs to be up.

## Getting Started:

1. Create a POJO that you want to seriazile os document with at least the followings fields:

`Long id
LocalDateTime createdAt
LocalDateTime updatedAt`

2. Make a default empty constructor in the POJO

3. Add an annotation @Entity. When initializing the application, the Component Scan will take place and create the respective 
table with the class name and add fields as jsonb document on database except for the default fields (id, createdAt and updatedAt).
The default fields (id, createdAt and updatedAt) are used for managment purpose of the table.
 
4. Get database connection:

`ConnectionFactory factory = new ConnectionFactory("jdbc:postgresql://localhost:5433/docker", "docker", "docker");
 Connection connection = factory.createConnection();`
             
5. Export table and Schema are optional properties, to enable them use methods below:

`connection.enableExportSchema();
connection.enableExportTable();
`
With this, it will create the respective table and schema in your database if they do not already exists.

6. Use operations below.


## Supported database operations:

1. Insert: `entityDAO.save(Object);`
2. Find All: `entityDAO.findAll(Class);`
3. Find: `entityDAO.find( filters..., Class );`
4. Update `entityDAO.update(Object, filters... )`
5. Delete `entityDAO.delete( filters..., Class)`
6. Select Native Query: `entityDAO.selectNativeQuery(query, Class)`
6. Native Query: `entityDAO.selectNativeQuery(query)`

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