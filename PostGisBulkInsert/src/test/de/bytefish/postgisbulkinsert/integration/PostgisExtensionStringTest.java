// Copyright (c) Philipp Wagner and Bertil Chapuis. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.postgisbulkinsert.integration;

import de.bytefish.pgbulkinsert.PgBulkInsert;
import de.bytefish.pgbulkinsert.mapping.AbstractMapping;
import de.bytefish.pgbulkinsert.util.PostgreSqlUtils;
import de.bytefish.postgisbulkinsert.PostgisExtensions;
import de.bytefish.postgisbulkinsert.utils.TransactionalTestBase;
import mil.nga.sf.Geometry;
import mil.nga.sf.wkt.GeometryReader;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class PostgisExtensionStringTest extends TransactionalTestBase {

    private static class StringToGeometryConverter {

        public static final Geometry convert(String value) {
            try {
                return GeometryReader.readGeometry(value);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private class PostgisEntity {

        private final String geometryString;

        public PostgisEntity(String geometry) {
            this.geometryString = geometry;
        }

        public String getGeometryString() {
            return geometryString;
        }
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        createTable();
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {

    }

    private class PostgisEntityMapping extends AbstractMapping<PostgisEntity> {

        public PostgisEntityMapping() {
            super(schema, "postgis_table");

            PostgisExtensions.mapPostgis(this, "geometry", x -> x != null ?
                    StringToGeometryConverter.convert(x.getGeometryString()) : null);
        }
    }


    private boolean createTable() throws SQLException {
        String sqlStatement = String.format("CREATE TABLE %s.postgis_table(\n", schema) +
                "                geometry Geometry(POINT) \n" +
                "            );";

        Statement statement = connection.createStatement();

        return statement.execute(sqlStatement);
    }

    @Test
    public void saveAll_Postgis_Test() throws SQLException {

        // PostGIS Entity to Store:
        PostgisEntity entity = new PostgisEntity("POINT (1 2)");

        // This list will be inserted.
        List<PostgisExtensionStringTest.PostgisEntity> entities = Arrays.asList(entity);

        // Build the Mapping:
        AbstractMapping<PostgisEntity> mapping = new PostgisEntityMapping();

        // And the Bulk Inserter:
        PgBulkInsert<PostgisEntity> bulkInsert = new PgBulkInsert<>(mapping);

        // And Insert the entities:
        bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(connection), entities);

        ResultSet rs = getAll();

        while (rs.next()) {
            String wkt = rs.getString("geom");
            Assert.assertEquals("POINT(1 2)", wkt);
        }
    }

    private ResultSet getAll() throws SQLException {
        String sqlStatement = String.format("SELECT ST_AsText(geometry) AS geom FROM %s.postgis_table", schema);
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlStatement);
    }
}