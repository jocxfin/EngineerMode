package jxl.biff;

public class BuiltInName {
    public static final BuiltInName AUTO_ACTIVATE = new BuiltInName("Auto_Activate", 10);
    public static final BuiltInName AUTO_CLOSE = new BuiltInName("Auto_Open", 2);
    public static final BuiltInName AUTO_DEACTIVATE = new BuiltInName("Auto_Deactivate", 11);
    public static final BuiltInName AUTO_OPEN = new BuiltInName("Auto_Open", 1);
    public static final BuiltInName CONSOLIDATE_AREA = new BuiltInName("Consolidate_Area", 0);
    public static final BuiltInName CRITERIA = new BuiltInName("Criteria", 5);
    public static final BuiltInName DATABASE = new BuiltInName("Database", 4);
    public static final BuiltInName DATA_FORM = new BuiltInName("Data_Form", 9);
    public static final BuiltInName EXTRACT = new BuiltInName("Extract", 3);
    public static final BuiltInName FILTER_DATABASE = new BuiltInName("_FilterDatabase", 13);
    public static final BuiltInName PRINT_AREA = new BuiltInName("Print_Area", 6);
    public static final BuiltInName PRINT_TITLES = new BuiltInName("Print_Titles", 7);
    public static final BuiltInName RECORDER = new BuiltInName("Recorder", 8);
    public static final BuiltInName SHEET_TITLE = new BuiltInName("Sheet_Title", 11);
    private static BuiltInName[] builtInNames = new BuiltInName[0];
    private String name;
    private int value;

    private BuiltInName(String n, int v) {
        this.name = n;
        this.value = v;
        BuiltInName[] oldnames = builtInNames;
        builtInNames = new BuiltInName[(oldnames.length + 1)];
        System.arraycopy(oldnames, 0, builtInNames, 0, oldnames.length);
        builtInNames[oldnames.length] = this;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public static BuiltInName getBuiltInName(int val) {
        BuiltInName ret = FILTER_DATABASE;
        for (int i = 0; i < builtInNames.length; i++) {
            if (builtInNames[i].getValue() == val) {
                ret = builtInNames[i];
            }
        }
        return ret;
    }
}
