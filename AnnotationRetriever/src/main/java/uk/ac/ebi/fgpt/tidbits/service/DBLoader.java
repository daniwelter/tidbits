package uk.ac.ebi.fgpt.tidbits.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by dwelter on 27/03/14.
 */
public class DBLoader {


    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public Collection<String> retrieveAllEntries() {

        Properties properties = new Properties();
        try{
            properties.load(getClass().getClassLoader().getResource("dbquery.properties").openStream());
            String setSchema = properties.getProperty("db.setSchema");

            getJdbcTemplate().execute(setSchema);

            String query = properties.getProperty("db.selectQuery");
            return getJdbcTemplate().query(query, new DataMapper());
        }
        catch (IOException e){
            throw new RuntimeException(
                    "Unable to create DBLoader service: failed to read dbquery.properties resource", e);
        }
    }


    private class DataMapper implements RowMapper<String> {
        public String mapRow(ResultSet resultSet, int i) throws SQLException {
            String cellName = resultSet.getString(1);

            return cellName;
        }
    }
}
