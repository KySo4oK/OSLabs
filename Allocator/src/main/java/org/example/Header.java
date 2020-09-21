package org.example;

public class Header {
    private int sizeOfBlock;
    private boolean isFree = true;
    private int addressStart;

    @Override
    public String toString() {
        return "Header{" +
                "sizeOfBlock=" + sizeOfBlock +
                ", isFree=" + isFree +
                ", addressStart=" + addressStart +
                '}';
    }

    public Header(int sizeOfBlock, int addressStart) {
        this.sizeOfBlock = sizeOfBlock;
        this.addressStart = addressStart;
    }

    public int getSizeOfBlock() {
        return sizeOfBlock;
    }

    public void setSizeOfBlock(int sizeOfBlock) {
        this.sizeOfBlock = sizeOfBlock;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getAddressStart() {
        return addressStart;
    }

    public void setAddressStart(int addressStart) {
        this.addressStart = addressStart;
    }
}
