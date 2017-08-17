package src.elevator;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.Font;
import src.elevator.Shared;

@SuppressWarnings("serial")
class Elevator3D extends Applet {
	private TransformGroup elevator = new TransformGroup();

	private TransformGroup door = new TransformGroup();

	private TransformGroup doorLeft = new TransformGroup();

	private TransformGroup doorRight = new TransformGroup();

	Shape3D doorL = new Shape3D();

	Shape3D doorR = new Shape3D();

	private Transform3D up = new Transform3D();

	private Transform3D translateT = new Transform3D();

	Vector3f tm = new Vector3f();

	private void addDoor() {

		Point3f p1 = new Point3f(Shared.gridSize + 0.03f,
				0.5f * Shared.floorHeight, 0.0f);
		Point3f p2 = new Point3f(Shared.gridSize + 0.03f,
				0.5f * Shared.floorHeight, -Shared.gridSize);
		Point3f p3 = new Point3f(Shared.gridSize + 0.03f, -0.5f
				* Shared.floorHeight, -Shared.gridSize);
		Point3f p4 = new Point3f(Shared.gridSize + 0.03f, -0.5f
				* Shared.floorHeight, 0.0f);

		Point3f p5 = new Point3f(Shared.gridSize + 0.03f,
				0.5f * Shared.floorHeight, Shared.gridSize);
		Point3f p6 = new Point3f(Shared.gridSize + 0.03f,
				0.5f * Shared.floorHeight, 0.0f);
		Point3f p7 = new Point3f(Shared.gridSize + 0.03f, -0.5f
				* Shared.floorHeight, 0.0f);
		Point3f p8 = new Point3f(Shared.gridSize + 0.03f, -0.5f
				* Shared.floorHeight, Shared.gridSize);

		doorR.setGeometry(Shared.createBoard(p1, p2, p3, p4), 0);
		doorR.setAppearance(Shared.createMaterialAppearance(Shared.black));
		doorRight.addChild(doorR);
		doorL.setGeometry(Shared.createBoard(p5, p6, p7, p8), 0);
		doorL.setAppearance(Shared.createMaterialAppearance(Shared.black));
		doorLeft.addChild(doorL);

		door.addChild(doorLeft);
		door.addChild(doorRight);
	}

	public void open(double size) {

		final float s = (float) size;

		new Thread() {
			public void run() {

				Point3f p1 = new Point3f(Shared.gridSize + 0.05f,
						0.5f * Shared.floorHeight, -s * Shared.gridSize);
				Point3f p2 = new Point3f(Shared.gridSize + 0.05f,
						0.5f * Shared.floorHeight, -Shared.gridSize);
				Point3f p3 = new Point3f(Shared.gridSize + 0.05f, -0.5f
						* Shared.floorHeight, -Shared.gridSize);
				Point3f p4 = new Point3f(Shared.gridSize + 0.05f, -0.5f
						* Shared.floorHeight, -s * Shared.gridSize);

				Point3f p5 = new Point3f(Shared.gridSize + 0.05f,
						0.5f * Shared.floorHeight, Shared.gridSize);
				Point3f p6 = new Point3f(Shared.gridSize + 0.05f,
						0.5f * Shared.floorHeight, s * Shared.gridSize);
				Point3f p7 = new Point3f(Shared.gridSize + 0.05f, -0.5f
						* Shared.floorHeight, s * Shared.gridSize);
				Point3f p8 = new Point3f(Shared.gridSize + 0.05f, -0.5f
						* Shared.floorHeight, Shared.gridSize);

				doorR.setGeometry(Shared.createBoard(p1, p2, p3, p4), 0);
				doorL.setGeometry(Shared.createBoard(p5, p6, p7, p8), 0);
				doorL.setAppearance(Shared
						.createMaterialAppearance(Shared.black));
				doorR.setAppearance(Shared
						.createMaterialAppearance(Shared.black));

			}

		}.start();

	}

	public void setlocation(double p) {
		final float position = (float) p - 1.0f;
		new Thread() {
			public void run() {

				elevator.getTransform(translateT);
				tm.set(0.0f, position * Shared.floorHeight, 0.0f);
				translateT.setTranslation(tm);
				elevator.setTransform(translateT);
			}
		}.start();

	}

	private Shape3D createLand() {
		Shape3D shape = new Shape3D();
		Point3f a = new Point3f(-500.f, -0.002f, -500.f);
		Point3f b = new Point3f(-500.f, -0.002f, 500.f);
		Point3f c = new Point3f(500.f, -0.002f, 500.f);
		Point3f d = new Point3f(500.f, -0.002f, -500.f);

		shape.addGeometry(Shared.createBoard(a, b, c, d));
		Shared.createMaterialAppearance(Shared.white);

		return shape;
	}

	private Shape3D createFloor() {
		Shape3D floor = new Shape3D();
		Point3f x2 = new Point3f(-Shared.gridSize, 0.0f, -5 * Shared.gridSize);
		Point3f x3 = new Point3f(-Shared.gridSize, 0.0f, -Shared.gridSize);
		Point3f p2 = new Point3f(5 * Shared.gridSize, 0.0f, -5
				* Shared.gridSize);
		Point3f p5 = new Point3f(5 * Shared.gridSize, 0.0f, -1
				* Shared.gridSize);
		for (int i = 0; i < 10; i++) {
			floor.addGeometry(Shared.createBoard(x3, p5, p2, x2));
			x2.setY(x2.y + Shared.floorHeight);
			p2.setY(p2.y + Shared.floorHeight);
			p5.setY(p5.y + Shared.floorHeight);
			x3.setY(x3.y + Shared.floorHeight);
		}
		floor.setAppearance(Shared.createMaterialAppearance(new Color3f(0.3f,
				0.6f, 0.32f)));
		return floor;
	}

	private Shape3D createBuilding() {

		Shape3D building = new Shape3D();
		Point3f p1 = new Point3f(-5 * Shared.gridSize, 0.0f, -5
				* Shared.gridSize);
		Point3f p2 = new Point3f(5 * Shared.gridSize, 0.0f, -5
				* Shared.gridSize);
		Point3f p3 = new Point3f(5 * Shared.gridSize, Shared.height, -5
				* Shared.gridSize);
		Point3f p4 = new Point3f(-5 * Shared.gridSize, Shared.height, -5
				* Shared.gridSize);

		Point3f p5 = new Point3f(5 * Shared.gridSize, 0.0f, -1
				* Shared.gridSize);
		Point3f p6 = new Point3f(5 * Shared.gridSize, Shared.height, -1
				* Shared.gridSize);
		Point3f p7 = new Point3f(-5 * Shared.gridSize, Shared.height, -1
				* Shared.gridSize);
		// Point3f p8 = new Point3f(-5 * Shared.gridSize, 0.0f, -1
		// * Shared.gridSize);

		Point3f x1 = new Point3f(-Shared.gridSize, Shared.height, -5
				* Shared.gridSize);
		Point3f x2 = new Point3f(-Shared.gridSize, 0.0f, -5 * Shared.gridSize);
		Point3f x3 = new Point3f(-Shared.gridSize, 0.0f, -Shared.gridSize);
		Point3f x4 = new Point3f(-Shared.gridSize, Shared.height,
				-Shared.gridSize);

		building.addGeometry(Shared.createBoard(p1, p2, p3, p4));
		building.addGeometry(Shared.createBoard(p5, p2, p3, p6));
		building.addGeometry(Shared.createBoard(p4, p7, p6, p3));
		building.addGeometry(Shared.createBoard(x4, x3, x2, x1));

		building.setAppearance(Shared.createMaterialAppearance(Shared.cyan));
		return building;
	}

	private void addLight(TransformGroup s) {
		DirectionalLight light1 = new DirectionalLight();
		light1.setDirection(new Vector3f(-2.0f, -1.0f, -2.0f));
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				50.);
		light1.setInfluencingBounds(bounds);
		s.addChild(light1);

	}

	public BranchGroup createBG(SimpleUniverse su) {
		BranchGroup root = new BranchGroup();
		TransformGroup still = new TransformGroup();
		TransformGroup all = new TransformGroup();
		TransformGroup moveThings = new TransformGroup();
		TransformGroup view = null;
		TransformGroup textT = new TransformGroup();
		Transform3D scale = new Transform3D();
		scale.setScale(0.5);
		textT.setTransform(scale);

		Box elevatorElement = new Box(Shared.gridSize, Shared.floorHeight / 2,
				Shared.gridSize, Shared.createMaterialAppearance(Shared.red));
		Transform3D translate = new Transform3D();
		Transform3D rotate1 = new Transform3D();
		Transform3D rotate2 = new Transform3D();
		Transform3D translateE = new Transform3D();
		Transform3D up = new Transform3D();
		translateE.setTranslation(new Vector3d(-2 * Shared.gridSize + 0.02f,
				elevatorElement.getYdimension(), -2 * Shared.gridSize));

		// rotate1.rotX(Math.PI / 8.5);
		 rotate2.rotY(-Math.PI / 12.0);
		translate.setTranslation(new Vector3f(-0.2f, -2.0f, -1.0f));
		rotate1.mul(rotate2);
		rotate1.mul(translate);
		up.rotZ(Math.PI / 2.0);

		Font3D font = new Font3D(new Font("宋体", Font.ROMAN_BASELINE, 1),
				new FontExtrusion());
		Text3D title = new Text3D(font, "南京大学软件学院", new Point3f(-4.0f,
				17.0f * Shared.floorHeight, -2.5f * Shared.gridSize));
		title.setCapability(Text3D.ALLOW_ALIGNMENT_WRITE);
		title.setPath(Text3D.PATH_DOWN);
		Shape3D text = new Shape3D(title);
		text.setAppearance(Shared.createMaterialAppearance(Shared.yellow));

		elevator.addChild(elevatorElement);
		addDoor();
		elevator.addChild(door);
		moveThings.addChild(elevator);
		moveThings.setTransform(translateE);
		all.addChild(still);
		all.setTransform(rotate1);
		Shape3D stillOb1 = createBuilding();
		textT.addChild(text);
		still.addChild(textT);
		still.addChild(stillOb1);
		still.addChild(createLand());
		still.addChild(createFloor());
		all.addChild(moveThings);

		view = su.getViewingPlatform().getViewPlatformTransform();
		KeyNavigatorBehavior key = new KeyNavigatorBehavior(view);
		key.setSchedulingBounds(new BoundingSphere(new Point3d(), 500.0));
		root.addChild(key);
		root.addChild(all);
		addLight(all);

		return root;
	}

	public Elevator3D() {

		doorL.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		doorR.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		elevator.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		doorL.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		doorR.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		up.rotZ(Math.PI / 2.0);

		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		add("Center", canvas);
		SimpleUniverse univers = new SimpleUniverse(canvas);
		BranchGroup scene = this.createBG(univers);
		scene.compile();
		univers.getViewingPlatform().setNominalViewingTransform();
		univers.getViewer().getView().setLocalEyeLightingEnable(true);

		univers.addBranchGraph(scene);

	}
}