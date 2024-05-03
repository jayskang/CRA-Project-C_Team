package read;

import cores.AddressConstraint;

import static java.lang.Integer.parseInt;


public class ReadModule implements ReadCore {

    public void read(String lba) {

        if(isValidAddress(lba)){
           return;
        }


    }

    public boolean isValidAddress(String lba) {
        return parseInt(lba) >= AddressConstraint.MAX_BOUNDARY || parseInt(lba) <AddressConstraint.MIN_BOUNDARY;
    }
}
