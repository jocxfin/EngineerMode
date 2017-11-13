package jxl.biff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.common.Assert;
import jxl.write.biff.File;

public class Fonts {
    private ArrayList fonts = new ArrayList();

    public void addFont(FontRecord f) {
        if (!f.isInitialized()) {
            int pos = this.fonts.size();
            if (pos >= 4) {
                pos++;
            }
            f.initialize(pos);
            this.fonts.add(f);
        }
    }

    public FontRecord getFont(int index) {
        if (index > 4) {
            index--;
        }
        return (FontRecord) this.fonts.get(index);
    }

    public void write(File outputFile) throws IOException {
        Iterator i = this.fonts.iterator();
        while (i.hasNext()) {
            outputFile.write((FontRecord) i.next());
        }
    }

    IndexMapping rationalize() {
        int i;
        Iterator it;
        IndexMapping mapping = new IndexMapping(this.fonts.size() + 1);
        ArrayList newfonts = new ArrayList();
        int numremoved = 0;
        for (i = 0; i < 4; i++) {
            FontRecord fr = (FontRecord) this.fonts.get(i);
            newfonts.add(fr);
            mapping.setMapping(fr.getFontIndex(), fr.getFontIndex());
        }
        for (i = 4; i < this.fonts.size(); i++) {
            fr = (FontRecord) this.fonts.get(i);
            boolean duplicate = false;
            it = newfonts.iterator();
            while (it.hasNext() && !duplicate) {
                FontRecord fr2 = (FontRecord) it.next();
                if (fr.equals(fr2)) {
                    duplicate = true;
                    mapping.setMapping(fr.getFontIndex(), mapping.getNewIndex(fr2.getFontIndex()));
                    numremoved++;
                }
            }
            if (!duplicate) {
                boolean z;
                newfonts.add(fr);
                int newindex = fr.getFontIndex() - numremoved;
                if (newindex <= 4) {
                    z = false;
                } else {
                    z = true;
                }
                Assert.verify(z);
                mapping.setMapping(fr.getFontIndex(), newindex);
            }
        }
        it = newfonts.iterator();
        while (it.hasNext()) {
            fr = (FontRecord) it.next();
            fr.initialize(mapping.getNewIndex(fr.getFontIndex()));
        }
        this.fonts = newfonts;
        return mapping;
    }
}
