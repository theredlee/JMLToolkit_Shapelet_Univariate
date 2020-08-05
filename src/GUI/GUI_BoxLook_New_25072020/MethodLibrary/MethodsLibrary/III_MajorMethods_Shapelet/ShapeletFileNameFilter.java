package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.III_MajorMethods_Shapelet;

import java.io.File;
import java.io.FileFilter;

class ShapeletFileNameFilter implements FileFilter {
    public boolean accept(File pathname) {
        if (pathname.getName().toLowerCase().contains("shapelet") ||
                pathname.getName().toLowerCase().contains("weight") ||
                pathname.getName().toLowerCase().contains("original") ) {
            //System.out.println("Reading file: " + list[i].getName());
            return true;
        }
        else {
            return false;
        }
    }
}

