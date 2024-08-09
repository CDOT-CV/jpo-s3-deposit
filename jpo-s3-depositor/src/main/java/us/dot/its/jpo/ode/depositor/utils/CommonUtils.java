package us.dot.its.jpo.ode.depositor.utils;

public class CommonUtils {
    public static String getEnvironmentVariable(String variableName) {
		String value = System.getenv(variableName);
		return value;
	 }

    public static String getEnvironmentVariable(String variableName, String defaultValue) {
		String value = System.getenv(variableName);
		if (value == null || value.equals("")) {
		   System.out.println("Something went wrong retrieving the environment variable " + variableName);
		   System.out.println("Using default value: " + defaultValue);
		   return defaultValue;
		}
		return value;
	 }
}
