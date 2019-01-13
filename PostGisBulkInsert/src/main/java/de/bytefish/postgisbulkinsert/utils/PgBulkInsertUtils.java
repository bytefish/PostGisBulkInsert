// Copyright (c) Philipp Wagner and Bertil Chapuis. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.postgisbulkinsert.utils;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;
import de.bytefish.pgbulkinsert.model.ColumnDefinition;
import de.bytefish.pgbulkinsert.pgsql.PgBinaryWriter;
import de.bytefish.pgbulkinsert.pgsql.handlers.IValueHandler;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PgBulkInsertUtils {

    private PgBulkInsertUtils() {}

    public static <TEntity, TProperty> void addColumn(AbstractMapping<TEntity> mapping, String columnName, IValueHandler<TProperty> valueHandler, Function<TEntity, TProperty> propertyGetter) {

        BiConsumer<PgBinaryWriter, TEntity> action = new BiConsumer<PgBinaryWriter, TEntity>() {
            @Override
            public void accept(PgBinaryWriter binaryWriter, TEntity entity) {
                binaryWriter.write(valueHandler, propertyGetter.apply(entity));
            }
        };

        ColumnDefinition<TEntity> columnDefinition = new ColumnDefinition<>(columnName, action);

        mapping.getColumns().add(columnDefinition);
    }
}
