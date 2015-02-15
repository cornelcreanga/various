import java.util.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.security.NoSuchAlgorithmException;

/**

 ver:bits:date:resource:[ext]:rand:counter

 where

 ver = 1
 bits = how many bits of partial-preimage the stamp is claimed to have
 date = YYMMDD[hhmm[ss]]
 resource = resource string (eg IP address, email address)
 ext = extension -- ignored in the current version
 Format of extension:

 [name1[=val1[,val2...]];[name2[=val1[,val2...]]...]]
 Note the value can also contain =. Example extension (not a real one):

 name1=2,3;name2;name3=var1=2,var2=3,2,val

 Which would be extension name1 has values 2 and 3; extension name2 has no values; extension name3 has 3 values ``var1=2'', ``var2=3'', ``2'' and ``val''.
 The hashcash extension may interpret the values as it sees fit eg ``var1=2'' could be the value of an option to the extension name3.

 rand = string of random characters from alphabet a-zA-Z0-9+/= to avoid preimage with other sender's stamps
 counter = to find a stamp with the desired number of preimage bits need to try lots of different strings this counter is incremented on each try.
 The Counter is also composed of characters from the alphabet a-zA-Z0-9+/=. (Note an implementation is not required to count sequentially).
 */
public class HashCash implements Comparable<HashCash> {

    private static final int hashLength = 160;
    private static final String dateFormatString = "yyMMddHHmmss";

    private String myToken;
    private int myValue;
    private Calendar myDate;
    private Map<String, List<String> > myExtensions;

    private String myResource;

    public HashCash(String cash) throws NoSuchAlgorithmException {
        myToken = cash;
        String[] parts = cash.split(":");

        try {
            int index = 1;
            myValue = Integer.parseInt(parts[index++]);
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
            Calendar tempCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            tempCal.setTime(dateFormat.parse(parts[index++]));

            myResource = parts[index++];
            myExtensions = deserializeExtensions(parts[index++]);

            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(cash.getBytes());
            byte[] tempBytes = md.digest();
            int tempValue = numberOfLeadingZeros(tempBytes);
            myValue = (tempValue > myValue ? myValue : tempValue);
        } catch (java.text.ParseException ex) {
            throw new IllegalArgumentException("Improperly formed HashCash", ex);
        }
    }

    private HashCash() throws NoSuchAlgorithmException {
    }

    /**
     * Mints a  HashCash  using now as the date
     * @param resource the string to be encoded in the HashCash
     * @throws NoSuchAlgorithmException If SHA1 is not a supported Message Digest
     */
    public static HashCash mintCash(String resource, int value) throws NoSuchAlgorithmException {
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return mintCash(resource, null, now, value);
    }


    /**
     * Mints a  HashCash
     * @param resource the string to be encoded in the HashCash
     * @throws NoSuchAlgorithmException If SHA1 is not a supported Message Digest
     */
    public static HashCash mintCash(String resource, Calendar date, int value)
            throws NoSuchAlgorithmException {
        return mintCash(resource, null, date, value);
    }

    /**
     * Mints a version 1 HashCash using now as the date
     * @param resource the string to be encoded in the HashCash
     * @param extensions Extra data to be encoded in the HashCash
     * @throws NoSuchAlgorithmException If SHA1 is not a supported Message Digest
     */
    public static HashCash mintCash(String resource, Map<String, List<String> > extensions, int value)
            throws NoSuchAlgorithmException {
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return mintCash(resource, extensions, now, value);
    }


    /**
     * Mints a  HashCash
     * @param resource the string to be encoded in the HashCash
     * @param extensions Extra data to be encoded in the HashCash
     * @throws NoSuchAlgorithmException If SHA1 is not a supported Message Digest
     */
    public static HashCash mintCash(String resource, Map<String, List<String> > extensions, Calendar date, int value)
            throws NoSuchAlgorithmException {

        if(value < 0 || value > hashLength)
            throw new IllegalArgumentException("Value must be between 0 and " + hashLength);

        if(resource.contains(":"))
            throw new IllegalArgumentException("Resource may not contain a colon.");

        HashCash result = new HashCash();

        MessageDigest md = MessageDigest.getInstance("SHA1");

        result.myResource = resource;
        result.myExtensions = (null == extensions ? new HashMap<String, List<String> >() : extensions);
        result.myDate = date;

        String prefix;

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);

        result.myValue = value;
        prefix =  "1:" + value + ":" + dateFormat.format(date.getTime()) + ":" + resource + ":" +
                serializeExtensions(extensions) + ":";
        result.myToken = generateCash(prefix, value, md);

        return result;
    }

    // Accessors
    /**
     * Two objects are considered equal if they are both of type HashCash and have an identical string representation
     */
    public boolean equals(Object obj) {
        if(obj instanceof HashCash)
            return toString().equals(obj.toString());
        else
            return super.equals(obj);
    }

    /**
     * Returns the canonical string representation of the HashCash
     */
    public String toString() {
        return myToken;
    }

    /**
     * Extra data encoded in the HashCash
     */
    public Map<String, List<String> > getExtensions() {
        return myExtensions;
    }

    /**
     * The primary resource being protected
     */
    public String getResource() {
        return myResource;
    }

    /**
     * The minting date
     */
    public Calendar getDate() {
        return myDate;
    }

    /**
     * The value of the HashCash (e.g. how many leading zero bits it has)
     */
    public int getValue() {
        return myValue;
    }

    // Private utility functions
    /**
     * Actually tries various combinations to find a valid hash.  Form is of prefix + random_hex + ":" + random_hex
     * @throws NoSuchAlgorithmException If SHA1 is not a supported Message Digest
     */
    private static String generateCash(String prefix, int value, MessageDigest md)
            throws NoSuchAlgorithmException {
        SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
        byte[] tmpBytes = new byte[4];
        rnd.nextBytes(tmpBytes);
        long random = unsignedIntToLong(tmpBytes);
        rnd.nextBytes(tmpBytes);
        long counter = unsignedIntToLong(tmpBytes);

        byte[] input = new byte[64];
        int tempLen = 0, inputLen = 0;
        byte[] tmp = prefix.getBytes();
        tempLen = tmp.length;
        System.arraycopy(tmp,0,input,0,tempLen);
        inputLen+=tempLen;

        tmp = Long.toHexString(random).getBytes();
        tempLen = tmp.length;
        System.arraycopy(tmp,0,input,inputLen,tempLen);
        inputLen+=tempLen;
        input[inputLen++]=':';

        byte[] toTry = new byte[128];
        System.arraycopy(input,0,toTry,0,inputLen);
        int lenToTry = 0;

        int tempValue;
        byte[] bArray;
        do {
            lenToTry = inputLen;
            counter++;

            tmp = Long.toHexString(counter).getBytes();
            tempLen = tmp.length;
            System.arraycopy(tmp,0,toTry,inputLen,tempLen);
            lenToTry+=tempLen;

            md.reset();
            md.update(toTry,0,lenToTry);
            bArray = md.digest();
            tempValue = numberOfLeadingZeros(bArray);

        } while ( tempValue < value);

        return new String(toTry,0,lenToTry);
    }


    private static long unsignedIntToLong(byte[] b) {
        long l = 0;
        l |= b[0] & 0xFF;
        l <<= 8;
        l |= b[1] & 0xFF;
        l <<= 8;
        l |= b[2] & 0xFF;
        l <<= 8;
        l |= b[3] & 0xFF;
        return l;
    }

    /**
     * Serializes the extensions with (key, value) seperated by semi-colons and values seperated by commas
     */
    private static String serializeExtensions(Map<String, List<String> > extensions) {
        if(null == extensions || extensions.isEmpty())
            return "";

        StringBuilder result = new StringBuilder(16*extensions.size());
        List<String> tempList;
        boolean first = true;

        for(String key: extensions.keySet()) {
            if(key.contains(":") || key.contains(";") || key.contains("="))
                throw new IllegalArgumentException("Extension key contains an illegal character. " + key);
            if(!first)
                result.append(";");
            first = false;
            result.append(key);
            tempList = extensions.get(key);

            if(null != tempList) {
                result.append("=");
                for(int i = 0; i < tempList.size(); i++) {
                    if(tempList.get(i).contains(":") || tempList.get(i).contains(";") || tempList.get(i).contains(","))
                        throw new IllegalArgumentException("Extension value contains an illegal character. " + tempList.get(i));
                    if(i > 0)
                        result.append(",");
                    result.append(tempList.get(i));
                }
            }
        }
        return result.toString();
    }


    private static Map<String, List<String> > deserializeExtensions(String extensions) {
        Map<String, List<String> > result = new HashMap<String, List<String> >();
        if(null == extensions || extensions.length() == 0)
            return result;

        String[] items = extensions.split(";");

        for (String item : items) {
            String[] parts = item.split("=", 2);
            if (parts.length == 1)
                result.put(parts[0], null);
            else
                result.put(parts[0], Arrays.asList(parts[1].split(",")));
        }

        return result;
    }

    /**
     * Counts the number of leading zeros in a byte array.
     */
    private static int numberOfLeadingZeros(byte[] values) {
        int result = 0;
        int temp = 0;
        for (byte value : values) {
            temp = numberOfLeadingZeros(value);
            result += temp;
            if (temp != 8)//value is 0
                break;
        }

        return result;
    }

    private static int numberOfLeadingZeros(byte value) {
        if(value < 0)
            return 0;
        if(value < 1)
            return 8;
        else if (value < 2)
            return  7;
        else if (value < 4)
            return 6;
        else if (value < 8)
            return 5;
        else if (value < 16)
            return 4;
        else if (value < 32)
            return 3;
        else if (value < 64)
            return 2;
        else return 1;
    }

    public int compareTo(HashCash other) {
        if(null == other)
            throw new NullPointerException();

        return Integer.valueOf(getValue()).compareTo(other.getValue());
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        int no = 18;
        HashCash hashCash = HashCash.mintCash("secret_key",no);
//        String s = "1:17:150109:secret_key::34c5ab5:7081a9a7";
//        HashCash h = new HashCash(s);
        MessageDigest md = MessageDigest.getInstance("SHA-1");

        byte[] digest = md.digest(hashCash.toString().getBytes());
//        double[] times = new double[100];
//
//        for (int i = 0; i < 100; i++) {
//            hashCash = HashCash.mintCash("secret_key",no);
//        }
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            hashCash = HashCash.mintCash("secret_key",no);
            System.out.println(hashCash);

        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);
        //14009 12644 13538 17501 13952
        //12541 13582 13544 12959
//        Arrays.sort(times);
//        for (double time : times) {
//            System.out.println(time);
//        }
//
//        Mean mean = new Mean();
//        System.out.println(mean.evaluate(times,0,times.length));
//        Median median = new Median();
//        System.out.println(median.evaluate(times,0,times.length));
//        Variance variance = new Variance();
//        System.out.println(variance.evaluate(times, 0, times.length));

    }

}
