package jxl.biff;

import jxl.CellReferenceHelper;
import jxl.biff.DVParser.Condition;
import jxl.biff.drawing.ComboBox;
import jxl.biff.drawing.Comment;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.write.biff.CellValue;

public class BaseCellFeatures {
    public static final ValidationCondition BETWEEN = new ValidationCondition(DVParser.BETWEEN);
    public static final ValidationCondition EQUAL = new ValidationCondition(DVParser.EQUAL);
    public static final ValidationCondition GREATER_EQUAL = new ValidationCondition(DVParser.GREATER_EQUAL);
    public static final ValidationCondition GREATER_THAN = new ValidationCondition(DVParser.GREATER_THAN);
    public static final ValidationCondition LESS_EQUAL = new ValidationCondition(DVParser.LESS_EQUAL);
    public static final ValidationCondition LESS_THAN = new ValidationCondition(DVParser.LESS_THAN);
    public static final ValidationCondition NOT_BETWEEN = new ValidationCondition(DVParser.NOT_BETWEEN);
    public static final ValidationCondition NOT_EQUAL = new ValidationCondition(DVParser.NOT_EQUAL);
    public static Logger logger = Logger.getLogger(BaseCellFeatures.class);
    private ComboBox comboBox;
    private String comment;
    private Comment commentDrawing;
    private double commentHeight;
    private double commentWidth;
    private boolean dataValidation;
    private boolean dropDown;
    private DVParser dvParser;
    private DataValiditySettingsRecord validationSettings;
    private CellValue writableCell;

    protected static class ValidationCondition {
        private static ValidationCondition[] types = new ValidationCondition[0];
        private Condition condition;

        ValidationCondition(Condition c) {
            this.condition = c;
            ValidationCondition[] oldtypes = types;
            types = new ValidationCondition[(oldtypes.length + 1)];
            System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
            types[oldtypes.length] = this;
        }
    }

    protected BaseCellFeatures() {
    }

    public BaseCellFeatures(BaseCellFeatures cf) {
        this.comment = cf.comment;
        this.commentWidth = cf.commentWidth;
        this.commentHeight = cf.commentHeight;
        this.dropDown = cf.dropDown;
        this.dataValidation = cf.dataValidation;
        this.validationSettings = cf.validationSettings;
        if (cf.dvParser != null) {
            this.dvParser = new DVParser(cf.dvParser);
        }
    }

    protected String getComment() {
        return this.comment;
    }

    public double getCommentWidth() {
        return this.commentWidth;
    }

    public double getCommentHeight() {
        return this.commentHeight;
    }

    public final void setWritableCell(CellValue wc) {
        this.writableCell = wc;
    }

    public void setReadComment(String s, double w, double h) {
        this.comment = s;
        this.commentWidth = w;
        this.commentHeight = h;
    }

    public void setValidationSettings(DataValiditySettingsRecord dvsr) {
        boolean z;
        if (dvsr == null) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        this.validationSettings = dvsr;
        this.dataValidation = true;
    }

    public void removeComment() {
        this.comment = null;
        if (this.commentDrawing != null) {
            this.writableCell.removeComment(this.commentDrawing);
            this.commentDrawing = null;
        }
    }

    public void removeDataValidation() {
        if (this.dataValidation) {
            DVParser dvp = getDVParser();
            if (dvp.extendedCellsValidation()) {
                logger.warn("Cannot remove data validation from " + CellReferenceHelper.getCellReference(this.writableCell) + " as it is part of the shared reference " + CellReferenceHelper.getCellReference(dvp.getFirstColumn(), dvp.getFirstRow()) + "-" + CellReferenceHelper.getCellReference(dvp.getLastColumn(), dvp.getLastRow()));
                return;
            }
            this.writableCell.removeDataValidation();
            clearValidationSettings();
        }
    }

    public final void setCommentDrawing(Comment c) {
        this.commentDrawing = c;
    }

    public boolean hasDataValidation() {
        return this.dataValidation;
    }

    private void clearValidationSettings() {
        this.validationSettings = null;
        this.dvParser = null;
        this.dropDown = false;
        this.comboBox = null;
        this.dataValidation = false;
    }

    public boolean hasDropDown() {
        return this.dropDown;
    }

    public void setComboBox(ComboBox cb) {
        this.comboBox = cb;
    }

    public DVParser getDVParser() {
        if (this.dvParser != null) {
            return this.dvParser;
        }
        if (this.validationSettings == null) {
            return null;
        }
        this.dvParser = new DVParser(this.validationSettings.getDVParser());
        return this.dvParser;
    }

    public void shareDataValidation(BaseCellFeatures source) {
        if (this.dataValidation) {
            logger.warn("Attempting to share a data validation on cell " + CellReferenceHelper.getCellReference(this.writableCell) + " which already has a data validation");
            return;
        }
        clearValidationSettings();
        this.dvParser = source.getDVParser();
        this.validationSettings = null;
        this.dataValidation = true;
        this.dropDown = source.dropDown;
        this.comboBox = source.comboBox;
    }
}
