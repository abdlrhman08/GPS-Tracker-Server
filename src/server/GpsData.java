package server;

public class GpsData {
	public static double Longitude = 0;
	public static double Latitude = 0;
	

	public static double StartLongitude = 0;
	public static double StartLatitude = 0;
	
	public static double destinationLat = 32.45157;
	public static double destinationLong = 29.12453;
	
	public static double movedDistance = 0;
	public static double linearDistance = 0;
	
	public static int Speed = 0;
	
	public static String Time;
	
	public static boolean TivaConn = false;
	public static boolean firstAppConnection = true;
	
	public static double calculateDistance(double lat1, double long1, double lat2, double long2) {
		double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(long2 - long1);

	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	              Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	              Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double distance = 6371000 * c;

	    return distance;
	}
}
