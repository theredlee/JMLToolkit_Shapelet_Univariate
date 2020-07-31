package GUI.GUI_BoxLook_New_25072020.Controller.I_GUIComponentsController;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;

public class GUIComponentsController extends GUIComponentsController_abstract {

    public GUIComponentsController(){
        initialize();
    }

    public void initialize(){
        initializeInstance();
    }

    public void initializeInstance(){
        this.aGUIComponents = new GUIComponents();
        this.aGUIComponents.frmTimeSeries.setVisible(true);
    }

    public void setInstance(GUIComponents aGUIComponents){
        this.aGUIComponents = aGUIComponents;
    }

    public GUIComponents getInstance(){
        return this.aGUIComponents;
    }

}
