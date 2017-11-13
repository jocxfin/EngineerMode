package jxl.biff;

import jxl.common.Logger;

public abstract class HeaderFooter {
    private static Logger logger = Logger.getLogger(HeaderFooter.class);
    private Contents centre;
    private Contents left;
    private Contents right;

    protected static class Contents {
        private StringBuffer contents;

        protected Contents() {
            this.contents = new StringBuffer();
        }

        protected Contents(String s) {
            this.contents = new StringBuffer(s);
        }

        protected Contents(Contents copy) {
            this.contents = new StringBuffer(copy.getContents());
        }

        protected String getContents() {
            return this.contents == null ? "" : this.contents.toString();
        }

        protected boolean empty() {
            if (this.contents == null || this.contents.length() == 0) {
                return true;
            }
            return false;
        }
    }

    protected abstract Contents createContents();

    protected abstract Contents createContents(String str);

    protected abstract Contents createContents(Contents contents);

    protected HeaderFooter() {
        this.left = createContents();
        this.right = createContents();
        this.centre = createContents();
    }

    protected HeaderFooter(HeaderFooter hf) {
        this.left = createContents(hf.left);
        this.right = createContents(hf.right);
        this.centre = createContents(hf.centre);
    }

    protected HeaderFooter(String s) {
        if (s == null || s.length() == 0) {
            this.left = createContents();
            this.right = createContents();
            this.centre = createContents();
            return;
        }
        int leftPos = s.indexOf("&L");
        int rightPos = s.indexOf("&R");
        int centrePos = s.indexOf("&C");
        if (leftPos == -1 && rightPos == -1 && centrePos == -1) {
            this.centre = createContents(s);
        } else {
            if (leftPos != -1) {
                int endLeftPos = s.length();
                if (centrePos > leftPos) {
                    endLeftPos = centrePos;
                    if (rightPos > leftPos && centrePos > rightPos) {
                        endLeftPos = rightPos;
                    }
                } else if (rightPos > leftPos) {
                    endLeftPos = rightPos;
                }
                this.left = createContents(s.substring(leftPos + 2, endLeftPos));
            }
            if (rightPos != -1) {
                int endRightPos = s.length();
                if (centrePos > rightPos) {
                    endRightPos = centrePos;
                    if (leftPos > rightPos && centrePos > leftPos) {
                        endRightPos = leftPos;
                    }
                } else if (leftPos > rightPos) {
                    endRightPos = leftPos;
                }
                this.right = createContents(s.substring(rightPos + 2, endRightPos));
            }
            if (centrePos != -1) {
                int endCentrePos = s.length();
                if (rightPos > centrePos) {
                    endCentrePos = rightPos;
                    if (leftPos > centrePos && rightPos > leftPos) {
                        endCentrePos = leftPos;
                    }
                } else if (leftPos > centrePos) {
                    endCentrePos = leftPos;
                }
                this.centre = createContents(s.substring(centrePos + 2, endCentrePos));
            }
        }
        if (this.left == null) {
            this.left = createContents();
        }
        if (this.centre == null) {
            this.centre = createContents();
        }
        if (this.right == null) {
            this.right = createContents();
        }
    }

    public String toString() {
        StringBuffer hf = new StringBuffer();
        if (!this.left.empty()) {
            hf.append("&L");
            hf.append(this.left.getContents());
        }
        if (!this.centre.empty()) {
            hf.append("&C");
            hf.append(this.centre.getContents());
        }
        if (!this.right.empty()) {
            hf.append("&R");
            hf.append(this.right.getContents());
        }
        return hf.toString();
    }
}
