package src.elevator;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.vecmath.*;

public class Shared {
	public static final Color3f red = new Color3f(1.0f, 0.0f, 0.0f);

	public static final Color3f green = new Color3f(0.0f, 1.0f, 0.0f);

	public static final Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);

	public static final Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f);

	public static final Color3f cyan = new Color3f(0.0f, 1.0f, 1.0f);

	public static final Color3f magenta = new Color3f(1.0f, 0.0f, 1.0f);

	public static final Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

	public static final Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

	public static final float gridSize = 0.25f;

	public static final float height = 4.5f;

	public static final float floorHeight = height / 10;

	public static Appearance createMaterialAppearance(Color3f color) {

		Appearance appear = new Appearance();
		Material material = new Material();
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		material.setDiffuseColor(color);
		material.setSpecularColor(color);
		material.setShininess(1.2f);
		appear.setMaterial(material);
		appear.setPolygonAttributes(polyAttrib);

		return appear;
	}

	public static Appearance createWireFrameAppearance(Color3f color) {

		Appearance materialAppear = new Appearance();
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		materialAppear.setPolygonAttributes(polyAttrib);
		ColoringAttributes redColoring = new ColoringAttributes();
		redColoring.setColor(color);
		materialAppear.setColoringAttributes(redColoring);

		return materialAppear;
	}

	public static Geometry createBoard(Point3f A, Point3f B, Point3f C,
			Point3f D) {

		QuadArray board = new QuadArray(4, GeometryArray.COORDINATES
				| GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);

		board.setCoordinate(0, A);
		board.setCoordinate(1, B);
		board.setCoordinate(2, C);
		board.setCoordinate(3, D);
		Vector3f a = new Vector3f(A.x - B.x, A.y - B.y, A.z - B.z);
		Vector3f b = new Vector3f(C.x - B.x, C.y - B.y, C.z - B.z);
		Vector3f n = new Vector3f();
		n.cross(b, a);
		n.normalize();
		board.setNormal(0, n);
		board.setNormal(1, n);
		board.setNormal(2, n);
		board.setNormal(3, n);

		TexCoord2f q = new TexCoord2f();
		q.set(0.0f, 1.0f);
		board.setTextureCoordinate(0, 0, q);
		q.set(0.0f, 0.0f);
		board.setTextureCoordinate(0, 1, q);
		q.set(1.0f, 0.0f);
		board.setTextureCoordinate(0, 2, q);
		q.set(1.0f, 1.0f);
		board.setTextureCoordinate(0, 3, q);

		return board;
	}
}