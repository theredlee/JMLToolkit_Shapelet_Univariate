package GUI.GUI_BoxLook_New_25072020.Controller.III_VariablesController;

import GUI.GUI_BoxLook_New_25072020.Variables.Variables;

public class VariablesController extends VariablesController_abstract {
    public VariablesController(){
        initialize();
    }

    public void initialize(){
        initializeInstance();
    }

    public void initializeInstance(){
        this.aVariables = new Variables();
    }

    public void setInstance(Variables aVariables){
        this.aVariables = aVariables;
    }

    public Variables getInstance(){
        return this.aVariables;
    }
}
