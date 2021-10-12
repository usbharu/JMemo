package usbharu.jmemo.memo;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class PaintCanvas extends Canvas implements MouseMotionListener, MouseListener, KeyListener, MouseWheelListener {
	Graphics2D graphics;

	private float scale = 1f;

	private final int penWidth = 1;

	private final Cursor defCursor = Cursor.getDefaultCursor();
	private final Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private final Point dragPoint = new Point();

	private int oldX;
	private int oldY;
	private int x;
	private int y;
	//----------------------test----------
//	private final Point testPoint = new Point(250,300);
	//-------------------
	private final Point canvasPos = new Point();

	private boolean wasPressedSpaceKey = false;
	private boolean wasPressedMiddleButton = false;

	private BufferedImage bufferImage;

	public PaintCanvas(int w, int h) {
		setSize(new Dimension(w, h));
		setBackground(Color.GRAY);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseWheelListener(this);
		setFocusable(true);

		bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) bufferImage.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		graphics.setColor(Color.BLACK);
//		graphics.drawRect(testPoint.x-20, testPoint.y-20, 40,40);
//		graphics.drawRect(testPoint.x, testPoint.y, 1,1);
		graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
//		graphics.drawLine(0,0,1000,1000);
		repaint();
	}

	/**
	 * Paints this canvas.
	 * <p>
	 * Most applications that subclass {@code Canvas} should
	 * override this method in order to perform some useful operation
	 * (typically, custom painting of the canvas).
	 * The default operation is simply to clear the canvas.
	 * Applications that override this method need not call
	 * super.paint(g).
	 *
	 * @param g the specified Graphics context
	 * @see #update(Graphics)
	 * @see Component#paint(Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		if (oldX + oldY + x + y >= 0) {
			BasicStroke stroke = new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			graphics.setStroke(stroke);
			graphics.setColor(Color.BLACK);
			graphics.drawLine(oldX + canvasPos.x, oldY + canvasPos.y, x + canvasPos.x, y + canvasPos.y);
		}

		System.out.println("(bufferImage.getWidth()*scale)+canvasPos.x = " + (bufferImage.getWidth() * scale) + canvasPos.x);
		System.out.println("(bufferImage.getHeight()*scale)+canvasPos.y = " + (bufferImage.getHeight() * scale) + canvasPos.y);


		g.drawImage(bufferImage, 0, 0, getWidth(), getHeight(), canvasPos.x, canvasPos.y, (int) (getWidth() * scale) + canvasPos.x, (int) (getHeight() * scale) + canvasPos.y, null);
		g.setColor(Color.BLUE);
	}

	/**
	 * Updates this canvas.
	 * <p>
	 * This method is called in response to a call to {@code repaint}.
	 * The canvas is first cleared by filling it with the background
	 * color, and then completely redrawn by calling this canvas's
	 * {@code paint} method.
	 * Note: applications that override this method should either call
	 * super.update(g) or incorporate the functionality described
	 * above into their own code.
	 *
	 * @param g the specified Graphics context
	 * @see #paint(Graphics)
	 * @see Component#update(Graphics)
	 */
	@Override
	public void update(Graphics g) {
//		super.update(g);
		paint(g);
	}

	/**
	 * Invoked when the mouse button has been clicked (pressed
	 * and released) on a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Point point = e.getPoint();
		point.x *= scale;
		point.y *= scale;
		if (e.getButton() == MouseEvent.BUTTON2) {
			wasPressedMiddleButton = true;
		}
		if (wasPressedSpaceKey || wasPressedMiddleButton) {
			dragPoint.setLocation(point);
		} else {
			x = point.x;
			y = point.y;
		}
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON2) {
			wasPressedMiddleButton = false;
		}
	}

	/**
	 * Invoked when the mouse enters a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Invoked when the mouse exits a component.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Invoked when a mouse button is pressed on a component and then
	 * dragged.  {@code MOUSE_DRAGGED} events will continue to be
	 * delivered to the component where the drag originated until the
	 * mouse button is released (regardless of whether the mouse position
	 * is within the bounds of the component).
	 * <p>
	 * Due to platform-dependent Drag&amp;Drop implementations,
	 * {@code MOUSE_DRAGGED} events may not be delivered during a native
	 * Drag&amp;Drop operation.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		Point point = e.getPoint();
		System.out.println("point = " + point);
		System.out.println("canvasPos = " + canvasPos);
		point.x *= scale;
		point.y *= scale;
		if (wasPressedSpaceKey || wasPressedMiddleButton) {
			canvasPos.x += dragPoint.x - point.x;
			canvasPos.y += dragPoint.y - point.y;
			dragPoint.setLocation(point);
		} else {
			oldX = x;
			oldY = y;
			x = point.x;
			y = point.y;
		}
		repaint();
	}

	/**
	 * Invoked when the mouse cursor has been moved onto a component
	 * but no buttons have been pushed.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		x = -1;
		y = -1;
		oldX = -1;
		oldY = -1;
	}

	/**
	 * Invoked when a key has been typed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key typed event.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Invoked when a key has been pressed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key pressed event.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			wasPressedSpaceKey = true;
		}
	}

	/**
	 * Invoked when a key has been released.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key released event.
	 *
	 * @param e the event to be processed
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			wasPressedSpaceKey = false;
		}
	}

	/**
	 * Invoked when the mouse wheel is rotated.
	 *
	 * @param e the event to be processed
	 * @see MouseWheelEvent
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point point = e.getPoint();
		if (e.getWheelRotation() > 0 && scale < 2) {
			scale *= 1.1f;
		} else if (e.getWheelRotation() < 0 && scale > 0.1f) {
			scale *= 0.9f;
		} else {
			return;
		}
		System.out.println("scale = " + scale);

		canvasPos.x = (int) ((point.x + canvasPos.x) * (reverse(scale) - 1f));
		canvasPos.y = (int) ((point.y + canvasPos.y) * (reverse(scale) - 1f));

		if ((canvasPos.x < 0 || canvasPos.y < 0) && (bufferImage.getWidth() / scale - canvasPos.x < getWidth() || bufferImage.getHeight() / scale - canvasPos.y < getHeight())) {
			resize(-canvasPos.x, -canvasPos.y, (int) (bufferImage.getWidth() * scale - canvasPos.x), (int) (bufferImage.getHeight() * scale - canvasPos.y));
			canvasPos.setLocation(0, 0);
		}

		repaint();
	}

	private float reverse(float f) {
		float returnF = 1 - (f - 1);
		if (returnF <= 0) {
			return 0.1f;
		}
		return returnF;
	}

	private void resize(int posX, int posY, int width, int height) {
		BufferedImage oldImage = bufferImage;
		bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) bufferImage.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		graphics.drawImage(oldImage, posX, posY, null);
	}
}