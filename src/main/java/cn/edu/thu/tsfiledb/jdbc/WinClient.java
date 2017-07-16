package cn.edu.thu.tsfiledb.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cn.edu.thu.tsfiledb.exception.ArgsErrorException;

public class WinClient extends AbstractClient {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("cn.edu.thu.tsfiledb.jdbc.TsfileDriver");
		Connection connection = null;
		boolean printToConsole = true;
		Options options = createOptions();
		HelpFormatter hf = new HelpFormatter();
		hf.setWidth(MAX_HELP_CONSOLE_WIDTH);
		CommandLine commandLine = null;
		CommandLineParser parser = new DefaultParser();

		if (args == null || args.length == 0) {
			hf.printHelp(TSFILEDB_CLI_PREFIX, options, true);
			return;
		}
		init();
		args = checkPasswordArgs(args);

		try {
			commandLine = parser.parse(options, args);
			if (commandLine.hasOption(HELP_ARGS)) {
				hf.printHelp(TSFILEDB_CLI_PREFIX, options, true);
				return;
			}
			if (commandLine.hasOption(ISO8601_ARGS)) {
				setTimeFormatForNumber();
			}
			if (commandLine.hasOption(MAX_PRINT_ROW_COUNT_ARGS)) {
				try {
					maxPrintRowCount = Integer.valueOf(commandLine.getOptionValue(MAX_PRINT_ROW_COUNT_ARGS));
					if (maxPrintRowCount < 0) {
						maxPrintRowCount = Integer.MAX_VALUE;
					}
				} catch (NumberFormatException e) {
					System.out.println(
							TSFILEDB_CLI_PREFIX + "> error format of max print row count, it should be number");
					return;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			hf.printHelp(TSFILEDB_CLI_PREFIX, options, true);
			return;
		}
		Scanner scanner = new Scanner(System.in);
		try {
			String s;

			try {
				String host = checkRequiredArg(HOST_ARGS, HOST_NAME, commandLine);
				String port = checkRequiredArg(PORT_ARGS, PORT_NAME, commandLine);
				String username = checkRequiredArg(USERNAME_ARGS, USERNAME_NAME, commandLine);

				String password = commandLine.getOptionValue(PASSWORD_ARGS);
				if (password == null) {
					System.out.print(TSFILEDB_CLI_PREFIX + "> please input password: ");
					password = scanner.nextLine();
				}
				try {
					connection = DriverManager.getConnection("jdbc:tsfile://" + host + ":" + port + "/", username,
							password);
				} catch (SQLException e) {
					System.out.println(TSFILEDB_CLI_PREFIX + "> " + e.getMessage());
					return;
				}
			} catch (ArgsErrorException e) {
				System.out.println(TSFILEDB_CLI_PREFIX + ": " + e.getMessage());
				return;
			}

			System.out.println(" _______________________________.___.__          \n"
					+ " \\__    ___/   _____/\\_   _____/|   |  |   ____  \n"
					+ "   |    |  \\_____  \\  |    __)  |   |  | _/ __ \\ \n"
					+ "   |    |  /        \\ |     \\   |   |  |_\\  ___/ \n"
					+ "   |____| /_______  / \\___  /   |___|____/\\___  >   version 0.0.1\n"
					+ "                  \\/      \\/                  \\/ \n");

			System.out.println(TSFILEDB_CLI_PREFIX + "> login successfully");

			while (true) {
				System.out.print(TSFILEDB_CLI_PREFIX + "> ");
				s = scanner.nextLine();
				if (s == null) {
					continue;
				} else {
					String[] cmds = s.trim().split(";");
					for (int i = 0; i < cmds.length; i++) {
						String cmd = cmds[i];
						if (cmd != null && !cmd.trim().equals("")) {
							if (cmd.toLowerCase().equals(QUIT_COMMAND) || cmd.toLowerCase().equals(EXIT_COMMAND)) {
								System.out.println(TSFILEDB_CLI_PREFIX + "> " + cmd.toLowerCase() + " normally");
								return;
							}

							if (cmd.toLowerCase().equals(SHOW_METADATA_COMMAND)) {
								System.out.println(connection.getMetaData());
								continue;
							}

							Statement statement = connection.createStatement();
							try {
								boolean hasResultSet = statement.execute(cmd.trim());
								if (hasResultSet) {
									ResultSet resultSet = statement.getResultSet();
									output(resultSet, printToConsole, cmd.trim());
								}
								statement.close();
								System.out.println(TSFILEDB_CLI_PREFIX + "> execute successfully.");
							} catch (TsfileSQLException e) {
								System.out.println(TSFILEDB_CLI_PREFIX + "> statement error: " + e.getMessage());
							} catch (Exception e) {
								System.out.println(TSFILEDB_CLI_PREFIX + "> connection error: " + e.getMessage());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(TSFILEDB_CLI_PREFIX + "> exit client with error " + e.getMessage());
		} finally {

			if (scanner != null) {
				scanner.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

}