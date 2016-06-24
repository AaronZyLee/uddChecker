package uddchecker.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class UddTextHover implements ITextHover {

    public static String processStr_helper(String str) {
        String conv_str = "";

        Integer check_or = str.indexOf(';');

        if (check_or != -1) {
            String[] split_str = str.split(";");
            for (int i = 0; i < split_str.length; i++) {
                String parenth_st;
                String st = split_str[i];
                Pattern pattern = Pattern.compile(",|;");
                if (pattern.matcher(st).find()) {
                    parenth_st = "(" + st + ")";
                } else {
                    parenth_st = st;
                }
                String per_parenth_st = parenth_st.replace(",", " && ").replace("&amp;&amp", " && ");
                conv_str += per_parenth_st;

                if (i < split_str.length - 1) {
                    conv_str += " || ";
                }
            }
        } else {
            conv_str = str.replace(",", " && ");
        }
        return conv_str;
    }

    public static String processStr(String str) {
        System.out.println("ORIGINAL:\n" + str);

        // &amp;&amp ---> ,;
        Integer check_amp = str.indexOf("&amp;&amp;");
        if (check_amp != -1) {
            str = str.replace("&amp;&amp;", " , ");
        }

        // || ---> ;
        Integer check_double_line = str.indexOf("||");
        if (check_double_line != -1) {
            str = str.replace("||", ";");
        }

        System.out.println("ONLY ; , \n" + str);

        LinkedList<String> group_strs = new LinkedList<>();

        int j = 0;
        int i = 0;
        while (i < str.length()) {
            if (str.charAt(i) == '(') {
                if (i != 0) {
                    group_strs.add(str.substring(j, i));
                }
                for (int k = i+1; k < str.length(); k++) {
                    if (str.charAt(k) == ')') {
                        group_strs.add(str.substring(i, k+1));
                        i = k+1;
                        j = k+1;
                        break;
                    }
                }
            } else if (i == str.length()-1) {
                group_strs.add(str.substring(j, i+1));
                break;
            } else {
                i++;
            }
        }
        System.out.println("GROUPING: \n" + group_strs);

        for (int iter = 0; iter < group_strs.size(); iter ++) {
            if (group_strs.get(iter).charAt(0) == '(') {
                String preprocess_str = group_strs.get(iter);
                group_strs.set(iter, processStr_helper(preprocess_str));
            }
        }

        String post_str = "";

        for (String s: group_strs) {
            post_str += s;
        }

        System.out.println(group_strs);

        return processStr_helper(post_str);

    }

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		
		String conv_str = "";
		if (hoverRegion != null) {
			try {
				if (hoverRegion.getLength() > -1) {
					str = textViewer.getDocument().get(hoverRegion.getOffset(), hoverRegion.getLength());
					conv_str = processString(str);
				}
				return conv_str;

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
