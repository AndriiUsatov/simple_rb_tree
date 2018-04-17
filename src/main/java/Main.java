import tree.SimpleRedBlackTree;
import tree.SimpleRedBlackTreeOld;

/**
 * Created by andrii on 14.08.17.
 */
public class Main {

    public static void main(String[] args) {
//    SimpleRedBlackTreeOld<Integer,String> map = new SimpleRedBlackTreeOld<Integer, String>(new Comparator() {
//        public int compare(Object o, Object t1) {
//            Integer i1 = (Integer)o;
//            Integer i2 = (Integer)t1;
//            return i1.intValue() < i2.intValue() ? 1 : (i1.intValue() == i2.intValue() ? 0 : -1);
//        }
//    });
        SimpleRedBlackTreeOld<Integer, String> map = new SimpleRedBlackTreeOld<Integer, String>();
//        System.out.println(map.size());

        map.put(10, "");
        map.put(25, "");
        map.put(38, "");
        map.put(50, "");
        map.put(75, "");
        map.put(63, "");
        map.put(99, "");
        map.put(100, "");
        map.put(150, "");
        map.put(125, "");
        map.put(101, "");
        map.put(137, "");
        map.put(175, "");
        map.put(163, "");
        map.put(200, "");
        map.showAll();
        map.remove(50);
        int x = 0;

//        map.put(1,"first");
//        map.put(2,"second");
//        map.put(3,"third");
//        map.put(4,"fourth");
//        map.put(5,"fifth");
//        map.put(6,"sixth");
//        map.put(7,"seventh");
//        map.put(8,"eighth");
//        map.put(9,"ninth");
//        map.put(10,"tenth");

//        map.remove(4);
//        map.put(4,"fourth - 2");
//        System.out.println("hey");
//        map.put(15,"fifteenth");
//        map.remove(7);
//        map.put(-100,"oh my god");
//        map.put(11,"eleventh");


//        map.remove(1);
//        for (int i = 1; i < 11; i++) {
////            if (i == 5)
////                continue;
//            System.out.println(map.get(i));
//        }
////        System.out.println(map.get(5));
//        System.out.println(map.size());
//        System.out.println(map.entrySet().size());
//        for(SimpleRedBlackTreeOld.Entry<Integer,String> entry : map.entrySet())
//            System.out.println("Key is: " + entry.getKey() + "\tValue is: " + entry.getValue());
    }
}
