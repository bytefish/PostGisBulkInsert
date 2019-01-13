# PostGISBulkInsert #

[PostGisBulkInsert] extends [PgBulkInsert] to enable Postgres bulk insert for PostGIS Types.

## Maven ##

[PostGisBulkInsert] is available in the Central Maven Repository. 

You can add the following dependencies to your pom.xml to include [PostGisBulkInsert] in your project.

```xml
<dependency>
	<groupId>de.bytefish</groupId>
	<artifactId>postgisbulkinsert</artifactId>
	<version>0.1</version>
</dependency>
```

## Usage ##



```java

public class PostgisExtensionTest extends TransactionalTestBase {

    // The Data to bulk insert to Postgres:
    private class PostgisEntity {

        private final Geometry geometry;

        public PostgisEntity(Geometry geometry) {
            this.geometry = geometry;
        }

        public Geometry getGeometry() {
            return geometry;
        }
    }

    // The Mapping between the Table in Postgres and the Domain Model:
    private class PostgisEntityMapping extends AbstractMapping<PostgisEntity> {

        public PostgisEntityMapping() {
            super(schema, "postgis_table");

            PostgisExtensions.mapPostgis(this, "geometry", PostgisEntity::getGeometry);
        }
    }
    
    @Test
    public void saveAll_Postgis_Test() throws SQLException {

        // PostGIS Entity to Store: POINT(1, 1):
        PostgisEntity entity = new PostgisEntity(new Point(1, 1));

        // This list will be inserted.
        List<PostgisExtensionTest.PostgisEntity> entities = Arrays.asList(entity);

        // Build the Mapping:
        AbstractMapping<PostgisEntity> mapping = new PostgisEntityMapping();

        // And the Bulk Inserter:
        PgBulkInsert<PostgisEntity> bulkInsert = new PgBulkInsert<>(mapping);

        // And Insert the entities:
        bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities);

        // ...
    }
```

## Thanks ##

* All credits for the library go to [@bchapuis](https://github.com/bchapuis) for the initial implementation.

[PostGisBulkInsert]: https://github.com/bytefish/PostGisBulkInsert
[PgBulkInsert]: https://github.com/bytefish/PgBulkInsert