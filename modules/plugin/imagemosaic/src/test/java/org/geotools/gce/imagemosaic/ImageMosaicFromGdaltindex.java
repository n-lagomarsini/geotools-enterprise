/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2013, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.Hints;
import org.geotools.test.OnlineTestCase;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Testing using a Postgis database for storing the index for the ImageMosaic
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * 
 */
public class ImageMosaicFromGdaltindex extends Assert{

    private final static Logger LOGGER = Logging.getLogger(ImageMosaicFromGdaltindex.class);

    static String tempFolderName1 = "waterTempPG";

    @Test
    public void testCreateImageMosaicFromGDAL() throws Exception {
//        final File workDir = new File(TestData.file(this, "."), tempFolderName1);
//        if (!workDir.exists())
//            assertTrue(workDir.mkdir());
//        FileUtils
//                .copyFile(TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
//        TestData.unzipFile(this, tempFolderName1 + "/watertemp.zip");
        tempFolderName1 = "/mnt/c/Users/cancellieri/Downloads/MAPSET/ay_my/";
        File tempFolder=new File(tempFolderName1);
        Assert.assertTrue(tempFolder.exists());
        final URL timeElevURL = tempFolder.toURI().toURL();
            
//        final URL timeElevURL = TestData.url(this, tempFolderName1);

        // place datastore.properties file in the dir for the indexing
//        FileWriter out = null;
//        try {
//            out = new FileWriter(new File(tempFolder,"/datastore.properties"));
////                    new File(TestData.file(this, "."), tempFolderName1+ "/datastore.properties"));
//                    
//
//            final Set<Object> keyset = fixture.keySet();
//            for (Object key : keyset) {
//                final String key_ = (String) key;
//                final String value = fixture.getProperty(key_);
//                out.write(key_.replace(" ", "\\ ") + "=" + value.replace(" ", "\\ ") + "\n");
//            }
//            out.flush();
//        } finally {
//            if (out != null) {
//                IOUtils.closeQuietly(out);
//            }
//        }
        
        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(
                timeElevURL, null);
        assertNotNull(format);
        ImageMosaicReader reader = TestUtils.getReader(timeElevURL, format, null);
        assertNotNull(reader);
        //
        // final String[] metadataNames = reader.getMetadataNames();
        // assertNotNull(metadataNames);
        // assertEquals(12, metadataNames.length);
        //
        // assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        // assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        //
        // // dispose and create new reader
        // reader.dispose();
        // final MyImageMosaicReader reader1 = new MyImageMosaicReader(timeElevURL);
        // final RasterManager rasterManager = reader1
        // .getRasterManager(reader1.getGridCoverageNames()[0]);
        //
        // // query
        // final SimpleFeatureType type = rasterManager.granuleCatalog.getType("waterTempPG2");
        // Query query = null;
        // if (type != null) {
        // // creating query
        // query = new Query(type.getTypeName());
        //
        // // sorting and limiting
        // // max number of elements
        // query.setMaxFeatures(1);
        //
        // // sorting
        // final SortBy[] clauses = new SortBy[] {
        // new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property("ingestion"),
        // SortOrder.DESCENDING),
        // new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property("elevation"),
        // SortOrder.ASCENDING), };
        // query.setSortBy(clauses);
        //
        // }
        //
        // // checking that we get a single feature and that feature is correct
        // final Collection<GranuleDescriptor> features = new ArrayList<GranuleDescriptor>();
        // rasterManager.getGranuleDescriptors(query, new GranuleCatalogVisitor() {
        //
        // @Override
        // public void visit(GranuleDescriptor granule, Object o) {
        // features.add(granule);
        //
        // }
        // });
        // assertEquals(features.size(), 1);
        // GranuleDescriptor granule = features.iterator().next();
        // SimpleFeature sf = granule.getOriginator();
        // assertNotNull(sf);
        // Object ingestion = sf.getAttribute("ingestion");
        // assertTrue(ingestion instanceof Timestamp);
        // final GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        // gc.setTimeInMillis(1225497600000l);
        // assertEquals(0, (((Timestamp) ingestion).compareTo(gc.getTime())));
        // Object elevation = sf.getAttribute("elevation");
        // assertTrue(elevation instanceof Integer);
        // assertEquals(((Integer) elevation).intValue(), 0);
        //
        // // Reverting order (the previous timestamp shouldn't match anymore)
        // final SortBy[] clauses = new SortBy[] {
        // new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property("ingestion"),
        // SortOrder.ASCENDING),
        // new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property("elevation"),
        // SortOrder.DESCENDING), };
        // query.setSortBy(clauses);
        //
        // // checking that we get a single feature and that feature is correct
        // features.clear();
        // rasterManager.getGranuleDescriptors(query, new GranuleCatalogVisitor() {
        //
        // @Override
        // public void visit(GranuleDescriptor granule, Object o) {
        // features.add(granule);
        //
        // }
        // });
        // assertEquals(features.size(), 1);
        // granule = features.iterator().next();
        // sf = granule.getOriginator();
        // assertNotNull(sf);
        // ingestion = sf.getAttribute("ingestion");
        // assertTrue(ingestion instanceof Timestamp);
        // assertNotSame(0, (((Timestamp) ingestion).compareTo(gc.getTime())));
        // elevation = sf.getAttribute("elevation");
        // assertTrue(elevation instanceof Integer);
        // assertNotSame(((Integer) elevation).intValue(), 0);

    }


}

