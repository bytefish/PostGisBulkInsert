// Copyright (c) Philipp Wagner and Bertil Chapuis. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.postgisbulkinsert;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;
import de.bytefish.postgisbulkinsert.handlers.PostgisValueHandler;
import de.bytefish.postgisbulkinsert.utils.PgBulkInsertUtils;
import mil.nga.sf.Geometry;

import java.util.function.Function;

public class PostgisExtensions {

    public static <TEntity> void mapPostgis(AbstractMapping<TEntity> mapping, String columnName, Function<TEntity, Geometry> propertyGetter) {
        PgBulkInsertUtils.addColumn(mapping, columnName, new PostgisValueHandler(), propertyGetter);
    }

}
