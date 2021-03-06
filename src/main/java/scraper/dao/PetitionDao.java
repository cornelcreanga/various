package scraper.dao;

import scraper.model.Petition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PetitionDao {


    public Petition createValueObject() {
        return new Petition();
    }

    public Petition getObject(Connection conn, int id) throws NotFoundException, SQLException {

        Petition valueObject = createValueObject();
        valueObject.setId(id);
        load(conn, valueObject);
        return valueObject;
    }


    public void load(Connection conn, Petition valueObject) throws NotFoundException, SQLException {

        String sql = "SELECT * FROM petition WHERE (id = ? ) ";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, valueObject.getId());

            singleQuery(conn, stmt, valueObject);

        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public Petition loadByName(Connection conn,  String name) throws NotFoundException {

        String sql = "SELECT * FROM petition WHERE (name = ? ) ";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            Petition p = new Petition();
            singleQuery(conn, stmt, p);
            return p;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    /**
     * LoadAll-method. This will read all contents from database table and
     * build a List containing valueObjects. Please note, that this method
     * will consume huge amounts of resources if table has lot's of rows.
     * This should only be used when target tables have only small amounts
     * of data.
     *
     * @param conn         This method requires working database connection.
     */
    public List loadAll(Connection conn) throws SQLException {

        String sql = "SELECT * FROM petition ORDER BY id ASC ";
        List searchResults = listQuery(conn, conn.prepareStatement(sql));

        return searchResults;
    }




    public synchronized void create(Connection conn, Petition valueObject){

        String sql = "INSERT INTO petition ( name, link) VALUES (?, ?) ";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getLink());

            int rowcount = databaseUpdate(conn, stmt);
            if (rowcount != 1) {
                //System.out.println("PrimaryKey Error when updating DB!");
                throw new SQLRuntimeException("PrimaryKey Error when updating DB!");
            }

        } catch(SQLException e){
            throw new SQLRuntimeException(e);
        }


        /**
         * The following query will read the automatically generated primary key
         * back to valueObject. This must be done to make things consistent for
         * upper layer processing logic.
         */
        sql = "SELECT last_insert_id()";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                valueObject.setId((int)result.getLong(1));
            } else {
                throw new SQLException("Unable to find primary-key for created object!");
            }
        } catch(SQLException e){
            throw new SQLRuntimeException(e);
        }

    }


    public void save(Connection conn, Petition valueObject)
            throws NotFoundException, SQLException {

        String sql = "UPDATE petition SET name = ?, link = ? WHERE (id = ? ) ";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getLink());

            stmt.setInt(3, valueObject.getId());

            int rowcount = databaseUpdate(conn, stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be saved! (PrimaryKey not found)");
                throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
            }
            if (rowcount > 1) {
                //System.out.println("PrimaryKey Error when updating DB! (Many objects were affected!)");
                throw new SQLException("PrimaryKey Error when updating DB! (Many objects were affected!)");
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }


    public void delete(Connection conn, Petition valueObject)
            throws NotFoundException, SQLException {

        String sql = "DELETE FROM petition WHERE (id = ? ) ";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, valueObject.getId());

            int rowcount = databaseUpdate(conn, stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
            if (rowcount > 1) {
                //System.out.println("PrimaryKey Error when updating DB! (Many objects were deleted!)");
                throw new SQLException("PrimaryKey Error when updating DB! (Many objects were deleted!)");
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }


    public void deleteAll(Connection conn) throws SQLException {

        String sql = "DELETE FROM petition";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            int rowcount = databaseUpdate(conn, stmt);
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }


    public int countAll(Connection conn) throws SQLException {

        String sql = "SELECT count(*) FROM petition";
        PreparedStatement stmt = null;
        ResultSet result = null;
        int allRows = 0;

        try {
            stmt = conn.prepareStatement(sql);
            result = stmt.executeQuery();

            if (result.next())
                allRows = result.getInt(1);
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return allRows;
    }


    public List searchMatching(Connection conn, Petition valueObject) throws SQLException {

        List searchResults;

        boolean first = true;
        StringBuffer sql = new StringBuffer("SELECT * FROM petition WHERE 1=1 ");

        if (valueObject.getId() != 0) {
            if (first) { first = false; }
            sql.append("AND id = ").append(valueObject.getId()).append(" ");
        }

        if (valueObject.getName() != null) {
            if (first) { first = false; }
            sql.append("AND name LIKE '").append(valueObject.getName()).append("%' ");
        }

        if (valueObject.getLink() != null) {
            if (first) { first = false; }
            sql.append("AND link LIKE '").append(valueObject.getLink()).append("%' ");
        }


        sql.append("ORDER BY id ASC ");

        // Prevent accidential full table results.
        // Use loadAll if all rows must be returned.
        if (first)
            searchResults = new ArrayList();
        else
            searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

        return searchResults;
    }



    /**
     * databaseUpdate-method. This method is a helper method for internal use. It will execute
     * all database handling that will change the information in tables. SELECT queries will
     * not be executed here however. The return value indicates how many rows were affected.
     * This method will also make sure that if cache is used, it will reset when data changes.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     */
    protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

        int result = stmt.executeUpdate();

        return result;
    }



    /**
     * databaseQuery-method. This method is a helper method for internal use. It will execute
     * all database queries that will return only one row. The resultset will be converted
     * to valueObject. If no rows were found, NotFoundException will be thrown.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     * @param valueObject  Class-instance where resulting data will be stored.
     */
    protected void singleQuery(Connection conn, PreparedStatement stmt, Petition valueObject)
            throws NotFoundException, SQLException {

        ResultSet result = null;

        try {
            result = stmt.executeQuery();

            if (result.next()) {

                valueObject.setId(result.getInt("id"));
                valueObject.setName(result.getString("name"));
                valueObject.setLink(result.getString("link"));

            } else {
                //System.out.println("Petition Object Not Found!");
                throw new NotFoundException("Petition Object Not Found!");
            }
        } finally {
            if (result != null)
                result.close();
            if (stmt != null)
                stmt.close();
        }
    }


    /**
     * databaseQuery-method. This method is a helper method for internal use. It will execute
     * all database queries that will return multiple rows. The resultset will be converted
     * to the List of valueObjects. If no rows were found, an empty List will be returned.
     *
     * @param conn         This method requires working database connection.
     * @param stmt         This parameter contains the SQL statement to be excuted.
     */
    protected List listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

        ArrayList searchResults = new ArrayList();
        ResultSet result = null;

        try {
            result = stmt.executeQuery();

            while (result.next()) {
                Petition temp = createValueObject();

                temp.setId(result.getInt("id"));
                temp.setName(result.getString("name"));
                temp.setLink(result.getString("link"));

                searchResults.add(temp);
            }

        } finally {
            if (result != null)
                result.close();
            if (stmt != null)
                stmt.close();
        }

        return (List)searchResults;
    }


}
