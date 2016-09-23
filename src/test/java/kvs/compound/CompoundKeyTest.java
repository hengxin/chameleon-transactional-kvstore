package kvs.compound;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class CompoundKeyTest {
    CompoundKey key00 = new CompoundKey("0", "0");
    CompoundKey key01 = new CompoundKey("0", "1");

    CompoundKey key30 = new CompoundKey("3", "0");
    CompoundKey key20 = new CompoundKey("2", "0");

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void equals() throws Exception {
        Assert.assertNotEquals("key00 is not equal to key01", key00, key01);
        Assert.assertNotEquals("key30 is not equal to key20", key30, key20);
    }

}