package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import projects.entity.Project;
import projects.exception.DbException;
//import projects.util.DbConnection;

public class ProjectDao {

  public Project insertProject(Project project) {

    String sql = "INSERT INTO project "
        + "(project_name, estimated_hours, actual_hours, difficulty, notes) "
        + "VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DbConnection.getConnection()) {

      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareStatement(sql,
          PreparedStatement.RETURN_GENERATED_KEYS)) {

        setParameter(stmt, 1, project.getProjectName(), String.class);
        setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
        setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
        setParameter(stmt, 4, project.getDifficulty(), Integer.class);
        setParameter(stmt, 5, project.getNotes(), String.class);

        stmt.executeUpdate();

        try (ResultSet rs = stmt.getGeneratedKeys()) {
          if (rs.next()) {
            project.setProjectId(rs.getInt(1));
          }
        }

        commitTransaction(conn);
        return project;

      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }

    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public List<Project> fetchAllProjects() {

    String sql = "SELECT project_id, project_name FROM project";

    try (Connection conn = DbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      List<Project> projects = new LinkedList<>();

      while (rs.next()) {

        Project project = new Project();

        project.setProjectId(rs.getInt("project_id"));
        project.setProjectName(rs.getString("project_name"));

        projects.add(project);
      }

      return projects;

    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public Optional<Project> fetchProjectById(Integer projectId) {

    String sql = "SELECT * FROM project WHERE project_id=?";

    try (Connection conn = DbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      setParameter(stmt, 1, projectId, Integer.class);

      try (ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {

          Project project = new Project();

          project.setProjectId(rs.getInt("project_id"));
          project.setProjectName(rs.getString("project_name"));
          project.setEstimatedHours(rs.getBigDecimal("estimated_hours"));
          project.setActualHours(rs.getBigDecimal("actual_hours"));
          project.setDifficulty(rs.getInt("difficulty"));
          project.setNotes(rs.getString("notes"));

          return Optional.of(project);
        }

        return Optional.empty();
      }

    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public boolean modifyProjectDetails(Project project) {

    String sql =
        "UPDATE project SET project_name=?, estimated_hours=?, actual_hours=?, difficulty=?, notes=? WHERE project_id=?";

    try (Connection conn = DbConnection.getConnection()) {

      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareStatement(sql)) {

        setParameter(stmt, 1, project.getProjectName(), String.class);
        setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
        setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
        setParameter(stmt, 4, project.getDifficulty(), Integer.class);
        setParameter(stmt, 5, project.getNotes(), String.class);
        setParameter(stmt, 6, project.getProjectId(), Integer.class);

        int rows = stmt.executeUpdate();

        commitTransaction(conn);

        return rows == 1;

      } catch (Exception e) {

        rollbackTransaction(conn);
        throw new DbException(e);
      }

    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public boolean deleteProject(Integer projectId) {

    String sql = "DELETE FROM project WHERE project_id=?";

    try (Connection conn = DbConnection.getConnection()) {

      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareStatement(sql)) {

        setParameter(stmt, 1, projectId, Integer.class);

        int rows = stmt.executeUpdate();

        commitTransaction(conn);

        return rows == 1;

      } catch (Exception e) {

        rollbackTransaction(conn);
        throw new DbException(e);
      }

    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  private void startTransaction(Connection conn) throws SQLException {
    conn.setAutoCommit(false);
  }

  private void commitTransaction(Connection conn) throws SQLException {
    conn.commit();
    conn.setAutoCommit(true);
  }

  private void rollbackTransaction(Connection conn) throws SQLException {
    conn.rollback();
    conn.setAutoCommit(true);
  }

  private <T> void setParameter(PreparedStatement stmt, int index, T value,
      Class<T> type) throws SQLException {

    if (value == null) {
      stmt.setObject(index, null);
      return;
    }

    if (type == String.class) {
      stmt.setString(index, (String) value);
    } else if (type == Integer.class) {
      stmt.setInt(index, (Integer) value);
    } else if (type == BigDecimal.class) {
      stmt.setBigDecimal(index, (BigDecimal) value);
    } else {
      stmt.setObject(index, value);
    }
  }
}