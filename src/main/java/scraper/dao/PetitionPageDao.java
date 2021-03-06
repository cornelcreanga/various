package scraper.dao;

import scraper.model.PetitionPage;

import java.sql.*;
import java.util.*;

public class PetitionPageDao {

    public PetitionPage createValueObject() {
        return new PetitionPage();
    }


    public PetitionPage getObject(Connection conn, int id) throws NotFoundException, SQLException {

        PetitionPage valueObject = createValueObject();
        valueObject.setId(id);
        load(conn, valueObject);
        return valueObject;
    }


    public void load(Connection conn, PetitionPage valueObject) throws NotFoundException, SQLException {

        String sql = "SELECT * FROM petition_page WHERE (id = ? ) ";
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

    public PetitionPage loadByPetitionAndPage(Connection conn,  int petitionId,int petitionPage) throws NotFoundException {

        String sql = "SELECT * FROM petition_page WHERE (petitionId = ? and page=?) ";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, petitionId);
            stmt.setInt(2, petitionPage);
            PetitionPage p = new PetitionPage();
            singleQuery(conn, stmt, p);
            return p;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List loadAll(Connection conn) throws SQLException {

        String sql = "SELECT * FROM petition_page ORDER BY id ASC ";
        List searchResults = listQuery(conn, conn.prepareStatement(sql));

        return searchResults;
    }


    public synchronized void create(Connection conn, PetitionPage valueObject) throws SQLException {

        String sql = "";
        PreparedStatement stmt = null;
        ResultSet result = null;

        try {
            sql = "INSERT INTO petition_page ( petitionId, page) VALUES (?, ?) ";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, valueObject.getPetitionId());
            stmt.setInt(2, valueObject.getPage());

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
            if (stmt != null)
                stmt.close();
        }

    }

    public void save(Connection conn, PetitionPage valueObject)
            throws NotFoundException, SQLException {

        String sql = "UPDATE petition_page SET petitionId = ?, page = ? WHERE (id = ? ) ";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, valueObject.getPetitionId());
            stmt.setInt(2, valueObject.getPage());

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


    public void delete(Connection conn, PetitionPage valueObject)
            throws NotFoundException, SQLException {

        String sql = "DELETE FROM petition_page WHERE (id = ? ) ";
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

        String sql = "DELETE FROM petition_page";
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

        String sql = "SELECT count(*) FROM petition_page";
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


    public List searchMatching(Connection conn, PetitionPage valueObject) throws SQLException {

        List searchResults;

        boolean first = true;
        StringBuffer sql = new StringBuffer("SELECT * FROM petition_page WHERE 1=1 ");

        if (valueObject.getId() != 0) {
            if (first) { first = false; }
            sql.append("AND id = ").append(valueObject.getId()).append(" ");
        }

        if (valueObject.getPetitionId() != 0) {
            if (first) { first = false; }
            sql.append("AND petitionId = ").append(valueObject.getPetitionId()).append(" ");
        }

        if (valueObject.getPage() != 0) {
            if (first) { first = false; }
            sql.append("AND page = ").append(valueObject.getPage()).append(" ");
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


    protected void singleQuery(Connection conn, PreparedStatement stmt, PetitionPage valueObject)
            throws NotFoundException, SQLException {

        ResultSet result = null;

        try {
            result = stmt.executeQuery();

            if (result.next()) {

                valueObject.setId(result.getInt("id"));
                valueObject.setPetitionId(result.getInt("petitionId"));
                valueObject.setPage(result.getInt("page"));

            } else {
                //System.out.println("PetitionPage Object Not Found!");
                throw new NotFoundException("PetitionPage Object Not Found!");
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
                PetitionPage temp = createValueObject();

                temp.setId(result.getInt("id"));
                temp.setPetitionId(result.getInt("petitionId"));
                temp.setPage(result.getInt("page"));

                searchResults.add(temp);
            }

        } finally {
            if (stmt != null)
                stmt.close();
        }

        return (List)searchResults;
    }


}
