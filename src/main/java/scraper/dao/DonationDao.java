package scraper.dao;

import scraper.model.Donation;

import java.sql.*;
import java.util.*;
import java.math.*;


public class DonationDao {



    public Donation createValueObject() {
        return new Donation();
    }


    public Donation getObject(Connection conn, int id) throws NotFoundException, SQLException {

        Donation valueObject = createValueObject();
        valueObject.setId(id);
        load(conn, valueObject);
        return valueObject;
    }


    public void load(Connection conn, Donation valueObject) throws NotFoundException, SQLException {

        String sql = "SELECT * FROM donation WHERE (id = ? ) ";
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


    public List loadAll(Connection conn) throws SQLException {

        String sql = "SELECT * FROM donation ORDER BY id ASC ";
        List searchResults = listQuery(conn, conn.prepareStatement(sql));

        return searchResults;
    }



    public synchronized void create(Connection conn, Donation valueObject) throws SQLException {

        String sql = "";
        PreparedStatement stmt = null;
        ResultSet result = null;

        try {
            sql = "INSERT INTO donation ( name, amount,campaign_id) VALUES (?, ?, ?) ";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, valueObject.getName());
            stmt.setInt(2, valueObject.getAmount());
            stmt.setInt(3, valueObject.getCampaignId());

            int rowcount = databaseUpdate(conn, stmt);
            if (rowcount != 1) {
                //System.out.println("PrimaryKey Error when updating DB!");
                throw new SQLException("PrimaryKey Error when updating DB!");
            }

        } finally {
            if (stmt != null)
                stmt.close();
        }


        /**
         * The following query will read the automatically generated primary key
         * back to valueObject. This must be done to make things consistent for
         * upper layer processing logic.
         */
        sql = "SELECT last_insert_id()";

        try {
            stmt = conn.prepareStatement(sql);
            result = stmt.executeQuery();

            if (result.next()) {

                valueObject.setId((int)result.getLong(1));

            } else {
                //System.out.println("Unable to find primary-key for created object!");
                throw new SQLException("Unable to find primary-key for created object!");
            }
        } finally {
            if (result != null)
                result.close();
            if (stmt != null)
                stmt.close();
        }

    }


    public void save(Connection conn, Donation valueObject)
            throws NotFoundException, SQLException {

        String sql = "UPDATE donation SET name = ?, amount = ?,campaign_id=? WHERE (id = ? ) ";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, valueObject.getName());
            stmt.setInt(2, valueObject.getAmount());
            stmt.setInt(3, valueObject.getCampaignId());

            stmt.setInt(4, valueObject.getId());

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


    public void delete(Connection conn, Donation valueObject)
            throws NotFoundException, SQLException {

        String sql = "DELETE FROM donation WHERE (id = ? ) ";
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

        String sql = "DELETE FROM donation";
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

        String sql = "SELECT count(*) FROM donation";
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


    public List searchMatching(Connection conn, Donation valueObject) throws SQLException {

        List searchResults;

        boolean first = true;
        StringBuffer sql = new StringBuffer("SELECT * FROM donation WHERE 1=1 ");

        if (valueObject.getId() != 0) {
            if (first) { first = false; }
            sql.append("AND id = ").append(valueObject.getId()).append(" ");
        }

        if (valueObject.getName() != null) {
            if (first) { first = false; }
            sql.append("AND name LIKE '").append(valueObject.getName()).append("%' ");
        }

        if (valueObject.getAmount() != 0) {
            if (first) { first = false; }
            sql.append("AND amount =").append(valueObject.getAmount());
        }

        if (valueObject.getCampaignId() != 0) {
            if (first) { first = false; }
            sql.append("AND campaign_id = ").append(valueObject.getCampaignId()).append(" ");
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


    protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

        int result = stmt.executeUpdate();

        return result;
    }


    protected void singleQuery(Connection conn, PreparedStatement stmt, Donation valueObject)
            throws NotFoundException, SQLException {

        ResultSet result = null;

        try {
            result = stmt.executeQuery();

            if (result.next()) {

                valueObject.setId(result.getInt("id"));
                valueObject.setName(result.getString("name"));
                valueObject.setAmount(result.getInt("amount"));
                valueObject.setCampaignId(result.getInt("campaign_id"));

            } else {
                //System.out.println("Donation Object Not Found!");
                throw new NotFoundException("Donation Object Not Found!");
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    protected List listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

        ArrayList searchResults = new ArrayList();
        ResultSet result = null;

        try {
            result = stmt.executeQuery();

            while (result.next()) {
                Donation temp = createValueObject();

                temp.setId(result.getInt("id"));
                temp.setName(result.getString("name"));
                temp.setAmount(result.getInt("amount"));
                temp.setCampaignId(result.getInt("campaign_id"));

                searchResults.add(temp);
            }

        } finally {
            if (stmt != null)
                stmt.close();
        }

        return (List)searchResults;
    }


}