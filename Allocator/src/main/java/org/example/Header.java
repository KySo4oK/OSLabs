package org.example;

public class Header {
    private int size;
    private boolean isFree = true;
    private int addressStart;

    public Header() {

    }

    @Override
    public String toString() {
        return "Header{" +
                "sizeOfBlock=" + size +
                ", isFree=" + isFree +
                ", addressStart=" + addressStart +
                '}';
    }

    public Header(int size, int addressStart) {
        this.size = size;
        this.addressStart = addressStart;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
