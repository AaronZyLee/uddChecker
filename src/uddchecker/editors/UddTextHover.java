package uddchecker.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;

public class UddTextHover implements ITextHover {

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		
		String str = null;
		
		if (hoverRegion != null) {
			try {
				if (hoverRegion.getLength() > -1)
					str = textViewer.getDocument().get(hoverRegion.getOffset(), hoverRegion.getLength());
				return str;
			} catch (BadLocationException x) {
			}
		}
		return null;	//JavaEditorMessages.getString("JavaTextHover.emptySelection"); 
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		Point selection= textViewer.getSelectedRange();
		if (selection.x <= offset && offset < selection.x + selection.y)
			return new Region(selection.x, selection.y);
		return new Region(offset, 0);
	}

}
