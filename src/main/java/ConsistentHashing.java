import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.util.List;

public class ConsistentHashing {

    public static void main(String[] args) {
        List<String> servers = Lists.newArrayList("server1", "server2", "server3", "server4", "server5");

        for (int i = 0; i < 10; i++) {
            int bucket = Hashing.consistentHash(HashCode.fromLong(i), servers.size());
            System.out.println("First time routed to: " + servers.get(bucket));
        }

//        int bucket = Hashing.consistentHash(HashCode.fromLong(1), servers.size());
//        System.out.println("First time routed to: " + servers.get(bucket));

        // one of the back end servers is removed from the (middle of the) pool
        servers.remove(servers.size()-1);

        for (int i = 0; i < 10; i++) {
            int bucket = Hashing.consistentHash(HashCode.fromLong(i), servers.size());
            System.out.println("Second time routed to: " + servers.get(bucket));
        }


//        bucket = Hashing.consistentHash(HashCode.fromLong(2), servers.size());
//        System.out.println("Second time routed to: " + servers.get(bucket));
    }

}
