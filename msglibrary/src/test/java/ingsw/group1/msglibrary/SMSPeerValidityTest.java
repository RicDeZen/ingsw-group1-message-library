package ingsw.group1.msglibrary;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ingsw.group1.msglibrary.exceptions.InvalidAddressException;

import static org.junit.Assert.assertEquals;

/**
 * @author Giorgia Bortoletti
 * @author Riccardo De Zen
 */
@Config(sdk = Build.VERSION_CODES.P)
@RunWith(Parameterized.class)
public class SMSPeerValidityTest {

    private static final RandomSMSPeerGenerator GENERATOR = new RandomSMSPeerGenerator();
    private static final int TEST_RUNS = 200;
    private final int run;
    private final boolean expectedValid;

    /**
     * @return the Parameters for the test, in the form:
     * - Run number.
     * - Expected Validity.
     * First half of the runs is for valid numbers. Second half is for invalid numbers.
     */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> params = new ArrayList<>();
        for(int i = 0; i < TEST_RUNS; i++){
            params.add(
                    new Object[]{i,i<=TEST_RUNS/2}
            );
        }
        return params;
    }

    public SMSPeerValidityTest(int run, boolean expectedValid){
        this.run = run;
        this.expectedValid = expectedValid;
    }

    @Test
    public void constructorBehaves(){
        boolean actualValid = true;
        try{
            if(expectedValid){
                String address = GENERATOR.generateValidAddress();
                System.out.println(address);
                new SMSPeer(GENERATOR.generateValidAddress());
            }
            else{
                new SMSPeer(GENERATOR.generateInvalidAddress());
            }
        }
        catch(InvalidAddressException e){
            actualValid = false;
        }
        assertEquals(expectedValid,actualValid);
    }

}
