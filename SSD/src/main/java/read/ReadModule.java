package read;

import cores.AddressConstraint;

import static java.lang.Integer.parseInt;


public class ReadModule implements ReadCore {

    public void read(int lba) {

        if(isValidAddress(lba)){
           return;
        }


    }

    public boolean isValidAddress(int lba) {
        return lba >= AddressConstraint.MAX_BOUNDARY || lba <AddressConstraint.MIN_BOUNDARY;
    }
}
