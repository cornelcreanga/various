package scraper.util;

import scraper.DbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CityInfo {

    static Map<Integer,String> codes = new HashMap<>();
    static{
        codes.put(1,"Alba");
        codes.put(2,"Arad");
        codes.put(3,"Arges");
        codes.put(4,"Bacau");
        codes.put(5,"Bihor");
        codes.put(6,"Bistrita-Nasaud");
        codes.put(7,"Botosani");
        codes.put(8,"Braila");
        codes.put(9,"Brasov");
        codes.put(10,"Bucuresti");
        codes.put(11,"Buzau");
        codes.put(12,"Caras-Severin");
        codes.put(13,"Cluj");
        codes.put(14,"Constanta");
        codes.put(15,"Covasna");
        codes.put(16,"Dambovita");
        codes.put(17,"Dolj");
        codes.put(18,"Galati");
        codes.put(19,"Gorj");
        codes.put(20,"Harghita");
        codes.put(21,"Hunedoara");
        codes.put(22,"Ialomita");
        codes.put(23,"Iasi");
        codes.put(25,"Maramures");
        codes.put(26,"Mehedinti");
        codes.put(27,"Mures");
        codes.put(28,"Neamt");
        codes.put(29,"Olt");
        codes.put(30,"Prahova");
        codes.put(31,"Salaj");
        codes.put(32,"Satu Mare");
        codes.put(33,"Sibiu");
        codes.put(34,"Suceava");
        codes.put(35,"Teleorman");
        codes.put(36,"Timis");
        codes.put(37,"Tulcea");
        codes.put(38,"Vaslui");
        codes.put(39,"Valcea");
        codes.put(40,"Vrancea");
        codes.put(41,"Calarasi");
        codes.put(42,"Giurgiu");
        codes.put(43,"Ilfov");
    }

    private static class CityData{
        public String countyName;
        public String alternate;
        public String region;

        public CityData(String name, String region) {
            this.countyName = name;
            this.region = region;
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DbConnection.get();
        Map<String,CityData> map = new HashMap<>();

        try(Statement st = connection.createStatement()){
            ResultSet rs = st.executeQuery("select asciiname,admin1_code from geoname_all");
            while(rs.next()){
                String name = rs.getString(1);
                name = name.replace("-"," ");
//                if (name.contains("Mures"))
//                    System.out.println(name);

                String codeAsString = rs.getString(2);
                int code = 0;
                if (codeAsString.length()==0)
                    continue;

                code = Integer.parseInt(codeAsString);
                if (code==0)
                    continue;
                String countyName = codes.get(code);
                String region = "";
                switch (code){
                    case 2:
                    case 5:region="Crisana";break;
                    case 25:
                    case 32:region="Maramures";break;
                    case 7:
                    case 34:region="Bucovina";break;
                    case 36:
                    case 12:region="Banat";break;
                    case 14:
                    case 37:region="Dobrogea";break;
                    case 1:
                    case 9:
                    case 21:
                    case 33:
                    case 15:
                    case 20:
                    case 27:
                    case 13:
                    case 6:
                    case 31:region="Transilvania";break;
                    case 17:
                    case 29:
                    case 26:
                    case 19:
                    case 39:region="Oltenia";break;
                    case 3:
                    case 16:
                    case 30:
                    case 11:
                    case 8:
                    case 35:
                    case 42:
                    case 43:
                    case 41:
                    case 22:region="Muntenia";break;
                    case 10:region="Bucuresti";break;
                    default:
                }

                map.put(name.toLowerCase(),new CityData(countyName,region));
            }
        }

        int f=0,t=0;
        List<String> notFound = new ArrayList();
        try(Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);){
            ResultSet rs = st.executeQuery("select id,city_cleaned,city_county,city_region,city_country from petition_signature where city is not null");
            while(rs.next()) {
                int id = rs.getInt(1);
                String city = rs.getString(2);
                CityData cityData = map.get(city.toLowerCase());
                if (cityData == null) {
                    f++;
                    notFound.add(city);
                    //System.out.println(city);
                }
                else {
                    t++;
                    rs.updateString(3, cityData.countyName);
                    rs.updateString(4,cityData.region);
                    rs.updateString(5,"Romania");
                    rs.updateRow();
                }
            }
            System.out.println(t);
            System.out.println(f);
        }
        connection.commit();
        connection.close();
        System.out.println("done");
//        Collections.sort(notFound);
//        for (int i = 0; i < notFound.size(); i++) {
//            System.out.println(notFound.get(i));
//
//        }

    }

}

