package read;

import cores.SSDConstraint;

import static java.lang.Integer.parseInt;


public class ReadModule implements ReadCore {

    public void read(int lba) {

        if (isValidAddress(lba)) {
            return;
        }


    }

    public boolean isValidAddress(int lba) {
        return lba >= SSDConstraint.MAX_BOUNDARY || lba < SSDConstraint.MIN_BOUNDARY;
    }
}