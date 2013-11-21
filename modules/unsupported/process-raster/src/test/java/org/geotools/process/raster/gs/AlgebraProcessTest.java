package org.geotools.process.raster.gs;

import it.geosolutions.jaiext.algebra.AlgebraDescriptor.Operator;

import java.awt.image.RenderedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class AlgebraProcessTest {

    public static final String REFERENCE_PATH_FOR_TEST = "/home/geosolutions/processtest/notHumanTargets";

    public static boolean coveragesPresent = true;

    private static List<GridCoverage2D> coverages;

    private static AlgebricCoverageProcess process;

    private static RenderedImage[] images;

    private static int dataType;   

    
    @BeforeClass
    @Ignore
    public static void initialSetup() {
        process = new AlgebricCoverageProcess();
        AbstractGridCoverage2DReader reader = null;
        AbstractGridFormat format;
        GridCoverage2D coverage2D = null;
        try {
            File tiffDirectory = new File(REFERENCE_PATH_FOR_TEST);
            if (tiffDirectory.exists() && tiffDirectory.isDirectory()) {
                File[] files = tiffDirectory.listFiles();

                int numCoverages = files.length;

                coverages = new ArrayList<GridCoverage2D>(numCoverages);
                
                images = new RenderedImage[numCoverages];    
                
                for (int i = 0; i < numCoverages; i++) {

                    File tiff = files[i];

                    if(tiff.exists() && tiff.canRead()){

                        format = GridFormatFinder.findFormat(tiff);
                        reader = format.getReader(tiff);

                        coverage2D = reader.read(null);

                        coverages.add(coverage2D);

                        images[i] = coverage2D.getRenderedImage();
                        
                        reader.dispose();
                        
                    }else{
                        coveragesPresent = false;
                    }
                    
                }                
            }           
        } catch (Exception e) {
            
            if(reader != null){
                reader.dispose();
            }
            
            throw new IllegalArgumentException("An error occurred when reading the input coverages");
        }
        
        dataType = images[0].getSampleModel().getDataType();  
        
        if(!coveragesPresent){
            System.err.println("Some files cannot be read correctly");
        }
        
    }
    
    @Test
    @Ignore
    public void testAddOperation(){        
        if(coveragesPresent){
            Operator algebricOp = Operator.SUM;
            
            GridCoverage2D addCoverages = process.execute(coverages, algebricOp, null, null, null,null);
            
            RenderedImage added = addCoverages.getRenderedImage();                       
            
            PlanarImage.wrapRenderedImage(added).getTiles(); 
            
            addCoverages.dispose(false);
        }
    }
    
    @Test
    @Ignore
    public void testSubtractOperation(){        
        if(coveragesPresent){
            Operator algebricOp = Operator.SUBTRACT;
            
            GridCoverage2D subCoverages = process.execute(coverages, algebricOp, null, null, null,null);
            
            RenderedImage subtracted = subCoverages.getRenderedImage();
            PlanarImage.wrapRenderedImage(subtracted).getTiles(); 
            
            subCoverages.dispose(false);
        }
    }
    
    @Test
    @Ignore
    public void testMultiplyOperation(){        
        if(coveragesPresent){
            Operator algebricOp = Operator.MULTIPLY;
            
            GridCoverage2D multiplyCoverages = process.execute(coverages, algebricOp, null, null, null,null);
            
            RenderedImage multiplied = multiplyCoverages.getRenderedImage();
            PlanarImage.wrapRenderedImage(multiplied).getTiles();  
            
            multiplyCoverages.dispose(false);
        }
    }
    
    @Test
    @Ignore
    public void testDivideOperation(){        
        if(coveragesPresent){
            Operator algebricOp = Operator.DIVIDE;
            
            GridCoverage2D divideCoverages = process.execute(coverages, algebricOp, null, null, null,null);
            
            RenderedImage divided = divideCoverages.getRenderedImage();
            PlanarImage.wrapRenderedImage(divided).getTiles();
            
            divideCoverages.dispose(false);
        }
    }
}
