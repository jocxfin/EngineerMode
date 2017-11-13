package jxl.write;

import jxl.CellFeatures;
import jxl.biff.BaseCellFeatures;

public class WritableCellFeatures extends CellFeatures {
    public static final ValidationCondition BETWEEN = BaseCellFeatures.BETWEEN;
    public static final ValidationCondition EQUAL = BaseCellFeatures.EQUAL;
    public static final ValidationCondition GREATER_EQUAL = BaseCellFeatures.GREATER_EQUAL;
    public static final ValidationCondition GREATER_THAN = BaseCellFeatures.GREATER_THAN;
    public static final ValidationCondition LESS_EQUAL = BaseCellFeatures.LESS_EQUAL;
    public static final ValidationCondition LESS_THAN = BaseCellFeatures.LESS_THAN;
    public static final ValidationCondition NOT_BETWEEN = BaseCellFeatures.NOT_BETWEEN;
    public static final ValidationCondition NOT_EQUAL = BaseCellFeatures.NOT_EQUAL;

    public WritableCellFeatures(CellFeatures cf) {
        super(cf);
    }

    public void removeComment() {
        super.removeComment();
    }

    public void removeDataValidation() {
        super.removeDataValidation();
    }
}
