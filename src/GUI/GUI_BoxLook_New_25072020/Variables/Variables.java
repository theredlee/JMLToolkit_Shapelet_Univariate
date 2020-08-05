package GUI.GUI_BoxLook_New_25072020.Variables;

public class Variables extends Variables_abstract {
    public Variables(){
        initialize();
    }

    public void initialize(){
        initializeVariables();
    }

    public void initializeVariables(){
        this.initializeTS = true;
        this.load_SPLet_YesOrNo = false;
        this.initialize_TS_list = true;
        this.setting_TS_listModal = false;
        this.firstTSDrawing = true;
        this.firstTSDrawing_linePlot = false;
        this.stackModelOn = false;
        this.switchDot = false;

        this.globalStartPosition = 0;
        this.globalBestMatchSP = 0;
        this.globalBestMatchEP = 0;
        this.lastTSIndex = 0; // Default value = 0
        this.bottomTSTraceCount = 0; // Default value = 0
        this.root = System.getProperty("user.dir");
    }
}
