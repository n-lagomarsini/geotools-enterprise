/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.process.raster;

import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.process.ProcessException;
import org.geotools.referencing.CRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 *
 * @author Daniele Romagnoli, GeoSolutions
 *
 *
 * @source $URL$
 */
public class BaseCoverageAlgebraProcess {
    
    static final String MISMATCHING_ENVELOPE_MESSAGE = "coverages should share the same Envelope";
    
    static final String MISMATCHING_GRID_MESSAGE = "coverages should have the same gridRange";
    
    static final String MISMATCHING_CRS_MESSAGE = "coverages should share the same CoordinateReferenceSystem";
    
    static final String MISMATCHING_DATA_TYPE_MESSAGE = "coverages should have the same data type";
    
    private BaseCoverageAlgebraProcess() {
        
    }

    public static void checkCompatibleCoverages(GridCoverage2D coverageA, GridCoverage2D coverageB) throws ProcessException {
        if (coverageA == null || coverageB == null){
            String coveragesNull = coverageA == null ? (coverageB == null ? "coverageA and coverageB" : "coverageA") : "coverageB";  
            throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, coveragesNull));
        }
        
        // 
        // checking same CRS
        // 
        CoordinateReferenceSystem crsA = coverageA.getCoordinateReferenceSystem();
        CoordinateReferenceSystem crsB = coverageB.getCoordinateReferenceSystem();
        if (!CRS.equalsIgnoreMetadata(crsA, crsB)){
            MathTransform mathTransform = null;
            try {
                mathTransform = CRS.findMathTransform(crsA, crsB);
            } catch (FactoryException e) {
                throw new ProcessException("Exceptions occurred while looking for a mathTransform between the 2 coverage's CRSs", e );
            }
            if (mathTransform != null && !mathTransform.isIdentity()){
                throw new ProcessException(MISMATCHING_CRS_MESSAGE);
            }
        }
        
        // 
        // checking same Envelope and grid range
        // 
        Envelope envA = coverageA.getEnvelope();
        Envelope envB = coverageB.getEnvelope();
        if (!envA.equals(envB)) {
            throw new ProcessException(MISMATCHING_ENVELOPE_MESSAGE);
        }
        
        GridEnvelope gridRangeA = coverageA.getGridGeometry().getGridRange();
        GridEnvelope gridRangeB = coverageA.getGridGeometry().getGridRange();
        if (gridRangeA.getSpan(0) != gridRangeB.getSpan(0)
                || gridRangeA.getSpan(1) != gridRangeB.getSpan(1)) {
            throw new ProcessException(MISMATCHING_GRID_MESSAGE);
        }
    }

    public static void checkCompatibleCoverages(List<GridCoverage2D> coverages) throws ProcessException {
        
        int numSources = coverages.size();
        String coveragesNull;
        // 
        // checking null coverages
        // 
        for(int i = 0; i < numSources; i++){
            GridCoverage2D coverage = coverages.get(i);
            if (coverage == null){
                coveragesNull = "coverage "+i;  
                throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, coveragesNull));
            }
        }
        
        GridCoverage2D coverage0 = coverages.get(0);
        
        CoordinateReferenceSystem crs0 = coverage0.getCoordinateReferenceSystem();
        CoordinateReferenceSystem crs;
        
        Envelope env0 = coverage0.getEnvelope();
        Envelope env;
        
        GridEnvelope gridRange0 = coverage0.getGridGeometry().getGridRange();
        GridEnvelope gridRange;
        
        int dataType0 = coverage0.getRenderedImage().getSampleModel().getDataType();
        int dataType;
        
        for(int i = 1; i < numSources; i++){
            GridCoverage2D coverage = coverages.get(i);  
            
            // 
            // checking same CRS
            // 
            crs = coverage.getCoordinateReferenceSystem();
            
            if (!CRS.equalsIgnoreMetadata(crs0, crs)){
                MathTransform mathTransform = null;
                try {
                    mathTransform = CRS.findMathTransform(crs0, crs);
                } catch (FactoryException e) {
                    throw new ProcessException("Exceptions occurred while looking for a mathTransform between the coverage's CRSs", e );
                }
                if (mathTransform != null && !mathTransform.isIdentity()){
                    throw new ProcessException(MISMATCHING_CRS_MESSAGE);
                }
            }
            
            // 
            // checking same Envelope and grid range
            // 

            env = coverage.getEnvelope();
            if (!env0.equals(env)) {
                throw new ProcessException(MISMATCHING_ENVELOPE_MESSAGE);
            }
            

            gridRange = coverage.getGridGeometry().getGridRange();
            if (gridRange0.getSpan(0) != gridRange.getSpan(0)
                    || gridRange0.getSpan(1) != gridRange.getSpan(1)) {
                throw new ProcessException(MISMATCHING_GRID_MESSAGE);
            }        
            
            // 
            // checking same data type
            //
            dataType = coverage.getRenderedImage().getSampleModel().getDataType();
            if(dataType !=dataType0){
                throw new ProcessException(MISMATCHING_DATA_TYPE_MESSAGE);
            } 
        }
    }    
}
