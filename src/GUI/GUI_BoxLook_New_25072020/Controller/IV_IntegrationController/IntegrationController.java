package GUI.GUI_BoxLook_New_25072020.Controller.IV_IntegrationController;

import GUI.GUI_BoxLook_New_25072020.Controller.III_VariablesController.VariablesController;
import GUI.GUI_BoxLook_New_25072020.Controller.II_MethodsController.MethodsController;
import GUI.GUI_BoxLook_New_25072020.Controller.I_GUIComponentsController.GUIComponentsController;

public class IntegrationController extends IntegrationController_abstract {

    public IntegrationController(){
        initialize();
    }
    public void initialize(){
        initializeInstances();
    }

    public void initializeInstances(){
        this.aGUIComponentsController = new GUIComponentsController();
        this.aVariablesController = new VariablesController();
        this.aMethodsController = new MethodsController(this.aGUIComponentsController.getInstance(), this.aVariablesController.getInstance());
    }
}
