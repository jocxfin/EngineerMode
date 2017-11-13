package jxl;

import java.util.Date;

public interface DateCell extends Cell {
    Date getDate();

    boolean isTime();
}
