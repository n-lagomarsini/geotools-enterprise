package org.geotools.process.raster.gs;

import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.util.SimpleInternationalString;

public class AlgebricOpProcessFactory extends AnnotatedBeanProcessFactory {

    public AlgebricOpProcessFactory() {
        
        super(new SimpleInternationalString("Process executing various algebric operations on a list of coverages"),
                "algebric", 
                AlgebricCoverageProcess.class);
    }
}
