// Copyright (c) Philipp Wagner and Bertil Chapuis. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.postgisbulkinsert.handlers;

import de.bytefish.pgbulkinsert.pgsql.handlers.BaseValueHandler;
import mil.nga.sf.Geometry;
import mil.nga.sf.util.ByteWriter;
import mil.nga.sf.wkb.GeometryWriter;

import java.io.DataOutputStream;
import java.nio.ByteOrder;


public class PostgisValueHandler extends BaseValueHandler<Geometry> {

    @Override
    protected void internalHandle(DataOutputStream buffer, Geometry value) throws Exception {
        ByteWriter writer = new ByteWriter();
        writer.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        GeometryWriter.writeGeometry(writer, value);
        byte[] wkb = writer.getBytes();
        buffer.writeInt(wkb.length);
        buffer.write(wkb);
    }

}