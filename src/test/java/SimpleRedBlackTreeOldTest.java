import org.junit.Assert;
import org.junit.Test;
import tree.SimpleRedBlackTreeOld;


public class SimpleRedBlackTreeOldTest {
    SimpleRedBlackTreeOld<Integer,String> tree;
    @org.junit.Before
    public void setUp(){
        tree = new SimpleRedBlackTreeOld<Integer, String>();
    }
    @Test
    public void testRemoveFirst(){
        tree.put(50,"50");
        tree.removeFirst(50);
        Assert.assertEquals(0,tree.size());
    }

    @Test
    public void testRemoveSecond(){
        tree.put(50,"50");
        tree.removeSecond(50);
        Assert.assertEquals(0,tree.size());
    }
}
