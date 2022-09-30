package fileManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@SpringBootTest
class FileManagerApplicationTests {

	/**
	 * В настоящем тесте мы проверяем корректность импорта из входящего JSON.
	 * 1. Сравниваем количество получившихся в рамках работы программы дочерних элементов с действительным.
	 * 2. Сравниваем id родительской папки из программы с фактическим id родительской папки.
	 * 3. Сравниваем объект с родительской папкой другого объекта (должны совпадать).
	 * 4. Сравниваем размер корневой папки с ожидаемым.
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test
	public void testImport() throws IOException, SQLException {
		String s = File.separator;
		String file = "C:" + s + "Users" + s + "79022" + s + "Desktop" + s + "Java" +
				s + "demo" + s + "ForTests" + s + "ForTest.txt";
		String x = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
		x = x.replaceAll(System.getProperty("line.separator"), "");
		x = x.replaceAll(" ", "");
		x = x.replaceAll("None", "\"None\"");

		JSONObject json1 = Request.parse(x);
		Request.importR(json1);

		Node parent = TreeStr.mainFolder.get("d515e43f-f3f6-4471-bb77-6b455017a2d2");
		Assert.assertEquals(2, parent.children.size());
		Assert.assertEquals("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1", parent.parent.id);

		Node parent_1 = TreeStr.mainFolder.get("b1d8fd7d-2ae3-47d5-b2f9-0f094af800d4").parent;
		Assert.assertEquals(parent_1, parent);
		Assert.assertEquals(384, parent.parent.size);
	}

	/**
	 * В настоящем тесте мы проверяем корректность работы метода, возвращающего JSON из получившейся
	 * структуры файловой системы.
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test
	public void testNodes() throws SQLException, IOException {
		testImport();
		Map x = (Map)Request.nodesR("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
		Assert.assertEquals(1, ((ArrayList) x.get("children")).size());
	}

	/**
	 * Проверяем корректность работы программы при обновлении характеристик эелементов,
	 * изменения файловой структуры.
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test
	public void testUpdates() throws IOException, SQLException {
		testImport();
		String s = File.separator;
		String file = "C:" + s + "Users" + s + "79022" + s + "Desktop" + s + "Java" +
				s + "demo" + s + "ForTests" + s + "ForTest_update.txt";
		String x = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
		x = x.replaceAll(System.getProperty("line.separator"), "");
		x = x.replaceAll(" ", "");
		x = x.replaceAll("None", "\"None\"");

		Object obj = JSONValue.parse(x);
		JSONArray json = (JSONArray) obj;

		Request.importR((JSONObject) json.get(0));
		Node parent = TreeStr.mainFolder.get("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
		Assert.assertEquals(384, parent.children.get(0).size);
		Request.importR((JSONObject) json.get(1));
		Assert.assertEquals(128, parent.size);
		Assert.assertEquals(384, parent.parent.size);
		Assert.assertEquals(2, parent.parent.children.size());
	}

	/**
	 * Проверяем корректность функционала, связаного с удалением выбранного элемента.
	 * @throws SQLException
	 */
	@Test
	public void testDelete()throws SQLException, IOException {
		testImport();
		Request.deleteR("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");
		try {
			JSONObject y = Request.nodesR("863e1a7a-1304-42ae-943b-179184c077e3");
			System.out.println(false);
		} catch (NullPointerException e) {
			System.out.println(true);
		}
	}

	/**
	 * Проверяем корректность функционала, связаного с экспортом файловой системы в базу данных.
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test
	public void testExportInDB() throws IOException, SQLException {
		testImport();
		DBActions.exportInDB();
		String sql;
		String size = "";
		try (var connection = ConnectManager.get();
			 var statement = connection.createStatement()) {
			sql = "SELECT size FROM treeStr WHERE id = '069cb8d7-bbdd-47d3-ad8f-82ef4c269df1'";
			var executeResult = statement.executeQuery(sql);
			while (executeResult.next()) {
				size = executeResult.getString("size");
			}
		}
		Assert.assertEquals("384", size);
		testUpdates();
		DBActions.exportInDB();
		try (var connection = ConnectManager.get();
			 var statement = connection.createStatement()) {
			sql = "SELECT size FROM treeStr WHERE id = '069cb8d7-bbdd-47d3-ad8f-82ef4c269df1'";
			var executeResult = statement.executeQuery(sql);
			while (executeResult.next()) {
				size = executeResult.getString("size");
			}
		}
		Assert.assertEquals("128", size);
	}

	/**
	 * Проверяем корректность функционала, связаного с импортом файловой системы из базы данных
	 * в приложение.
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test
	public void testImportFromDB() throws IOException, SQLException{
		testImport();
		testExportInDB();
		DBActions.importFromDB();
		Assert.assertEquals(2, TreeStr.mainFolder.get("069cb8d7-bbdd-47d3-ad8f-82ef4c269fobo").children.size());
		Assert.assertEquals("d515e43f-f3f6-4471-bb77-6b455017a2d2", TreeStr.mainFolder.get("863e1a7a-1304-42ae-943b-179184c077e3").parent.id);
	}
}


