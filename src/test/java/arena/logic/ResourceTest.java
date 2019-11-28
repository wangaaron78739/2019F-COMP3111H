package arena.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest {

    @Before
    public void setUp() throws Exception {
        Resource.setResourceAmount(1000);
    }

    @After
    public void tearDown() throws Exception {
        Resource.setResourceAmount(1000);
    }

    @Test
    public void getResourceAmount() {
        assert(Resource.getResourceAmount()==1000);
    }

    @Test
    public void deductAmount() {
        if (!(Resource.deductAmount(500))) throw new AssertionError();
        if (!(Resource.deductAmount(500))) throw new AssertionError();
        if ((Resource.deductAmount(100))) throw new AssertionError();
    }

    @Test
    public void canDeductAmount() {
        assert(Resource.canDeductAmount(500));
        assert(Resource.canDeductAmount(0));
        assert(!Resource.canDeductAmount(1001));
        assert(!Resource.canDeductAmount(-1));
    }

    @Test
    public void setResourceAmount() {
        assert(Resource.getResourceAmount()==1000);
        Resource.setResourceAmount(100);
        assert(Resource.getResourceAmount()==100);
        Resource.setResourceAmount(999);
        assert(Resource.getResourceAmount()==999);
        Resource.setResourceAmount(0);
        assert(Resource.getResourceAmount()==0);
    }

    @Test
    public void addResourceAmount() {
        assert(Resource.getResourceAmount()==1000);
        Resource.addResourceAmount(100);
        assert(Resource.getResourceAmount()==1100);
        Resource.addResourceAmount(300);
        assert(Resource.getResourceAmount()==1400);
        Resource.addResourceAmount(0);
        assert(Resource.getResourceAmount()==1400);
    }
}