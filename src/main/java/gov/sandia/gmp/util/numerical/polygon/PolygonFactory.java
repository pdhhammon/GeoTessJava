package gov.sandia.gmp.util.numerical.polygon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolygonFactory
{

	/**
	 * Load a Polygon from a File. If the extension is kml or kmz, the file is interpreted
	 * to be a file generated by Google Earth. If not a kml or kmz file, the first line of 
	 * the file is loaded and if it is 'POLYGON3D' then a new Polygon3D object is loaded 
	 * from an ascii file and returned.  Otherwise, a new Polygon (2D polygon object) is 
	 * loaded from an ascii file and returned. 
	 * 
	 * <ul>
	 * Ascii files are parsed as follows:
	 * <li>Records that start with '#' are ignored.
	 * <li>If there is a record that starts with 'lat' then all boundary point records
	 * will be assumed to be in order lat-lon. If there is a record that starts with 'lon'
	 * then all boundary point records will be assumed to be in order lon-lat. If no
	 * record starts with 'lat' or 'lon', boundary point records are assumed to be in
	 * order lat-lon.
	 * <li>If there is a record that starts with 'reference' then the record is assumed to
	 * contain information about the referencePoint. The second and third tokens in the
	 * record are interpreted as the latitude and longitude of the referencePoint, in
	 * degrees (the order depends on the lat-lon record described above). If the fourth
	 * and final token starts with 'in' then the reference point is defined to be 'inside'
	 * the polygon, otherwise it is assumed to be outside the polygon.
	 * <li>All other records are assumed to specify a boundary point in lat-lon or lon-lat
	 * order, in degrees.
	 * </ul>
	 * <p>
	 * For ascii files, if the referencePoint is not defined as described above then the
	 * referencePoint will be the anti-pode of the normalized vector sum of the polygon
	 * boundary points and will be deemed to be 'outside' the polygon.
	 * 
	 * <p>
	 * For kmz/kml files, the referencePoint will always be the anti-pode of the
	 * normalized vector sum of the polygon boundary points and will be deemed to be
	 * 'outside' the polygon.
	 * 
	 * 
	 * @param file
	 *            name of the file containing the polygon definition
	 * @throws PolygonException
	 * @throws IOException
	 */
	public static Polygon getPolygon(File file) throws IOException
	{
		if (!file.exists())
			throw new IOException(String.format("File does not exist%n%s",
					file.getCanonicalPath()));
		
		String fileExtension = "";
		int idx = file.getName().lastIndexOf('.');
		if (idx >= 0)
			fileExtension = file.getName().substring(idx + 1);

		if (fileExtension.equalsIgnoreCase("kml") || fileExtension.equalsIgnoreCase("kmz"))
			return new Polygon().readKMLZ(file);

		ArrayList<String> records = Polygon.readRecords(file);

		Polygon polygon;
		if (records.get(0).trim().toUpperCase().startsWith("POLYGON3D"))
			polygon = new Polygon3D();
		else 
			polygon = new Polygon();
		
		polygon.parseRecords(records);
		
		return polygon;

	}
	
	public static List<Polygon> getPolygons(File file) throws IOException
	{
		if (!file.exists())
			throw new IOException(String.format("File does not exist%n%s",
					file.getCanonicalPath()));
		
		String fileExtension = "";
		int idx = file.getName().lastIndexOf('.');
		if (idx >= 0)
			fileExtension = file.getName().substring(idx + 1);

		if (fileExtension.equalsIgnoreCase("kml") || fileExtension.equalsIgnoreCase("kmz"))
			return Polygon.readPolygonsKMLZ(file);

		List<Polygon> polygons = new ArrayList<>();
		
		ArrayList<String> records = Polygon.readRecords(file);

		Polygon polygon;
		if (records.get(0).trim().toUpperCase().startsWith("POLYGON3D"))
			polygon = new Polygon3D();
		else 
			polygon = new Polygon();
		
		polygon.parseRecords(records);
		polygons.add(polygon);
		
		return polygons;

	}
}