package org.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseConnection {
	public static void main(String[] args){
		
		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "root";
		
		
		
		try(Connection con = DriverManager.getConnection(url, user, password)){
			
			Scanner sc = new Scanner(System.in);
			
			
			
			System.out.println("inserisci nome nazione da cercare");
			String userName = sc.nextLine();
			
			
			
			String sql = "select c.name , c.country_id , r.name, c2.name  \r\n"
					+ "from countries c \r\n"
					+ "join regions r \r\n"
					+ "on c.region_id = r.region_id \r\n"
					+ "join continents c2 \r\n"
					+ "on r.continent_id = c2.continent_id \r\n"
					+ "where c.name like '%" + userName + "%'"
					+ "order by c.name ";

			try(PreparedStatement ps = con.prepareStatement(sql)){
				try(ResultSet rs = ps.executeQuery()){
					while(rs.next()) {
						
						final String nameC = rs.getString(1);
						final int id = rs.getInt(2);
						final String nameR = rs.getString(3);
						final String nameC2 = rs.getString(4);
						
						System.out.println(nameC + " - " + id + " - " 
								+ nameR + " - " + nameC2);
					}
				}
			}
			
			System.out.println("inserisci un id che vedi");
			int userId = sc.nextInt();
			
			String sql2 = "select DISTINCT l.`language` "
			        + "from countries c "
			        + "join country_stats cs "
			        + "on c.country_id = cs.country_id "
			        + "join country_languages cl "
			        + "on c.country_id = cl.country_id "
			        + "join languages l "
			        + "on l.language_id = cl.language_id "
			        + "where c.country_id = " + userId;

			String sql3 = "select DISTINCT cs.`year`, cs.population, cs.gdp "
			        + "from countries c "
			        + "join country_stats cs "
			        + "on c.country_id = cs.country_id "
			        + "join country_languages cl "
			        + "on c.country_id = cl.country_id "
			        + "join languages l "
			        + "on l.language_id = cl.language_id "
			        + "where c.country_id = " + userId + " "
			        + "and cs.year = ("
			        + "  select MAX(cs2.year) "
			        + "  from country_stats cs2 "
			        + "  join country_languages cl2 "
			        + "	 on cs2.country_id = cl2.country_id "
			        + "  where cl2.language_id = cl.language_id"
			        + ")";

			
			try(PreparedStatement ps2 = con.prepareStatement(sql2)){
				try(ResultSet rs2 = ps2.executeQuery()){
					while(rs2.next()) {
						
						final String nameL = rs2.getString(1);

						
						System.out.println(nameL);
					}
				}
			}
			
			try(PreparedStatement ps2 = con.prepareStatement(sql3)){
				try(ResultSet rs2 = ps2.executeQuery()){
					while(rs2.next()) {
						

						final int year = rs2.getInt(1);
						final Long populaton = rs2.getLong(2);
						final Long gdp = rs2.getLong(3);
						
						System.out.println(year + " - " + populaton + " - " 
								+ gdp);
					}
				}
			}
			
			
			
			sc.close();
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		
	}
}
