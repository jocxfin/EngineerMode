package jxl;

public final class HeaderFooter extends jxl.biff.HeaderFooter {

    public static class Contents extends Contents {
        Contents() {
        }

        Contents(String s) {
            super(s);
        }

        Contents(Contents copy) {
            super((Contents) copy);
        }

        public boolean empty() {
            return super.empty();
        }
    }

    public HeaderFooter(HeaderFooter hf) {
        super((jxl.biff.HeaderFooter) hf);
    }

    public HeaderFooter(String s) {
        super(s);
    }

    public String toString() {
        return super.toString();
    }

    protected Contents createContents() {
        return new Contents();
    }

    protected Contents createContents(String s) {
        return new Contents(s);
    }

    protected Contents createContents(Contents c) {
        return new Contents((Contents) c);
    }
}
