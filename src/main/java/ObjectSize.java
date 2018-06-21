import org.ehcache.sizeof.SizeOf;

public class ObjectSize {

    public static void main(String[] args) {
        Class clazz = ObjectSize.class;
        String s = "cuaaacu";
        SizeOf sizeOf = SizeOf.newInstance();
        long shallowSize = sizeOf.sizeOf(clazz);
        long deepSize = sizeOf.deepSizeOf(clazz);
        System.out.println(shallowSize);
        System.out.println(deepSize);
        shallowSize = sizeOf.sizeOf(s);
        deepSize = sizeOf.deepSizeOf(s);
        System.out.println(shallowSize);
        System.out.println(deepSize);

    }

}
